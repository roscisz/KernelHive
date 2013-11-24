package pl.gda.pg.eti.kernelhive.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo.WorkflowState;
import pl.gda.pg.eti.kernelhive.common.clusterService.HasID;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;
import pl.gda.pg.eti.kernelhive.engine.job.PartitionerJob;

public class Workflow extends HasID {

	private final Map<Integer, EngineJob> jobs = new HashMap<>();
	public WorkflowInfo info = new WorkflowInfo();
	private WorkflowState state = WorkflowState.PENDING;
	private final String result = null;
	private final List<EngineGraphNodeDecorator> graph;
	private final Date startTime;

	public Workflow(final List<EngineGraphNodeDecorator> graph,
			final String workflowName, final String inputDataURL) {
		super();

		this.graph = graph;

		final EngineGraphNodeDecorator rootNode = getRootNode();
		configureJobs(rootNode.getGraphNode(), null);
		getJobByGraphNode(rootNode.getGraphNode()).deployDataFromURL(
				inputDataURL);
		initWorkflowInfo(workflowName);
		this.startTime = new Date();
	}

	private void configureJobs(final IGraphNode node,
			final EngineJob previousJob) {
		final EngineJob newJob = configureJob(node);
		if (previousJob != null) {
			previousJob.addFollowingJob(newJob);
		}
		for (final IGraphNode followingNode : node.getFollowingNodes()) {
			configureJobs(followingNode, newJob);
		}
	}

	private EngineJob configureJob(final IGraphNode node) {
		EngineJob ret;
		final EngineGraphNodeDecorator decoratorNode = getDecoratorByIGraphNode(node);
		if (node.getType().equals(GraphNodeType.EXPANDABLE)) {
			ret = new PartitionerJob(decoratorNode, this);
		} else {
			ret = new EngineJob(decoratorNode, this);
		}
		registerJob(ret);
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

	public List<EngineJob> getReadyJobs() {
		final List<EngineJob> ret = new ArrayList<EngineJob>();
		for (final EngineJob job : jobs.values()) {
			if (job.state == JobState.READY) {
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
		this.info.result = this.result;
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
		System.out.println("Time in seconds: "
				+ ((new Date()).getTime() - this.startTime.getTime()) / 1000);
	}

	public void debugTime() {
		System.out.println("Debug time in seconds: "
				+ getDebugTime());
	}

	public long getDebugTime() {
		return ((new Date()).getTime() - this.startTime.getTime()) / 1000;
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
