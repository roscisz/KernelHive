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
package pl.gda.pg.eti.kernelhive.repository.graph.node;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;

public interface IGraphNode extends Serializable {

	String getNodeId();

	String getName();

	void setName(String name);

	GraphNodeType getType();

	List<IGraphNode> getFollowingNodes();

	boolean canAddFollowingNode(IGraphNode node);

	boolean addFollowingNode(IGraphNode node);

	boolean removeFollowingNode(IGraphNode node);

	List<IGraphNode> getPreviousNodes();

	boolean canAddPreviousNode(IGraphNode node);

	boolean addPreviousNode(IGraphNode node);

	boolean removePreviousNode(IGraphNode node);

	IGraphNode getParentNode();

	void setParentNode(IGraphNode node);

	List<IGraphNode> getChildrenNodes();

	boolean addChildNode(IGraphNode node);

	boolean removeChildNode(IGraphNode node);

	boolean canAddChildNode(IGraphNode node);

	Map<String, Object> getProperties();

	void setProperties(Map<String, Object> properties);

	List<ValidationResult> validate();
}
