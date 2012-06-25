package pl.gda.pg.eti.kernelhive.gui.graph;

import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.source.ISourceFile;



public interface IGraphNode {
	
	IGraphNode getParentNode();
	void setParentNode(IGraphNode node);
	
	String getNodeId();
	void setNodeId(String id);
	
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
