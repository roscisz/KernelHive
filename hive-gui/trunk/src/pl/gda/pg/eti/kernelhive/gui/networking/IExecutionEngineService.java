package pl.gda.pg.eti.kernelhive.gui.networking;

/**
 * workflow service interface
 * @author mschally
 *
 */
public interface IExecutionEngineService {
	/**
	 * invokes browse infrastructure action 
	 * @param listener, listener implementing callback method for this action
	 */
	void browseInfrastructure(ExecutionEngineServiceListener listener);
	/**
	 * invokes get workflow results action
	 * @param workflowId, id of the specific workflow we want to retrieve results
	 * @param listener, listener implementing callback method for this action
	 */
	void getWorkflowResults(Integer workflowId, ExecutionEngineServiceListener listener);
	/**
	 * invokes terminate workflow action
	 * @param workflowId, id of the workflow to terminate
	 * @param listener, listener implementing callback method for this action
	 */
	void terminateWorkflow(Integer workflowId, ExecutionEngineServiceListener listener);
	/**
	 * invokes browse workflows action
	 * @param listener, listener implementing callback method for this action
	 */
	void browseWorkflows(ExecutionEngineServiceListener listener);
	/**
	 * invokes submit workflow for execution action
	 * @param workflowStream, workflow description serialized to xml format
	 * @param listener, listener implementing callback method for this action
	 */
	void submitWorkflow(String workflowStream, ExecutionEngineServiceListener listener);
}
