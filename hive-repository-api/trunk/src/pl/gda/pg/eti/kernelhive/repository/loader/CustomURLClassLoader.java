/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
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
package pl.gda.pg.eti.kernelhive.repository.loader;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Vector;
import java.util.jar.JarFile;

class CustomURLClassLoader extends URLClassLoader {

	public CustomURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public CustomURLClassLoader(URL[] urls) {
		super(urls);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getLoadedClasses() {
		try {
			Class cLClass = this.getClass();
			while (cLClass != ClassLoader.class) {
				cLClass = cLClass.getSuperclass();
			}
			Field f = cLClass.getDeclaredField("classes");
			f.setAccessible(true);

			Vector<Class> classes = (Vector<Class>) f.get(this);
			for (Class c : classes) {
				System.out.println(c.toString());
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void close() {
		try {
			Class clazz = java.net.URLClassLoader.class;
			Field ucp = clazz.getDeclaredField("ucp");
			ucp.setAccessible(true);
			Object sunMiscURLClassPath = ucp.get(this);
			Field loaders = sunMiscURLClassPath.getClass().getDeclaredField(
					"loaders");
			loaders.setAccessible(true);
			Object collection = loaders.get(sunMiscURLClassPath);
			for (Object sunMiscURLClassPathJarLoader : ((Collection) collection)
					.toArray()) {
				try {
					Field loader = sunMiscURLClassPathJarLoader.getClass()
							.getDeclaredField("jar");
					loader.setAccessible(true);
					Object jarFile = loader.get(sunMiscURLClassPathJarLoader);
					((JarFile) jarFile).close();
				} catch (Throwable e) {
					// skip
				}
			}
		} catch (Throwable e) {
			// ERROR
		}
	}
}
