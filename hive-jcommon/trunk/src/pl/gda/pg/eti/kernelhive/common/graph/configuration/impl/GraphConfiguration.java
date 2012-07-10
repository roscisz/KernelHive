package pl.gda.pg.eti.kernelhive.common.graph.configuration.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;

import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.IGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.impl.GenericGraphNode;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;
import pl.gda.pg.eti.kernelhive.common.source.SourceFile;

public class GraphConfiguration implements IGraphConfiguration {

	private static final String ROOT_NODE = "kh:project";
	private static final String ROOT_NODE_NAME_ATTRIBUTE = "name";
	private static final String NODE = "kh:node";
	private static final String NODE_ID_ATTRIBUTE = "id";
	private static final String NODE_HASH_ATTRIBUTE = "hash";
	private static final String NODE_PARENT_ID_ATTRIBUTE = "parent-id";
	private static final String NODE_NAME_ATTRIBUTE = "name";
	private static final String NODE_X_ATTRIBUTE = "x";
	private static final String NODE_Y_ATTRIBUTE = "y";
	private static final String NODE_TYPE_ATTRIBUTE = "type";
	private static final String SEND_TO_NODE = "kh:send-to";
	private static final String FOLLOWING_NODE = "kh:following-node";
	private static final String FOLLOWING_NODE_ID_ATTRIBUTE = "id";
	private static final String NODE_SOURCE_FILES = "kh:node-source-files";
	private static final String SOURCE_FILE = "kh:source-file";
	private static final String SOURCE_FILE_SRC_ATTRIBUTE = "src";
	private static final String FIRST_CHILDREN_NODE = "kh:first-children-nodes";
	private static final String CHILD_NODE = "kh:child-node";
	private static final String CHILD_NODE_ID_ATTRIBUTE = "id";

	private static final Logger LOG = Logger.getLogger(GraphConfiguration.class
			.getName());

	private File configFile;
	private XMLConfiguration config;

	public GraphConfiguration() {
		config = new XMLConfiguration();
		config.setRootElementName(ROOT_NODE);
	}

	public GraphConfiguration(File configFile) {
		this.configFile = configFile;
		config = new XMLConfiguration();
		config.setFile(this.configFile);
		config.setRootElementName(ROOT_NODE);
	}

