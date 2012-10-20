package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;

public class Workflow extends HasID {

	private Map<Integer, Job> jobs = new Hashtable<Integer, Job>();

	public WorkflowInfo info = new WorkflowInfo();
	private WorkflowState state = WorkflowState.PENDING;
	private String result = null;

	public enum WorkflowState {
		PENDING("pending"), COMPLETED("completed"), TERMINATED("terminated"), SUBMITTED(
				"submitted"), ERROR("error"), WARNING("warning"), PROCESSING(
				"processing");

		private String state;

		WorkflowState(String state) {
			this.state = state;
		}

		@Override
		public String toString() {
			return state;
		}
	}

	public Workflow(List<EngineGraphNodeDecorator> graph, String workflowName, String inputDataURL) {
		super();

		for (EngineGraphNodeDecorator node : graph) {
			Job newJob = new Job(node, this);
			if (node.getGraphNode().getPreviousNodes().size() == 0) {
				newJob.state = JobState.READY;
				newJob.inputDataUrl = inputDataURL;
			}
			int nOutputs = node.getGraphNode().getFollowingNodes().size();
			if(nOutputs == 0)
				newJob.nOutputs = 1;
			else newJob.nOutputs = nOutputs;
			// TODO: if many kernels in one job, assign each kernel to
			// individual job
			newJob.assignedKernel = node.getKernels().get(0);

			jobs.put(newJob.ID, newJob);
		}
		
		initWorkflowInfo(workflowName);
	}

	private void initWorkflowInfo(String workflowName) {		
		this.info.ID = this.ID;
		this.info.name = workflowName;		
	}

	public List<Job> getReadyJobs() {
		List<Job> ret = new ArrayList<Job>();
		for (Job job : jobs.values())
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

	public Job getJobByID(int jobID) {
		return jobs.get(jobID);
	}

	public Job getJobByGraphNode(IGraphNode graphNode) {
		for(Job job : jobs.values())
			if(job.node.getGraphNode().equals(graphNode))
				return job;
		return null;
	}

	public boolean containsJob(Job jobOver) {
		return jobs.containsValue(jobOver);
	}

	public void finish(String resultURL) {
		this.state = WorkflowState.COMPLETED;
		this.info.result = resultURL + this.info.name;		
	}
}
