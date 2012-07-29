package pl.gda.pg.eti.kernelhive.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Task;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.engine.optimizers.SimpleDeviceOptimizer;
import pl.gda.pg.eti.kernelhive.engine.optimizers.SimpleJobOptimizer;
import pl.gda.pg.eti.kernelhive.engine.optimizers.SimpleTaskOptimizer;

public class HiveEngine {
	
	private static HiveEngine instance;
	
	private Map<String, Cluster> clusters = new HashMap<String, Cluster>();
	private Map<Integer, Task> tasks = new HashMap<Integer, Task>();
		
	private HiveEngine() {
		
	}
	
	public static synchronized HiveEngine getInstance() {
		if(instance == null) {
			instance = new HiveEngine();
		}
		return instance;		
	}

	public void updateCluster(String id, Cluster cluster) {
		this.clusters.put(id, cluster);
		System.out.println("Engine knows about clusters: " + clusters);
	}
	
	public Integer runTask(List<EngineGraphNodeDecorator> nodes) {
		Task newTask = initializeTask(nodes);
		tasks.put(newTask.ID, newTask);		
		processTask(newTask);		
		return newTask.ID;
	}
	
	private Task initializeTask(List<EngineGraphNodeDecorator> nodes) {
		Task newTask = new Task(nodes);

		// TODO: coś jeszcze? Jeśli nie, to ta metoda jest niepotrzebna :P
		
		return newTask;
	}

	private void processTask(Task task) {
		List<Job> readyJobs = task.getReadyJobs();
		
		if(readyJobs.size() == 0) {
			if(task.checkFinished())
				; // przygotowujemy wyniki do pobrania
			else
				; // coś się zablokowało			
		}
		
		readyJobs = new SimpleTaskOptimizer().arrangeJobs(readyJobs);
		
		List<Device> devices = new SimpleDeviceOptimizer().arrangeDevices(clusters.values());
		
		readyJobs = new SimpleJobOptimizer().scheduleJobs(readyJobs, devices);
		
		for(Job job : readyJobs) {
			job.run();			
		}							
	}
	
	public void cleanup() {
		// jeżeli któryś job za długo nie odpowiada to najpierw włącz mu tryb nie odpowiada, a potem wywal
		// wyodrębnij wolne zadania i spróbuj odpalić optimizerem aż się znulluje		
	}
						
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public Cluster getCluster(String ip) {
		return clusters.get(ip);
	}

}
