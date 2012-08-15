package pl.gda.pg.eti.kernelhive.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Workflow;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
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
		this.clusters.put(id, cluster);
		System.out.println("Engine knows about clusters: " + clusters);
	}
	
	public Integer runWorkflow(List<EngineGraphNodeDecorator> nodes) {
		Workflow newWorkflow = initializeTask(nodes);
		workflows.put(newWorkflow.ID, newWorkflow);
		
		processWorkflow(newWorkflow);			
		
		return newWorkflow.ID;
	}
	
	private void processWorkflow(Workflow newWorkflow) {
		List<Job> readyJobs = new SimpleOptimizer().processWorkflow(newWorkflow, clusters.values());	
		
		for(Job job : readyJobs) {
			job.run();
		}	
	}

	private Workflow initializeTask(List<EngineGraphNodeDecorator> nodes) {
		Workflow newTask = new Workflow(nodes);

		// TODO: coś jeszcze? Jeśli nie, to ta metoda jest niepotrzebna :P
		
		return newTask;
	}
	
	public void cleanup() {
		// jeżeli któryś job za długo nie odpowiada to najpierw włącz mu tryb nie odpowiada, a potem wywal
		// wyodrębnij wolne zadania i spróbuj odpalić optimizerem aż się znulluje
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

}
