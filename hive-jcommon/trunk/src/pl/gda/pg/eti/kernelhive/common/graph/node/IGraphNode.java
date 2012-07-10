package pl.gda.pg.eti.kernelhive.common.graph.node;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;



public interface IGraphNode {
	
	String getNodeId();
	void setNodeId(String id);
	
	String getName();
	void setName(String name);
	
	GraphNodeType getType();

	int getX();
	int getY();
	void setX(int x);
	void setY(int y);
	
	List<IGraphNode> getFollowingNodes();
	boolean canAddFollowingNode(IGraphNode node);
	boolean addFollowingNode(IGraphNode node);
	boolean removeFollowingNode(IGraphNode node);
	
	List<IGraphNode> getPreviousNodes();
	boolean canAddPreviousNode(IGraphNode node);
	boolean addPreviousNode(IGraphNode node);
	boolean removePreviousNode(IGraphNode node);
	
	IGraphNode getParentNode();
	void setParentNode(IGraphNode node);
	
	List<IGraphNode> getChildrenNodes();
	boolean addChildNode(IGraphNode node);
	boolean removeChildNode(IGraphNode node);
	boolean canAddChildNode(IGraphNode node);
	
	List<ISourceFile> getSourceFiles();
	boolean addSourceFile(ISourceFile file);
	boolean removeSourceFile(ISourceFile file);
	boolean canRemoveSourceFile(ISourceFile file);
	
	boolean validate();
}
