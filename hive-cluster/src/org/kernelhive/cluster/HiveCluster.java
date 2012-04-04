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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(DaemonContext arg0) throws DaemonInitException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() throws Exception {
		ClusterManager cm = new ClusterManager();	
		
		while(true);
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
