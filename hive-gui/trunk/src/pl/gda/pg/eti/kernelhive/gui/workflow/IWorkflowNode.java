package pl.gda.pg.eti.kernelhive.gui.workflow;

import java.util.List;

import com.mxgraph.model.mxCell;

public interface IWorkflowNode {

	mxCell getGraphNode();
	void setGraphNode(mxCell graphNode);
	List<IWorkflowNode> getFollowingNodes();
	List<IWorkflowNode> getPreviousNodes();
	List<IWorkflowNode> getChildrenNodes();
	IWorkflowNode getParentNode();
	void setParentNode(IWorkflowNode node);
	long getNodeId();
	void setNodeId(long id);
	
	boolean addFollowingNode(IWorkflowNode node);
	void removeFollowingNode(IWorkflowNode node);
	boolean addPreviousNode(IWorkflowNode node);
	void removePreviousNode(IWorkflowNode node);
	boolean addChildrenNode(IWorkflowNode node);
	void removeChildrenNode(IWorkflowNode node);
	
	boolean delete();
}
