package pl.gda.pg.eti.kernelhive.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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

public class Workflow extends HasID {

	private final Map<Integer, EngineJob> jobs = new HashMap<>();
	public WorkflowInfo info = new WorkflowInfo();
	private WorkflowState state = WorkflowState.PENDING;
	private final String result = null;
	private final List<EngineGraphNodeDecorator> graph;
	private final Date startTime;
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
		this.startTime = new Date();		
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
		List<EngineJob> ret = new ArrayList<EngineJob>();
		final EngineGraphNodeDecorator decoratorNode = getDecoratorByIGraphNode(node);
		if (node.getType().equals(GraphNodeType.EXPANDABLE)) {
			ret.add(new PartitionerJob(decoratorNode, this));
		}
		else if(true) { // node.getType().equals(GraphNodeType.MULTIDATA)
			System.out.println("OK we have data graph node, addresses to assign: " +
					inputAddresses.size());
			Iterator<DataAddress> iterator = inputAddresses.iterator();
			while(iterator.hasNext()) {
				System.out.println("Adding engineJob");
				EngineJob nj = new EngineJob(decoratorNode, this);
				nj.tryToCollectDataAddresses(iterator);
				ret.add(nj);
			}
		} else {
			ret.add(new EngineJob(decoratorNode, this));
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
		// OBSOLETE:
		this.info.name = resultURL;
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
