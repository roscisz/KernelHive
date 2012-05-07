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
	
	public ProjectNode(){
		
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
	
	
	
	
	
}