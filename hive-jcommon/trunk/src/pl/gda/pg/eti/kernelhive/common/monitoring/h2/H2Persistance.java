/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.common.monitoring.h2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author szymon
 */
public class H2Persistance {

	private static final Logger logger = Logger.getLogger(H2Persistance.class.getName());
	private Connection connection;
	private H2PersistanceHelper helper = new H2PersistanceHelper();

	public H2Persistance(Connection connection) {
		this.connection = connection;
	}

	public void createTable(H2Entity entity) throws H2PersistanceException {
		createTable(entity.getClass(), entity.getTableName());
	}

	public void createTable(Class c, String name) throws H2PersistanceException {
		List<String> columns = new LinkedList<>();
		List<Method> getters = helper.getGetters(c);
		for (Method m : getters) {
			String fieldName = helper.methodToField(m.getName());
			String identity = fieldName.toLowerCase().equals("id") ? " IDENTITY" : "";

			Class returnType = m.getReturnType();
			if (returnType == String.class) {
				columns.add(fieldName + " VARCHAR(255)" + identity);
			} else if (helper.isInt(returnType)) {
				columns.add(fieldName + " INT" + identity);
			} else if (helper.isLong(returnType)) {
				columns.add(fieldName + " BIGINT" + identity);
			} else {
				logger.log(Level.WARNING, "Unsupported type: {0}", returnType.getName());
			}
		}
		Collections.sort(columns);
		String sql = String.format("CREATE TABLE IF NOT EXISTS %s(%s)",
				name, StringUtils.join(columns, ","));
		logger.severe("sql: " + sql);
		try {
			connection.createStatement().execute(sql);
		} catch (SQLException ex) {
			throw new H2PersistanceException(
					String.format("Cannot create table, SQL: \"%s\"", sql), ex);
		}
	}

	public Long save(H2Entity unit) throws H2PersistanceException {
		H2Entity saved = select(unit);
		if (saved == null) {
			return insert(unit);
		} else {
			update(unit);
			return unit.getId();
		}
	}

	public Long insert(H2Entity unit) throws H2PersistanceException {
		List<String> values = helper.serialize(unit);
		String sql = String.format("INSERT INTO %s VALUES(%s)", unit.getTableName(),
				StringUtils.join(values, ","));
		logger.severe("sql: " + sql);
		try {
			prepareTable(unit);
			connection.createStatement().execute(sql);
			ResultSet result = connection.createStatement().executeQuery("CALL SCOPE_IDENTITY()");
			if (result.first()) {
				return result.getLong(1);
			} else {
				throw new H2PersistanceException("Cannot get ID of the inserted element");
			}
		} catch (SQLException ex) {
			throw new H2PersistanceException(
					String.format("Cannot insert element, SQL: \"%s\"", sql), ex);
		}
	}

	public void update(H2Entity unit) throws H2PersistanceException {
		Map<String, String> kvp = helper.serializeToMap(unit);
		List<String> entries = new ArrayList<>();
		for (Entry<String, String> entry : kvp.entrySet()) {
			if (!entry.getKey().toLowerCase().equals("id")) {
				entries.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
			}
		}
		String sql = String.format("UPDATE %s SET %s WHERE id=%s", unit.getTableName(),
				StringUtils.join(entries, ","), String.valueOf(unit.getId()));
		logger.severe("sql: " + sql);
		try {
			connection.createStatement().execute(sql);
		} catch (SQLException ex) {
			throw new H2PersistanceException(
					String.format("Cannot update entity, SQL: %s", sql), ex);
		}
	}

	public <T extends H2Entity> T select(T entity) throws H2PersistanceException {
		String sql = String.format("SELECT * FROM %s WHERE ID = '%d'",
				entity.getTableName(), entity.getId());
		logger.severe("sql: " + sql);
		try {
			prepareTable(entity);
			ResultSet result = connection.createStatement().executeQuery(sql);
			if (result.first()) {
				H2Entity newEntity = null;
				try {
					newEntity = entity.getClass().newInstance();
					return deserialize((T) newEntity, result);
				} catch (InstantiationException | IllegalAccessException ex) {
					logger.log(Level.SEVERE, String.format(
							"Cannot create instance of %s", entity.getClass().getName()), ex);
					return null;
				}
			} else {
				return null;
			}
		} catch (SQLException ex) {
			throw new H2PersistanceException(String.format("Error selecting entity from %s, SQL: \"%s\"",
					entity.getTableName(), sql), ex);
		}
	}

