package pl.gda.pg.eti.kernelhive.common.graph.builder;

/**
 * graph node builder exception
 * @author mschally
 *
 */
public class GraphNodeBuilderException extends Exception {

	private static final long serialVersionUID = 8082762799415111721L;
	
	public GraphNodeBuilderException(){
		super();
	}
	
	public GraphNodeBuilderException(String message){
		super(message);
	}
	
	public GraphNodeBuilderException(Throwable t){
		super(t);
	}
	
	public GraphNodeBuilderException(String message, Throwable t){
		super(message, t);
	}
}
