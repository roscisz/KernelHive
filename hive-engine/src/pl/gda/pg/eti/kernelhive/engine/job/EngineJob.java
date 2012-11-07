package pl.gda.pg.eti.kernelhive.engine.job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.DataAddress;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.engine.Workflow;

public class EngineJob extends Job {
	
	public List<EngineJob> followingJobs = new ArrayList<EngineJob>();
	public Workflow workflow;

	public EngineJob(EngineGraphNodeDecorator node, Workflow workflow) {
		super(node);
		this.workflow = workflow;
	}
	
	public void addFollowingJob(EngineJob followingJob) {
		this.followingJobs.add(followingJob);
		this.nOutputs = followingJobs.size();
	}
	
	public void tryCollectFollowingJobsData(Iterator<DataAddress> dataIterator) {
		System.out.println("Job " + this.ID + " try to collect following");
		for(Job followingJob : followingJobs) {
			System.out.println("Follow: " + followingJob.ID);
			followingJob.tryToCollectDataAddresses(dataIterator);
		}
	}

	public void deployDataFromURL(String inputDataURL) {
		this.inputDataUrl = inputDataURL;
		getReady();		
	}
}
