/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 * Copyright (c) 2016 Adrian Boguszewski
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
package pl.gda.pg.eti.kernelhive.engine;

import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo.WorkflowState;
import pl.gda.pg.eti.kernelhive.common.clusterService.DataAddress;
import pl.gda.pg.eti.kernelhive.common.clusterService.HasID;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;
import pl.gda.pg.eti.kernelhive.engine.job.PartitionerJob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Workflow extends HasID {

	private final Map<Integer, EngineJob> jobs = new TreeMap<>();
	public WorkflowInfo info = new WorkflowInfo();
	private WorkflowState state = WorkflowState.PENDING;
	private final List<EngineGraphNodeDecorator> graph;
	private final long startTime;
	private static final Logger LOG = Logger.getLogger(Workflow.class.getName());

	// For tests:
	List<DataAddress> inputAddresses = new ArrayList<DataAddress>();

	public Workflow(final List<EngineGraphNodeDecorator> graph,
			final String workflowName, final String inputDataURL) {
		super();

		this.graph = graph;
		
		LOG.info("Input data url: " + inputDataURL);
		this.inputAddresses = Job.parseAddresses(inputDataURL);		

		final EngineGraphNodeDecorator rootNode = getRootNode();
		configureJobs(rootNode.getGraphNode(), new ArrayList<EngineJob>());
		//getJobByGraphNode(rootNode.getGraphNode()).deployDataFromURL(inputDataURL);
		initWorkflowInfo(workflowName);
		this.startTime = System.nanoTime();
	}
	
	private void configureJobs(final IGraphNode node,
			final List<EngineJob> previousJobs) {
		List<EngineJob> newJobs = configureJob(node);
		for(EngineJob pj : previousJobs) {
			for(EngineJob ej : newJobs)
				pj.addFollowingJob(ej);
		}
		for (final IGraphNode followingNode : node.getFollowingNodes()) {
			configureJobs(followingNode, newJobs);
		}
	}

	private List<EngineJob> configureJob(final IGraphNode node) {
		List<EngineJob> ret = new ArrayList<>();
		final EngineGraphNodeDecorator decoratorNode = getDecoratorByIGraphNode(node);
		if (node.getType().equals(GraphNodeType.EXPANDABLE)) {
			ret.add(new PartitionerJob(decoratorNode, this));
		}
		else if(decoratorNode.getGraphNode().getPreviousNodes().isEmpty()) {
			System.out.println("OK we have data graph node, addresses to assign: " +
					inputAddresses.size());
			Iterator<DataAddress> iterator = inputAddresses.iterator();
			while(iterator.hasNext()) {
				System.out.println("Adding engineJob");
				EngineJob nj = getJobByGraphNode(decoratorNode.getGraphNode());
				if (nj == null) {
					nj = new EngineJob(decoratorNode, this);
				}
				nj.numData = inputAddresses.size();
				nj.tryToCollectDataAddresses(getId(), iterator);
				ret.add(nj);
			}
		} else {
			EngineJob nj = getJobByGraphNode(decoratorNode.getGraphNode());
			if (nj == null) {
				nj = new EngineJob(decoratorNode, this);
			}
			ret.add(nj);
		}
		for(EngineJob ej : ret)
			registerJob(ej);
		return ret;
	}

	private EngineGraphNodeDecorator getRootNode() {
		for (final EngineGraphNodeDecorator node : this.graph) {
			if (node.getGraphNode().getPreviousNodes().isEmpty()) {
				return node;
			}
		}
		return null;
	}

	private void initWorkflowInfo(final String workflowName) {
		this.info.ID = getId();
		this.info.name = workflowName;
	}

	public List<EngineJob> getJobsByState(Job.JobState state) {
		final List<EngineJob> ret = new ArrayList<EngineJob>();
		for (final EngineJob job : jobs.values()) {
			if (job.state == state) {
				ret.add(job);
			}
		}
		return ret;
	}

	public boolean checkFinished() {

		for (final Job job : jobs.values()) {
			if (job.state != JobState.FINISHED) {
				return false;
			}
		}

		return true;
	}

	public WorkflowInfo getWorkflowInfo() {
		this.info.state = this.state;
		return this.info;
	}

	public EngineJob getJobByID(final int jobID) {
		return jobs.get(jobID);
	}

	public EngineJob getJobByGraphNode(final IGraphNode graphNode) {
		for (final EngineJob job : jobs.values()) {
			if (job.node.getGraphNode().equals(graphNode)) {
				return job;
			}
		}
		return null;
	}

	public boolean containsJob(final Job jobOver) {
		return jobs.containsValue(jobOver);
	}

	public void finish(final String resultURL) {
		this.state = WorkflowState.COMPLETED;
		this.info.result = resultURL;
		this.info.elapsedTime = getDebugTime();
		debugTime();
	}

	public void debugTime() {
		System.out.println("Debug time in seconds: "
				+ getDebugTime());
	}

	public double getDebugTime() {
		return (System.nanoTime() - this.startTime) / 1000000000.0;
	}

	public void registerJob(final EngineJob job) {
		this.jobs.put(job.getId(), job);
	}

	private EngineGraphNodeDecorator getDecoratorByIGraphNode(
			final IGraphNode node) {
		for (final EngineGraphNodeDecorator decorator : this.graph) {
			if (decorator.getGraphNode().equals(node)) {
				return decorator;
			}
		}
		return null;
	}

	public Map<Integer, EngineJob> getJobs() {
		return jobs;
	}

	public void cancel() {
		jobs.clear();
	}
}
