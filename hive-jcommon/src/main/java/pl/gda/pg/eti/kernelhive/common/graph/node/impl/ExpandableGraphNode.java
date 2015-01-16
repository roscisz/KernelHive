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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class ExpandableGraphNode extends GenericGraphNode {

	private static final String MAX_INSTANCES = "max";
	private static final String MIN_INSTANCES = "min";

	/**
	 * 
	 */
	private static final long serialVersionUID = -946871554482056013L;

	public ExpandableGraphNode(final String id) {
		super(id);
		type = GraphNodeType.EXPANDABLE;
		initProperties();
	}

	public ExpandableGraphNode(final String id, final String name) {
		super(id, name);
		type = GraphNodeType.EXPANDABLE;
		initProperties();
	}

	public ExpandableGraphNode(final String id, final String name,
			final List<IGraphNode> followingNodes,
			final List<IGraphNode> childrenNodes,
			final List<IGraphNode> previousNodes,
			final Map<String, Object> properties) {
		super(id, name, followingNodes, childrenNodes, previousNodes,
				properties);
		type = GraphNodeType.EXPANDABLE;
		initProperties();
	}

	private void initProperties() {
		this.properties.put(MAX_INSTANCES, "10");
		this.properties.put(MIN_INSTANCES, "1");

	}

	@Override
	public boolean canAddFollowingNode(final IGraphNode node) {
		if (followingNodes.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean canAddPreviousNode(final IGraphNode node) {
		if (previousNodes.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean canAddChildNode(final IGraphNode node) {
		return false;
	}

	@Override
	public List<ValidationResult> validate() {
		final List<ValidationResult> results = new ArrayList<ValidationResult>();
		// children nodes? (must be 0)
		if (!(getChildrenNodes() != null && getChildrenNodes().size() == 0)) {
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has children nodes",
					ValidationResultType.INVALID));
		}
		// previous nodes? (must be 1 or 0)
		if (!(getPreviousNodes() != null && getPreviousNodes().size() <= 1)) {
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has more then 1 previous node",
					ValidationResultType.INVALID));
		}
		// following nodes? (must be 1 or 0)
		if (!(getFollowingNodes() != null && getFollowingNodes().size() <= 1)) {
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has more than 1 following node",
					ValidationResultType.INVALID));
		}

		// previous validations ok?
		if (results.size() == 0) {
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") validated correctly",
					ValidationResultType.VALID));
		}
		return results;
	}

	public int getMaxInstances() {
		return Integer.parseInt((String) this.getProperties()
				.get(MAX_INSTANCES));
	}

	public int getMinInstances() {
		return Integer.parseInt((String) this.getProperties()
				.get(MIN_INSTANCES));
	}

}
