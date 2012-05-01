package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.File;
import java.util.List;

import pl.gda.pg.eti.kernelhive.gui.workflow.IWorkflowNode;

public interface IProjectNode {

	/**
	 * get source files for this node
	 * @return list of source files connected to this node
	 */
	List<File> getSourceFiles();
	/**
	 * set source files for this node
	 * @param files list of source files
	 */
	void setSourceFiles(List<File> files);
	/**
	 * gets workflow node (graphical representation of project node)
	 * @return workflow node
	 */
	IWorkflowNode getWorkflowNode();
	/**
	 * sets workflow node
	 * @param node workflow node
	 */
	void setWorkflowNode(IWorkflowNode node);
}
