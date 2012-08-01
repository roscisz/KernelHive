package pl.gda.pg.eti.kernelhive.engine.interfaces;
import java.util.List;

import javax.ejb.Remote;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Workflow;

@Remote
public interface IClientBeanRemote {
	
	// TODO: uwierzytelnienie
	public Integer runWorkflow(String graphConfigurationString);
	public List<WorkflowInfo> browseWorkflows();
	public String getResults(Integer workflowID);
	public void deleteWorkflow(Integer workflowID);
	public List<ClusterInfo> browseInfrastructure();

}
