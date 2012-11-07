package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.engine.Workflow;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IOptimizer;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;

public class SimpleOptimizer implements IOptimizer {

	@Override
	public List<Job> processWorkflow(Workflow workflow, Collection<Cluster> infrastructure) {
		List<EngineJob> readyJobs = workflow.getReadyJobs();
		List<Job> scheduledJobs = new ArrayList<Job>();

		if(readyJobs.size() == 0) {
			if(workflow.checkFinished())
				; // przygotowujemy wyniki do pobrania
			else
				; // coś się zablokowało
		}
				
		for(Job readyJob : readyJobs) {
			System.out.println("Ready job " + readyJob);
			etiquete: 
			for(Cluster cluster : infrastructure) {
				System.out.println("Cluster: " + cluster);
				for(Unit unit : cluster.unitList) {
					System.out.println("Unit: " + unit);
					for(Device device : unit.devices) {
						System.out.println("Dev: " + device);
						if(!device.busy && readyJob.canBeScheduledOn(device)) {
							readyJob.schedule(device);
							scheduledJobs.add(readyJob);
						}
						if(readyJob.state == JobState.SCHEDULED) {
							System.out.println("Scheduled job.");
							break etiquete;
						}						
					}
				}				
			}
		}			
		return scheduledJobs;
	}
}
