package pl.gda.pg.eti.kernelhive.engine.interfaces;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;

public interface IJobOptimizer {
	
	public List<Job> scheduleJobs(List<Job> jobs, List<Device> infrastructure);

}
