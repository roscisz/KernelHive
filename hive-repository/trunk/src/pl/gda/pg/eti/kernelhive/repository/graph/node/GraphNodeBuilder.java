package pl.gda.pg.eti.kernelhive.repository.graph.node;

import java.util.Map;

import pl.gda.pg.eti.kernelhive.repository.graph.node.CompositeGraphNode;
import pl.gda.pg.eti.kernelhive.repository.graph.node.GraphNodeBuilderException;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.repository.graph.node.MergerGraphNode;
import pl.gda.pg.eti.kernelhive.repository.graph.node.PartitionerGraphNode;
import pl.gda.pg.eti.kernelhive.repository.graph.node.ProcessorGraphNode;
import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeTypeFactory;
import pl.gda.pg.eti.kernelhive.repository.graph.node.type.IGraphNodeType;
import pl.gda.pg.eti.kernelhive.repository.graph.node.util.NodeIdGenerator;
import pl.gda.pg.eti.kernelhive.repository.graph.node.util.NodeNameGenerator;
/**
 * Graph Node Builder Implementation
 * @author mschally
 *
 */
public class GraphNodeBuilder implements IGraphNodeBuilder {
	
	IGraphNodeType type;
	String id = null;
	String name = null;
	Map<String, Object> properties = null;
		
	public GraphNodeBuilder(){	}

	@Override
	public IGraphNodeBuilder setType(IGraphNodeType type) {
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
		if(id==null){
			id = NodeIdGenerator.generateId();
		}
		try{
			if(type.toString().equals(GraphNodeTypeFactory.COMPOSITE_TYPE)){
				node = new CompositeGraphNode(id);
			} else if(type.toString().equals(GraphNodeTypeFactory.DATA_MERGER_TYPE)){
				node = new MergerGraphNode(id);
			} else if(type.toString().equals(GraphNodeTypeFactory.DATA_PARTITIONER_TYPE)){
				node = new PartitionerGraphNode(id);
			} else if(type.toString().equals(GraphNodeTypeFactory.DATA_PROCESSOR_TYPE)){
				node = new ProcessorGraphNode(id);
			} else{
				throw new GraphNodeBuilderException("KH: unrecognised graph node type: "+type.toString());
			}
			
			
			if(name!=null){
				node.setName(name);
			} else{
				node.setName(NodeNameGenerator.generateName());
			}
			if(properties!=null){
				node.setProperties(properties);
			}
			return node;
		} catch(NullPointerException e){
			throw new GraphNodeBuilderException(e);
		}
	}	
}
