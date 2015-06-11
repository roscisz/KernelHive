/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import pl.gda.pg.eti.kernelhive.common.clusterService.Device.DeviceType;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;

public class Job extends HasID {

	public enum JobState {
		PENDING,
		READY,
		PREFETCHING,
		PREFETCHING_FINISHED,
		SCHEDULED,
		PREPARING,
		PROCESSING,
		FINISHED
	}
	public int numData = 1;
	private int collectedAddresses = 0;
	private List<DataAddress> dataAddresses = new ArrayList<>();
	public EngineGraphNodeDecorator node;
	public Device device;
	public JobState state = JobState.PENDING;
	public int progress = -1;
	private int unassignedInt = 0;
	public String inputDataUrl;
	public int nOutputs = 1;

	public Job() {
	}

	public Job(EngineGraphNodeDecorator node) {
		this.node = node;
	}

	private String getOffsets() {
		int[] offsets = getAssignedKernel().getOffset();
		return concatKernelAttrs(offsets);
	}

	private String getGlobalSizes() {
		int[] globalSizes = getAssignedKernel().getGlobalSize();
		if(this.device.getDeviceType() == DeviceType.CPU)
			return "8 1 1";
		return concatKernelAttrs(globalSizes);
	}

	private String getLocalSizes() {
		int[] localSizes = getAssignedKernel().getLocalSize();
		if(this.device.getDeviceType() == DeviceType.CPU)
			return "1 1 1";
		return concatKernelAttrs(localSizes);
	}

	private String getOutputSize() {
		return "" + this.getAssignedKernel().getOutputSize();
	}

	private String getDataString() {
		return numData + serializeAddresses(dataAddresses);
	}

	private String concatKernelAttrs(int[] attrs) {
		return attrs[0] + " " + attrs[1] + " " + attrs[2];
	}

	private String getClusterHost() {
		// FIXME: it shouldn't happen:
		if (device == null) {
			return "hive-cluster";
		}
		return device.getUnit().cluster.hostname;
	}

	private int getClusterTCPPort() {
		if (device == null) {
			return unassignedInt;
		}
		return device.getUnit().cluster.TCPPort;
	}

	private int getClusterUDPPort() {
		if (device == null) {
			return unassignedInt;
		}
		return device.getUnit().cluster.UDPPort;
	}

	private int getUnitId() {
		if (this.device == null) {
			return unassignedInt;
		}
		return new BigDecimal(this.device.getUnit().getUnitId()).intValue();
	}

	private String getDeviceId() {
		if (device == null) {
			return "UNASSIGNED";
		}
		return device.identifier;
	}

	public GraphNodeType getJobType() {
		return node.getGraphNode().getType();
	}

	private String getResultDataHost() {
		// FIXME: it shouldn't happen:
		if (device == null) {
			return "hive-cluster";
		}
		return device.getUnit().cluster.hostname;
	}

	public void run() {
		this.state = JobState.PREPARING;
		this.device.getUnit().cluster.runJob(this);
	}
	
	public void runPrefetching() {
		this.state = JobState.PREFETCHING;
		this.device.getUnit().cluster.runJob(this);
	}

	public void schedule(Device device) {
		this.device = device;
		this.state = JobState.SCHEDULED;
		this.device.busy = true;
	}

	public JobInfo getJobInfo() {
		JobInfo ret = new JobInfo();

		ret.unitID = getUnitId();
		ret.kernelString = this.getAssignedKernel().getKernel();

		ret.ID = getId();
		ret.clusterHost = getClusterHost();
		ret.clusterTCPPort = getClusterTCPPort();
		ret.clusterUDPPort = getClusterUDPPort();
		ret.deviceID = getDeviceId();
		ret.offsets = getOffsets();
		ret.globalSizes = getGlobalSizes();
		ret.localSizes = getLocalSizes();
		ret.outputSize = getOutputSize();
		ret.dataString = getDataString();
		System.out.println("Setting data string " + ret.dataString);
		ret.jobType = getJobType();
		ret.nOutputs = nOutputs;
		ret.resultDataHost = getResultDataHost();
		ret.state = this.state;

		System.out.println("Setting inputDataUrl " + inputDataUrl);
		ret.inputDataUrl = inputDataUrl;

		return ret;
	}

	protected IKernelString getAssignedKernel() {
		// TODO: many kernels for one job?
		return this.node.getKernels().get(0);
	}

	public void tryToCollectDataAddresses(Iterator<DataAddress> dataIterator) {
		System.out.println("Job " + getId() + " trying to collect " + numData + " addresses.");

		while (collectedAddresses != numData) {
			try {
				dataAddresses.add(dataIterator.next());
				collectedAddresses++;
			} catch (NoSuchElementException nsee) {
				break;
			}
		}
		System.out.println("Job " + getId() + " collected " + collectedAddresses + " dataAddresses.");
		if (collectedAddresses == numData) {
			getReady();
		}
	}
	
	public void finish() {
		this.state = JobState.FINISHED;
		this.device.busy = false;
	}
	
	public void finishPrefetching(List<DataAddress> prefetchedAddresses) {
		this.dataAddresses = prefetchedAddresses;		
		this.state = JobState.PREFETCHING_FINISHED;
	}

	public boolean canBeScheduledOn(Device device) {
		return true;
	}

	protected void getReady() {
		this.state = JobState.READY;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
		// TODO: change progress to enum
		if(progress >= 40)
			this.state = JobState.PROCESSING;		
		if(progress == 100)
			this.finish();
	}
	
	public static List<DataAddress> parseAddresses(String returnData) {
		List<DataAddress> ret = new ArrayList<>();

		String[] addresses = returnData.split(" ");

		int nAddresses = addresses.length / 3;

		for (int i = 0; i != nAddresses; i++) {
			DataAddress newAddress = new DataAddress();
			newAddress.hostname = addresses[3 * i];
			newAddress.port = Integer.parseInt(addresses[3 * i + 1]);
			newAddress.ID = addresses[3 * i + 2];
			ret.add(newAddress);
		}

		return ret;
	}
	
	public static String serializeAddresses(List<DataAddress> dataAddresses) {		
		StringBuilder ret = new StringBuilder();
		
		for (DataAddress da : dataAddresses) {
			ret.append(" ")
					.append(da.hostname)
					.append(" ")
					.append(da.port)
					.append(" ")
					.append(da.ID);
		}
		
		return ret.toString();		
	}
	
	public static List<DataAddress> getAddressesForDataString(String dataString) {
		return parseAddresses(dataString.split(" ", 2)[1]);
	}
}
