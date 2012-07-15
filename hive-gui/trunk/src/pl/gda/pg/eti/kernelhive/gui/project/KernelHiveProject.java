package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.configuration.IGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.impl.GraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;

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
	private List<IGraphNode> nodes = new ArrayList<IGraphNode>();
	private transient IGraphConfiguration config;

	public KernelHiveProject(File projectDir, String projectName) {
		this.projectName = projectName;
		this.projectDir = projectDir;
		config = new GraphConfiguration();
	}

	@Override
	public void addProjectNode(IGraphNode node) {
		//TODO change - incoming node's source files are to be moved to new files
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
	public List<IGraphNode> getProjectNodes() {
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
		nodes = config.loadGraph();
	}

	@Override
	public void removeProjectNode(IGraphNode node, boolean removeFromDisc) {
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
		config.saveGraph(this.nodes);
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
	public void setProjectNodes(List<IGraphNode> nodes) {
		this.nodes = nodes;
	}

}