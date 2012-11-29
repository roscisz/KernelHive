package pl.gda.pg.eti.kernelhive.common.clientService;

import java.util.ArrayList;
import java.util.List;

public class ClusterInfo {

	public List<UnitInfo> unitInfos;
	public String hostname;
	public int TCPPort, UDPPort, dataPort;
	public int ID;

	public ClusterInfo() {
		
	}
	
	public ClusterInfo(int ID, List<UnitInfo> unitInfos, String hostname, int TCPPort, int UDPPort, int dataPort) {
		this.ID = ID;
		this.unitInfos = unitInfos;
		this.TCPPort = TCPPort;
		this.UDPPort = UDPPort;
		this.dataPort = dataPort;
	}

	@Override
	public String toString() {
		return "Cluster " + ID + " on " + hostname + " serving TCP (" + TCPPort + "), UDP + " + UDPPort + ", TCP data (" + dataPort +"), with " + unitInfos.size() + " units registered.";
	}	
}
