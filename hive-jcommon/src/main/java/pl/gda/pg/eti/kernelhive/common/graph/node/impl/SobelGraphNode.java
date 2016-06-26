package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class SobelGraphNode extends GenericGraphNode {

	public SobelGraphNode(String id) {
		super(id);
		type = GraphNodeType.SOBEL;
	}

	@Override
	public boolean canAddFollowingNode(IGraphNode node) {
		return followingNodes.size() == 0 && node.getType().isImageType();
	}

	@Override
	public boolean canAddPreviousNode(IGraphNode node) {
		return previousNodes.size() == 0 && node.getType().isImageType();
	}

	@Override
	public boolean canAddChildNode(IGraphNode node) {
		return false;
	}

	@Override
	public List<ValidationResult> validate() {
		List<ValidationResult> results = new ArrayList<>();
		// children nodes? (must be 0)
		if (getChildrenNodes() != null && getChildrenNodes().size() > 0) {
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has children nodes",
					ValidationResult.ValidationResultType.INVALID));
		}
		// previous nodes? (must be 1 or 0)
		if (getPreviousNodes() != null && getPreviousNodes().size() > 1) {
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has more then 1 previous node",
					ValidationResult.ValidationResultType.INVALID));
		}
		// following nodes? (must be 1 or 0)
		if (getFollowingNodes() != null && getFollowingNodes().size() > 1) {
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has more than 1 following node",
					ValidationResult.ValidationResultType.INVALID));
		}

		// previous validations ok?
		if (results.size() == 0) {
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") validated correctly",
					ValidationResult.ValidationResultType.VALID));
		}
		return results;
	}

}
