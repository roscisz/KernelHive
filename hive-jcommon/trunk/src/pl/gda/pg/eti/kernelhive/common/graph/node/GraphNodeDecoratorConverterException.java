package pl.gda.pg.eti.kernelhive.common.graph.node;

public class GraphNodeDecoratorConverterException extends Exception {

	private static final long serialVersionUID = 1626571766765434951L;
	
	private AbstractGraphNodeDecorator nodeDecorator;
	
	public GraphNodeDecoratorConverterException(AbstractGraphNodeDecorator nodeDecorator){
		super();
		this.nodeDecorator = nodeDecorator;
	}
	
	public GraphNodeDecoratorConverterException(String message, AbstractGraphNodeDecorator nodeDecorator){
		super(message);
		this.nodeDecorator = nodeDecorator;
	}
	
	public GraphNodeDecoratorConverterException(Throwable t, AbstractGraphNodeDecorator nodeDecorator){
		super(t);
		this.nodeDecorator = nodeDecorator;
	}
	
	public GraphNodeDecoratorConverterException(String message, Throwable t, AbstractGraphNodeDecorator nodeDecorator){
		super(message, t);
		this.nodeDecorator = nodeDecorator;
	}
	
	public AbstractGraphNodeDecorator getNodeDecorator(){
		return nodeDecorator;
	}

}
