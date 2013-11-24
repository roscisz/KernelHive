/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.common.monitoring.h2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author szymon
 */
public class H2PersistanceHelper {

	private static final Logger logger = Logger.getLogger(H2PersistanceHelper.class.getName());

	public List<Method> getGetters(Class clazz) {
		return getAnnotatedMethods(clazz, H2Getter.class);
	}

	public List<Method> getSetters(Class clazz) {
		return getAnnotatedMethods(clazz, H2Setter.class);
	}

	public List<Method> getAnnotatedMethods(Class clazz, Class annotationClass) {
		List<Method> methods = new LinkedList<>();
		for (Method method : clazz.getMethods()) {
			if (method.getAnnotation(annotationClass) != null) {
				methods.add(method);
			}
		}
		return methods;
	}

	public boolean isInt(Class c) {
		return c == Integer.class
				|| c == int.class;
	}

	public boolean isLong(Class c) {
		return c == Long.class
				|| c == long.class;
	}

	public String methodToField(String methodName) {
		if (methodName.indexOf("get") == 0) {
			String tmp = methodName.replaceFirst("get", "");
			return Character.toLowerCase(tmp.charAt(0))
					+ tmp.substring(1);
		} else if (methodName.indexOf("set") == 0) {
			String tmp = methodName.replaceFirst("set", "");
			return Character.toLowerCase(tmp.replaceFirst("set", "").charAt(0))
					+ tmp.substring(1);
		} else {
			return methodName;
		}
	}

	public List<String> serialize(H2Entity unit) {
		List<String> values = new LinkedList<>();
		Class c = unit.getClass();
		Map<String, Method> methods = new TreeMap<>();
		for (Method method : getGetters(c)) {
			methods.put(method.getName(), method);
		}
		for (String methodName : methods.keySet()) {
			Method method = methods.get(methodName);
			try {
				Object result = method.invoke(unit);
				values.add(result != null ? "'" + result + "'" : "null");
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				logger.log(Level.SEVERE, "Error while invoking DbGetter", ex);
			}
		}
		return values;
	}

	public Map<String, String> serializeToMap(H2Entity unit) {
		Map<String, String> values = new HashMap<>();
		Class c = unit.getClass();
		for (Method method : getGetters(c)) {
			try {
				Object result = method.invoke(unit);
				values.put(methodToField(method.getName()),
						result != null ? "'" + result + "'" : "null");
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				logger.log(Level.SEVERE, "Error while invoking DbGetter", ex);
			}
		}
		return values;
	}
}
