package pl.gda.pg.eti.kernelhive.engine.job;

import java.util.ArrayList;
import java.util.Iterator;

import pl.gda.pg.eti.kernelhive.common.clusterService.Job;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.engine.HiveEngine;
import pl.gda.pg.eti.kernelhive.engine.Workflow;

public class PartitionerJob extends EngineJob {
	
	public PartitionerJob(EngineGraphNodeDecorator node, Workflow workflow) {
		super(node, workflow);
		
		//System.out.println("Created partitionerJob with workflow " + workflow);
		
		// TODO: obtain unrolling arguments from node 
		
	}

	@Override
	protected IKernelString getAssignedKernel() {
		return this.node.getPartitionKernels().get(0);
	}

	@Override
	protected void getReady() {
		super.getReady();		
		unroll();
	}

	private void unroll() {
		MergerJob merger = new MergerJob(this.node, this.workflow);
		this.workflow.registerJob(merger);
		merger.followingJobs = this.followingJobs;
		this.followingJobs = new ArrayList<EngineJob>();			
			
		// FIXME: dirty dirty code
		int processorCounter = HiveEngine.queryFreeDevicesNumber() * 30;//HiveEngine.queryFreeDevicesNumberSpecific() * 22;
		System.out.println("SET processorCounter " + processorCounter);
		for(int i = 0; i != processorCounter; i++) {			
			ProcessorJob processor = new ProcessorJob(this.node, this.workflow);
			processor.followingJobs.add(merger);
			this.followingJobs.add(processor);
			this.workflow.registerJob(processor);
		}			
		this.nOutputs = processorCounter;
		merger.numData = processorCounter;
	}

	@Override
	protected GraphNodeType getJobType() {
		return GraphNodeType.PARTITIONER;
	}	
	
	
	
}
