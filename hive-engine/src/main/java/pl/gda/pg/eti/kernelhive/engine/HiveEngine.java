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
package pl.gda.pg.eti.kernelhive.engine;

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.JobProgress;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.DataAddress;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Db;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Persistance;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.PreviewObject;
import pl.gda.pg.eti.kernelhive.engine.http.file.utils.HttpFileUploadClient;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IOptimizer;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.ClusterDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.DeviceDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.UnitDefinition;
import pl.gda.pg.eti.kernelhive.engine.optimizers.EnergyOptimizer;
import pl.gda.pg.eti.kernelhive.engine.optimizers.PackageOptimizer;
import pl.gda.pg.eti.kernelhive.engine.optimizers.SimpleOptimizer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiveEngine {

	private static HiveEngine instance;
	// FIXME: obsolete
	private static String resultUploadURL = "http://localhost:8080/hive-engine/upload";
	private static String resultDownloadURL = "http://localhost:8080/hive-engine/download?filename=";
	
	private Map<Integer, Cluster> clusters = new HashMap<>();
	private Map<String, Cluster> clustersIps = new HashMap<>();
	private Map<Integer, Workflow> workflows = new HashMap<>();
	private Map<Integer, List<PreviewObject>> jobPreviews = new HashMap<>();
	int nextClusterId = 0;
	private IOptimizer optimizer;

	private static int energyLimit = 1000;
	
	private List<EngineGraphNodeDecorator> nodes;
	private String projectName;
	private String inputDataURL;
	private static final Logger LOG = Logger.getLogger(HiveEngine.class.getName());

	private HiveEngine() {
		this.optimizer = new SimpleOptimizer();
		//this.optimizer = new PackageOptimizer();
		//this.optimizer = new PrefetchingOptimizer(new SimpleOptimizer());
		//this.optimizer = new EnergyOptimizer(new GreedyKnapsackSolver());
	}

	public static synchronized HiveEngine getInstance() {
		if (instance == null) {
			instance = new HiveEngine();
			LOG.info("Creating HiveEngine singleton " + instance);
		}
		return instance;
	}

	public void updateCluster(Cluster cluster, String ip) {
		H2Persistance persistance;
		try {
			persistance = new H2Persistance(new H2Db().open());
		} catch (H2Db.InitializationException ex) {
			LOG.log(Level.SEVERE, "Error initializing persistance layer", ex);
			return;
		}

		if (cluster.id == null) {
			// if no cluster ID given, try to search cluster by IP
			if (clustersIps.containsKey(ip)) {
				cluster.id = clustersIps.get(ip).id;
			} else {
				// if not found in memory, try in database
				Map<String, String> clusterSelector = new HashMap<>();
				clusterSelector.put("hostname", cluster.hostname);
				try {
					List<ClusterDefinition> clusters = persistance.selectAll(new ClusterDefinition(), clusterSelector);
					if (clusters.size() > 0) {
						cluster.id = BigDecimal.valueOf(clusters.get(0).getId()).intValue();
					} else {
						// if not found in DB, assign new ID
						cluster.id = ClusterHelper.getNextId();
					}
				} catch (H2Persistance.H2PersistanceException ex) {
					LOG.log(Level.SEVERE, "Error getting cluster", ex);
					cluster.id = ClusterHelper.getNextId();
				}
			}
		}
		if (this.clusters.containsKey(cluster.id)) {
			Cluster oldCluster = this.clusters.get(cluster.id);
			// what was that for?
			//oldCluster.runJob(null);
			LOG.info(String.format("Cluster update: %d on %s [%s]", oldCluster.id, oldCluster.hostname, oldCluster));
			oldCluster.merge(cluster);
			LOG.info(String.format("Cluster after update [%s]", oldCluster));
			cluster = oldCluster;
		} else {
			this.clusters.put(cluster.id, cluster);
			clustersIps.put(ip, cluster);
			LOG.info(String.format("New cluster: %d on %s", cluster.id, cluster.hostname));
		}
		clustersIps.put(ip, cluster);

		try {
			persistance.save(new ClusterDefinition(cluster));

			// clean whole cluster tree
			Map<String, String> unitSelector = new HashMap<>();
			unitSelector.put("clusterId", String.valueOf(cluster.id));
			List<UnitDefinition> oldUnitList = persistance.selectAll(new UnitDefinition(), unitSelector);
			for (UnitDefinition unit : oldUnitList) {
				Map<String, String> deviceSelector = new HashMap<>();
				deviceSelector.put("unitId", String.valueOf(unit.getId()));
				persistance.removeAll(new DeviceDefinition(), deviceSelector);
				persistance.remove(unit);
			}

			for (Unit unit : cluster.getUnitList()) {
				persistance.save(new UnitDefinition(unit));

				Map<String, String> deviceSelector = new HashMap<>();
				deviceSelector.put("unitId", String.valueOf(unit.getId()));
				persistance.removeAll(new DeviceDefinition(), deviceSelector);

				for (Device device : unit.getDevices()) {
					DeviceDefinition dd = new DeviceDefinition(device);
					persistance.save(dd);
				}
			}
		} catch (H2Persistance.H2PersistanceException ex) {
			LOG.log(Level.SEVERE, "Error while storing Cluster", ex);
		}

		System.out.println("Engine knows about clusters: " + clusters);
	}

	public Integer runWorkflow(List<EngineGraphNodeDecorator> nodes, String projectName, String inputDataURL) {
		LOG.info("Got new workflow");
		Workflow newWorkflow = new Workflow(nodes, projectName, inputDataURL);
		workflows.put(newWorkflow.getId(), newWorkflow);

		processWorkflow(newWorkflow);

		return newWorkflow.getId();
	}

	private void processWorkflow(Workflow workflow) {
		LOG.log(Level.INFO, "Processing workflow {0}", workflow.getId());

		List<Job> scheduledJobs = optimizer.processWorkflow(workflow, clusters.values());

		for (Job job : scheduledJobs) {
			// FIXME temporary only for test
			/*if(job instanceof MergerJob) {
			 System.out.println("RESULT FOR LIMIT " + energyLimit + ": " + workflow.getDebugTime());
			 job.finish();
			 workflows.clear();
			 energyLimit += 40;
			 if(energyLimit <= 2000)
			 runWorkflow(nodes, projectName, inputDataURL);
			 }
			 else*/
			job.run();
		}
	}

	public void cleanup() {
		// TODO: timeout
		for (Workflow workflow : workflows.values()) {
			// maybe it's not good idea, it can cause premature job scheduling of non finished workflows
//			processWorkflow(workflow);
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public Cluster getClusterByIp(String ip) {
		return clustersIps.get(ip);
	}

	public List<Cluster> getInfrastructure() {
		return new ArrayList(clusters.values());
	}

	public List<ClusterInfo> getInfrastructureInfo() {
		List<ClusterInfo> ret = new ArrayList<>();

		for (Cluster cluster : clusters.values()) {
			ret.add(cluster.getClusterInfo());
		}

		return ret;
	}

	public List<WorkflowInfo> browseWorkflows() {
		List<WorkflowInfo> ret = new LinkedList<>();

		for (Workflow workflow : workflows.values()) {
			ret.add(workflow.getWorkflowInfo());
		}

		return ret;
	}

	public void onProgress(int jobID, int progress) {
		LOG.info(String.format("ON progress job %d, progress %d", jobID, progress));
		EngineJob job = getJobByID(jobID);
		if (job != null) {
			Job.JobState stateBefore = job.state;
			job.setProgress(progress);
			if (job.state != stateBefore) {
				// maybe it's not good idea, it can cause premature job scheduling of non finished workflows
//				processWorkflow(job.workflow);
			}
		}
	}

	public void onJobOver(int jobID, String returnData) {
		EngineJob jobOver = getJobByID(jobID);
		
		if(jobOver.state == Job.JobState.PREFETCHING)
			onPrefetchingOver(jobOver, returnData);
		else onProcessingOver(jobOver, returnData);
	}
	
	private void onProcessingOver(EngineJob jobOver, String returnData) {
			
		LOG.warning(String.format("JOB OVER %d, %s", jobOver.getId(), returnData));

		jobOver.finish();

		List<DataAddress> resultAddresses = Job.parseAddresses(returnData);
		jobOver.workflow.debugTime();

		if (jobOver.workflow.getJobsByState(Job.JobState.FINISHED).size() == jobOver.workflow.getJobs().keySet().size()) {
			jobOver.workflow.finish(returnData);
		} else {
			jobOver.tryCollectFollowingJobsData(resultAddresses);
            processWorkflow(jobOver.workflow);
        }
	}
	
	private void onPrefetchingOver(EngineJob jobOver, String returnData) {
		System.out.println(jobOver.getId() + " - Prefetching over");
		jobOver.finishPrefetching(Job.getAddressesForDataString(returnData));
	}

	private String deployResult(byte[] result) {
		HttpFileUploadClient uploadClient = new HttpFileUploadClient();
		try {
			return uploadClient.postFileUpload(resultUploadURL, result);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, "Deployment error", e);
		}
		return "";
	}

	private Job getJobByGraphNode(IGraphNode graphNode) {
		for (Workflow workflow : workflows.values()) {
			Job job = workflow.getJobByGraphNode(graphNode);
			if (job != null) {
				return job;
			}
		}
		return null;
	}

	private EngineJob getJobByID(int jobID) {
		for (Workflow workflow : workflows.values()) {
			EngineJob job = workflow.getJobByID(jobID);
			if (job != null) {
				return job;
			}
		}
		return null;
	}
	
	public static int queryFreeDevicesNumber() {
		int ret = 0;
		for (Cluster cluster : instance.clusters.values()) {
			for (Unit unit : cluster.getUnitList()) {
				for (Device device : unit.getDevices()) {
					if (device.isAvailable()) {
						ret++;
					}
				}
			}
		}
		return ret;
	}

	public List<JobProgress> getWorkflowProgress(int workflowId) {
		List<JobProgress> workflowProgress = new ArrayList<>();
		Workflow workflow = workflows.get(workflowId);
		if (workflow != null) {
			for (EngineJob job : workflow.getJobs().values()) {
				workflowProgress.add(new JobProgress(job.getId(), job.getJobType(),
						job.state, job.progress));
			}
		}
		return workflowProgress;
	}

	public void terminateWorkflow(int workflowId) {
		Workflow workflow = workflows.get(workflowId);
		Logger.getLogger(getClass().getName()).info("engine terminate " + (workflow != null));
		if (workflow != null) {
			workflow.cancel();
		}
		workflows.remove(workflowId);
	}

	public void saveJobPreview(int jobId, List<PreviewObject> data) {
		jobPreviews.put(jobId, data);
	}

	public List<PreviewObject> getWorkflowPreview(int workflowId) {
		Workflow workflow = workflows.get(workflowId);
		if (workflow != null) {
			Map<Integer, EngineJob> jobs = workflow.getJobs();
			List<PreviewObject> previews = new ArrayList<>();
			for (int jobId : jobs.keySet()) {
				if (jobPreviews.containsKey(jobId)) {
					previews.addAll(jobPreviews.get(jobId));
				}
			}
			return previews;
		} else {
			LOG.warning(String.format("Workflow %d cannot be found", workflowId));
			return null;
		}
	}

	// FIXME: dirty dirty code
	public static int queryFreeDevicesNumberSpecific() {
		return ((EnergyOptimizer) instance.optimizer).chooseDevices(instance.clusters.values()).size();
	}

	public static List<Device> getAvailableDevices(Collection<Cluster> infrastructure) {
		List<Device> ret = new ArrayList<>();
		for (Cluster cluster : infrastructure) {
			for (Unit unit : cluster.getUnitList()) {
				for (Device device : unit.devices) {
					if (device.isAvailable()) {
						ret.add(device);
					}
				}
			}
		}
		return ret;
	}

	public static int getEnergyLimit() {
		return energyLimit;
	}

	public static void setEnergyLimit(int energyLimit) {
		HiveEngine.energyLimit = energyLimit;
	}
}
