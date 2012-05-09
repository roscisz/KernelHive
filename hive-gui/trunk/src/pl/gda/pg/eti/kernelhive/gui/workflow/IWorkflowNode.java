package pl.gda.pg.eti.kernelhive.gui.workflow;

import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.project.IProjectNode;

public interface IWorkflowNode {

	List<IWorkflowNode> getFollowingNodes();
	List<IWorkflowNode> getPreviousNodes();
	List<IWorkflowNode> getChildrenNodes();
	IWorkflowNode getParentNode();
	void setParentNode(IWorkflowNode node);
	String getNodeId();
	void setNodeId(String id);
	IProjectNode getProjectNode();
	void setProjectNode(IProjectNode node);
	
	boolean addFollowingNode(IWorkflowNode node);
	void removeFollowingNode(IWorkflowNode node);
	boolean addPreviousNode(IWorkflowNode node);
	void removePreviousNode(IWorkflowNode node);
	boolean addChildrenNode(IWorkflowNode node);
	void removeChildrenNode(IWorkflowNode node);
	
	int getX();
	int getY();
	void setX(int x);
	void setY(int y);
	
	void delete();
}