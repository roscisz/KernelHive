package pl.gda.pg.eti.kernelhive.common.clientService;

public class ClusterInfo {
	
	//OBSOLETE:
	public String info;

	public ClusterInfo() {
		
	}
	
	public ClusterInfo(String string) {
		this.info = string;
	}

	@Override
	public String toString() {
		return "ClusterInfo [info=" + info + "]";
	}	
}
