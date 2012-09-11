package pl.gda.pg.eti.kernelhive.gui.networking;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.ws.WebServiceException;

import pl.gda.pg.eti.kernelhive.common.clientService.ClientBeanService;
import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;

/**
 * 
 * @author mschally
 *
 */
public class ExecutionEngineService implements IExecutionEngineService {

	private static ExecutionEngineService _service;
	private ExecutorService executorService;
	private ClientBeanService clientService;
	
	public static ExecutionEngineService getInstance() throws ExecutionEngineServiceException{
		if(_service==null){
			_service = new ExecutionEngineService();
		}
		return _service;
	}
	
	private ExecutionEngineService() throws ExecutionEngineServiceException{
		executorService = Executors.newFixedThreadPool(5);
		try{
		clientService = new ClientBeanService();
		} catch(WebServiceException e){
			throw new ExecutionEngineServiceException(e);
		}
	}

	@Override
	public void browseInfrastructure(final ExecutionEngineServiceListener listener) {
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
			final ExecutionEngineServiceListener listener) {
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
			final ExecutionEngineServiceListener listener) {
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
	public void browseWorkflows(final ExecutionEngineServiceListener listener) {
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
			final ExecutionEngineServiceListener listener) {
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
