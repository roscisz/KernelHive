package pl.gda.pg.eti.kernelhive.engine.interfaces;

import java.util.Collection;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Cluster;
import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Workflow;

public interface IOptimizer {
	/**	
	 * Given a submitted workflow and available infrastructure,
	 * return a list of scheduled jobs with assigned devices to
	 * be deployed by the Engine.
	 * @param workflow
	 * @param infrastructure
	 * @return
	 */
	 public List<Job> processWorkflow(Workflow workflow, Collection<Cluster> infrastructure);

	
}
