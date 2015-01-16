/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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

import pl.gda.pg.eti.kernelhive.cluster.monitoring.MonitoringServer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBean;
import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBeanService;
import pl.gda.pg.eti.kernelhive.common.communication.CommunicationException;
import pl.gda.pg.eti.kernelhive.common.communication.DataPublisher;
import pl.gda.pg.eti.kernelhive.common.communication.NetworkAddress;

public class ClusterManager {

	private static final Logger logger = Logger.getLogger(ClusterManager.class.getName());
	private static final int UPDATE_PERIOD = 30 * 1000;   // every 30s
	private static final int TCP_PORT = 31338;
	private static final int DATA_PORT = 31339;
	private static final int UDP_PORT = 31340;
	private static final int MONITORING_UDP_PORT = 31341;
	private String engineHostname;
	private String clusterHostname;
	private Integer enginePort;
	private Cluster cluster;
	private ClusterBean clusterBean;
	private DataPublisher dataPublisher;
	private UnitServer unitServer;
	private MonitoringServer monitoringServer;
	private RunnerServer runnerServer;
	private JobRunner jobRunner;
	private Timer clusterUpdateTimer = new Timer();

	public ClusterManager(String clusterHostname, String engineHostname, Integer enginePort) {
		this.clusterHostname = clusterHostname;
		this.engineHostname = engineHostname;
		this.enginePort = enginePort;

		this.setClusterBean();

		cluster = new Cluster(TCP_PORT, DATA_PORT, UDP_PORT, clusterHostname);
		updateClusterInEngine(true);
	}

	private void initClusterManager() {
		dataPublisher = new DataPublisher(new NetworkAddress(clusterHostname, DATA_PORT));
		unitServer = new UnitServer(clusterHostname, TCP_PORT, cluster, clusterBean);
		monitoringServer = new MonitoringServer(MONITORING_UDP_PORT, cluster);
		runnerServer = new RunnerServer(UDP_PORT, clusterBean);
		jobRunner = new JobRunner(cluster, clusterBean, unitServer, dataPublisher);

		try {
			unitServer.start();
			runnerServer.start();
			dataPublisher.start();
			monitoringServer.start();
		} catch (CommunicationException ex) {
			logger.log(Level.SEVERE, "Error while starting one of the servers", ex);
			try {
				unitServer.stop();
			} catch (CommunicationException e) {
				logger.log(Level.SEVERE, "Error while stopping UnitServer", ex);
			}
			try {
				dataPublisher.stop();
			} catch (CommunicationException ex1) {
				Logger.getLogger(ClusterManager.class.getName()).log(Level.SEVERE, null, ex1);
			}
			runnerServer.stop();
			monitoringServer.stop();
			return;
		}

		jobRunner.run();

		clusterUpdateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				logger.info("Timer run 1");
				updateClusterInEngine(true);
			}
		}, UPDATE_PERIOD);
	}

	private void setClusterBean() {
		try {
			ClusterBeanService cbs = new ClusterBeanService(
					new URL("http://" + engineHostname + ":" + enginePort + "/ClusterBeanService/ClusterBean?wsdl"),
					new QName("http://engine.kernelhive.eti.pg.gda.pl/",
					"ClusterBeanService"));
			clusterBean = cbs.getClusterBeanPort();
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, "Error while starting ClusterBeanService", e);
		}
	}

	private void updateClusterInEngine(final boolean init) {
		logger.info("Updating cluster in engine: " + cluster.hostname + " with " + cluster.getUnitList().size() + " units");
		try {
			int clusterId = clusterBean.update(cluster);
			cluster.id = clusterId;
			logger.info("Updated cluster in engine. Got ID: " + clusterId);
			if (init) {
				initClusterManager();
			}
			clusterUpdateTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					updateClusterInEngine(false);
				}
			}, UPDATE_PERIOD);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error updating cluster in engine. Next try in 3s...", e);
			clusterUpdateTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					updateClusterInEngine(init);
				}
			}, 3000);
		}
	}
}
