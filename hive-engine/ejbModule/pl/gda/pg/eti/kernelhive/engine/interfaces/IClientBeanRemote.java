package pl.gda.pg.eti.kernelhive.engine.interfaces;
import java.util.List;

import javax.ejb.Remote;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Task;

@Remote
public interface IClientBeanRemote {
	
	// TODO: uwierzytelnienie
	public Integer runTask(String graphConfigurationString);
	public List<Task> browseTasks();
	public String getResults(Integer taskID);
	public void deleteTask(Integer taskID);
	public List<Cluster> browseInfrastructure();

}
