package pl.gda.pg.eti.kernelhive.repository.graph.node;

import java.util.Map;

import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;
import pl.gda.pg.eti.kernelhive.repository.graph.node.util.NodeIdGenerator;
import pl.gda.pg.eti.kernelhive.repository.graph.node.util.NodeNameGenerator;

/**
 * Graph Node Builder Implementation
 * 
 * @author mschally
 * 
 */
public class GraphNodeBuilder implements IGraphNodeBuilder {

	GraphNodeType type;
	String id = null;
	String name = null;
	Map<String, Object> properties = null;

	public GraphNodeBuilder() {
	}

	@Override
	public IGraphNodeBuilder setType(GraphNodeType type) {
		this.type = type;
		return this;
	}

	@Override
	public IGraphNodeBuilder setId(String id) {
		this.id = id;
		return this;
	}

	@Override
	public IGraphNodeBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public IGraphNodeBuilder setProperties(Map<String, Object> properties) {
		this.properties = properties;
		return this;
	}

	@Override
	public IGraphNode build() throws GraphNodeBuilderException {
		IGraphNode node = null;
		if (id == null) {
			id = NodeIdGenerator.generateId();
		}
		try {
			if (type.toString().equals(GraphNodeType.COMPOSITE)) {
				node = new CompositeGraphNode(id);
			} else if (type.toString().equals(GraphNodeType.MERGER)) {
				node = new MergerGraphNode(id);
			} else if (type.toString().equals(GraphNodeType.PARTITIONER)) {
				node = new PartitionerGraphNode(id);
			} else if (type.toString().equals(GraphNodeType.PROCESSOR)) {
				node = new ProcessorGraphNode(id);
			} else {
				throw new GraphNodeBuilderException(
						"KH: unrecognised graph node type: " + type.toString());
			}

			if (name != null) {
				node.setName(name);
			} else {
				node.setName(NodeNameGenerator.generateName());
			}
			if (properties != null) {
				node.setProperties(properties);
			}
			return node;
		} catch (NullPointerException e) {
			throw new GraphNodeBuilderException(e);
		}
	}
}
