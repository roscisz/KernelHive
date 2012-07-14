package pl.gda.pg.eti.kernelhive.common.graph.factory.impl;

import pl.gda.pg.eti.kernelhive.common.graph.factory.IGraphNodeFactory;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.impl.GenericGraphNode;

public class GraphNodeFactory implements IGraphNodeFactory {

	@Override
	public IGraphNode createGraphNode(GraphNodeType type) {
		switch(type){
			case GENERIC:
				IGraphNode node = new GenericGraphNode();
				return node;
			case PARTITIONER:
				break;
			case MERGER:
				break;
			case PROCESSOR:
				break;
			case MASTERSLAVE:
				break;
			case DAC:
				break;
			case COMPOSITE:
				break;
			default:
				break;
		}
		return null;
	}

}
