package pl.gda.pg.eti.kernelhive.gui.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.project.IProjectNode;

public class WorkflowGraphNode implements IWorkflowNode, Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2821362814931651685L;
	private IProjectNode projectNode;
	private IWorkflowNode parentNode;
	private String nodeId;
	private int x, y;
	private List<IWorkflowNode> followingNodes;
	private List<IWorkflowNode> previousNodes;
	private List<IWorkflowNode> childrenNodes;
	
	
	public WorkflowGraphNode(IProjectNode projectNode, String id){
		this.projectNode = projectNode;
		followingNodes = new ArrayList<IWorkflowNode>();
		previousNodes = new ArrayList<IWorkflowNode>();
		childrenNodes = new ArrayList<IWorkflowNode>();
		nodeId = id;
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
	public void setParentNode(IWorkflowNode node) {
		parentNode = node;
	}

	@Override
	public String getNodeId() {
		return nodeId;
	}

	@Override
	public void setNodeId(String id) {
		nodeId = id;
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
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IProjectNode getProjectNode() {
		return projectNode;
	}

	@Override
	public void setProjectNode(IProjectNode node) {
		this.projectNode = node;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

}
