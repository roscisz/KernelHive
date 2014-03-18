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
package pl.gda.pg.eti.kernelhive.repository.kernel.repository;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.repository.kernel.repository.IKernelPathEntry;

public class KernelPathEntry implements IKernelPathEntry, Serializable {
	
	private static final long serialVersionUID = -3446193945659803907L;
	private final String name;
	private final String id;
	private final URL path;
	private final Map<String, Object> properties;
	
	public KernelPathEntry(String id, String name, URL path, Map<String, Object> properties){
		this.name = name;
		this.path = path;
		this.id = id;
		this.properties = properties;
	}

	/**
	 * gets the kernel name
	 * @return {@link String}
	 */
	public String getName() {
		return name;
	}

	/**
	 * gets the kernel java resource path
	 * @return {@link URL}
	 */
	public URL getPath() {
		return path;
	}

	/**
	 * gets kernel properties {@link Map}
	 * @return {@link Map}
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	/**
	 * gets kernel id
	 * @return {@link String}
	 */
	public String getId(){
		return id;
	}
}
