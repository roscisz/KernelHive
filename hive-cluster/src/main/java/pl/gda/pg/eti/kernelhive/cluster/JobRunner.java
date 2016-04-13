/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Szymon Bultrowicz
 * Copyright (c) 2016 Adrian Boguszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.cluster;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBean;
import pl.gda.pg.eti.kernelhive.common.clusterService.DataAddress;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.JobInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.communication.DataPublisher;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
		if(jobInfo.state == Job.JobState.PREFETCHING)
			runPrefetching(jobInfo);
		else runJobOnUnit(jobInfo);
	}
		
	private void runJobOnUnit(JobInfo jobInfo) {
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
	
	private void runPrefetching(JobInfo jobInfo) {
		logger.info("Running prefetching for job " + jobInfo.ID);
		Unit destUnit = unitServer.getProxy(jobInfo.unitID).getUnit();
		try {
			//new Thread(new DataManager(jobInfo, destUnit.getHostname(), destUnit.getDataServerPort(), clusterBean)).run();
			new Thread(new DataManager(jobInfo, "172.20.0.75", 27017, unitServer)).run();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private void deployKernel(JobInfo jobInfo) {
		Unit destUnit = unitServer.getProxy(jobInfo.unitID).getUnit();
		try {
			DataManager dp = new DataManager(jobInfo, destUnit.getHostname(), destUnit.getDataServerPort(), null);
			DataAddress dataAddress = dp.uploadData(jobInfo.kernelString, new DataAddress("localhost", 27017, null));
			jobInfo.kernelHost = dataAddress.hostname;
			jobInfo.kernelPort = dataAddress.port;
			jobInfo.kernelID = dataAddress.ID;
		} catch (UnknownHostException e) {
			logger.log(Level.SEVERE, null, e);
		}
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
