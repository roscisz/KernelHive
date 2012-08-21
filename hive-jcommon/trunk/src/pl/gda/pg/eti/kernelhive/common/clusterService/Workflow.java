package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;

public class Workflow extends HasID {
	
	private Map<Integer, Job> jobs = new Hashtable<Integer, Job>();
	
	public enum WorkflowState {
		PENDING("pending"), COMPLETED("completed"), 
		TERMINATED("terminated"), SUBMITTED("submitted"),
		ERROR("error"), WARNING("warning"), PROCESSING("processing");
		
		private String state;
		
		WorkflowState(String state){
			this.state = state;
		}
		
		@Override
		public String toString(){
			return state;
		}		
	}
	
	public Workflow(List<EngineGraphNodeDecorator> graph, String inputDataURL) {
		super();
				
		for(EngineGraphNodeDecorator node : graph) {
			Job newJob = new Job(node, this);			
			if(node.getGraphNode().getChildrenNodes().size() == 0) {				
				newJob.state = JobState.READY;
				// FIXME:
				newJob.inputDataUrl = "http://gracik.mine.nu/data";//inputDataURL; 
			}
			// TODO: if many kernels in one job, assign each kernel to individual job
			newJob.assignedKernel = node.getKernels().get(0);
			
			jobs.put(newJob.ID, newJob);
			
			// TODO: wrzucić kernele joba na serwer i ustawić jobowi adres kernela
		}
	}
	
	public List<Job> getReadyJobs() {
		List<Job> ret = new ArrayList<Job>();
		for(Job job : jobs.values())
			if(job.state == JobState.READY)
				ret.add(job);		
		return ret;
	}

	public boolean checkFinished() {
		
		for(Job job : jobs.values())
			if(job.state != JobState.FINISHED)
				return false;
				
		return true;
	}

}