	public <T extends H2Entity> List<T> selectAll(T entity) throws H2PersistanceException {
		String sql = String.format("SELECT * FROM %s", entity.getTableName());
		return selectAll(entity, sql);
	}

	public <T extends H2Entity> List<T> selectAll(T entity, String propertyName,
			String propertyValue) throws H2PersistanceException {
		String sql = String.format("SELECT * FROM %s WHERE %s = '%s'",
				entity.getTableName(), propertyName, propertyValue);
		return selectAll(entity, sql);
	}

	public <T extends H2Entity> List<T> selectAll(T entity, Map<String, String> params)
			throws H2PersistanceException {
		return selectAll(entity, String.format("SELECT * FROM %s WHERE %s",
				entity.getTableName(), concatWithAnd(params)));
	}

	private <T extends H2Entity> List<T> selectAll(T entity, String sql) throws H2PersistanceException {
		logger.severe("sql: " + sql);
		List<T> results = new LinkedList<>();
		try {
			prepareTable(entity);
			ResultSet resultSet = connection.createStatement().executeQuery(sql);
			while (resultSet.next()) {
				try {
					T newEntity = (T) entity.getClass().newInstance();
					results.add(deserialize(newEntity, resultSet));
				} catch (InstantiationException | IllegalAccessException ex) {
					logger.log(Level.SEVERE, "Cannot create new entity instance", ex);
				}

			}
		} catch (SQLException ex) {
			throw new H2PersistanceException("Error getting all from "
					+ entity.getTableName(), ex);
		}
		return results;
	}

	public <T extends H2Entity> void removeAll(T entity, Map<String, String> params)
			throws H2PersistanceException {
		String sql = String.format("DELETE FROM %s WHERE %s", entity.getTableName(), concatWithAnd(params));
		execute(entity, sql);
	}

	public <T extends H2Entity> void remove(T entity)
			throws H2PersistanceException {
		String sql = String.format("DELETE FROM %s WHERE ID=%d", entity.getTableName(), entity.getId());
		execute(entity, sql);
	}

	private <T extends H2Entity> void execute(T entity, String sql) throws H2PersistanceException {
		logger.severe("sql: " + sql);
		try {
			prepareTable(entity);
			connection.createStatement().execute(sql);
		} catch (SQLException ex) {
			throw new H2PersistanceException("Error getting all from "
					+ entity.getTableName(), ex);
		}
	}

	public void prepareTable(H2Entity entity) throws H2PersistanceException {
		try {
			ResultSet result = connection.createStatement().executeQuery(String.format("SELECT * \n"
					+ "                 FROM INFORMATION_SCHEMA.TABLES \n"
					+ "                 WHERE TABLE_SCHEMA = 'PUBLIC' \n"
					+ "                 AND  TABLE_NAME = '%s'", entity.getTableName()));
			if (!result.first()) {
				createTable(entity);
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE,
					String.format("Cannot check if %s exists", entity.getTableName()), ex);
		}
	}

	public <T extends H2Entity> T deserialize(T entity, ResultSet result) throws H2PersistanceException {
		List<Method> setters = helper.getSetters(entity.getClass());
		for (Method m : setters) {
			String fieldName = helper.methodToField(m.getName());
			try {
				Object field = result.getObject(fieldName);
				m.invoke(entity, field);
			} catch (SQLException ex) {
				throw new H2PersistanceException("Error getting entity from "
						+ entity.getTableName(), ex);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				logger.log(Level.SEVERE, "Error while calling setter", ex);
			}
		}
		return entity;
	}

	private String concatWithAnd(Map<String, String> params) {
		StringBuilder sql = new StringBuilder();
		boolean first = true;
		for (Entry<String, String> entry : params.entrySet()) {
			if (!first) {
				sql.append(" AND ");
			}
			sql.append(String.format("%s='%s'", entry.getKey(), entry.getValue()));
			first = false;
		}
		return sql.toString();
	}

	public static class H2PersistanceException extends Exception {

		public H2PersistanceException(String message) {
			super(message);
		}

		public H2PersistanceException(String message, Exception internalException) {
			super(message, internalException);
		}
	}
}
