package pl.gda.pg.eti.kernelhive.engine.interfaces;
import javax.ejb.Remote;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.JobInfo;

@Remote
public interface IClusterBeanRemote {
	
	public void update(Cluster data);
	public JobInfo getJob();
	public void reportOver(int jobID, String returnData);
	public void reportProgress(int jobID, int progress);
	//public void returnResult(byte[] result);

}
