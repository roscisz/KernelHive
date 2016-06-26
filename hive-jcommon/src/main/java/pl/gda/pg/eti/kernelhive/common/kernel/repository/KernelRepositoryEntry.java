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
package pl.gda.pg.eti.kernelhive.common.kernel.repository;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

public class KernelRepositoryEntry implements Serializable {

	private static final long serialVersionUID = -4658587858383597251L;
	private final GraphNodeType type;
	private final String description;
	private final List<KernelPathEntry> kernelsPaths;
	private final Map<String, Object> properties;
	
	
	public KernelRepositoryEntry(GraphNodeType type, String description, List<KernelPathEntry> kernelsPaths, Map<String, Object> properties){
		this.type = type;
		this.description = description;
		this.kernelsPaths = kernelsPaths;
		this.properties = properties;
	}
	
	/**
	 * return graph node type
	 * @return {@link GraphNodeType}
	 */
	public GraphNodeType getGraphNodeType(){
		return type;
	}
	
	/**
	 * return list of {@link KernelPathEntry} objects
	 * @return {@link List}
	 */
	public List<KernelPathEntry> getKernelPaths(){
		return kernelsPaths;
	}
	
	/**
	 * gets {@link KernelPathEntry} for given kernel name
	 * @param name String
	 * @return {@link URL}
	 */
	public KernelPathEntry getKernelPathEntryForName(String name){
		for(KernelPathEntry e : kernelsPaths){
			if(e.getName().equals(name)){
				return e;
			}
		}
		return null;
	}
	
	/**
	 * gets {@link KernelPathEntry} for given kernel id
	 * @param id {@link String}
	 * @return {@link KernelPathEntry}
	 */
	public KernelPathEntry getKernelPathEntryForId(String id){
		for(KernelPathEntry e: kernelsPaths){
			if(e.getId().equals(id)){
				return e;
			}
		}
		return null;
	}
	
	/**
	 * gets entry description
	 * @return {@link String}
	 */
	public String getDescription(){
		return description;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public String toString(){
		if(description!=null){
			return description+" ("+type.toString()+")";
		} else{
			return type.toString();
		}		
	}
}
