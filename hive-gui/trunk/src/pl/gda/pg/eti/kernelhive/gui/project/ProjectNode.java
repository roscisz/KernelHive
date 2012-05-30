package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.workflow.IWorkflowNode;



public class ProjectNode implements IProjectNode, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7901782079125239603L;

	public static enum ProjectNodeType{
		
	}
	
	private List<File> sourceFiles;
	private IWorkflowNode workflowNode;
	
	public ProjectNode(){
		sourceFiles = new ArrayList<File>();
	}

	@Override
	public List<File> getSourceFiles() {
		return sourceFiles;
	}
	
	@Override
	public void setSourceFiles(List<File> files) {
		sourceFiles = files;
	}

	@Override
	public IWorkflowNode getWorkflowNode() {
		return workflowNode;
	}

	@Override
	public void setWorkflowNode(IWorkflowNode node) {
		workflowNode = node;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sourceFiles == null) ? 0 : sourceFiles.hashCode());
		result = prime * result
				+ ((workflowNode == null) ? 0 : workflowNode.hashCode());
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
		ProjectNode other = (ProjectNode) obj;
		if (sourceFiles == null) {
			if (other.sourceFiles != null)
				return false;
		} else if (!sourceFiles.equals(other.sourceFiles))
			return false;
		if (workflowNode == null) {
			if (other.workflowNode != null)
				return false;
		} else if (!workflowNode.equals(other.workflowNode))
			return false;
		return true;
	}
	
	
	
}