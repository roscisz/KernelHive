package pl.gda.pg.eti.kernelhive.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo.WorkflowState;
import pl.gda.pg.eti.kernelhive.common.clusterService.HasID;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;
import pl.gda.pg.eti.kernelhive.engine.job.MergerJob;
import pl.gda.pg.eti.kernelhive.engine.job.PartitionerJob;

public class Workflow extends HasID {

	private Map<Integer, EngineJob> jobs = new Hashtable<Integer, EngineJob>();

	public WorkflowInfo info = new WorkflowInfo();
	private WorkflowState state = WorkflowState.PENDING;
	private String result = null;
	
	private List<EngineGraphNodeDecorator> graph;

	private Date startTime;

	public Workflow(List<EngineGraphNodeDecorator> graph, String workflowName, String inputDataURL) {
		super();
		
		this.graph = graph;
		
		EngineGraphNodeDecorator rootNode = getRootNode();
		configureJobs(rootNode.getGraphNode(), null);
		getJobByGraphNode(rootNode.getGraphNode()).deployDataFromURL(inputDataURL);		
		initWorkflowInfo(workflowName);
		this.startTime = new Date();
	}

	private void configureJobs(IGraphNode node, EngineJob previousJob) {
		EngineJob newJob = configureJob(node);
		if(previousJob != null)
			previousJob.addFollowingJob(newJob);
		for(IGraphNode followingNode : node.getFollowingNodes()) {
			configureJobs(followingNode, newJob);
		}					
	}

	private EngineJob configureJob(IGraphNode node) {
		EngineJob ret;
		EngineGraphNodeDecorator decoratorNode = getDecoratorByIGraphNode(node);
		if(node.getType().equals(GraphNodeType.EXPANDABLE))
			ret = new PartitionerJob(decoratorNode, this);
		else ret = new EngineJob(decoratorNode, this);
		registerJob(ret);
		return ret;
	}


	private EngineGraphNodeDecorator getRootNode() {
		for(EngineGraphNodeDecorator node : this.graph)
			if(node.getGraphNode().getPreviousNodes().isEmpty())
				return node;
		return null;
	}

	private void initWorkflowInfo(String workflowName) {		
		this.info.ID = this.ID;
		this.info.name = workflowName;		
	}

	public List<EngineJob> getReadyJobs() {
		List<EngineJob> ret = new ArrayList<EngineJob>();
		for (EngineJob job : jobs.values())
			if (job.state == JobState.READY)
				ret.add(job);
		return ret;
	}

	public boolean checkFinished() {

		for (Job job : jobs.values())
			if (job.state != JobState.FINISHED)
				return false;

		return true;
	}

	public WorkflowInfo getWorkflowInfo() {
		this.info.state = this.state;
		this.info.result = this.result;
		return this.info;
	}

	public EngineJob getJobByID(int jobID) {
		return jobs.get(jobID);
	}

	public EngineJob getJobByGraphNode(IGraphNode graphNode) {
		for(EngineJob job : jobs.values())
			if(job.node.getGraphNode().equals(graphNode))
				return job;
		return null;
	}

	public boolean containsJob(Job jobOver) {
		return jobs.containsValue(jobOver);
	}

	public void finish(String resultURL) {
		this.state = WorkflowState.COMPLETED;
		this.info.result = resultURL;
		System.out.println("Time in seconds: " + ((new Date()).getSeconds() - this.startTime.getSeconds()) );
	}

	public void registerJob(EngineJob job) {
		this.jobs.put(job.ID, job);		
	}
	
	private EngineGraphNodeDecorator getDecoratorByIGraphNode(IGraphNode node) {
		for(EngineGraphNodeDecorator decorator : this.graph)
			if(decorator.getGraphNode().equals(node))
				return decorator;
		return null;
	}
}
