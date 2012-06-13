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

import pl.gda.pg.eti.kernelhive.gui.file.FileUtils;
import pl.gda.pg.eti.kernelhive.gui.project.node.IProjectNode;
import pl.gda.pg.eti.kernelhive.gui.project.node.impl.GenericProjectNode;
import pl.gda.pg.eti.kernelhive.gui.source.ISourceFile;
import pl.gda.pg.eti.kernelhive.gui.source.SourceFile;

/**
 * 
 * @author mschally
 *
 */
public class KernelHiveProject implements Serializable, IProject,
		ConfigurationListener {

	private static final long serialVersionUID = -4797108604024696381L;
	private static final Logger LOG = Logger.getLogger(KernelHiveProject.class
			.getName());

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
	private static final String CHILD_NODE = "kh:child-node";
	private static final String CHILD_NODE_ID_ATTRIBUTE = "id";

	private String projectName;
	private File projectDir;
	private File projectFile;
	private List<IProjectNode> nodes = new ArrayList<IProjectNode>();
	private transient XMLConfiguration config;

	public KernelHiveProject(String projectDir, String projectName) {
		this.projectName = projectName;
		this.projectDir = new File(projectDir);
		config = new XMLConfiguration();
		config.addConfigurationListener(this);
	}

	@Override
	public void addProjectNode(IProjectNode node) {
		if (!nodes.contains(node)) {
			nodes.add(node);
		}
	}

	private void addProjectNodeToConfig(IProjectNode node)
			throws ConfigurationException {
		try {
			// create project node
			Node configNode = new Node(NODE);
			Node idAttr = new Node(NODE_ID_ATTRIBUTE, node.getNodeId());
			idAttr.setAttribute(true);
			Node hashAttr = new Node(NODE_HASH_ATTRIBUTE, node.hashCode());
			hashAttr.setAttribute(true);
			Node parentAttr = new Node(NODE_PARENT_ID_ATTRIBUTE, node.getParentNode() != null ? node
					.getParentNode().getNodeId() : "");
			parentAttr.setAttribute(true);
			Node xAttr = new Node(NODE_X_ATTRIBUTE, node.getX());
			xAttr.setAttribute(true);
			Node yAttr = new Node(NODE_Y_ATTRIBUTE, node.getY());
			yAttr.setAttribute(true);
			configNode.addAttribute(idAttr);
			configNode.addAttribute(parentAttr);
			configNode.addAttribute(hashAttr);
			configNode.addAttribute(xAttr);
			configNode.addAttribute(yAttr);
			// create "send-to" subnode
			Node sendToNode = new Node(SEND_TO_NODE);
			for (IProjectNode wfNode : node.getFollowingNodes()) {
				Node followingNode = new Node(FOLLOWING_NODE);
				Node followingIdAttr = new Node(FOLLOWING_NODE_ID_ATTRIBUTE,
						wfNode.getNodeId());
				followingIdAttr.setAttribute(true);
				followingNode.addAttribute(followingIdAttr);
				sendToNode.addChild(followingNode);
			}
			configNode.addChild(sendToNode);
			// create "first-children-nodes" subnode
			Node childrenNode = new Node(FIRST_CHILDREN_NODE);
			for (IProjectNode wfNode : node.getChildrenNodes()) {
				if (wfNode.getPreviousNodes().size() == 0) {
					Node childNode = new Node(CHILD_NODE);
					Node childIdAttr = new Node(CHILD_NODE_ID_ATTRIBUTE,
							wfNode.getNodeId());
					childIdAttr.setAttribute(true);
					childNode.addAttribute(childIdAttr);
					childrenNode.addChild(childNode);
				}
			}
			configNode.addChild(childrenNode);
			// create "node-source" subnode
			Node sourcesNode = new Node(NODE_SOURCE_FILES);
			for (ISourceFile f : node.getSourceFiles()) {
				Node sourceNode = new Node(SOURCE_FILE);
				Node srcAttr = new Node(SOURCE_FILE_SRC_ATTRIBUTE, (new File(
						FileUtils.translateAbsoluteToRelativePath(
								projectFile.getAbsolutePath(),
								f.getFile().getAbsolutePath()))));
				srcAttr.setAttribute(true);
				sourceNode.addAttribute(srcAttr);
				sourcesNode.addChild(sourceNode);
			}
			configNode.addChild(sourcesNode);
			config.getRoot().addChild(configNode);
		} catch (NullPointerException e) {
			throw new ConfigurationException(e);
		}
	}

	@Override
	public void configurationChanged(ConfigurationEvent event) {
		LOG.info("KH: debug: configurationChanged fired!");
		List<ConfigurationNode> attributes = config.getRoot().getAttributes(
				ROOT_NODE_NAME_ATTRIBUTE);
		if (attributes.size() > 0) {
			projectName = (String) attributes.get(0).getValue();
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
	public List<IProjectNode> getProjectNodes() {
		return nodes;
	}

	private String initLoadProjectName() throws ConfigurationException {
		String projectName;
		List<ConfigurationNode> attrList = config.getRoot().getAttributes(
				ROOT_NODE_NAME_ATTRIBUTE);
		if (attrList.size() > 0) {
			projectName = (String) attrList.get(0).getValue();
			return projectName;
		} else {
			throw new ConfigurationException("no attribute '"
					+ ROOT_NODE_NAME_ATTRIBUTE
					+ "' in root node of the configuration");
		}
	}

	private List<IProjectNode> initNodesCreation()
			throws ConfigurationException {
		List<IProjectNode> nodes = new ArrayList<IProjectNode>();
		for (ConfigurationNode node : config.getRoot().getChildren(NODE)) {
			String id = null;
			int x = -1, y = -1;

			List<ConfigurationNode> idAttrList = node
					.getAttributes(NODE_ID_ATTRIBUTE);
			List<ConfigurationNode> xAttrList = node
					.getAttributes(NODE_X_ATTRIBUTE);
			List<ConfigurationNode> yAttrList = node
					.getAttributes(NODE_Y_ATTRIBUTE);

			if (idAttrList.size() > 0)
				id = (String) idAttrList.get(0).getValue();
			if (xAttrList.size() > 0)
				x = Integer.parseInt((String) xAttrList.get(0).getValue());
			if (yAttrList.size() > 0)
				y = Integer.parseInt((String) yAttrList.get(0).getValue());

			IProjectNode projectNode = new GenericProjectNode(id);
			projectNode.setX(x);
			projectNode.setY(y);

			List<ConfigurationNode> sourceFilesList = node
					.getChildren(NODE_SOURCE_FILES);
			if (sourceFilesList.size() > 0) {
				ConfigurationNode sourcesNode = sourceFilesList.get(0);
				sourceFilesList = sourcesNode.getChildren(SOURCE_FILE);
				for (ConfigurationNode src : sourceFilesList) {
					List<ConfigurationNode> srcAttrs = src
							.getAttributes(SOURCE_FILE_SRC_ATTRIBUTE);
					if (srcAttrs.size() > 0) {
						ConfigurationNode srcAttr = srcAttrs.get(0);
						String absolutePath = FileUtils
								.translateRelativeToAbsolutePath(
										config.getBasePath(),
										(String) srcAttr.getValue());
						if (absolutePath == null) {
							throw new ConfigurationException(
									"KH: could not foud source file with a stored filepath: "
											+ srcAttr.getValue()
											+ ", basepath of the config file: "
											+ config.getBasePath());
						}
						File file = new File(absolutePath);
						if (file.exists()) {
							projectNode.addSourceFile(new SourceFile(new File(absolutePath)));
						} else {
							throw new ConfigurationException(
									"KH: could not found source file with a filepath: "
											+ absolutePath);
						}
					}
				}
			}
			nodes.add(projectNode);
		}
		return nodes;
	}

	private void initNodesLinking(List<IProjectNode> nodes)
			throws ConfigurationException {
		for (ConfigurationNode node : config.getRoot().getChildren(NODE)) {
			List<ConfigurationNode> idAttrList = node
					.getAttributes(NODE_ID_ATTRIBUTE);
			List<ConfigurationNode> parentIdAttrList = node
					.getAttributes(NODE_PARENT_ID_ATTRIBUTE);
			IProjectNode projectNode = null;
			String id = null, parentId = null;
			if (idAttrList.size() > 0) {
				id = (String) idAttrList.get(0).getValue();
			}
			if (parentIdAttrList.size() > 0) {
				parentId = (String) parentIdAttrList.get(0).getValue();
				if (parentId.equalsIgnoreCase("")) {
					parentId = null;
				}
			}

			if (id != null) {
				for (IProjectNode pn : nodes) {
					if (pn.getNodeId().equalsIgnoreCase(id)) {
						projectNode = pn;
						break;
					}
				}
			}
			if (parentId != null) {
				IProjectNode parentNode = null;
				for (IProjectNode pn : nodes) {
					if (pn.getNodeId()
							.equalsIgnoreCase(parentId)) {
						parentNode = pn;
						break;
					}
				}
				if (projectNode != null && parentNode != null) {
					projectNode.setParentNode(
							parentNode);
				} else {
					throw new ConfigurationException(
							"KH: nodes with following ids could not be initialized: "
									+ id + " " + parentId);
				}
			}
			List<ConfigurationNode> followingNodes = node
					.getChildren(SEND_TO_NODE);
			if (followingNodes.size() > 0) {
				followingNodes = followingNodes.get(0).getChildren(
						FOLLOWING_NODE);
				if (followingNodes.size() > 0) {
					for (ConfigurationNode followingNode : followingNodes) {
						List<ConfigurationNode> idList = followingNode
								.getAttributes(FOLLOWING_NODE_ID_ATTRIBUTE);
						if (idList.size() > 0) {
							String followingId = (String) idList.get(0)
									.getValue();
							IProjectNode followingProjectNode = null;
							for (IProjectNode pn : nodes) {
								if (pn.getNodeId()
										.equalsIgnoreCase(followingId)) {
									followingProjectNode = pn;
									break;
								}
							}
							if (followingProjectNode != null) {
								projectNode.addFollowingNode(
										followingProjectNode);
							}
						}
					}
				}
			}

		}
	}

	@Override
	public void initProject() throws ConfigurationException {
		config.setRootElementName(ROOT_NODE);
		setProjectName(projectName);
		save();
	}

	@Override
	public void load() {
		load(projectFile);
	}

	@Override
	public void load(File file) {
		try {
			config.setFile(file);
			config.load();
			// config.validate();//TODO write schema
			loadFromConfig();
		} catch (ConfigurationException e) {
			LOG.severe("KH: could not load project from file: "
					+ file.getPath() + " " + e.getMessage());
			e.printStackTrace();

			this.nodes = null;
			this.projectDir = null;
			this.projectFile = null;
			this.projectName = null;
		}
	}

	private void loadFromConfig() throws ConfigurationException {
		// initial creation of project nodes
		List<IProjectNode> nodes = initNodesCreation();
		// linking project nodes with one another
		initNodesLinking(nodes);

		this.nodes = nodes;

		// load project name
		this.projectName = initLoadProjectName();
	}

	@Override
	public void removeProjectNode(IProjectNode node, boolean removeFromDisc) {
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

	@Deprecated
	private void removeProjectNodeFromConfig(IProjectNode node) {
		for (ConfigurationNode configNode : config.getRoot().getChildren(NODE)) {
			List<ConfigurationNode> attrList = configNode
					.getAttributes(NODE_ID_ATTRIBUTE);
			if (attrList.size() > 0) {
				String id = (String) attrList.get(0).getValue();
				if (id.equalsIgnoreCase(node.getNodeId())) {
					config.getRoot().removeChild(configNode);
					break;
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
		XMLConfiguration tempConfig = (XMLConfiguration) config.clone();
		try {
			config.clear();
			for (IProjectNode node : nodes) {
				addProjectNodeToConfig(node);
			}
			setProjectNameInConfig(projectName);
			config.save(file);
		} catch (ConfigurationException e) {
			config = tempConfig;
			config.save(file);
			throw e;
		}
	}

	@Override
	public void setProjectDirectory(File dir) {
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

	private void setProjectNameInConfig(String name) {
		List<ConfigurationNode> attributes = config.getRoot().getAttributes(
				ROOT_NODE_NAME_ATTRIBUTE);
		if (attributes.size() > 0) {
			attributes.get(0).setValue(name);
		} else {
			Node attr = new Node(ROOT_NODE_NAME_ATTRIBUTE);
			attr.setAttribute(true);
			attr.setValue(name);
			config.getRoot().addAttribute(attr);
		}
	}

	@Override
	public void setProjectNodes(List<IProjectNode> nodes) {
		this.nodes = nodes;
	}

}