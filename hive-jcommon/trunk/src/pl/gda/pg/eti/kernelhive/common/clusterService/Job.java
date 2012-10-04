package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;

public class Job extends HasID {
	
	public enum JobState {
		PENDING,
		READY,
		SCHEDULED,
		PROCESSING,
		FINISHED
	}
	
	private int numData;
	private List<DataAddress> dataAddresses = new ArrayList<DataAddress>();
	private Workflow task;
	
	public EngineGraphNodeDecorator node;
	public IKernelString assignedKernel;
	
	public Device device;
	
	public JobState state = JobState.PENDING;
	public int progress = -1;
		
	private int unassignedInt = 0;

	public String inputDataUrl;

	public Job() {				
	}
	
	public Job(EngineGraphNodeDecorator node, Workflow task) {
		this.node = node;
		this.task = task;
		prepareNumData();
	}				

	private void prepareNumData() {
		if(this.node.getGraphNode().getType() == GraphNodeType.MERGER)
			this.numData = 2;
		else this.numData = 1;		
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
	
	private String getOutputSize() {
		return "" + this.assignedKernel.getOutputSize();
	}
	
	private String getDataString() {
		StringBuilder ret = new StringBuilder();
		
		ret.append("" + numData);
		
		for(DataAddress da : dataAddresses) {
			ret.append(" " + da.hostname);
			ret.append(" " + da.port);
			ret.append(" " + da.ID);
		}
		
		return ret.toString();
	}
	
	private String concatKernelAttrs(int[] attrs) {
		return attrs[0] + " " + attrs[1] + " " + attrs[2];
	}

	private String getClusterHost() {
		if(device == null) return "hive-cluster";
		return device.unit.cluster.hostname;		
	}

	private int getClusterTCPPort() {
		if(device == null) return unassignedInt;
		return device.unit.cluster.TCPPort;
	}
	
	private int getClusterUDPPort() {
		if(device == null) return unassignedInt;
		return device.unit.cluster.UDPPort;
	}
	
	private int getUnitId() {
		if(this.device == null) return unassignedInt;
		return this.device.unit.ID;
	}	

	private String getDeviceId() {
		if(device == null) return "UNASSIGNED";
		return device.name;
	}
	
	private GraphNodeType getJobType() {
		return node.getGraphNode().getType();
	}

	public void run() {
		this.device.unit.cluster.runJob(this);	
		this.state = JobState.PROCESSING;
	}

	public void schedule(Device device) {
		this.device = device;
		this.state = JobState.SCHEDULED;
		this.device.busy = true;
	}

	public JobInfo getJobInfo() {
		// TODO: assert state
		
		JobInfo ret = new JobInfo();
		
		ret.unitID = getUnitId();
		ret.kernelString = this.assignedKernel.getKernel();
		
		ret.ID = ID;
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
		
		System.out.println("Setting inputDataUrl " + inputDataUrl);
		ret.inputDataUrl = inputDataUrl;
		
		return ret;
	}

	public void collectDataAddresses(Iterator<DataAddress> dataIterator) {	
		System.out.println("Job " + ID + " trying to collect " + numData + " addresses.");
		dataAddresses.clear();
		for(int i = 0; i != numData; i++) {
			dataAddresses.add(dataIterator.next());			
		}			
		System.out.println("Job " + this.ID + " collected dataAddresses.");
	}

	public void finish() {
		this.state = JobState.FINISHED;
		this.device.busy = false;		
	}
}
