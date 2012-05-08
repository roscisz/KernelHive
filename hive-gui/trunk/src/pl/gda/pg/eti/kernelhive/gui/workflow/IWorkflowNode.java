package pl.gda.pg.eti.kernelhive.gui.workflow;

import java.util.List;

import com.mxgraph.model.mxCell;

public interface IWorkflowNode {

	List<IWorkflowNode> getFollowingNodes();
	List<IWorkflowNode> getPreviousNodes();
	List<IWorkflowNode> getChildrenNodes();
	IWorkflowNode getParentNode();
	void setParentNode(IWorkflowNode node);
	mxCell getGraphCell();
	String getNodeId();
	void setNodeId(String id);
	
	boolean addFollowingNode(IWorkflowNode node);
	void removeFollowingNode(IWorkflowNode node);
	boolean addPreviousNode(IWorkflowNode node);
	void removePreviousNode(IWorkflowNode node);
	boolean addChildrenNode(IWorkflowNode node);
	void removeChildrenNode(IWorkflowNode node);
	
	void delete();
}