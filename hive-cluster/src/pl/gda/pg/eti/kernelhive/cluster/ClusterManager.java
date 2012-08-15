package pl.gda.pg.eti.kernelhive.cluster;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.namespace.QName;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBean;
import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBeanService;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.JobInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.communication.CommunicationException;
import pl.gda.pg.eti.kernelhive.common.communication.DataPublisher;
import pl.gda.pg.eti.kernelhive.common.communication.NetworkAddress;
import pl.gda.pg.eti.kernelhive.common.communication.TCPServer;
import pl.gda.pg.eti.kernelhive.common.communication.TCPServerListener;
import pl.gda.pg.eti.kernelhive.common.communication.UDPServer;
import pl.gda.pg.eti.kernelhive.common.communication.UDPServerListener;

public class ClusterManager implements TCPServerListener, UDPServerListener {
	
	private Hashtable<SocketChannel, UnitProxy> unitsMap = new Hashtable<SocketChannel, UnitProxy>();
	
	private Cluster cluster = new Cluster();
	private ClusterBean clusterBean;
		
	public ClusterManager() {
		try {
			TCPServer unitServer = new TCPServer(new NetworkAddress("localhost", 31338), this);
			DataPublisher dp = new DataPublisher(new NetworkAddress("localhost", 31340));
			UDPServer runnerServer = new UDPServer(31339, this);
		} catch (CommunicationException e) {
			// TODO: Exit gracefully
			e.printStackTrace();
		}
			
		ClusterBeanService cbs;
		try {
			cbs = new ClusterBeanService(new URL("http://hive-engine:8080/ClusterBeanService/ClusterBean?wsdl"), new QName("http://engine.kernelhive.eti.pg.gda.pl/", "ClusterBeanService"));
			clusterBean = cbs.getClusterBeanPort();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}	
		
		//new Thread(this).start();
		
		while(true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tryProcessJob();
		}
	}
	
	private void tryProcessJob() {
		JobInfo jobInfo = tryUpdateAndGetJob(cluster, clusterBean);
		if(jobInfo != null)
			runJob(jobInfo);
		else System.out.println("Got null job, nvm.");
	}

	private void runJob(JobInfo jobInfo) {
		System.out.println("Run job " + jobInfo.runString);
		UnitProxy proxy = getProxyById(jobInfo.unitID);
		proxy.runJob(jobInfo);		
	}

	private UnitProxy getProxyById(int unitID) {
		for(UnitProxy proxy : unitsMap.values())
			if(proxy.unit.ID == unitID)
				return proxy;
		return null;
	}

	private JobInfo tryUpdateAndGetJob(Cluster cluster, ClusterBean clusterBean) {
		if(clusterBean != null) {
			updateClusterInEngine(cluster, clusterBean);
			return clusterBean.getJob();
		}
		return null;
	}

	@Override
	public void onConnection(SocketChannel channel) {
		
		System.out.println("Got connection from channel " + channel);		
		
	}

	@Override
	public void onTCPMessage(SocketChannel channel, String message) {			
		System.out.println("Message " + message + " from channel " + channel);
		// FIXME: define separators in one place
		String[] command = message.split(" ", 2);
		
		if(command[0].equals("UPDATE")) {
			commandUpdate(channel, command[1]);
		}
		else if(command[0].equals("OVER")) {
			commandOver(command[1]);
		}
		
	}

	private void commandUpdate(SocketChannel channel, String data) {
		UnitProxy proxy = unitsMap.get(channel);
		if(proxy == null) {
			Unit unit = new Unit(cluster);
			cluster.unitList.add(unit);
			proxy = new UnitProxy(channel, unit);
			unitsMap.put(channel,  proxy);
			//tryUpdateAndGetJob(cluster, clusterBean);
		}
		proxy.unit.update(data);
		System.out.println("Now we have " + unitsMap.size() + " clients");
	}
	
	private void updateClusterInEngine(Cluster cluster, ClusterBean clusterBean) {
		System.out.println("Updating cluster in engine");
		clusterBean.update(cluster);
		System.out.println("Updated cluster in engine");
		
	}

	private void commandOver(String message) {
		String[] command = message.split(" ", 2);
		int id = Integer.parseInt(command[0]);
		onJobDone(id, command[1]);		
	}
	
	@Override
	public void onDisconnection(SocketChannel channel) {
		
		System.out.println("Channel " + channel + " disconnected.");
		unitsMap.remove(channel);
		System.out.println("Now we have " + unitsMap.size() + " clients");		
		
	}

	@Override
	public void onUDPMessage(String message) {
		String[] report = message.split(" ", 3);
		int id = Integer.parseInt(report[0]);		
		int percent = Integer.parseInt(report[1]);
		
		// TODO: Update progress in Job representation
		System.out.println("JobID " + id + ": " + percent + "% done.");
	}

	private void onJobDone(int id, String status) {
		System.out.println("Job " + id + " over.");				
		
		/*id++;
		if(id < currentJobs.size()) {
			System.out.println("Running job " + id);
			currentJobs.get(id).setDataAddress(status);
			currentJobs.get(id).run();
		}
		else System.out.println("All jobs done.");
		*/		
	}
	
	private UnitProxy findUnitProxy(Unit unit) {
		for(UnitProxy proxy : unitsMap.values())
			if(proxy.unit.equals(unit))
				return proxy;
		return null;
	}
}
