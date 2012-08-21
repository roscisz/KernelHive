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
	public String dataHost;
	public int dataPort;
	public int dataID;	
	
	@Override
	public String toString() {	
		StringBuilder ret = new StringBuilder();
		
		ret.append(" " + jobType.toString());
		ret.append(" " + ID);
		ret.append(" " + clusterHost);
		ret.append(" " + clusterTCPPort);
		ret.append(" " + clusterUDPPort);
		ret.append(" " + deviceID);
		ret.append(" " + offsets);
		ret.append(" " + globalSizes);
		ret.append(" " + localSizes);
		ret.append(" " + kernelHost);
		ret.append(" " + kernelPort);
		ret.append(" " + kernelID);
		// TODO: many input data entities
		ret.append(" 1");
		ret.append(" " + dataHost);
		ret.append(" " + dataPort);
		ret.append(" " + dataID);
		ret.append(" " + resultDataHost);
		ret.append(" " + resultDataPort);
		
		return ret.toString();
	}
	
	
}
