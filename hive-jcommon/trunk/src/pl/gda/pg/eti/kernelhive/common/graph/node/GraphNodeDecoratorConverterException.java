package pl.gda.pg.eti.kernelhive.common.graph.node;

public class GraphNodeDecoratorConverterException extends Exception {

	private static final long serialVersionUID = 1626571766765434951L;
	
	public GraphNodeDecoratorConverterException(){
		super();
	}
	
	public GraphNodeDecoratorConverterException(String message){
		super(message);
	}
	
	public GraphNodeDecoratorConverterException(Throwable t){
		super(t);
	}
	
	public GraphNodeDecoratorConverterException(String message, Throwable t){
		super(message, t);
	}

}
