package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.engine.Workflow;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IOptimizer;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;

public class PrefetchingOptimizer implements IOptimizer {

	private Map<EngineJob, EngineJob> prefetchingMap = new HashMap<EngineJob, EngineJob>();
	private IOptimizer baseOptimizer;
	
	public PrefetchingOptimizer(IOptimizer baseOptimizer) {
		this.baseOptimizer = baseOptimizer;
	}
	
	@Override
	public List<Job> processWorkflow(Workflow workflow,
			Collection<Cluster> infrastructure) {
		// First schedule jobs that were prefetched
		List<Job> scheduledJobs = schedulePrefetchedJobs();
		
		System.out.println(scheduledJobs.size() + " after schedulePrefetchedJobs");
				
		// Then schedule jobs to free resources
		scheduledJobs.addAll(baseOptimizer.processWorkflow(workflow, infrastructure));
		
		// Then start prefetchings
		List<EngineJob> processingJobs = workflow.getJobsByState(Job.JobState.PROCESSING);
		processingJobs.removeAll(prefetchingMap.keySet());
					
		if(processingJobs.size() > 0) {
			List<EngineJob> readyJobs = workflow.getJobsByState(Job.JobState.READY);
			Iterator<EngineJob> readyIterator = readyJobs.iterator();
			for(EngineJob pj : processingJobs) {
				if(!readyIterator.hasNext())
					break;
				
				EngineJob prefetchingJob = readyIterator.next();
				
				prefetchingJob.device = pj.device;				
				prefetchingJob.runPrefetching();
				
				prefetchingMap.put(pj, prefetchingJob);
			}
		}

		return scheduledJobs;
	}

	private List<Job> schedulePrefetchedJobs() {
		List<Job> scheduledJobs = new ArrayList<Job>();
		List<EngineJob> toRemove = new ArrayList<EngineJob>();
		for(EngineJob processingJob : prefetchingMap.keySet()) {
			System.out.println("schedule prefetched examining " + processingJob.getId());
			if(processingJob.state == JobState.FINISHED) {
				System.out.println("finished ok");
				EngineJob prefetchingJob = prefetchingMap.get(processingJob);
				if(prefetchingJob.state == JobState.PREFETCHING_FINISHED) {
					System.out.println("Second pref finished, ok :)");
					prefetchingJob.schedule(prefetchingJob.device);
					scheduledJobs.add(prefetchingJob);
					toRemove.add(processingJob);					
				}
				// Do not schedule new job if we are waiting for prefetching
				else prefetchingJob.device.busy = true;
			}
		}	
		
		for(EngineJob tr : toRemove)
			prefetchingMap.remove(tr);
		
		return scheduledJobs;
	}

}
