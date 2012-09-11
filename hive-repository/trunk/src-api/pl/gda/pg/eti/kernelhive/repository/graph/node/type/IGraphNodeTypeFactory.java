package pl.gda.pg.eti.kernelhive.repository.graph.node.type;

import java.util.List;


public interface IGraphNodeTypeFactory {
	
	public static final String COMPOSITE_TYPE = "composite";
	public static final String DATA_PROCESSOR_TYPE = "DataProcessor";
	public static final String DATA_MERGER_TYPE = "DataMerger";
	public static final String DATA_PARTITIONER_TYPE = "DataPartitioner";

	IGraphNodeType createGraphNodeType(String type);
	List<IGraphNodeType> getAvailableGraphNodeTypes();
}
