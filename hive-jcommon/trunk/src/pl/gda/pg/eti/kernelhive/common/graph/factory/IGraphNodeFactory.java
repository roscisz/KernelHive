package pl.gda.pg.eti.kernelhive.common.graph.factory;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;

public interface IGraphNodeFactory {
	
	IGraphNode createGraphNode(GraphNodeType type);

}
