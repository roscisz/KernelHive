package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;

/**
 * 
 * @author marcel
 * 
 */
public class GenericGraphNode implements IGraphNode, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7431190941339020419L;
	protected IGraphNode parentNode;
	protected String nodeId;
	protected int x, y;
	protected GraphNodeType type = GraphNodeType.GENERIC;
	protected List<IGraphNode> followingNodes;
	protected List<IGraphNode> previousNodes;
	protected List<IGraphNode> childrenNodes;
	protected List<ISourceFile> sourceFiles;

	public GenericGraphNode() {
		followingNodes = new ArrayList<IGraphNode>();
		previousNodes = new ArrayList<IGraphNode>();
		childrenNodes = new ArrayList<IGraphNode>();
		sourceFiles = new ArrayList<ISourceFile>();
	}

	public GenericGraphNode(String id) {
		followingNodes = new ArrayList<IGraphNode>();
		previousNodes = new ArrayList<IGraphNode>();
		childrenNodes = new ArrayList<IGraphNode>();
		sourceFiles = new ArrayList<ISourceFile>();
		nodeId = id;
	}

	public GenericGraphNode(String id, List<IGraphNode> followingNodes,
			List<IGraphNode> childrenNodes, List<IGraphNode> previousNodes,
			List<ISourceFile> sourceFiles) {
		nodeId = id;
		if (followingNodes != null) {
			this.followingNodes = followingNodes;
		} else {
			this.followingNodes = new ArrayList<IGraphNode>();
		}
		if (childrenNodes != null) {
			this.childrenNodes = childrenNodes;
		} else {
			this.childrenNodes = new ArrayList<IGraphNode>();
		}
		if (previousNodes != null) {
			this.previousNodes = previousNodes;
		} else {
			this.previousNodes = new ArrayList<IGraphNode>();
		}
		if (sourceFiles != null) {
			this.sourceFiles = sourceFiles;
		} else {
			this.sourceFiles = new ArrayList<ISourceFile>();
		}
	}

	@Override
	public List<IGraphNode> getFollowingNodes() {
		return followingNodes;
	}

	@Override
	public List<IGraphNode> getPreviousNodes() {
		return previousNodes;
	}

	@Override
	public List<IGraphNode> getChildrenNodes() {
		return childrenNodes;
	}

	@Override
	public IGraphNode getParentNode() {
		return parentNode;
	}

	@Override
	public void setParentNode(IGraphNode node) {
		if (parentNode != null) {
			parentNode.getChildrenNodes().remove(this);
		}
		parentNode = node;
		if (parentNode != null) {
			parentNode.getChildrenNodes().add(this);
		}
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
	public boolean addFollowingNode(IGraphNode node) {
		if (!followingNodes.contains(node) && canAddFollowingNode(node)) {
			followingNodes.add(node);
			boolean result = node.addPreviousNode(this);
			if (result == false) {
				followingNodes.remove(node);
				node.removePreviousNode(node);
			}
			return result;
		} else {
			return true;
		}
	}

	@Override
	public boolean removeFollowingNode(IGraphNode node) {
		if (followingNodes.contains(node)) {
			boolean result = followingNodes.remove(node);
			result &= node.removePreviousNode(this);
			return result;
		} else {
			return true;
		}
	}

	@Override
	public boolean addPreviousNode(IGraphNode node) {
		if (!previousNodes.contains(node) && canAddPreviousNode(node)) {
			previousNodes.add(node);
			node.addFollowingNode(this);
			return true;
		} else {
			return true;
		}
	}

	@Override
	public boolean removePreviousNode(IGraphNode node) {
		if (previousNodes.contains(node)) {
			boolean result = previousNodes.remove(node);
			result &= node.removeFollowingNode(this);
			return result;
		} else {
			return true;
		}
	}

	@Override
	public boolean addChildNode(IGraphNode node) {
		if (!childrenNodes.contains(node) && canAddChildNode(node)) {
			childrenNodes.add(node);
			node.setParentNode(this);
			return true;
		} else {
			return true;
		}
	}

	@Override
	public boolean removeChildNode(IGraphNode node) {
		if (childrenNodes.contains(node)) {
			boolean result = childrenNodes.remove(node);
			node.setParentNode(null);
			return result;
		} else {
			return true;
		}
	}

	@Override
	public boolean addSourceFile(ISourceFile file) {
		return sourceFiles.add(file);
	}

	@Override
	public boolean removeSourceFile(ISourceFile file) {
		if (canRemoveSourceFile(file)) {
			return sourceFiles.remove(file);
		} else {
			return false;
		}
	}

	@Override
	public List<ISourceFile> getSourceFiles() {
		return sourceFiles;
	}

	@Override
	public boolean canAddFollowingNode(IGraphNode node) {
		return true;
	}

	@Override
	public boolean canAddPreviousNode(IGraphNode node) {
		return true;
	}

	@Override
	public boolean canAddChildNode(IGraphNode node) {
		return true;
	}

	@Override
	public boolean canRemoveSourceFile(ISourceFile file) {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((parentNode == null) ? 0 : parentNode.hashCode());
		result = prime * result
				+ ((sourceFiles == null) ? 0 : sourceFiles.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + nodeId.hashCode();
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
		GenericGraphNode other = (GenericGraphNode) obj;
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
		if (sourceFiles == null) {
			if (other.sourceFiles != null)
				return false;
		} else if (!sourceFiles.equals(other.sourceFiles))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GenericProjectNode [nodeId=" + nodeId + "]";
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public GraphNodeType getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
