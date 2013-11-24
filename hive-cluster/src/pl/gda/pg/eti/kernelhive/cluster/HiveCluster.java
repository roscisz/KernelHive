package pl.gda.pg.eti.kernelhive.cluster;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public class HiveCluster implements Daemon {

	private String clusterHostname;
	private static String engineHostname;
	private static final Logger logger = Logger.getLogger(HiveCluster.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			HiveCluster cluster = new HiveCluster();
			parseArguments(args, cluster);
			cluster.start();
		} catch (IllegalArgumentException e) {
			logger.severe("Cluster and engine hostnames should be provided as argument.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception during initialization", e);
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
		while (true);
	}

	@Override
	public void stop() throws Exception {
		System.out.println("SVC STOP");
	}

	private static void parseArguments(String[] args, HiveCluster instance) throws IllegalArgumentException {
		if (args.length < 2) {
			throw new IllegalArgumentException();
		}
		instance.clusterHostname = args[0];
		engineHostname = args[1];
	}

	public static String getEngineHostname() {
		return engineHostname;
	}
}
