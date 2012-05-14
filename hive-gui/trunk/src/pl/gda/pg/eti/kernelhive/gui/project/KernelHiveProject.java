package pl.gda.pg.eti.kernelhive.gui.project;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.commons.configuration.tree.ConfigurationNode;

import pl.gda.pg.eti.kernelhive.gui.file.io.FileUtils;
import pl.gda.pg.eti.kernelhive.gui.workflow.IWorkflowNode;
import pl.gda.pg.eti.kernelhive.gui.workflow.WorkflowGraphNode;

public class KernelHiveProject implements Serializable, IProject, ConfigurationListener {

	private static final long serialVersionUID = -4797108604024696381L;
	private static final Logger LOG = Logger.getLogger(KernelHiveProject.class.getName());
	
	private static final String ROOT_NODE = "kh:project";
	private static final String ROOT_NODE_NAME_ATTRIBUTE = "name";
	private static final String NODE = "kh:node";
	private static final String NODE_ID_ATTRIBUTE = "id";
	private static final String NODE_HASH_ATTRIBUTE = "hash";
	private static final String NODE_PARENT_ID_ATTRIBUTE = "parent-id";
	private static final String NODE_X_ATTRIBUTE = "x";
	private static final String NODE_Y_ATTRIBUTE = "y";
	private static final String SEND_TO_NODE = "kh:send-to";
	private static final String FOLLOWING_NODE = "kh:following-node";
	private static final String FOLLOWING_NODE_ID_ATTRIBUTE = "id";
	private static final String NODE_SOURCE_FILES = "kh:node-source-files";
	private static final String SOURCE_FILE = "kh:source-file";
	private static final String SOURCE_FILE_SRC_ATTRIBUTE = "src";
	private static final String FIRST_CHILDREN_NODE = "kh:first-children-nodes";
	private static final String CHILD_NODE = "kh:cild-node";
	private static final String CHILD_NODE_ID_ATTRIBUTE = "id";
 	
	private String projectName;
	private File projectDir;
	private File projectFile;
	private List<IProjectNode> nodes = new ArrayList<IProjectNode>();
	private transient XMLConfiguration config;
	
	public KernelHiveProject(String projectDir, String projectName){
		this.projectName = projectName;
		this.projectDir = new File(projectDir);
		config = new XMLConfiguration();
		config.addConfigurationListener(this);
	}
	
	@Override
	public void save() throws ConfigurationException{
		if((projectFile==null)||(!projectFile.exists())){
			projectFile = new File(projectDir+
						System.getProperty("file.separator")+
						"project.xml");
		} 
		save(projectFile);
	}
	
	@Override
	public void save(File file) throws ConfigurationException {
		File f;
		try {
			f = FileUtils.createNewFile(file.getPath());
		} catch (IOException e) {
			LOG.severe("KH: error creating new file");
			e.printStackTrace();
			throw new ConfigurationException(e);
		}
		config.save(f);		
	}
	
	@Override
	public void load(){
		load(projectFile);
	}

