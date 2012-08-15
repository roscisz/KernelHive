package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;

public class Job extends HasID {
	
	public enum JobState {
		PENDING,
		READY,
		SCHEDULED,
		PROCESSING,
		FINISHED
	}
	
	public String kernelHost;
	public int kernelPort;
	public int kernelId;
	public List<String> dataHosts = new ArrayList<String>();
	public List<Integer> dataPorts = new ArrayList<Integer>();
	public List<Integer> dataIds = new ArrayList<Integer>();	
	public IKernelString assignedKernel;
	
	public Device device;
	
	private Workflow task;
	public EngineGraphNodeDecorator node;
	public JobState state = JobState.PENDING;
	public int progress = -1;
	
	private String unassignedString = "UNASSIGNED";
	private int unassignedInt = 0;

	public Job() {
		
	}
	
	public Job(EngineGraphNodeDecorator node, Workflow task) {
		this.node = node;
		this.task = task;	
	}		

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder("bin");	
				
		ret.append(" " + ID);
		ret.append(" " + getClusterHost());
		ret.append(" " + getClusterTCPPort());
		ret.append(" " + getClusterUdpPort());
		ret.append(" " + getDeviceId());
		ret.append(" " + getOffsets());
		ret.append(" " + getGlobalSizes());
		ret.append(" " + getLocalSizes());
		ret.append(" " + kernelHost);
		ret.append(" " + kernelId);
		ret.append(" " + getNumData());
		for(String dataHost : dataHosts)
			ret.append(" " + dataHost);
		for(Integer dataPort : dataPorts)
			ret.append(" " + dataPort);
		for(Integer dataId : dataIds)
			ret.append(" " + dataId);
		
		return ret.toString();
		
		/*return "bin " + ID + " localhost 31339 localhost 31338 " + dataHost + 
				" " + dataPort +
				" localhost 31340 " + deviceID +
				" 1 0 4096 64 456 " + dataID;*/
	}		

	private String getOffsets() {
		int[] offsets = assignedKernel.getOffset();
		return concatKernelAttrs(offsets);
	}

	private String getGlobalSizes() {
		int[] globalSizes = assignedKernel.getGlobalSize();
		return concatKernelAttrs(globalSizes);
	}
	
	private String getLocalSizes() {
		int[] localSizes = assignedKernel.getLocalSize();
		return concatKernelAttrs(localSizes);
	}
	
	private String concatKernelAttrs(int[] attrs) {
		return attrs[0] + " " + attrs[1] + " " + attrs[2];
	}

	private String getClusterHost() {
		if(device == null) return unassignedString;
		return device.unit.cluster.hostname;		
	}

	private int getClusterTCPPort() {
		if(device == null) return unassignedInt;
		return device.unit.cluster.TCPPort;
	}
	
	private int getClusterUdpPort() {
		if(device == null) return unassignedInt;
		return device.unit.cluster.UDPPort;
	}

	private String getDeviceId() {
		if(device == null) return unassignedString;
		return device.name;
	}

	/*
	public void setDataAddress(String status) {
		System.out.println(status);
		String[] dataAddress = status.split(" ");
		this.dataHost = dataAddress[0];
		this.dataPort = dataAddress[1];
		this.dataID = dataAddress[2];		
	}*/	
	
	public int getNumData() {
		return dataHosts.size();
	}

	public void run() {
		System.out.println("RUN");
		this.device.unit.cluster.runJob(this);	
		this.state = JobState.PROCESSING;
	}

	public void schedule(Device device) {
		this.device = device;
		this.state = JobState.SCHEDULED;		
	}

	public JobInfo getJobInfo() {
		// TODO: assert state
		
		JobInfo ret = new JobInfo();
		
		ret.runString = this.toString();
		ret.unitID = this.device.unit.ID;
		
		return ret;
	}	
}
