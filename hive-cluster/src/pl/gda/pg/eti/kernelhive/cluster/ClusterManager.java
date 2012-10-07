package pl.gda.pg.eti.kernelhive.cluster;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBean;
import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBeanService;
import pl.gda.pg.eti.kernelhive.common.clusterService.JobInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.communication.CommunicationException;
import pl.gda.pg.eti.kernelhive.common.communication.DataPublisher;
import pl.gda.pg.eti.kernelhive.common.communication.Decoder;
import pl.gda.pg.eti.kernelhive.common.communication.NetworkAddress;
import pl.gda.pg.eti.kernelhive.common.communication.TCPServer;
import pl.gda.pg.eti.kernelhive.common.communication.TCPServerListener;
import pl.gda.pg.eti.kernelhive.common.communication.UDPServer;
import pl.gda.pg.eti.kernelhive.common.communication.UDPServerListener;

public class ClusterManager implements TCPServerListener, UDPServerListener {	
	
	private final String clusterDataHostname = "hive-cluster";
	private final String clusterTcpHostname = "hive-cluster";	
	private final int clusterTCPPort = 31338;
	private final int clusterDataPort = 31339;
	private final int clusterUDPPort = 31340;	

	private Hashtable<SocketChannel, UnitProxy> unitsMap = new Hashtable<SocketChannel, UnitProxy>();
	
	private Cluster cluster = new Cluster(clusterTCPPort, clusterDataPort, clusterUDPPort, clusterTcpHostname);
	private ClusterBean clusterBean;
	private DataPublisher dataPublisher;
		
	public ClusterManager() {
		try {
			TCPServer unitServer = new TCPServer(new NetworkAddress(clusterTcpHostname, clusterTCPPort), this);
			dataPublisher = new DataPublisher(new NetworkAddress(clusterDataHostname, clusterDataPort));
			UDPServer runnerServer = new UDPServer(clusterUDPPort, this);
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
		
		tryUpdateInEngine();
				
		while(true) {
			System.out.println("Cluster getJobThread cycle");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tryProcessJob();
		}
	}
	
	private void tryUpdateInEngine() {
		while(clusterBean == null)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		updateClusterInEngine(cluster, clusterBean);
	}

	private void tryProcessJob() {
		JobInfo jobInfo = tryGetJob(cluster, clusterBean);
		if(jobInfo != null)
			runJob(jobInfo);
	}

	private void runJob(JobInfo jobInfo) {
		System.out.println("Kernel: " + jobInfo.kernelString);
		UnitProxy proxy = getProxyById(jobInfo.unitID);
		deployKernel(jobInfo);
		deployDataIfURL(jobInfo);
		proxy.runJob(jobInfo);		
	}

	private void deployKernel(JobInfo jobInfo) {
		jobInfo.kernelHost = clusterDataHostname;
		jobInfo.kernelPort = clusterDataPort;
		jobInfo.kernelID = dataPublisher.publish(TCPServer.byteBufferToArray(Decoder.encode(jobInfo.kernelString)));		
	}
	
	private void deployDataIfURL(JobInfo jobInfo) {
		
		// TODO: error if inputdataUrl and dataString are both not null
		if(jobInfo.inputDataUrl != null) {
			System.out.println("Deploying data from " + jobInfo.inputDataUrl);
			byte[] data = downloadURL(jobInfo.inputDataUrl);
			jobInfo.dataString = "1 " + clusterDataHostname + " " + clusterDataPort + " " + dataPublisher.publish(data);
		}		
	}

	private UnitProxy getProxyById(int unitID) {
		for(UnitProxy proxy : unitsMap.values())
			if(proxy.unit.ID == unitID)
				return proxy;
		return null;
	}

	private JobInfo tryGetJob(Cluster cluster, ClusterBean clusterBean) {
		if(clusterBean != null) {
			System.out.println("Cluster lookin for a job");
			JobInfo ret = clusterBean.getJob();
			System.out.println("Got job: " + ret);
			return ret;
		}
		System.out.println("Cluster trying to get job but clusterBean is null");
		return null;
	}

	@Override
	public void onConnection(SocketChannel channel) {
		
		System.out.println("Got connection from channel " + channel);	
		
	}

	@Override
	public void onTCPMessage(SocketChannel channel, ByteBuffer messageBuffer) {
		String message = Decoder.decode(messageBuffer);	
		
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
		System.out.println("Command update...");
		UnitProxy proxy = unitsMap.get(channel);
		if(proxy == null) {
			Unit unit = new Unit(cluster);
			cluster.unitList.add(unit);
			proxy = new UnitProxy(channel, unit);
			unitsMap.put(channel,  proxy);
		}
		proxy.unit.update(data);
		System.out.println("Now we have " + unitsMap.size() + " clients");
		for(UnitProxy up : unitsMap.values())
			System.out.println(up.unit);		
		tryUpdateInEngine();
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
		if(cluster.unitList.contains(channel))
			cluster.unitList.remove(unitsMap.get(channel).unit);
		if(unitsMap.containsKey(channel))
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
		
		clusterBean.reportOver(id, status);
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
	
	private byte[] downloadURL(String urlString)
	{
		List<Byte> bytes = new LinkedList<Byte>();

	    URL url;
		try {
			url = new URL(urlString);			
			InputStream is = url.openStream();
			DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
			int i = 0;
			Byte c;
			while(true) {
				c = dis.readByte();
				bytes.add(c);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (EOFException eoe) {
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	    	
		System.out.println("Bytes: " + bytes);
		
	    return byteListToByteArray(bytes);
	  }

	private byte[] byteListToByteArray(List<Byte> bytes) {
		System.out.println("Bytes: " + bytes.size());		
		byte[] ret = new byte[bytes.size()];
		
		int i = 0;
		for(Byte b : bytes) {
			ret[i] = b;
			i++;
		}
		
		return ret;
	}

}
