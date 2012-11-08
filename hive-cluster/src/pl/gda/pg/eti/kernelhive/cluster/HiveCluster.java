package pl.gda.pg.eti.kernelhive.cluster;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public class HiveCluster implements Daemon {
	private String clusterHostname;

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		try {
			HiveCluster cluster = new HiveCluster();
			cluster.clusterHostname = parseHostname(args);
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
		this.clusterHostname = parseHostname(dc.getArguments());
	}

	@Override
	public void start() throws Exception {
		System.out.println("SVC START");
		ClusterManager cm = new ClusterManager(this.clusterHostname);		
		
		while(true);
	}

	@Override
	public void stop() throws Exception {
		System.out.println("SVC STOP");
	}
	
	private static String parseHostname(String[] args) throws Exception {
		if(args.length < 1) throw new Exception("Cluster hostname should be provided as argument.");
		return args[0];
	}
}
