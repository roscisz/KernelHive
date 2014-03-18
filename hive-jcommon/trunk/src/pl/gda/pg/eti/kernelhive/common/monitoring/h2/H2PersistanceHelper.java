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
