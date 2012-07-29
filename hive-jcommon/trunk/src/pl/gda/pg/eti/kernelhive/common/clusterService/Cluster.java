package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.ArrayList;
import java.util.List;

public class Cluster extends HasID {
	
	public String hostname;
	public int TCPPort;
	public int UDPPort;
	public List<Unit> unitList = new ArrayList<Unit>();
	public List<Job> jobsToRun = new ArrayList<Job>();
	
	public void runJob(Job jobToRun) {
		jobsToRun.add(jobToRun);
		notifyAll();
	}
	
}
