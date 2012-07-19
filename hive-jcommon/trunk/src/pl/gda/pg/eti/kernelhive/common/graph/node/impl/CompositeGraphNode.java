package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class CompositeGraphNode extends GenericGraphNode {

	private static final long serialVersionUID = 2648665323946309816L;

	public CompositeGraphNode(String id) {
		super(id);
		type = GraphNodeType.COMPOSITE;
	}
	
	public CompositeGraphNode(String id, String name){
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
		//FIXME
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		results.add(new ValidationResult("OK", ValidationResultType.VALID));
		return results;
	}

}
