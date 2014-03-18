/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
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
