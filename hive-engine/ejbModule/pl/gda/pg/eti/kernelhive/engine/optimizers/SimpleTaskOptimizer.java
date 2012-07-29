package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.engine.interfaces.ITaskOptimizer;

public class SimpleTaskOptimizer implements ITaskOptimizer {

	@Override
	public List<Job> arrangeJobs(List<Job> jobs) {
		return jobs;
	}

}
