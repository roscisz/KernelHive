package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job.JobState;
import pl.gda.pg.eti.kernelhive.engine.HiveEngine;
import pl.gda.pg.eti.kernelhive.engine.Workflow;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackSolver;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IOptimizer;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;

public class EnergyOptimizer implements IOptimizer {
	
	private IKnapsackSolver knapsackSolver;
	//private List<Device> chosenDevices;
	
	public EnergyOptimizer(IKnapsackSolver knapsackSolver) {
		this.knapsackSolver = knapsackSolver;
	}

	@Override
	public List<Job> processWorkflow(Workflow workflow, Collection<Cluster> infrastructure) {
		List<EngineJob> readyJobs = workflow.getJobsByState(Job.JobState.READY);
		List<Job> scheduledJobs = new ArrayList<Job>();
		
		List<Device> chosenDevices = chooseDevices(infrastructure);
		
		for(Job readyJob : readyJobs) {
			//System.out.println("Trying to schedule job " + readyJob.ID);
			for(Device device : chosenDevices) {
				if(!device.isBusy()) {
					readyJob.schedule(device);
					scheduledJobs.add(readyJob);
				}
				if(readyJob.state == JobState.SCHEDULED) {				
					System.out.println("Scheduled job " + readyJob.getId() + " on device: " + device.name + " on unit " + device.getUnit().getId());
					break;
				}				
			}
		}			
		return scheduledJobs;
	}

	public List<Device> chooseDevices(Collection<Cluster> infrastructure) {
		//System.out.println("Choosing devices");
		int currentConsumption = 0;
		
		List<IKnapsackItem> availableItems = new ArrayList<IKnapsackItem>();		
		for(Device d : HiveEngine.getAvailableDevices(infrastructure)) {
			DeviceKnapsackItem item = new DeviceKnapsackItem(d);
			if(d.isBusy())
				currentConsumption += item.getWeight(); 
			else availableItems.add(item);				
		}
		
		int limit = HiveEngine.getEnergyLimit() - currentConsumption;
		//System.out.println("Limit " + limit);
		//for(IKnapsackItem item : availableItems)
			//System.out.println("Item " + item.getWeight() + " " + item.getValue());
		
		boolean[] knapsackResult = this.knapsackSolver.solve(availableItems, limit);
		
		List<Device> ret = new ArrayList<Device>();
		for(int i = 0; i != knapsackResult.length; i++) 
			if(knapsackResult[i]) {
				//System.out.println("Chosen device " + ((DeviceKnapsackItem)availableItems.get(i)).device.name);
				ret.add(((DeviceKnapsackItem)availableItems.get(i)).device);
			}
		return ret;		
	}

}
