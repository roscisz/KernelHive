package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class PartitionerGraphNode extends GenericGraphNode {

	private static final long serialVersionUID = -1053818034497261900L;

	public PartitionerGraphNode(String id) {
		super(id);
		type = GraphNodeType.PARTITIONER;
	}
	
	public PartitionerGraphNode(String id, String name){
		super(id, name);
		type = GraphNodeType.PARTITIONER;
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
		//FIXME
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		results.add(new ValidationResult("OK", ValidationResultType.VALID));
		return results;
	}

}
