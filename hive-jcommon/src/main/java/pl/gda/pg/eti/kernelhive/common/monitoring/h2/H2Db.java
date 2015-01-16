/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Szymon Bultrowicz
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.common.monitoring.h2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.tools.Server;

public class H2Db {

	private static final Logger logger = Logger.getLogger(H2Db.class.getName());
	private static final String DB_PATH = "/tmp/kh-data";
	private static final String DB_URL = "jdbc:h2:tcp://localhost:9092/" + DB_PATH;
	private static final String USERNAME = "kernelhive";
	private static final String PASSWORD = "kernelhive";
	Server server;
	Connection connection;

	public H2Db() throws InitializationException {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			throw new InitializationException("H2 driver not found", e);
		}
	}

	public Connection open() throws InitializationException {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			}
			return connection;
		} catch (SQLException ex) {
			throw new InitializationException("Cannot open connection", ex);
		}
	}

	public void close() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Error while closing H2 connection", ex);
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public static void checkDirectoryPermissions(String path) {
		File file = new File(path);
		file.getParentFile().setWritable(true);
	}

	public static class InitializationException extends Exception {

		public InitializationException(String message, Exception innerException) {
			super(message, innerException);
		}
	}
}