	@Override
	public void load(File file) {
		try {
			config.setFile(file);
			config.load();
			//config.validate();//TODO write schema
			//initial creation of project nodes
			for(ConfigurationNode node : config.getRoot().getChildren("kh:node")){
				List<ConfigurationNode> idAttrList = node.getAttributes("id");
				List<ConfigurationNode> xAttrList = node.getAttributes("x");
				List<ConfigurationNode> yAttrList = node.getAttributes("y");
				String id = null;
				int x=-1, y=-1;
				if(idAttrList.size()>0){
					id = (String) idAttrList.get(0).getValue();
				}
				if(xAttrList.size()>0){
					x = Integer.parseInt((String)xAttrList.get(0).getValue());
				}
				if(yAttrList.size()>0){
					y = Integer.parseInt((String)yAttrList.get(0).getValue());
				}
				IProjectNode projectNode = new ProjectNode(this);
				projectNode.setWorkflowNode(new WorkflowGraphNode(projectNode, id));
				projectNode.getWorkflowNode().setX(x);
				projectNode.getWorkflowNode().setY(y);
				List<ConfigurationNode> sourceFilesList = node.getChildren("kh:node-source-files");
				if(sourceFilesList.size()>0){
					sourceFilesList = node.getChildren("kh:source-file");
					List<File> filesList = new ArrayList<File>();
					for(ConfigurationNode src : sourceFilesList){
						filesList.add(new File((String) src.getValue()));
					}
					projectNode.setSourceFiles(filesList);
				}
				nodes.add(projectNode);
			}
			//linking project nodes with one another
			for(ConfigurationNode node : config.getRoot().getChildren("kh:node")){
				List<ConfigurationNode> idAttrList = node.getAttributes("id");
				List<ConfigurationNode> parentIdAttrList = node.getAttributes("parent-id");
				IProjectNode projectNode = null;
				String id = null, parentId = null;
				if(idAttrList.size()>0){
					id = (String) idAttrList.get(0).getValue();
				}
				if(parentIdAttrList.size()>0){
					parentId = (String) parentIdAttrList.get(0).getValue();
					if(parentId.equalsIgnoreCase("")){
						parentId = null;
					}
				}
				
				if(id!=null){
					for(IProjectNode pn : nodes){
						if(pn.getWorkflowNode().getNodeId().equalsIgnoreCase(id)){
							projectNode = pn;
							break;
						}
					}
				}
				if(parentId!=null){
					IProjectNode parentNode = null;
					for(IProjectNode pn : nodes){
						if(pn.getWorkflowNode().getNodeId().equalsIgnoreCase(parentId)){
							parentNode = pn;
							break;
						}
					}
					if(projectNode!=null&&parentNode!=null){
						projectNode.getWorkflowNode().setParentNode(parentNode.getWorkflowNode());
					} else{
						throw new ConfigurationException("KH: nodes with following ids could not be initialized: "+id+" "+parentId);
					}
				}
				List<ConfigurationNode> followingNodes = node.getChildren("kh:send-to");
				if(followingNodes.size()>0){
					followingNodes = followingNodes.get(0).getChildren("kh:following-node");
					if(followingNodes.size()>0){
						for(ConfigurationNode followingNode : followingNodes){
							List<ConfigurationNode> idList = followingNode.getAttributes("id");
							if(idList.size()>0){
								String followingId = (String) idList.get(0).getValue();
								IProjectNode followingProjectNode = null;
								for(IProjectNode pn : nodes){
									if(pn.getWorkflowNode().getNodeId().equalsIgnoreCase(followingId)){
										followingProjectNode = pn;
										break;
									}
								}
								if(followingProjectNode!=null){
									projectNode.getWorkflowNode().addFollowingNode(followingProjectNode.getWorkflowNode());
								}
							}
						}
					}
				}
				
			}
			//set project name
			List<ConfigurationNode> attrList = config.getRoot().getAttributes("name");
			if(attrList.size()>0){
				projectName = (String) attrList.get(0).getValue();
			} else{
				throw new ConfigurationException("no attribute 'name' in root node of the configuration");
			}
		} catch (ConfigurationException e) {
			LOG.severe("KH: could not load project from file: "+file.getPath()+" "+e.getMessage());
			e.printStackTrace();
		}
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
		List<ConfigurationNode> attributes = config.getRoot().getAttributes("name");
		if(attributes.size()>0){
			attributes.get(0).setValue(name);
		} else{
			Node attr = new Node("name");
			attr.setAttribute(true);
			attr.setValue(name);
			config.getRoot().addAttribute(attr);
		}
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
		config.clear();
		for(IProjectNode node : nodes){
			addProjectNodeToConfig(node);
		}
	}

	@Override
	public void addProjectNode(IProjectNode node) {
		if(!nodes.contains(node)){
			nodes.add(node);
			addProjectNodeToConfig(node);
		}		
	}
	
