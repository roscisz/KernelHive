package pl.gda.pg.eti.kernelhive.gui.networking;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;

public abstract class ExecutionEngineServiceListenerAdapter implements ExecutionEngineServiceListener {

	@Override
	public void infrastractureBrowseCompleted(List<ClusterInfo> clusterInfo) {
		
	}

	@Override
	public void getWorkflowResultsCompleted(String result) {

	}

	@Override
	public void submitWorkflowCompleted(Integer workflowId) {

	}

	@Override
	public void workflowBrowseCompleted(List<WorkflowInfo> workflowInfo) {

	}

	@Override
	public void terminateWorkflowCompleted() {

	}

}
