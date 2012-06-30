package pl.gda.pg.eti.kernelhive.gui.validator;

import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.graph.IGraphNode;

public class GraphValidator {

	public static List<ValidationResult> validateGraph(List<IGraphNode> graphNodes){
		//TODO
		//validate start nodes according to policy
		int count = 0;
		for(IGraphNode node : graphNodes){
			if(node.getPreviousNodes().size()==0){
				count++;
			}
			
		}
		
		return null;
	}
}