	@Override
	public String getProjectName() throws ConfigurationException {
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

	@Override
	public void setProjectName(String name) throws ConfigurationException {
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
	public void saveGraph(List<IGraphNode> nodes) throws ConfigurationException {
		saveGraph(nodes, configFile);
	}

	@Override
	public void saveGraph(List<IGraphNode> nodes, File file)
			throws ConfigurationException {
		XMLConfiguration tempConfig = (XMLConfiguration) config.clone();
		try {
			config.clear();
			for (IGraphNode node : nodes) {
				saveGraphNode(node, file);
			}
			config.save(file);
		} catch (ConfigurationException e) {
			config = tempConfig;
			config.save(file);
			throw e;
		}
	}

	@Override
	public List<IGraphNode> loadGraph() throws ConfigurationException {
		return loadGraph(this.configFile);
	}

	@Override
	public List<IGraphNode> loadGraph(File file) throws ConfigurationException {
		try {
			config.setFile(file);
			config.load();
			// config.validate();//TODO write schema
			return loadGraphFromXML();
		} catch (ConfigurationException e) {
			LOG.severe("KH: could not load graph from file: " + file.getPath()
					+ " " + e.getMessage());
			throw e;
		}
	}

	private void saveGraphNode(IGraphNode node, File file)
			throws ConfigurationException {
		try {
			// create project node
			Node configNode = new Node(NODE);
			Node idAttr = new Node(NODE_ID_ATTRIBUTE, node.getNodeId());
			idAttr.setAttribute(true);
			Node hashAttr = new Node(NODE_HASH_ATTRIBUTE, node.hashCode());
			hashAttr.setAttribute(true);
			Node parentAttr = new Node(NODE_PARENT_ID_ATTRIBUTE,
					node.getParentNode() != null ? node.getParentNode()
							.getNodeId() : "");
			parentAttr.setAttribute(true);
			Node xAttr = new Node(NODE_X_ATTRIBUTE, node.getX());
			xAttr.setAttribute(true);
			Node yAttr = new Node(NODE_Y_ATTRIBUTE, node.getY());
			yAttr.setAttribute(true);
			Node nameAttr = new Node(NODE_NAME_ATTRIBUTE, node.getName());
			nameAttr.setAttribute(true);
			Node typeAttr = new Node(NODE_TYPE_ATTRIBUTE, node.getType()
					.toString());
			typeAttr.setAttribute(true);
			configNode.addAttribute(idAttr);
			configNode.addAttribute(parentAttr);
			configNode.addAttribute(hashAttr);
			configNode.addAttribute(xAttr);
			configNode.addAttribute(yAttr);
			configNode.addAttribute(nameAttr);
			configNode.addAttribute(typeAttr);
			// create "send-to" subnode
			Node sendToNode = new Node(SEND_TO_NODE);
			for (IGraphNode wfNode : node.getFollowingNodes()) {
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
			for (IGraphNode wfNode : node.getChildrenNodes()) {
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
						FileUtils.translateAbsoluteToRelativePath(file
								.getAbsolutePath(), f.getFile()
								.getAbsolutePath()))));
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

	private List<IGraphNode> loadGraphFromXML() throws ConfigurationException {
		// initial creation of project nodes
		List<IGraphNode> nodes = loadGraphNodes();
		// linking project nodes with one another
		linkGraphNodes(nodes);
		return nodes;
	}

	private List<IGraphNode> loadGraphNodes() throws ConfigurationException {
		List<IGraphNode> nodes = new ArrayList<IGraphNode>();
		for (ConfigurationNode node : config.getRoot().getChildren(NODE)) {
			String id = null, name = null;
			int x = -1, y = -1;
			GraphNodeType type = null;

			List<ConfigurationNode> idAttrList = node
					.getAttributes(NODE_ID_ATTRIBUTE);
			List<ConfigurationNode> xAttrList = node
					.getAttributes(NODE_X_ATTRIBUTE);
			List<ConfigurationNode> yAttrList = node
					.getAttributes(NODE_Y_ATTRIBUTE);
			List<ConfigurationNode> nameAttrList = node
					.getAttributes(NODE_NAME_ATTRIBUTE);
			List<ConfigurationNode> typeAttrList = node
					.getAttributes(NODE_TYPE_ATTRIBUTE);

			if (idAttrList.size() > 0)
				id = (String) idAttrList.get(0).getValue();
			if (xAttrList.size() > 0)
				x = Integer.parseInt((String) xAttrList.get(0).getValue());
			if (yAttrList.size() > 0)
				y = Integer.parseInt((String) yAttrList.get(0).getValue());
			if (nameAttrList.size() > 0)
				name = (String) nameAttrList.get(0).getValue();
			if (typeAttrList.size() > 0)
				type = GraphNodeType.getType((String) typeAttrList.get(0)
						.getValue());

			IGraphNode graphNode;
			if (type == GraphNodeType.GENERIC) {
				graphNode = new GenericGraphNode(id, name);// TODO
			} else {
				throw new ConfigurationException("KH: not generic! TEST DEBUG");
			}
			graphNode.setX(x);
			graphNode.setY(y);

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
							graphNode.addSourceFile(new SourceFile(new File(
									absolutePath)));
						} else {
							throw new ConfigurationException(
									"KH: could not found source file with a filepath: "
											+ absolutePath);
						}
					}
				}
			}
			nodes.add(graphNode);
		}
		return nodes;
	}

	private void linkGraphNodes(List<IGraphNode> nodes)
			throws ConfigurationException {
		for (ConfigurationNode node : config.getRoot().getChildren(NODE)) {
			List<ConfigurationNode> idAttrList = node
					.getAttributes(NODE_ID_ATTRIBUTE);
			List<ConfigurationNode> parentIdAttrList = node
					.getAttributes(NODE_PARENT_ID_ATTRIBUTE);
			IGraphNode graphNode = null;
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
				for (IGraphNode pn : nodes) {
					if (pn.getNodeId().equalsIgnoreCase(id)) {
						graphNode = pn;
						break;
					}
				}
			}
			if (parentId != null) {
				IGraphNode parentNode = null;
				for (IGraphNode pn : nodes) {
					if (pn.getNodeId().equalsIgnoreCase(parentId)) {
						parentNode = pn;
						break;
					}
				}
				if (graphNode != null && parentNode != null) {
					graphNode.setParentNode(parentNode);
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
							IGraphNode followingProjectNode = null;
							for (IGraphNode pn : nodes) {
								if (pn.getNodeId()
										.equalsIgnoreCase(followingId)) {
									followingProjectNode = pn;
									break;
								}
							}
							if (followingProjectNode != null) {
								graphNode
										.addFollowingNode(followingProjectNode);
							}
						}
					}
				}
			}

		}
	}

	@Override
	public void setConfigurationFile(File file) {
		this.configFile = file;
	}

	@Override
	public File getConfigurationFile() {
		return configFile;
	}
}
