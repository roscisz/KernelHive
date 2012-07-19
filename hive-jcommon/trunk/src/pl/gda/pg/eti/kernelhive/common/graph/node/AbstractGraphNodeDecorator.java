package pl.gda.pg.eti.kernelhive.common.graph.node;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;

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
}
