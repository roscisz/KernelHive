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
		//FIXME
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		results.add(new ValidationResult("OK", ValidationResultType.VALID));
		return results;
	}
}
