package pl.gda.pg.eti.kernelhive.repository.graph.node;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeTypeFactory;
import pl.gda.pg.eti.kernelhive.repository.graph.node.type.IGraphNodeTypeFactory;

public class PartitionerGraphNode extends GenericGraphNode {

	private static final long serialVersionUID = -1053818034497261900L;

	public PartitionerGraphNode(String id) {
		super(id);
		IGraphNodeTypeFactory factory = new GraphNodeTypeFactory();
		type = factory.createGraphNodeType(GraphNodeTypeFactory.DATA_PARTITIONER_TYPE);
	}
	
	public PartitionerGraphNode(String id, String name){
		super(id, name);
		IGraphNodeTypeFactory factory = new GraphNodeTypeFactory();
		type = factory.createGraphNodeType(GraphNodeTypeFactory.DATA_PARTITIONER_TYPE);
	}
	
	@Override
	public boolean canAddFollowingNode(IGraphNode node) {
		return true;
	}

	@Override
	public boolean canAddPreviousNode(IGraphNode node) {
		if(previousNodes.size()==0){
			return true;
		} else{
			return false;
		}
	}

	@Override
	public boolean canAddChildNode(IGraphNode node) {
		return false;
	}

	@Override
	public List<ValidationResult> validate() {
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		//children nodes? (must be 0)
		if (getChildrenNodes() != null && getChildrenNodes().size() > 0) {
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has children nodes",
					ValidationResultType.INVALID));
		}
		//previous nodes? (must be 1 or 0)
		if(getPreviousNodes()!=null&&getPreviousNodes().size()>1){
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has more then 1 previous node",
					ValidationResultType.INVALID));
		}
		//following nodes? (must be at least 1)
		if(getFollowingNodes()==null||getFollowingNodes().size()<1){
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has less than 1 following node",
					ValidationResultType.INVALID));
		}
		
		//previous validations ok?
		if(results.size()==0){
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") validated correctly", ValidationResultType.VALID));
		}		
		return results;
	}

}
