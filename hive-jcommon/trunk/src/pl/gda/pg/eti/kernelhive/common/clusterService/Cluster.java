package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;

public class Cluster extends HasID {
	
	public String hostname;
	public int TCPPort;
	public int UDPPort;
	public List<Unit> unitList = new ArrayList<Unit>();
	@XmlTransient
	public List<Job> jobsToRun = new ArrayList<Job>();
	
	public void runJob(Job jobToRun) {
		jobsToRun.add(jobToRun);
		notifyAll();
	}

	public ClusterInfo getClusterInfo() {
		StringBuilder sb = new StringBuilder();
		
		for(Unit unit : unitList)
			for(Device device : unit.devices)
				sb.append(device.toString());
		
		return new ClusterInfo(sb.toString());
	}
	
}
