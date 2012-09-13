package pl.gda.pg.eti.kernelhive.repository.graph.node;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;
import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;

public class CompositeGraphNode extends GenericGraphNode {

	private static final long serialVersionUID = 2648665323946309816L;

	public CompositeGraphNode(String id) {
		super(id);
		type = GraphNodeType.COMPOSITE;
	}

	public CompositeGraphNode(String id, String name) {
		super(id, name);
		type = GraphNodeType.COMPOSITE;
	}

	@Override
	public boolean canAddFollowingNode(IGraphNode node) {
		return false;
	}

	@Override
	public boolean canAddPreviousNode(IGraphNode node) {
		return false;
	}

	@Override
	public boolean canAddChildNode(IGraphNode node) {
		return true;
	}

	@Override
	public List<ValidationResult> validate() {
		List<ValidationResult> results = new ArrayList<ValidationResult>();

		// no validation on chidren nodes

		// previous nodes? (must be 1 or 0)
		if (getPreviousNodes() != null && getPreviousNodes().size() > 1) {
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has more then 1 previous node",
					ValidationResultType.INVALID));
		}
		// following nodes? (must be 1 or 0)
		if (getFollowingNodes() != null || getFollowingNodes().size() > 1) {
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

}
