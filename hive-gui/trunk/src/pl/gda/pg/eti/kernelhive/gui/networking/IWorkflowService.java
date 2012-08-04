package pl.gda.pg.eti.kernelhive.gui.networking;

/**
 * workflow service interface
 * @author mschally
 *
 */
public interface IWorkflowService {
	/**
	 * invokes browse infrastructure action 
	 * @param listener, listener implementing callback method for this action
	 */
	void browseInfrastructure(WorkflowServiceListener listener);
	/**
	 * invokes get workflow results action
	 * @param workflowId, id of the specific workflow we want to retrieve results
	 * @param listener, listener implementing callback method for this action
	 */
	void getWorkflowResults(Integer workflowId, WorkflowServiceListener listener);
	/**
	 * invokes terminate workflow action
	 * @param workflowId, id of the workflow to terminate
	 * @param listener, listener implementing callback method for this action
	 */
	void terminateWorkflow(Integer workflowId, WorkflowServiceListener listener);
	/**
	 * invokes browse workflows action
	 * @param listener, listener implementing callback method for this action
	 */
	void browseWorkflows(WorkflowServiceListener listener);
	/**
	 * invokes submit workflow for execution action
	 * @param workflowStream, workflow description serialized to xml format
	 * @param listener, listener implementing callback method for this action
	 */
	void submitWorkflow(String workflowStream, WorkflowServiceListener listener);
}
