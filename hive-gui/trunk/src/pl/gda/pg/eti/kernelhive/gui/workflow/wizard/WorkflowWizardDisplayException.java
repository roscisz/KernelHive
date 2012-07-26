package pl.gda.pg.eti.kernelhive.gui.workflow.wizard;

/**
 * 
 * @author mschally
 *
 */
public class WorkflowWizardDisplayException extends Exception {

	private static final long serialVersionUID = -2980962672689694025L;

	public WorkflowWizardDisplayException(){
		super();
	}
	
	public WorkflowWizardDisplayException(String message){
		super(message);
	}
	
	public WorkflowWizardDisplayException(Throwable t){
		super(t);
	}
	
	public WorkflowWizardDisplayException(String message, Throwable t){
		super(message, t);
	}
}
