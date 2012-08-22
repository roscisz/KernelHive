package pl.gda.pg.eti.kernelhive.gui.project.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.configuration.IGUIGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.impl.GUIGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;
import pl.gda.pg.eti.kernelhive.gui.project.IProject;

/**
 * 
 * @author mschally
 *
 */
public class KernelHiveProject implements Serializable, IProject {

	private static final long serialVersionUID = -4797108604024696381L;
	private static final Logger LOG = Logger.getLogger(KernelHiveProject.class
			.getName());

	private String projectName;
	private File projectDir;
	private File projectFile;
	private List<GUIGraphNodeDecorator> nodes = new ArrayList<GUIGraphNodeDecorator>();
	private transient IGUIGraphConfiguration config;

	public KernelHiveProject(File projectDir, String projectName) {
		this.projectName = projectName;
		this.projectDir = projectDir;
		config = new GUIGraphConfiguration();
	}

	@Override
	public void addProjectNode(GUIGraphNodeDecorator node) {
		if (!nodes.contains(node)) {
			nodes.add(node);
		}
	}

	@Override
	public File getProjectDirectory() {
		return projectDir;
	}

	@Override
	public File getProjectFile() {
		return projectFile;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	@Override
	public List<GUIGraphNodeDecorator> getProjectNodes() {
		return nodes;
	}

	@Override
	public void initProject() throws ConfigurationException {
		setProjectName(projectName);
		save();
	}

	@Override
	public void load() throws ConfigurationException {
		load(projectFile);
	}

	@Override
	public void load(File file) throws ConfigurationException {
		config.setConfigurationFile(file);
		nodes = config.loadGraphForGUI();
		projectName = config.getProjectName();
	}

	@Override
	public void removeProjectNode(GUIGraphNodeDecorator node, boolean removeFromDisc) {
		if (nodes.contains(node)) {
			nodes.remove(node);
			if (removeFromDisc) {
				List<ISourceFile> srcFiles = node.getSourceFiles();
				for (ISourceFile f : srcFiles) {
					f.getFile().delete();
				}
			}
		}
	}

	@Override
	public void save() throws ConfigurationException {
		if ((projectFile == null) || (!projectFile.exists())) {
			projectFile = new File(projectDir
					+ System.getProperty("file.separator") + "project.xml");
		}
		save(projectFile);
	}

	@Override
	public void save(File file) throws ConfigurationException {
		File f;
		if (!file.exists()) {
			try {
				f = FileUtils.createNewFile(file.getPath());
				file = f;
			} catch (IOException e) {
				LOG.severe("KH: error creating new file");
				e.printStackTrace();
				throw new ConfigurationException(e);
			}
		}
		config.setConfigurationFile(file);
		config.saveGraphForGUI(this.nodes);
		config.setProjectName(projectName);
	}

	@Override
	public void setProjectDirectory(File dir) {
		//FIXME not working
		boolean result = this.projectDir.renameTo(dir);
	}

	@Override
	public void setProjectFile(File file) {
		projectFile = file;
	}

	@Override
	public void setProjectName(String name) {
		this.projectName = name;
	}

	@Override
	public void setProjectNodes(List<GUIGraphNodeDecorator> nodes) {
		this.nodes = nodes;
	}

}