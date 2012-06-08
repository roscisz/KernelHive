package org.kernelhive.cluster;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public class HiveCluster implements Daemon {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			(new HiveCluster()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void destroy() {
		System.out.println("SVC DESTROY");		
	}

	@Override
	public void init(DaemonContext arg0) throws DaemonInitException, Exception {
		System.out.println("SVC INIT");
	}

	@Override
	public void start() throws Exception {
		System.out.println("SVC START");
		ClusterManager cm = new ClusterManager();	
		
		// TODO: connect to the execution engine
		
		while(true);
	}

	@Override
	public void stop() throws Exception {
		System.out.println("SVC STOP");
	}

}
