package pl.gda.pg.eti.kernelhive.gui.workflow.execution;

/**
 * 
 * @author mschally
 *
 */
public interface IWorkflowExecution {
	/**
	 * 
	 * @param stream
	 */
	void setSerializedGraphStream(byte[] stream);
	/**
	 * 
	 * @param username
	 */
	void setUsername(String username);
	/**
	 * 
	 * @param password
	 */
	void setPassword(char[] password);
	/**
	 * 
	 */
	void submitForExecution();
	/**
	 * 
	 * @param listener
	 */
	void addWorkflowExecutionListener(WorkflowExecutionListener listener);
	/**
	 * 
	 * @param listener
	 */
	void removeWorkflowExecutionListener(WorkflowExecutionListener listener);
}
