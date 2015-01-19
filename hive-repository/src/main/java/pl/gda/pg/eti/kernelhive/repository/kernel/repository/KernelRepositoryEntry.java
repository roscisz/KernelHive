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
import java.util.List;

import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;

public class KernelRepositoryEntry implements IKernelRepositoryEntry,
		Serializable {

	private static final long serialVersionUID = -4658587858383597251L;
	private final GraphNodeType type;
	private final String description;
	private final List<IKernelPathEntry> kernelsPaths;

	public KernelRepositoryEntry(GraphNodeType type, String description,
			List<IKernelPathEntry> kernelsPaths) {
		this.type = type;
		this.description = description;
		this.kernelsPaths = kernelsPaths;
	}

	/**
	 * return graph node type
	 * 
	 * @return {@link GraphNodeType}
	 */
	public GraphNodeType getGraphNodeType() {
		return type;
	}

	/**
	 * return list of {@link KernelPathEntry} objects
	 * 
	 * @return {@link List}
	 */
	public List<IKernelPathEntry> getKernelPaths() {
		return kernelsPaths;
	}

	/**
	 * gets {@link KernelPathEntry} for given kernel name
	 * 
	 * @param name
	 *            String
	 * @return {@link URL}
	 */
	public IKernelPathEntry getKernelPathEntryForName(String name) {
		for (IKernelPathEntry e : kernelsPaths) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * gets {@link KernelPathEntry} for given kernel id
	 * 
	 * @param id
	 *            {@link String}
	 * @return {@link KernelPathEntry}
	 */
	public IKernelPathEntry getKernelPathEntryForId(String id) {
		for (IKernelPathEntry e : kernelsPaths) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * gets entry description
	 * 
	 * @return {@link String}
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		if (description != null) {
			return description + " (" + type.toString() + ")";
		} else {
			return type.toString();
		}
	}
}
