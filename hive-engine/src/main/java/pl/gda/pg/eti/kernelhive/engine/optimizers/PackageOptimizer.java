package pl.gda.pg.eti.kernelhive.engine.optimizers;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.DataAddress;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.engine.HiveEngine;
import pl.gda.pg.eti.kernelhive.engine.Workflow;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IOptimizer;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PackageOptimizer implements IOptimizer {

	@Override
	public List<Job> processWorkflow(Workflow workflow, Collection<Cluster> infrastructure) {
		List<EngineJob> readyJobs = workflow.getJobsByState(Job.JobState.READY);
		List<Job> scheduledJobs = new ArrayList<>();

		List<Device> availableDevices = HiveEngine.getAvailableDevices(infrastructure);
		Collections.sort(availableDevices, new Comparator<Device>() {
			@Override
			public int compare(Device o1, Device o2) {
				return -Double.valueOf(getDevicePowerRatio(o1)).compareTo(getDevicePowerRatio(o2));
			}
		});

		int min = Math.min(readyJobs.size(), availableDevices.size());
		if (readyJobs.size() > min) {
			readyJobs = readyJobs.subList(0, min);
		}
		if (availableDevices.size() > min) {
			availableDevices = availableDevices.subList(0, min);
		}
		List<DataAddress> addresses = getAllDataAddresses(readyJobs);

		double powerRatioSum = getPowerRatio(availableDevices);
		int addressesNumber = addresses.size();

		for (int i = 0; i < readyJobs.size(); i++) {
			double addressesPerPower = addressesNumber / powerRatioSum;
			Device device = availableDevices.get(i);
			int addressesForTask = (int) Math.max(1.0, getDevicePowerRatio(device) * addressesPerPower);
			Job job = readyJobs.get(i);
			job.dataAddresses.clear();
			job.dataAddresses.put(1, addresses.subList(0, addressesForTask));
			job.numData = addressesForTask;

			job.schedule(device);
			scheduledJobs.add(job);

			addresses = addresses.subList(addressesForTask, addresses.size());
			addressesNumber -= addressesForTask;
			powerRatioSum -= getDevicePowerRatio(device);
		}

		return scheduledJobs;
	}

	private List<DataAddress> getAllDataAddresses(List<EngineJob> readyJobs) {
		Map<Integer, List<DataAddress>> map = new TreeMap<>();
		for (Job job : readyJobs) {
			map.putAll(job.dataAddresses);
		}
		List<DataAddress> addresses = new ArrayList<>();
		for (List<DataAddress> l : map.values()) {
			addresses.addAll(l);
		}
		return addresses;
	}

	private double getPowerRatio(List<Device> availableDevices) {
		double ratioSum = 0.0;
		for (Device device : availableDevices) {
			ratioSum += getDevicePowerRatio(device);
		}
		return ratioSum;
	}

	private double getDevicePowerRatio(Device device) {
		if(device.name.matches(".*Q8200.*")) return 1.058784;
		else if(device.name.matches(".*i3-2350M.*")) return 1.624494;
		else if(device.name.matches(".*i7-2600K.*")) return 4.129926;
		else if(device.name.matches(".*E5-2680v2.*")) return 6.230917;
		else if(device.name.matches(".*9800 GTX+.*")) return 1;
		else if(device.name.matches(".*GTX 480.*")) return 8.448889;
		else if(device.name.matches(".*GTS 450.*")) return 3.000285;
		else if(device.name.matches(".*Phi 5110P.*")) return 2.590545;
		else return 1;
	}

}
