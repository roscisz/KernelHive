package pl.gda.pg.eti.kernelhive.gui.workflow.execution;

import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.networking.IWorkflowService;
import pl.gda.pg.eti.kernelhive.gui.networking.WorkflowService;
import pl.gda.pg.eti.kernelhive.gui.networking.WorkflowServiceException;
import pl.gda.pg.eti.kernelhive.gui.networking.WorkflowServiceListenerAdapter;

public class WorkflowExecution implements IWorkflowExecution {
	
	private byte[] graphStream = null;
	private String username = null;
	private char[] password = null;
	private IWorkflowService service = null;
	private WorkflowServiceListenerAdapter serviceAdapter = null;
	private List<WorkflowExecutionListener> listeners;
	
	public WorkflowExecution(){
		try {
			service = new WorkflowService();
		} catch (WorkflowServiceException e) {
			e.printStackTrace();
		}
		initListeners();
	}
	
	private void initListeners(){
		serviceAdapter = new WorkflowServiceListenerAdapter() {
			@Override
			public void submitWorkflowCompleted(Integer workflowId) {
				for(WorkflowExecutionListener l : listeners){
					l.workflowSubmissionCompleted(workflowId);
				}
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
		if(graphStream!=null&&service!=null){
			service.submitWorkflow(new String(graphStream), serviceAdapter);
		}	
	}

	@Override
	public void addWorkflowExecutionListener(WorkflowExecutionListener listener) {
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}

	@Override
	public void removeWorkflowExecutionListener(
			WorkflowExecutionListener listener) {
		if(listeners.contains(listener)){
			listeners.remove(listener);
		}
	}
}
