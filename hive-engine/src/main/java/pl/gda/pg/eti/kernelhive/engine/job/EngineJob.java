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
package pl.gda.pg.eti.kernelhive.engine.job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.DataAddress;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.engine.Workflow;

public class EngineJob extends Job {
	
	public List<EngineJob> followingJobs = new ArrayList<>();
	private List<EngineJob> precedingJobs = new ArrayList<>();
	public Workflow workflow;

	public EngineJob(EngineGraphNodeDecorator node, Workflow workflow) {
		super(node);
		this.workflow = workflow;
	}
	
	public void addFollowingJob(EngineJob followingJob) {
		this.followingJobs.add(followingJob);
		followingJob.precedingJobs.add(this);
		this.nOutputs = followingJobs.size();
	}
	
	public void tryCollectFollowingJobsData(List<DataAddress> resultAddresses) {
		Iterator<DataAddress> dataIterator = resultAddresses.iterator();
		int remainingData = resultAddresses.size();
		int done = 0;
		for(EngineJob followingJob : followingJobs) {
			int dataForJob = 0;
			int id = 0;
			if (followingJob.precedingJobs.size() > 1) {
				for (EngineJob j : followingJob.precedingJobs) {
					dataForJob += j.nOutputs;
				}
				id = getId();
			} else {
				dataForJob = remainingData / (followingJobs.size() - done);
				remainingData -= dataForJob;
				done += 1;
				id = followingJob.getId();
			}
			followingJob.numData = dataForJob;
			followingJob.tryToCollectDataAddresses(id, dataIterator);
		}
	}

	public void deployDataFromURL(String inputDataURL) {
		this.inputDataUrl = inputDataURL;
		getReady();		
	}
}
