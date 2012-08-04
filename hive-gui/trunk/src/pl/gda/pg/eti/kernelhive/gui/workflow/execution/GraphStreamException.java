package pl.gda.pg.eti.kernelhive.gui.workflow.execution;

public class GraphStreamException extends Exception {

	private static final long serialVersionUID = 2070498065279061863L;

	public GraphStreamException(){
		super();
	}
	
	public GraphStreamException(String message){
		super(message);
	}
	
	public GraphStreamException(Throwable t){
		super(t);
	}
	
	public GraphStreamException(String message, Throwable t){
		super(message, t);
	}
}
