package pl.gda.pg.eti.kernelhive.gui.project.node;

import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.source.ISourceFile;



public interface IProjectNode {

	List<IProjectNode> getFollowingNodes();
	List<IProjectNode> getPreviousNodes();
	List<IProjectNode> getChildrenNodes();
	
	IProjectNode getParentNode();
	void setParentNode(IProjectNode node);
	String getNodeId();
	void setNodeId(String id);
	int getX();
	int getY();
	void setX(int x);
	void setY(int y);
	
	boolean addFollowingNode(IProjectNode node);
	boolean removeFollowingNode(IProjectNode node);
	boolean addPreviousNode(IProjectNode node);
	boolean removePreviousNode(IProjectNode node);
	boolean addChildNode(IProjectNode node);
	boolean removeChildNode(IProjectNode node);
	
	List<ISourceFile> getSourceFiles();
	boolean addSourceFile(ISourceFile file);
	boolean removeSourceFile(ISourceFile file);
	
	boolean canAddFollowingNode(IProjectNode node);
	boolean canAddPreviousNode(IProjectNode node);
	boolean canAddChildNode(IProjectNode node);
	boolean canRemoveSourceFile(ISourceFile file);
}
