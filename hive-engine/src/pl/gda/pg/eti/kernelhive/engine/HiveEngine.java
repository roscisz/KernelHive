package pl.gda.pg.eti.kernelhive.engine;

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

import pl.gda.pg.eti.kernelhive.common.clientService.ClusterInfo;
import pl.gda.pg.eti.kernelhive.common.clientService.JobProgress;
import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;
import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.DataAddress;
import pl.gda.pg.eti.kernelhive.common.clusterService.Device;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Unit;
import pl.gda.pg.eti.kernelhive.common.communication.DataDownloader;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.engine.http.file.utils.HttpFileUploadClient;
import pl.gda.pg.eti.kernelhive.engine.job.EngineJob;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Db;
import pl.gda.pg.eti.kernelhive.common.monitoring.h2.H2Persistance;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.PreviewObject;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IOptimizer;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.ClusterDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.DeviceDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.UnitDefinition;
import pl.gda.pg.eti.kernelhive.engine.optimizers.EnergyOptimizer;
import pl.gda.pg.eti.kernelhive.engine.optimizers.SimpleOptimizer;

public class HiveEngine {

	private static HiveEngine instance;
	private static String resultUploadURL = "http://localhost:8080/hive-engine/upload";
	private static String resultDownloadURL = "http://localhost:8080/hive-engine/download?filename=";
	private Map<Integer, Cluster> clusters = new HashMap<>();
	private Map<String, Cluster> clustersIps = new HashMap<>();
	private Map<Integer, Workflow> workflows = new HashMap<>();
	private Map<Integer, List<PreviewObject>> jobPreviews = new HashMap<>();
	int nextClusterId = 0;
	private IOptimizer optimizer;
	// TEMPORARY FOR TESTS:
	private static int energyLimit = 1460;
	private List<EngineGraphNodeDecorator> nodes;
	private String projectName;
	private String inputDataURL;
	private static final Logger LOG = Logger.getLogger(HiveEngine.class.getName());

	private HiveEngine() {
		//this.optimizer = new EnergyOptimizer(new GreedyKnapsackSolver());
		this.optimizer = new SimpleOptimizer();
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

	/**
	 * Copies memory stored parameters (such as devices busyness) from the old
	 * cluster to the new one
	 *
	 * @param oldCluster
	 * @param newCluster
	 */
	public Integer runWorkflow(List<EngineGraphNodeDecorator> nodes, String projectName, String inputDataURL) {
		LOG.info("Got new workflow");
		Workflow newWorkflow = new Workflow(nodes, projectName, inputDataURL);
		workflows.put(newWorkflow.getId(), newWorkflow);

		// FIXME TEMPORAry foR TESTS:
		this.nodes = nodes;
		this.projectName = projectName;
		this.inputDataURL = inputDataURL;

		processWorkflow(newWorkflow);

		return newWorkflow.getId();
	}

	private void processWorkflow(Workflow workflow) {
		LOG.log(Level.INFO, "Processing workflow {0}", workflow.getId());

		//List<Job> readyJobs = new SimpleOptimizer().processWorkflow(workflow, clusters.values());

		/*
		 List<EngineJob> readyJobs = workflow.getReadyJobs();
		
		 for(Job job : readyJobs)
		 job.run();
		 if(readyJobs.size() == 0) {
		 if(workflow.checkFinished())
		 ; // przygotowujemy wyniki do pobrania
		 else
		 ; // coś się zablokowało
		 }*/

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
			 else*/ job.run();
		}
	}

	public void cleanup() {
		// TODO: jeżeli któryś job za długo nie odpowiada to najpierw włącz mu tryb nie odpowiada, a potem wywal
		for (Workflow workflow : workflows.values()) {
			processWorkflow(workflow);
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
			job.progress = progress;
		}
	}

	public void onJobOver(int jobID, String returnData) {
		LOG.warning(String.format("JOB OVER %d, %s", jobID, returnData));

		EngineJob jobOver = getJobByID(jobID);
		if (jobOver != null) {
			jobOver.finish();

			List<DataAddress> resultAddresses = parseResults(returnData);
			Iterator<DataAddress> dataIterator = resultAddresses.iterator();

			jobOver.workflow.debugTime();

			if (jobOver.followingJobs.isEmpty()) {
				deployResults(jobOver.workflow, dataIterator);
			} else {
				jobOver.tryCollectFollowingJobsData(dataIterator);
				processWorkflow(jobOver.workflow);
			}
		} else {
			LOG.warning(String.format("Job %d not found", jobID));
		}
	}

	private void deployResults(Workflow finishingWorkflow, Iterator<DataAddress> dataIterator) {
		DataAddress resultAddress = dataIterator.next();
		byte[] result = DataDownloader.downloadData(resultAddress.hostname, resultAddress.port, resultAddress.ID);
		finishingWorkflow.finish(resultDownloadURL + deployResult(result));

		//DataAddress resultAddress = dataIterator.next();
		//byte[] result = DataDownloader.downloadData(resultAddress.hostname, resultAddress.port, resultAddress.ID);		
		//finishingWorkflow.finish(resultDownloadURL + deployResult(result));

		finishingWorkflow.finish("test");
		System.out.println(finishingWorkflow.info.result);
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

	private List<DataAddress> parseResults(String returnData) {
		List<DataAddress> ret = new ArrayList<>();

		String[] addresses = returnData.split(" ");

		int nAddresses = addresses.length / 3;

		for (int i = 0; i != nAddresses; i++) {
			DataAddress newAddress = new DataAddress();
			newAddress.hostname = addresses[3 * i];
			newAddress.port = Integer.parseInt(addresses[3 * i + 1]);
			newAddress.ID = Integer.parseInt(addresses[3 * i + 2]);
			ret.add(newAddress);
		}

		return ret;
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
