package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class ProcessorGraphNode extends GenericGraphNode {

	private static final long serialVersionUID = 2692263998267451932L;

	public ProcessorGraphNode(String id) {
		super(id);
		type = GraphNodeType.PROCESSOR;
	}
	
	public ProcessorGraphNode(String id, String name){
		super(id, name);
		type = GraphNodeType.PROCESSOR;
	}
	
	@Override
	public boolean canAddFollowingNode(IGraphNode node) {
		if(followingNodes.size()==0){
			return true;
		} else{
			return false;
		}
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
		//following nodes? (must be 1 or 0)
		if(getFollowingNodes()!=null&&getFollowingNodes().size()>1){
			results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
					+ name + ") of type '" + type
					+ "' cannot has more than 1 following node",
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