	private void addProjectNodeToConfig(IProjectNode node){
		//create project node
		Node configNode = new Node("kh:node");
		Node idAttr = new Node("id", node.getWorkflowNode().getNodeId());
		idAttr.setAttribute(true);
		Node hashAttr = new Node("hash", node.hashCode());
		hashAttr.setAttribute(true);
		Node parentAttr = new Node("parent-id", node.getWorkflowNode().getParentNode()!=null?node.getWorkflowNode().getParentNode().getNodeId():"");
		parentAttr.setAttribute(true);
		Node xAttr = new Node("x", node.getWorkflowNode().getX());
		xAttr.setAttribute(true);
		Node yAttr = new Node("y", node.getWorkflowNode().getY());
		yAttr.setAttribute(true);
		configNode.addAttribute(idAttr);
		configNode.addAttribute(parentAttr);
		configNode.addAttribute(hashAttr);
		configNode.addAttribute(xAttr);
		configNode.addAttribute(yAttr);
		//create "send-to" subnode
		Node sendToNode = new Node("kh:send-to");
		for(IWorkflowNode wfNode : node.getWorkflowNode().getFollowingNodes()){
			Node followingNode = new Node("kh:following-node");
			Node followingIdAttr = new Node("id", wfNode.getNodeId());
			followingIdAttr.setAttribute(true);
			followingNode.addAttribute(followingIdAttr);
			sendToNode.addChild(followingNode);
		}
		configNode.addChild(sendToNode);
		//create "first-children-nodes" subnode
		Node childrenNode = new Node("kh:first-children-nodes");
		for(IWorkflowNode wfNode : node.getWorkflowNode().getChildrenNodes()){
			if(wfNode.getPreviousNodes().size()==0){
				Node childNode = new Node("kh:child-node");
				Node childIdAttr = new Node("id", wfNode.getNodeId());
				childIdAttr.setAttribute(true);
				childNode.addAttribute(childIdAttr);
				childrenNode.addChild(childNode);
			}
		}
		configNode.addChild(childrenNode);
		//create "node-source" subnode
		Node sourcesNode = new Node("kh:node-source-files");
		for(File f: node.getSourceFiles()){
			Node sourceNode = new Node("kh:source-file");
			Node srcAttr = new Node("src", "."+System.getProperty("file.separator")+(new File(projectFile.getPath()).toURI().relativize(f.toURI()).getPath()));
			sourceNode.addAttribute(srcAttr);
			sourcesNode.addChild(sourceNode);
		}
		configNode.addChild(sourcesNode);		
	}
	
	private void removeProjectNodeFromConfig(IProjectNode node){
		for(ConfigurationNode configNode : config.getRoot().getChildren("kh:node")){
			List<ConfigurationNode> attrList = configNode.getAttributes("id");
			if(attrList.size()>0){
				String id = (String) attrList.get(0).getValue();
				if(id.equalsIgnoreCase(node.getWorkflowNode().getNodeId())){
					config.getRoot().removeChild(configNode);
					break;
				}
			}
		}
	}

	
	
	@Override
	public void removeProjectNode(IProjectNode node, boolean removeFromDisc) {
		if(nodes.contains(node)){
			nodes.remove(node);
			removeProjectNodeFromConfig(node);
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
		config.setRootElementName("kh:project");
		setProjectName(projectName);
		save();		
	}

	@Override
	public void configurationChanged(ConfigurationEvent event) {
		LOG.info("KH: debug: configurationChanged fired!");
		List<ConfigurationNode> attributes = config.getRoot().getAttributes("name");
		if(attributes.size()>0){
			projectName = (String) attributes.get(0).getValue();
		}		
	}

	@Override
	public File getProjectFile() {
		return projectFile;
	}

	@Override
	public void setProjectFile(File file) {
		projectFile = file;
	}
	
	


}