package pl.gda.pg.eti.kernelhive.cluster;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public class HiveCluster implements Daemon {
	private String clusterHostname;
	private String engineHostname;

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		try {
			HiveCluster cluster = new HiveCluster();
			parseArguments(args, cluster);
			cluster.start();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void destroy() {
		System.out.println("SVC DESTROY");		
	}

	@Override
	public void init(DaemonContext dc) throws DaemonInitException, Exception {
		System.out.println("SVC INIT");
		parseArguments(dc.getArguments(), this);
	}

	@Override
	public void start() throws Exception {
		System.out.println("SVC START");
		ClusterManager cm = new ClusterManager(this.clusterHostname, this.engineHostname);		
		
		// FIXME:
		while(true);
	}

	@Override
	public void stop() throws Exception {
		System.out.println("SVC STOP");
	}
	
	private static void parseArguments(String[] args, HiveCluster instance) throws Exception {
		if(args.length < 2) throw new Exception("Cluster and engine hostnames should be provided as argument.");
		instance.clusterHostname = args[0];
		instance.engineHostname = args[1];
	}
}
