package pl.gda.pg.eti.kernelhive.gui.networking;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;

public interface WorkflowServiceListener {

	void infrastractureBrowseCompleted(List<ClusterInfo> clusterInfo);
	void getWorkflowResultsCompleted(String result);
	void submitWorkflowCompleted(Integer workflowId);
	void workflowBrowseCompleted(List<WorkflowInfo> workflowInfo);
	void terminateWorkflowCompleted();

}
