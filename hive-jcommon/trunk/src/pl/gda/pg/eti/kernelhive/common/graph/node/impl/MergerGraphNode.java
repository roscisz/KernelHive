package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class MergerGraphNode extends GenericGraphNode {

	private static final long serialVersionUID = 3335110581355051053L;

	public MergerGraphNode(String id) {
		super(id);
		type = GraphNodeType.MERGER;
	}
	
	public MergerGraphNode(String id, String name){
		super(id, name);
		type = GraphNodeType.MERGER;
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
		return true;
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
