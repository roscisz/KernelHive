/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.cluster;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBean;
import pl.gda.pg.eti.kernelhive.common.clusterService.JobInfo;
import pl.gda.pg.eti.kernelhive.common.communication.DataPublisher;
import pl.gda.pg.eti.kernelhive.common.communication.Decoder;
import pl.gda.pg.eti.kernelhive.common.communication.TCPServer;

/**
 *
 * @author szymon
 */
public class JobRunner {

	private static final Logger logger = Logger.getLogger(JobRunner.class.getName());
	private Cluster cluster;
	private ClusterBean clusterBean;
	private UnitServer unitServer;
	private DataPublisher dataPublisher;

	public JobRunner(Cluster cluster, ClusterBean clusterBean,
			UnitServer unitServer, DataPublisher dataPublisher) {
		this.cluster = cluster;
		this.clusterBean = clusterBean;
		this.unitServer = unitServer;
		this.dataPublisher = dataPublisher;
	}

	public void run() {
		while (true) {
			boolean gotJob = tryProcessJob();
			// if got job, try to get next immediately
			// it not, wait a while and try again
			if (!gotJob) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					Logger.getLogger(getClass().getName()).log(
							Level.SEVERE, "Error while sleeping", e);
				}
			}
		}
	}

	private boolean tryProcessJob() {
		JobInfo jobInfo = tryGetJob();
		if (jobInfo != null) {
			Logger.getLogger(getClass().getName()).info("Got job " + jobInfo);
			runJob(jobInfo);
			return true;
		}
		return false;
	}

	private void runJob(JobInfo jobInfo) {
		//System.out.println("Kernel: " + jobInfo.kernelString);
		Logger.getLogger(getClass().getName()).info("run job for unit id: " + jobInfo.unitID);
		UnitProxy proxy = unitServer.getProxy(jobInfo.unitID);
		if (proxy == null) {
			logger.severe("No unit proxy found for " + jobInfo.unitID);
			return;
		}
		deployKernel(jobInfo);
		deployDataIfURL(jobInfo);
		proxy.runJob(jobInfo);
	}

	private void deployKernel(JobInfo jobInfo) {
		jobInfo.kernelHost = cluster.hostname;
		jobInfo.kernelPort = cluster.dataPort;
		jobInfo.kernelID = dataPublisher.publish(TCPServer.byteBufferToArray(Decoder.encode(jobInfo.kernelString)));
	}

	private void deployDataIfURL(JobInfo jobInfo) {

		// TODO: error if inputdataUrl and dataString are both not null
		if (jobInfo.inputDataUrl != null) {
			System.out.println("Deploying data from " + jobInfo.inputDataUrl);
			byte[] data = downloadURL(jobInfo.inputDataUrl);
			jobInfo.dataString = "1 " + cluster.hostname + " " + cluster.dataPort + " " + dataPublisher.publish(data);
		}
	}

	private JobInfo tryGetJob() {
		if (clusterBean != null) {
			try {
				JobInfo ret = clusterBean.getJob();
				return ret;
			} catch (Exception e) {
				logger.log(Level.WARNING, "Error getting job from engine", e);
			}
		} else {
			logger.warning("Cluster service is not set");
		}
		return null;
	}

	private byte[] downloadURL(String urlString) {
		List<Byte> bytes = new LinkedList<>();
		logger.info("Downloading stream from " + urlString);

		URL url;
		try {
			url = new URL(urlString);
			InputStream is = url.openStream();
			DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
			Byte c;
			while (true) {
				c = dis.readByte();
				bytes.add(c);
			}
		} catch (EOFException e) {
			logger.info("Downloading finished");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while downloading stream", e);
		}

		System.out.println("Bytes: " + bytes);

		return byteListToByteArray(bytes);
	}

	private byte[] byteListToByteArray(List<Byte> bytes) {
		System.out.println("Bytes: " + bytes.size());
		byte[] ret = new byte[bytes.size()];

		int i = 0;
		for (Byte b : bytes) {
			ret[i] = b;
			i++;
		}

		return ret;
	}
}
