package pl.gda.pg.eti.kernelhive.common.validation;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

public class GraphValidator {

	public static List<ValidationResult> validateGraph(List<IGraphNode> graphNodes){
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		results.addAll(validateStartGraphNodes(graphNodes));
		results.addAll(validateEndGraphNodes(graphNodes));
		for(IGraphNode node : graphNodes){
			results.addAll(node.validate());
		}
		//TODO validate children connections
		return null;
	}
	
	private static List<ValidationResult> validateStartGraphNodes(List<IGraphNode> graphNodes){
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		int count = 0;
		for(IGraphNode node : graphNodes){
			if((node.getParentNode()==null)&&(node.getPreviousNodes().size()==0)){
				count++;
			}
		}
		if(count>1){
			results.add(new ValidationResult("More than 1 start node is not allowed", ValidationResultType.INVALID));
		} else if(count==0){
			results.add(new ValidationResult("Cyclic graph detected - only directed acyclic graphs are permitted", ValidationResultType.INVALID));
		} else{
			results.add(new ValidationResult("Start Graph Nodes Validation OK", ValidationResultType.VALID));
		}
		return results;
	}
	
	private static List<ValidationResult> validateEndGraphNodes(List<IGraphNode> graphNodes){
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		int count = 0;
		for(IGraphNode node : graphNodes){
			if((node.getParentNode()==null)&&(node.getFollowingNodes().size()==0)){
				count++;
			}
		}
		if(count>1){
			results.add(new ValidationResult("More than 1 end node is not allowed", ValidationResultType.INVALID));
		} else if(count==0){
			results.add(new ValidationResult("Cyclic graph detected - only directed acyclic graphs are permitted", ValidationResultType.INVALID));
		} else{
			results.add(new ValidationResult("End Graph Nodes Validation OK", ValidationResultType.VALID));
		}
		return results;
	}
}
