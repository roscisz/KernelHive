package pl.gda.pg.eti.kernelhive.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.DataAddress;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.common.clusterService.Workflow;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.engine.optimizers.SimpleOptimizer;

public class HiveEngine {
	
	private static HiveEngine instance;
	
	private Map<String, Cluster> clusters = new HashMap<String, Cluster>();
	private Map<Integer, Workflow> workflows = new HashMap<Integer, Workflow>();
		
	private HiveEngine() {
	
	}
	
	public static synchronized HiveEngine getInstance() {
		if(instance == null) {
			instance = new HiveEngine();
			System.out.println("Creating HiveEngine singleton " + instance);
		}
		return instance;		
	}

	public void updateCluster(String id, Cluster cluster) {
		if(this.clusters.containsKey(id)) {
			this.clusters.get(id).runJob(null);
		}
		this.clusters.put(id, cluster);
		System.out.println("Engine knows about clusters: " + clusters);
	}
	
	public Integer runWorkflow(List<EngineGraphNodeDecorator> nodes, String projectName, String inputDataURL) {
		Workflow newWorkflow = new Workflow(nodes, projectName, inputDataURL);
		workflows.put(newWorkflow.ID, newWorkflow);
		
		processWorkflow(newWorkflow);			
		
		return newWorkflow.ID;
	}

	private void processWorkflow(Workflow workflow) {
		System.out.println("Processing workflow: " + workflow.ID);
		List<Job> readyJobs = new SimpleOptimizer().processWorkflow(workflow, clusters.values());	
		
		for(Job job : readyJobs) {
			job.run();
		}	
	}
	
	public void cleanup() {
		// jeżeli któryś job za długo nie odpowiada to najpierw włącz mu tryb nie odpowiada, a potem wywal
		for(Workflow workflow : workflows.values())
			processWorkflow(workflow);
	}
						
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public Cluster getCluster(String ip) {
		return clusters.get(ip);
	}

	public List<Cluster> getInfrastructure() {
		return new ArrayList<Cluster>(clusters.values());
	}

	public List<ClusterInfo> getInfrastructureInfo() {
		List<ClusterInfo> ret = new ArrayList<ClusterInfo>();
		
		for(Cluster cluster : clusters.values())
			ret.add(cluster.getClusterInfo());
		
		return ret;
	}

	public List<WorkflowInfo> browseWorkflows() {
		List<WorkflowInfo> ret = new LinkedList<WorkflowInfo>();
		
		for(Workflow workflow : workflows.values())
			ret.add(workflow.getWorkflowInfo());
		
		return ret;			
	}

	public void onProgress(int jobID, int progress) {
		System.out.println("ON progress job " + jobID + ", progress " + progress);		
	}

	public void onJobOver(int jobID, String returnData) {		
		System.out.println("JOB OVER " + jobID + ", " + returnData);	
		List<DataAddress> resultAddresses = parseResults(returnData);
		Job jobOver = getJobByID(jobID);		
		
		jobOver.finish();
		
		System.out.println("Job over: " + jobOver);
		
		Iterator<DataAddress> dataIterator = resultAddresses.iterator();
		
		for(IGraphNode graphNode : jobOver.node.getGraphNode().getFollowingNodes()) {
			Job followingJob = getJobByGraphNode(graphNode);
			followingJob.tryToCollectDataAddresses(dataIterator);						
			System.out.println("Following job: " + followingJob);
		}		
		
		processWorkflow(getWorkflowByJob(jobOver));		
	}
	
	private Workflow getWorkflowByJob(Job jobOver) {
		for(Workflow workflow : workflows.values()) {
			if(workflow.containsJob(jobOver))
				return workflow;
		}
		return null;
	}

	private Job getJobByGraphNode(IGraphNode graphNode) {
		for(Workflow workflow : workflows.values()) {
			Job job = workflow.getJobByGraphNode(graphNode);
			if(job != null)
				return job;			
		}
		return null;
	}

	private Job getJobByID(int jobID) {
		for(Workflow workflow : workflows.values()) {
			Job job = workflow.getJobByID(jobID);
			if(job != null)
				return job;
		}				
		return null;
	}
	
	private List<DataAddress> parseResults(String returnData) {
		List<DataAddress> ret = new ArrayList<DataAddress>();
		
		String[] addresses = returnData.split(" ");
		
		int nAddresses = addresses.length / 3;
				
		for(int i = 0; i != nAddresses; i++) {
			DataAddress newAddress = new DataAddress();
			newAddress.hostname = addresses[3 * i];
			newAddress.port = Integer.parseInt(addresses[3 * i + 1]);
			newAddress.ID = Integer.parseInt(addresses[3 * i + 2]);
			ret.add(newAddress);
		}
		
		return ret;
	}

}
