package pl.gda.pg.eti.kernelhive.gui.workflow;

import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.project.IProjectNode;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

public class WorkflowGraphNode implements IWorkflowNode {

	
	public IProjectNode projectNode;
	public IWorkflowNode parentNode;
	public mxGraph graph;
	public List<IWorkflowNode> followingNodes;
	public List<IWorkflowNode> previousNodes;
	public List<IWorkflowNode> childrenNodes;
	
	
	public WorkflowGraphNode(){
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
		//TODO update graph
	}

	@Override
	public String getNodeId() {
		return null;
		//TODO
	}

	@Override
	public void setNodeId(String id) {
		//TODO
	}

	@Override
	public boolean addFollowingNode(IWorkflowNode node) {
		if(!followingNodes.contains(node)){
			return followingNodes.add(node);
			//TODO update graph
		} else{
			return false;
		}
	}

	@Override
	public void removeFollowingNode(IWorkflowNode node) {
		if(followingNodes.contains(node)){
			followingNodes.remove(node);
		}
		//TODO update graph
	}

	@Override
	public boolean addPreviousNode(IWorkflowNode node) {
		if(!previousNodes.contains(node)){
			return previousNodes.add(node);
			//TODO update graph
		} else {
			return false;
		}
	}

	@Override
	public void removePreviousNode(IWorkflowNode node) {
		if(previousNodes.contains(node)){
			previousNodes.remove(node);
			//TODO update graph
		}		
	}

	@Override
	public boolean addChildrenNode(IWorkflowNode node) {
		if(!childrenNodes.contains(node)){
			return childrenNodes.add(node);
			//TODO update graph
		} else{
			return false;
		}
	}

	@Override
	public void removeChildrenNode(IWorkflowNode node) {
		if(childrenNodes.contains(node)){
			childrenNodes.remove(node);
			//TODO update graph
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public mxCell getGraphCell() {
		return null;
		//TODO
	}
	
}
