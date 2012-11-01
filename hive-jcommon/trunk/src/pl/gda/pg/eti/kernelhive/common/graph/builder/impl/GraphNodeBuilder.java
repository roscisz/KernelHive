package pl.gda.pg.eti.kernelhive.common.graph.builder.impl;

import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.graph.builder.GraphNodeBuilderException;
import pl.gda.pg.eti.kernelhive.common.graph.builder.IGraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.impl.CompositeGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.impl.ExpandableGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.impl.GenericGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.impl.MergerGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.impl.PartitionerGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.impl.ProcessorGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.NodeIdGenerator;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.NodeNameGenerator;

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
	public IGraphNodeBuilder setType(final GraphNodeType type) {
		this.type = type;
		return this;
	}

	@Override
	public IGraphNodeBuilder setId(final String id) {
		this.id = id;
		return this;
	}

	@Override
	public IGraphNodeBuilder setName(final String name) {
		this.name = name;
		return this;
	}

	@Override
	public IGraphNodeBuilder setProperties(final Map<String, Object> properties) {
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
			switch (type) {
			case GENERIC:
				node = new GenericGraphNode(id);
				break;
			case COMPOSITE:
				node = new CompositeGraphNode(id);
				break;
			case DAC:
				// break;
			case MASTERSLAVE:
				// break;
			case MERGER:
				node = new MergerGraphNode(id);
				break;
			case PARTITIONER:
				node = new PartitionerGraphNode(id);
				break;
			case PROCESSOR:
				node = new ProcessorGraphNode(id);
				break;
			case EXPANDABLE:
				node = new ExpandableGraphNode(id);
				break;
			default:
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
		} catch (final NullPointerException e) {
			throw new GraphNodeBuilderException(e);
		}
	}

}
