package pl.gda.pg.eti.kernelhive.gui.workflow.execution;

import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.networking.IWorkflowService;
import pl.gda.pg.eti.kernelhive.gui.networking.WorkflowService;
import pl.gda.pg.eti.kernelhive.gui.networking.WorkflowServiceListenerAdapter;

public class WorkflowExecution implements IWorkflowExecution {
	
	private byte[] graphStream = null;
	private String username = null;
	private char[] password = null;
	private IWorkflowService service;
	private WorkflowServiceListenerAdapter serviceAdapter;
	private List<WorkflowExecutionListener> listeners;
	
	public WorkflowExecution(){
		service = new WorkflowService();
		initListeners();
	}
	
	private void initListeners(){
		serviceAdapter = new WorkflowServiceListenerAdapter() {
			@Override
			public void submitWorkflowCompleted(Integer workflowId) {
				
			}
		};
	}
	
	@Override
	public void setSerializedGraphStream(byte[] stream) {
		graphStream = stream;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public void setPassword(char[] password) {
		this.password = password;
	}

	@Override
	public void submitForExecution() {
		if(graphStream!=null){
			service.submitWorkflow(new String(graphStream), serviceAdapter);
		}	
	}

	@Override
	public void addWorkflowExecutionListener(WorkflowExecutionListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeWorkflowExecutionListener(
			WorkflowExecutionListener listener) {
		// TODO Auto-generated method stub
		
	}
}
