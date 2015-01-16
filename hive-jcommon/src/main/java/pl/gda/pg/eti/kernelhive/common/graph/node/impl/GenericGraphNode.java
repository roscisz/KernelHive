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
package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class GenericGraphNode implements IGraphNode, Serializable {

	private static final long serialVersionUID = 7431190941339020419L;
	protected IGraphNode parentNode;
	protected final String nodeId;
	protected String name;
	protected GraphNodeType type = GraphNodeType.GENERIC;
	protected List<IGraphNode> followingNodes;
	protected List<IGraphNode> previousNodes;
	protected List<IGraphNode> childrenNodes;
	protected Map<String, Object> properties;

	public GenericGraphNode(final String id) {
		followingNodes = new ArrayList<IGraphNode>();
		previousNodes = new ArrayList<IGraphNode>();
		childrenNodes = new ArrayList<IGraphNode>();
		nodeId = id;
		properties = new HashMap<String, Object>();
		parentNode = null;
	}

	public GenericGraphNode(final String id, final String name) {
		followingNodes = new ArrayList<IGraphNode>();
		previousNodes = new ArrayList<IGraphNode>();
		childrenNodes = new ArrayList<IGraphNode>();
		nodeId = id;
		this.name = name;
		properties = new HashMap<String, Object>();
		parentNode = null;
	}

	public GenericGraphNode(final String id, final String name,
			final List<IGraphNode> followingNodes,
			final List<IGraphNode> childrenNodes,
			final List<IGraphNode> previousNodes,
			final Map<String, Object> properties) {
		nodeId = id;
		this.name = name;
		parentNode = null;
		if (followingNodes != null) {
			this.followingNodes = followingNodes;
		} else {
			this.followingNodes = new ArrayList<IGraphNode>();
		}
		if (childrenNodes != null) {
			this.childrenNodes = childrenNodes;
		} else {
			this.childrenNodes = new ArrayList<IGraphNode>();
		}
		if (previousNodes != null) {
			this.previousNodes = previousNodes;
		} else {
			this.previousNodes = new ArrayList<IGraphNode>();
		}
		if (properties != null) {
			this.properties = properties;
		} else {
			this.properties = new HashMap<String, Object>();
		}
	}

	@Override
	public List<IGraphNode> getFollowingNodes() {
		return followingNodes;
	}

	@Override
	public List<IGraphNode> getPreviousNodes() {
		return previousNodes;
	}

	@Override
	public List<IGraphNode> getChildrenNodes() {
		return childrenNodes;
	}

	@Override
	public IGraphNode getParentNode() {
		return parentNode;
	}

	@Override
	public void setParentNode(final IGraphNode node) {
		if (parentNode != null) {
			parentNode.getChildrenNodes().remove(this);
			disconnectPreviousNodes(this);
			disconnectFollowingNodes(this);
		}
		parentNode = node;
		if (parentNode != null) {
			parentNode.getChildrenNodes().add(this);
			disconnectPreviousNodes(this);
			disconnectFollowingNodes(this);
		}
	}

	@Override
	public String getNodeId() {
		return nodeId;
	}

	@Override
	public boolean addFollowingNode(final IGraphNode node) {
		if (!followingNodes.contains(node) && canAddFollowingNode(node)) {
			followingNodes.add(node);
			final boolean result = node.addPreviousNode(this);
			if (result == false) {
				followingNodes.remove(node);
				node.removePreviousNode(node);
			}
			return result;
		} else {
			return true;
		}
	}

	@Override
	public boolean removeFollowingNode(final IGraphNode node) {
		if (node != null && followingNodes.contains(node)) {
			boolean result = followingNodes.remove(node);
			result &= node.removePreviousNode(this);
			return result;
		} else {
			return true;
		}
	}

	@Override
	public boolean addPreviousNode(final IGraphNode node) {
		if (!previousNodes.contains(node) && canAddPreviousNode(node)) {
			previousNodes.add(node);
			node.addFollowingNode(this);
			return true;
		} else {
			return true;
		}
	}

	@Override
	public boolean removePreviousNode(final IGraphNode node) {
		if (node != null && previousNodes.contains(node)) {
			boolean result = previousNodes.remove(node);
			result &= node.removeFollowingNode(this);
			return result;
		} else {
			return true;
		}
	}

	@Override
	public boolean addChildNode(final IGraphNode node) {
		if (!childrenNodes.contains(node) && canAddChildNode(node)) {
			childrenNodes.add(node);
			node.setParentNode(this);
			disconnectPreviousNodes(node);
			disconnectFollowingNodes(node);
			return true;
		} else {
			return true;
		}
	}

	@Override
	public boolean removeChildNode(final IGraphNode node) {
		if (childrenNodes.contains(node)) {
			final boolean result = childrenNodes.remove(node);
			node.setParentNode(null);
			disconnectPreviousNodes(node);
			disconnectFollowingNodes(node);
			return result;
		} else {
			return true;
		}
	}

	private void disconnectPreviousNodes(final IGraphNode node) {
		final List<IGraphNode> list = new ArrayList<IGraphNode>(
				node.getPreviousNodes());
		Collections.copy(list, node.getPreviousNodes());
		for (final IGraphNode pNode : list) {
			node.removePreviousNode(pNode);
		}
	}

	private void disconnectFollowingNodes(final IGraphNode node) {
		final List<IGraphNode> list = new ArrayList<IGraphNode>(
				node.getFollowingNodes());
		Collections.copy(list, node.getFollowingNodes());
		for (final IGraphNode fNode : list) {
			node.removeFollowingNode(fNode);
		}
	}

	@Override
	public boolean canAddFollowingNode(final IGraphNode node) {
		return true;
	}

	@Override
	public boolean canAddPreviousNode(final IGraphNode node) {
		return true;
	}

	@Override
	public boolean canAddChildNode(final IGraphNode node) {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result
				+ ((parentNode == null) ? 0 : parentNode.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final GenericGraphNode other = (GenericGraphNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		if (parentNode == null) {
			if (other.parentNode != null)
				return false;
		} else if (!parentNode.equals(other.parentNode))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + " (" + type.toString() + ")";
	}

	@Override
	public List<ValidationResult> validate() {
		final List<ValidationResult> results = new ArrayList<ValidationResult>();
		results.add(new ValidationResult("OK", ValidationResultType.VALID));
		return results;
	}

	@Override
	public GraphNodeType getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public void setProperties(final Map<String, Object> properties) {
		this.properties = properties;
	}

}
