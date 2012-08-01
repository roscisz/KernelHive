package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Workflow;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IOptimizer;

public class SimpleOptimizer implements IOptimizer {

	@Override
	public List<Job> processWorkflow(Workflow workflow) {
		List<Job> readyJobs = workflow.getReadyJobs();

		if(readyJobs.size() == 0) {
			if(workflow.checkFinished())
				; // przygotowujemy wyniki do pobrania
			else
				; // coś się zablokowało
		}
			
		
			// TODO: do your stuff
			//readyJobs = new SimpleTaskOptimizer().arrangeJobs(readyJobs);
			
			//List<Device> devices = new SimpleDeviceOptimizer().arrangeDevices(clusters.values());
			
			//readyJobs = new SimpleJobOptimizer().scheduleJobs(readyJobs, devices);
		
		return readyJobs;
	}

}
