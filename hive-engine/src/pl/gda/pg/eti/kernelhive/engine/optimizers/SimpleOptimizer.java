package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.engine.HiveEngine;
import pl.gda.pg.eti.kernelhive.engine.Workflow;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IOptimizer;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;

public class SimpleOptimizer implements IOptimizer {

	@Override
	public List<Job> processWorkflow(Workflow workflow, Collection<Cluster> infrastructure) {
		List<EngineJob> readyJobs = workflow.getReadyJobs();
		List<Job> scheduledJobs = new ArrayList<Job>();
					
		for(Job readyJob : readyJobs) {
			//System.out.println("Trying to schedule job " + readyJob.ID);
			for(Device device : HiveEngine.getAvailableDevices(infrastructure)) {
				if(!device.isBusy()) {
					readyJob.schedule(device);
					scheduledJobs.add(readyJob);
				}
				if(readyJob.state == JobState.SCHEDULED) {				
					System.out.println("Scheduled job " + readyJob.ID + " on device: " + device.name + " on unit " + device.unit.ID);
					break;
				}				
			}
		}			
		return scheduledJobs;
	}
}
