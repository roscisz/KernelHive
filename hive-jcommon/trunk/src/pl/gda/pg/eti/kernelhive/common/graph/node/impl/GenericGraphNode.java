package pl.gda.pg.eti.kernelhive.common.graph.node.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;

/**
 * 
 * @author mschally
 * 
 */
public class GenericGraphNode implements IGraphNode, Serializable {

	private static final long serialVersionUID = 7431190941339020419L;
	@Deprecated
	private static final int REQUIRED_SOURCE_FILES_COUNT = 0;
	protected IGraphNode parentNode;
	protected String nodeId;
	protected String name;
	protected int x, y;
	protected GraphNodeType type = GraphNodeType.GENERIC;
	protected List<IGraphNode> followingNodes;
	protected List<IGraphNode> previousNodes;
	protected List<IGraphNode> childrenNodes;
	protected List<ISourceFile> sourceFiles;
	protected Map<String, Object> properties;

	public GenericGraphNode() {
		followingNodes = new ArrayList<IGraphNode>();
		previousNodes = new ArrayList<IGraphNode>();
		childrenNodes = new ArrayList<IGraphNode>();
		sourceFiles = new ArrayList<ISourceFile>();
		properties = new HashMap<String, Object>();
	}

	public GenericGraphNode(String id) {
		followingNodes = new ArrayList<IGraphNode>();
		previousNodes = new ArrayList<IGraphNode>();
		childrenNodes = new ArrayList<IGraphNode>();
		sourceFiles = new ArrayList<ISourceFile>();
		nodeId = id;
		properties = new HashMap<String, Object>();
	}
	
	public GenericGraphNode(String id, String name){
		followingNodes = new ArrayList<IGraphNode>();
		previousNodes = new ArrayList<IGraphNode>();
		childrenNodes = new ArrayList<IGraphNode>();
		sourceFiles = new ArrayList<ISourceFile>();
		nodeId = id;
		this.name = name;
		properties = new HashMap<String, Object>();
	}

	public GenericGraphNode(String id, String name, List<IGraphNode> followingNodes,
			List<IGraphNode> childrenNodes, List<IGraphNode> previousNodes,
			List<ISourceFile> sourceFiles, Map<String, Object> properties) {
		nodeId = id;
		this.name = name;
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
		if(properties!=null){
			this.properties = properties;
		} else{
			this.properties = new HashMap<String, Object>();
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result
				+ ((parentNode == null) ? 0 : parentNode.hashCode());
		result = prime * result
				+ ((sourceFiles == null) ? 0 : sourceFiles.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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
		if (sourceFiles == null) {
			if (other.sourceFiles != null)
				return false;
		} else if (!sourceFiles.equals(other.sourceFiles))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name+" ("+type.toString()+")";
	}

	@Override
	public List<ValidationResult> validate() {
		//FIXME
		List<ValidationResult> results = new ArrayList<ValidationResult>();
		for(ISourceFile f : sourceFiles){
			if(!f.getFile().exists()){
				results.add(new ValidationResult("The file: "+f.getFile().getAbsolutePath()+" does not exist", ValidationResultType.INVALID));
			}
			if(!f.getFile().isFile()){
				results.add(new ValidationResult("The file: "+f.getFile().getAbsolutePath()+" is not a file", ValidationResultType.INVALID));
			}
		}
		if(sourceFiles.size()<REQUIRED_SOURCE_FILES_COUNT){
			results.add(new ValidationResult("The graph node ("+nodeId+") is missing one of its required source files", ValidationResultType.INVALID));
		}
		
		if(results.size()==0){//everything ok
			results.add(new ValidationResult("OK", ValidationResultType.VALID));
		}
		
		return results;
	}

	@Override
	public GraphNodeType getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
