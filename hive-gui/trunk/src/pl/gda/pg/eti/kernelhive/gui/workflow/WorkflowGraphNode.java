package pl.gda.pg.eti.kernelhive.gui.workflow;

import java.io.Serializable;
import java.util.List;

import com.mxgraph.model.mxCell;

public class WorkflowGraphNode implements IWorkflowNode, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3965841723695416386L;

	private long nodeId;
	private IWorkflowNode parentNode;
	private mxCell graphNode;
	private List<IWorkflowNode> childrenNodes;
	private List<IWorkflowNode> previousNodes;
	private List<IWorkflowNode> followingNodes;
	
	public WorkflowGraphNode(){

	}
	
	@Override
	public mxCell getGraphNode() {
		return graphNode;
	}

	@Override
	public List<IWorkflowNode> getFollowingNodes() {
		return followingNodes;
	}

	@Override
	public List<IWorkflowNode> getPreviousNodes() {
		return previousNodes;
	}

	@Override
	public List<IWorkflowNode> getChildrenNodes() {
		return childrenNodes;
	}

	@Override
	public IWorkflowNode getParentNode() {
		return parentNode;
	}

	@Override
	public boolean addFollowingNode(IWorkflowNode node) {
		if(!followingNodes.contains(node)){
			return followingNodes.add(node);
		} else{
			return false;
		}
	}

	@Override
	public void removeFollowingNode(IWorkflowNode node) {
		if(followingNodes.contains(node)){
			followingNodes.remove(node);
		}
	}

	@Override
	public boolean addPreviousNode(IWorkflowNode node) {
		if(!previousNodes.contains(node)){
			return previousNodes.add(node);
		} else {
			return false;
		}
	}

	@Override
	public void removePreviousNode(IWorkflowNode node) {
		if(previousNodes.contains(node)){
			previousNodes.remove(node);
		}		
	}

	@Override
	public boolean addChildrenNode(IWorkflowNode node) {
		if(!childrenNodes.contains(node)){
			return childrenNodes.add(node);
		} else{
			return false;
		}
	}

	@Override
	public void removeChildrenNode(IWorkflowNode node) {
		if(childrenNodes.contains(node)){
			childrenNodes.remove(node);
		}
	}

	@Override
	public boolean delete() {
		// TODO
		return false;
	}

	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public void setGraphNode(mxCell graphNode) {
		this.graphNode = graphNode;
	}

	@Override
	public void setParentNode(IWorkflowNode node) {
		parentNode = node;
	}
}