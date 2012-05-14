package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.File;
import java.io.Serializable;
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
	private IProject project;
	
	public ProjectNode(IProject project){
		this.project = project;
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
	
	protected IProject getProject(){
		return project;
	}

	@Override
	public void delete(){
		//TODO
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((project == null) ? 0 : project.hashCode());
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
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
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