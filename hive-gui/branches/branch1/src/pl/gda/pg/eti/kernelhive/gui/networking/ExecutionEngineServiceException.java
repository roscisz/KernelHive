package pl.gda.pg.eti.kernelhive.gui.networking;

public class ExecutionEngineServiceException extends Exception {

	private static final long serialVersionUID = 6356306182848182842L;

	public ExecutionEngineServiceException(){
		super();
	}
	
	public ExecutionEngineServiceException(String message){
		super(message);
	}
	
	public ExecutionEngineServiceException(Throwable t){
		super(t);
	}
	
	public ExecutionEngineServiceException(String message, Throwable t){
		super(message, t);
	}
}
