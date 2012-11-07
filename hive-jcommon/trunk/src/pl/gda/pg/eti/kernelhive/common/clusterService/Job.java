package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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
	
	public int numData = 1;
	private int collectedAddresses = 0;
	private List<DataAddress> dataAddresses = new ArrayList<DataAddress>();
	
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
		return concatKernelAttrs(globalSizes);
	}
	
	private String getLocalSizes() {
		int[] localSizes = getAssignedKernel().getLocalSize();
		return concatKernelAttrs(localSizes);
	}
	
	private String getOutputSize() {
		return "" + this.getAssignedKernel().getOutputSize();
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
		// FIXME: it shouldn't happen:
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
	
	protected GraphNodeType getJobType() {
		return node.getGraphNode().getType();
	}
		
	private String getResultDataHost() {
		// FIXME: it shouldn't happen:
		if(device== null) return "hive-cluster";
		return device.unit.cluster.hostname;
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
		ret.kernelString = this.getAssignedKernel().getKernel();
		
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
		ret.nOutputs = nOutputs;
		ret.resultDataHost = getResultDataHost();
		
		System.out.println("Setting inputDataUrl " + inputDataUrl);
		ret.inputDataUrl = inputDataUrl;
		
		return ret;
	}

	protected IKernelString getAssignedKernel() {
		return this.node.getKernels().get(0);
	}

	public void tryToCollectDataAddresses(Iterator<DataAddress> dataIterator) {	
		System.out.println("Job " + ID + " trying to collect " + numData + " addresses.");
		
		while(collectedAddresses != numData) {
			try {
				dataAddresses.add(dataIterator.next());
				collectedAddresses++;
			}
			catch(NoSuchElementException nsee) {
				break;
			}			
		}
		System.out.println("Job " + this.ID + " collected " + collectedAddresses + " dataAddresses.");
		if(collectedAddresses == numData)
			getReady();
	}

	public void finish() {
		this.state = JobState.FINISHED;
		this.device.busy = false;		
	}

	public boolean canBeScheduledOn(Device device) {
		return true;
	}
	
	protected void getReady() {
		this.state = JobState.READY;		
	}
}
