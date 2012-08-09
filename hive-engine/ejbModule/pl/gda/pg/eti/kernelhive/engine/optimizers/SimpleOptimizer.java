package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.clusterService.Workflow;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IOptimizer;

public class SimpleOptimizer implements IOptimizer {

	@Override
	public List<Job> processWorkflow(Workflow workflow, Collection<Cluster> infrastructure) {
		List<Job> readyJobs = workflow.getReadyJobs();
		List<Job> scheduledJobs = new ArrayList<Job>();

		if(readyJobs.size() == 0) {
			if(workflow.checkFinished())
				; // przygotowujemy wyniki do pobrania
			else
				; // coś się zablokowało
		}
		
		System.out.println("scheduling: ");
		for(Job readyJob : readyJobs) {
			System.out.println("Job: " + readyJob);
			for(Cluster cluster : infrastructure) {
				for(Unit unit : cluster.unitList)
					for(Device device : unit.devices) {
						System.out.println("Trying device " + device);
						if(!device.busy && device.canBeScheduledOn(device)) {
							readyJob.schedule(device);
							scheduledJobs.add(readyJob);
						}
					}
				if(readyJob.state == JobState.SCHEDULED) {
					System.out.println("Scheduled job.");
					break;
				}
			}
		}
				
		return scheduledJobs;
	}

}
