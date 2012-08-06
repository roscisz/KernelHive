package pl.gda.pg.eti.kernelhive.gui.networking;

public class WorkflowServiceException extends Exception {

	private static final long serialVersionUID = 6356306182848182842L;

	public WorkflowServiceException(){
		super();
	}
	
	public WorkflowServiceException(String message){
		super(message);
	}
	
	public WorkflowServiceException(Throwable t){
		super(t);
	}
	
	public WorkflowServiceException(String message, Throwable t){
		super(message, t);
	}
}
