package pl.gda.pg.eti.kernelhive.engine.job;

import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.engine.Workflow;

public class MergerJob extends EngineJob {

	public MergerJob(EngineGraphNodeDecorator node, Workflow workflow) {
		super(node, workflow);
	}

	@Override
	protected IKernelString getAssignedKernel() {
		return this.node.getMergeKernels().get(0);
	}

	@Override
	public GraphNodeType getJobType() {
		return GraphNodeType.MERGER;
	}
}
