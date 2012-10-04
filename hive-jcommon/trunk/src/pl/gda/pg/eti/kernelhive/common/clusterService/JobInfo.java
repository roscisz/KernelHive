package pl.gda.pg.eti.kernelhive.common.clusterService;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;

public class JobInfo {
	
	public int unitID;
	// change to localhost? 
	private String resultDataHost = "hive-cluster";
	private int resultDataPort = 31339;
	public String kernelString;
	public GraphNodeType jobType;
	public String inputDataUrl;
	
	public String kernelHost;
	public int kernelPort;
	public int kernelID;
	public int ID;
	public String clusterHost;
	public int clusterTCPPort;
	public int clusterUDPPort;
	public String deviceID;
	public String offsets;
	public String globalSizes;
	public String localSizes;
	public String outputSize;
	public String dataString;
	
	@Override
	public String toString() {	
		StringBuilder ret = new StringBuilder();
		
		ret.append(" " + jobType.toString());
		ret.append(" " + ID);
		ret.append(" " + clusterHost);
		ret.append(" " + clusterTCPPort);
		ret.append(" " + clusterUDPPort);
		ret.append(" " + deviceID);		
		// FIXME:
		//ret.append(" 3");
		//ret.append(" " + offsets);
		//ret.append(" " + globalSizes);
		//ret.append(" " + localSizes);
		ret.append(" 3 0 0 0 512 1 1 64 1 1");
		ret.append(" " + outputSize);
		ret.append(" " + kernelHost);
		ret.append(" " + kernelPort);
		ret.append(" " + kernelID);
		ret.append(" " + dataString);
		
		// TODO: dynamic output number assignment
		if(jobType == GraphNodeType.PARTITIONER)
			ret.append(" 2");
		else ret.append(" 1");
		
		ret.append(" " + resultDataHost);
		ret.append(" " + resultDataPort);
		
		return ret.toString();
	}
	
	
}
