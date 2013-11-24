package pl.gda.pg.eti.kernelhive.engine.interfaces;

import java.util.List;

import javax.ejb.Remote;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.JobProgress;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;

@Remote
public interface IClientBeanRemote {

	// TODO: uwierzytelnienie
	public Integer submitWorkflow(String graphConfigurationString);

	public List<WorkflowInfo> browseWorkflows();

	public String getWorkflowResults(Integer workflowID);

	public void terminateWorkflow(Integer workflowID);

	public List<JobProgress> getWorkflowProgress(Integer workflowID);

	public List<ClusterInfo> browseInfrastructure();
}
