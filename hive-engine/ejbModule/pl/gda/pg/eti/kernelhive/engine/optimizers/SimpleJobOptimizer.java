package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IJobOptimizer;

public class SimpleJobOptimizer implements IJobOptimizer {

	@Override
	public List<Job> scheduleJobs(List<Job> jobs, List<Device> devices) {
		List<Job> scheduledJobs = new ArrayList<Job>();
		for(Job job : jobs) {
			Job scheduledJob = tryToSchedule(job, devices);
			// TODO: alternatively stop scheduling if null
			if(scheduledJob != null)
				scheduledJobs.add(scheduledJob);
		}
		return scheduledJobs;
	}

	private Job tryToSchedule(Job job, List<Device> devices) {
		//IGraphNode node = job.node.getGraphNode();		
		//node.getPreviousNodes().get("run-on");
		
		for(Device device : devices)
			if(!device.busy) {
				job.device = device;
				job.state = JobState.SCHEDULED;
				return job;
			}		
		return null;
	}

}
