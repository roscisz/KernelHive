package pl.gda.pg.eti.kernelhive.common.graph.builder.impl;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.builder.GraphNodeBuilderException;
import pl.gda.pg.eti.kernelhive.common.graph.builder.IGraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.impl.GenericGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.NodeIdGenerator;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.NodeNameGenerator;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;
/**
 * Graph Node Builder Implementation
 * @author mschally
 *
 */
public class GraphNodeBuilder implements IGraphNodeBuilder {
	
	GraphNodeType type;
	String id = null;
	String name = null;
	List<ISourceFile> sourceFiles = null;
		
	public GraphNodeBuilder(){	}

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
	public IGraphNodeBuilder setSourceFiles(List<ISourceFile> sourceFiles) {
		this.sourceFiles = sourceFiles;
		return this;
	}

	@Override
	public IGraphNode build() throws GraphNodeBuilderException {
		IGraphNode node = null;
		switch(type){
			case GENERIC:
				node = new GenericGraphNode();
				break;
			case COMPOSITE:
//				break;
			case DAC:
//				break;
			case MASTERSLAVE:
//				break;
			case MERGER:
//				break;
			case PARTITIONER:
//				break;
			case PROCESSOR:
//				break;
			default:
				throw new GraphNodeBuilderException("KH: unrecognised graph node type: "+type.toString());
		}
		if(id!=null){
			node.setNodeId(id);
		} else{
			node.setNodeId(NodeIdGenerator.generateId());
		}
		if(name!=null){
			node.setName(name);
		} else{
			node.setName(NodeNameGenerator.generateName());
		}
		if(sourceFiles!=null){
			for(ISourceFile s : sourceFiles){
				node.addSourceFile(s);
			}
		}		
		return node;
	}
}
