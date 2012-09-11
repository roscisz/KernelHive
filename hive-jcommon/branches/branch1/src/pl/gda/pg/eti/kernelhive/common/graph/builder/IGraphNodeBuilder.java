package pl.gda.pg.eti.kernelhive.common.graph.builder;

import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;

/**
 * Graph Node Builder
 * @author mschally
 *
 */
public interface IGraphNodeBuilder {

	/**
	 * sets graph node type
	 * @param type {@link GraphNodeType}
	 * @return {@link IGraphNodeBuilder}
	 */
	IGraphNodeBuilder setType(GraphNodeType type);
	/**
	 * sets graph node id
	 * @param id {@link String}
	 * @return {@link IGraphNodeBuilder}
	 */
	IGraphNodeBuilder setId(String id);
	/**
	 * sets graph node name
	 * @param name {@link String}
	 * @return {@link IGraphNodeBuilder}
	 */
	IGraphNodeBuilder setName(String name);
	/**
	 * sets graph node properties
	 * @param properties {@link Map}
	 * @return {@link IGraphNodeBuilder}
	 */
	IGraphNodeBuilder setProperties(Map<String, Object> properties);
	/**
	 * builds the graph node according to the previously set properties
	 * @return {@link IGraphNode}
	 * @throws GraphNodeBuilderException
	 */
	IGraphNode build() throws GraphNodeBuilderException;	
}
