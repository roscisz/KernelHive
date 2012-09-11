package pl.gda.pg.eti.kernelhive.repository.graph.node.type;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.repository.graph.node.type.IGraphNodeType;
import pl.gda.pg.eti.kernelhive.repository.graph.node.type.IGraphNodeTypeFactory;

public class GraphNodeTypeFactory implements IGraphNodeTypeFactory {

	@Override
	public IGraphNodeType createGraphNodeType(String type) {
		if(type.equals(COMPOSITE_TYPE)){
			return new GraphNodeType(type);
		} else if(type.equals(DATA_PROCESSOR_TYPE)){
			return new GraphNodeType(type);
		} else if(type.equals(DATA_MERGER_TYPE)){
			return new GraphNodeType(type);
		} else if(type.equals(DATA_PARTITIONER_TYPE)){
			return new GraphNodeType(type);
		}
		return null;
	}

	@Override
	public List<IGraphNodeType> getAvailableGraphNodeTypes() {
		List<IGraphNodeType> list = new ArrayList<IGraphNodeType>();
		list.add(new GraphNodeType(COMPOSITE_TYPE));
		list.add(new GraphNodeType(DATA_MERGER_TYPE));
		list.add(new GraphNodeType(DATA_PARTITIONER_TYPE));
		list.add(new GraphNodeType(DATA_PROCESSOR_TYPE));
		return list;
	}

}
