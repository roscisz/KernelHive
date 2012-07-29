package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;

public class Job extends HasID {
	
	public enum JobState {
		WAITING,
		READY,
		SCHEDULED,
		PROCESSING,
		FINISHED
	}

	// TODO: forward properties to the node
	public List<Integer> offsets;
	public List<Integer> globalSizes;
	public List<Integer> localSizes;
	public String kernelHost;
	public int kernelPort;
	public int kernelId;
	public List<String> dataHosts;
	public List<Integer> dataPorts;
	public List<Integer> dataIds;	
	
	public Device device;
	
	private Task task;
	public EngineGraphNodeDecorator node;
	public JobState state = JobState.WAITING;
	public int progress = -1;
		
	public Job(EngineGraphNodeDecorator node, Task task) {
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
		ret.append(" " + getNumDimensions());
		for(Integer offset : offsets)
			ret.append(" " + offset);
		for(Integer globalSize : globalSizes)
			ret.append(" " + globalSize);
		for(Integer localSize : localSizes)
			ret.append(" " + localSize);
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
	
	private String getClusterHost() {
		return device.unit.cluster.hostname;		
	}

	private int getClusterTCPPort() {
		return device.unit.cluster.TCPPort;
	}
	
	private int getClusterUdpPort() {
		return device.unit.cluster.UDPPort;
	}

	private String getDeviceId() {
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
	
	public int getNumDimensions() {
		return offsets.size();
	}
	
	public int getNumData() {
		return dataHosts.size();
	}

	public void run() {
		this.device.unit.cluster.runJob(this);		
	}		
}
