package pl.gda.pg.eti.kernelhive.gui.networking;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.gda.pg.eti.kernelhive.common.clientService.ClientBeanService;
import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;

/**
 * 
 * @author mschally
 *
 */
public class WorkflowService implements IWorkflowService {

	private ExecutorService executorService;
	private ClientBeanService clientService;	
	
	public WorkflowService(){
		executorService = Executors.newFixedThreadPool(5);
		clientService = new ClientBeanService();
	}


	@Override
	public void browseInfrastructure(final WorkflowServiceListener listener) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				List<ClusterInfo> clusterInfo = clientService.getClientBeanPort().browseInfrastructure();
				listener.infrastractureBrowseCompleted(clusterInfo);
			}
		};
		executorService.execute(r);
	}


	@Override
	public void getWorkflowResults(final Integer workflowId,
			final WorkflowServiceListener listener) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				String results = clientService.getClientBeanPort().getWorkflowResults(workflowId);
				listener.getWorkflowResultsCompleted(results);
			}
		};
		executorService.execute(r);
	}


	@Override
	public void terminateWorkflow(final Integer workflowId,
			final WorkflowServiceListener listener) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				clientService.getClientBeanPort().terminateWorkflow(workflowId);
				listener.terminateWorkflowCompleted();
			}
		};
		executorService.execute(r);
	}


	@Override
	public void browseWorkflows(final WorkflowServiceListener listener) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				List<WorkflowInfo> workflowInfo = clientService.getClientBeanPort().browseWorkflows();
				listener.workflowBrowseCompleted(workflowInfo);
			}
		};
		executorService.execute(r);
	}


	@Override
	public void submitWorkflow(final String workflowStream,
			final WorkflowServiceListener listener) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				Integer workflowId = clientService.getClientBeanPort().submitWorkflow(workflowStream);
				listener.submitWorkflowCompleted(workflowId);
			}
		};
		executorService.execute(r);
	}
}
