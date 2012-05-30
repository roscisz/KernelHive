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
	
	
	public WorkflowGraphNode(){
		followingNodes = new ArrayList<IWorkflowNode>();
		previousNodes = new ArrayList<IWorkflowNode>();
		childrenNodes = new ArrayList<IWorkflowNode>(); 
	}
	
	public WorkflowGraphNode(String id){
		followingNodes = new ArrayList<IWorkflowNode>();
		previousNodes = new ArrayList<IWorkflowNode>();
		childrenNodes = new ArrayList<IWorkflowNode>();
		nodeId = id;
	}
	
	public WorkflowGraphNode(IProjectNode projectNode, String id){
		this.projectNode = projectNode;
		this.projectNode.setWorkflowNode(this);
		followingNodes = new ArrayList<IWorkflowNode>();
		previousNodes = new ArrayList<IWorkflowNode>();
		childrenNodes = new ArrayList<IWorkflowNode>();
		nodeId = id;
	}
	
	public WorkflowGraphNode(IProjectNode projectNode, String id, List<IWorkflowNode> followingNodes, List<IWorkflowNode> childrenNodes, List<IWorkflowNode> previosNodes){
		this.projectNode = projectNode;
		this.projectNode.setWorkflowNode(this);
		nodeId = id;
		if(followingNodes!=null){
			this.followingNodes=followingNodes;
		} else {
			this.followingNodes= new ArrayList<IWorkflowNode>();
		}
		if(childrenNodes!=null){
			this.childrenNodes = childrenNodes;
		} else{
			this.childrenNodes = new ArrayList<IWorkflowNode>();
		}
		if(previosNodes!=null){
			this.previousNodes = previosNodes;
		} else{
			this.previousNodes = new ArrayList<IWorkflowNode>();
		}
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
		node.addChildrenNode(this);
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
			followingNodes.add(node);
			node.addPreviousNode(this);
			return true;
		} else{
			return true;
		}
	}

	@Override
	public void removeFollowingNode(IWorkflowNode node) {
		if(followingNodes.contains(node)){
			followingNodes.remove(node);
			node.removePreviousNode(this);
		}
	}

	@Override
	public boolean addPreviousNode(IWorkflowNode node) {
		if(!previousNodes.contains(node)){
			previousNodes.add(node);
			node.addFollowingNode(this);
			return true;
		} else {
			return true;
		}
	}

	@Override
	public void removePreviousNode(IWorkflowNode node) {
		if(previousNodes.contains(node)){
			previousNodes.remove(node);
			node.removeFollowingNode(this);			
		}		
	}

	@Override
	public boolean addChildrenNode(IWorkflowNode node) {
		if(!childrenNodes.contains(node)){
			childrenNodes.add(node);
			node.setParentNode(this);
			return true;
		} else{
			return true;
		}
	}

	@Override
	public void removeChildrenNode(IWorkflowNode node) {
		if(childrenNodes.contains(node)){
			childrenNodes.remove(node);
			node.setParentNode(null);
		}
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ nodeId.hashCode();
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkflowGraphNode other = (WorkflowGraphNode) obj;
		if (childrenNodes == null) {
			if (other.childrenNodes != null)
				return false;
		} else if (!childrenNodes.equals(other.childrenNodes))
			return false;
		if (followingNodes == null) {
			if (other.followingNodes != null)
				return false;
		} else if (!followingNodes.equals(other.followingNodes))
			return false;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		if (parentNode == null) {
			if (other.parentNode != null)
				return false;
		} else if (!parentNode.equals(other.parentNode))
			return false;
		if (previousNodes == null) {
			if (other.previousNodes != null)
				return false;
		} else if (!previousNodes.equals(other.previousNodes))
			return false;
		if (projectNode == null) {
			if (other.projectNode != null)
				return false;
		} else if (!projectNode.equals(other.projectNode))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nodeId;
	}
}
