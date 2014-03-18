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
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.engine.HiveEngine;
import pl.gda.pg.eti.kernelhive.engine.Workflow;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IOptimizer;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;

public class SimpleOptimizer implements IOptimizer {

	@Override
	public List<Job> processWorkflow(Workflow workflow, Collection<Cluster> infrastructure) {
		List<EngineJob> readyJobs = workflow.getJobsByState(Job.JobState.READY);
		List<Job> scheduledJobs = new ArrayList<>();

		for (Job readyJob : readyJobs) {
			System.out.println("Trying to schedule job " + readyJob.getId());
			for (Device device : HiveEngine.getAvailableDevices(infrastructure)) {
				if (!device.isBusy()) {
					readyJob.schedule(device);
					scheduledJobs.add(readyJob);
				}
				if (readyJob.state == JobState.SCHEDULED) {
					System.out.println("Scheduled job " + readyJob.getId()
							+ " on device: " + device.name + " on unit "
							+ device.getUnit().getId());
					break;
				}
			}
		}
		return scheduledJobs;
	}
}
