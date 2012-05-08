package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import pl.gda.pg.eti.kernelhive.gui.file.io.FileUtils;

public class KernelHiveProject implements Serializable, IProject {

	private static final long serialVersionUID = -4797108604024696381L;
	private static final Logger LOG = Logger.getLogger(KernelHiveProject.class.getName());
	
	private String projectName;
	private File projectDir;
	private File projectFile;
	private List<IProjectNode> nodes = new ArrayList<IProjectNode>();
	private XMLConfiguration config;
	
	public KernelHiveProject(String projectDir, String projectName){
		this.projectName = projectName;
		this.projectDir = new File(projectDir);
		config = new XMLConfiguration();
	}
	
	@Override
	public void save() throws ConfigurationException{
		if((projectFile==null)||(!projectFile.exists())){
			try {
				projectFile = FileUtils.createNewFile(projectDir+
						System.getProperty("file.separator")+
						"project.xml");
			} catch (IOException e) {
				LOG.severe("KH: error creating new file");
				e.printStackTrace();
				throw new ConfigurationException(e);
			}
		} 
		config.save(projectFile);
	}
	
	
	@Override
	public void load(){
		//TODO
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	@Override
	public File getProjectDirectory() {
		return projectDir;
	}

	@Override
	public void setProjectName(String name) {
		this.projectName = name;
		//TODO update xml via config object		
	}

	@Override
	public void setProjectDirectory(File dir) {
		boolean result = this.projectDir.renameTo(dir);
	}

	@Override
	public List<IProjectNode> getProjectNodes() {
		return nodes;
	}

	@Override
	public void setProjectNodes(List<IProjectNode> nodes) {
		this.nodes = nodes;		
	}

	@Override
	public void addProjectNode(IProjectNode node) {
		if(!nodes.contains(node)){
			nodes.add(node);
		}		
	}

	@Override
	public void removeProjectNode(IProjectNode node, boolean removeFromDisc) {
		if(nodes.contains(node)){
			nodes.remove(node);
			if(removeFromDisc){
				List<File> srcFiles = node.getSourceFiles();
				for(File f : srcFiles){
					f.delete();
				}
			}
		}		
	}

	@Override
	public void initProject() throws ConfigurationException {
		save();
		//TODO more specific?
	}
}