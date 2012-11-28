package pl.gda.pg.eti.kernelhive.common.graph.node;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;

/**
 * 
 * @author mschally
 *
 */
public abstract class AbstractGraphNodeDecorator {

	protected IGraphNode node;
	
	public AbstractGraphNodeDecorator(IGraphNode node){
		this.node = node;
	}
	
	public IGraphNode getGraphNode(){
		return node;
	}
	
	public abstract List<ValidationResult>  validate();
	
	protected boolean isValidationSuccess(List<ValidationResult> results) {
		for (ValidationResult r : results) {
			if (r.getType().equals(ValidationResultType.INVALID)) {
				return false;
			}
		}
		return true;
	}
}
