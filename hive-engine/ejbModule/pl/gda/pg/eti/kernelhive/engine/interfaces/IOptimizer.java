package pl.gda.pg.eti.kernelhive.engine.interfaces;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.clusterService.Workflow;

public interface IOptimizer {
	
	public List<Job> processWorkflow(Workflow workflow);
	
}
