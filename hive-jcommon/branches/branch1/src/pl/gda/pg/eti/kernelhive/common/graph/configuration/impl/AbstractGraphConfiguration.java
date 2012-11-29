package pl.gda.pg.eti.kernelhive.common.graph.configuration.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration.Node;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;

import pl.gda.pg.eti.kernelhive.common.graph.configuration.IGraphConfiguration;
import pl.gda.pg.eti.kernelhive.repository.graph.node.GraphNodeBuilderException;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;
import pl.gda.pg.eti.kernelhive.repository.loader.RepositoryLoaderService;

/**
 * 
 * @author mschally
 * 
 */
public abstract class AbstractGraphConfiguration implements IGraphConfiguration {

	protected static final String ROOT_NODE = "kh:project";
	protected static final String ROOT_NODE_NAME_ATTRIBUTE = "name";
	protected static final String NODE = "kh:node";
	protected static final String NODE_ID_ATTRIBUTE = "id";
	protected static final String NODE_HASH_ATTRIBUTE = "hash";
	protected static final String NODE_PARENT_ID_ATTRIBUTE = "parent-id";
	protected static final String NODE_NAME_ATTRIBUTE = "name";
	protected static final String NODE_TYPE_ATTRIBUTE = "type";
	protected static final String SEND_TO_NODE = "kh:send-to";
	protected static final String FOLLOWING_NODE = "kh:following-node";
	protected static final String FOLLOWING_NODE_ID_ATTRIBUTE = "id";
	protected static final String PROPERTIES_NODE = "kh:properties";
	protected static final String PROPERTY_NODE = "kh:property";
	protected static final String PROPERTY_NODE_KEY_ATTRIBUTE = "key";
	protected static final String PROPERTY_NODE_VALUE_ATTRIBUTE = "value";
	protected static final String FIRST_CHILDREN_NODE = "kh:first-children-nodes";
	protected static final String CHILD_NODE = "kh:child-node";
	protected static final String CHILD_NODE_ID_ATTRIBUTE = "id";

	private static Logger LOG = Logger
			.getLogger(AbstractGraphConfiguration.class.getName());

	protected File configFile;
	protected XMLConfiguration config;
	protected RepositoryLoaderService repositoryLoaderService;

	public AbstractGraphConfiguration() {
		config = new XMLConfiguration();
		config.setRootElementName(ROOT_NODE);
	}

	public AbstractGraphConfiguration(final File file) {
		config = new XMLConfiguration();
		config.setRootElementName(ROOT_NODE);
		readFromFile(file);
	}

	protected void readFromFile(final File file) {
		this.configFile = file;
		config.setFile(this.configFile);
	}

	protected Node createChildrenSubNode(final IGraphNode node)
			throws ConfigurationException {
		final Node childrenNode = new Node(FIRST_CHILDREN_NODE);
		for (final IGraphNode wfNode : node.getChildrenNodes()) {
			if (wfNode.getPreviousNodes().size() == 0) {
				final Node childNode = new Node(CHILD_NODE);
				final Node childIdAttr = new Node(CHILD_NODE_ID_ATTRIBUTE,
						wfNode.getNodeId());
				childIdAttr.setAttribute(true);
				childNode.addAttribute(childIdAttr);
				childrenNode.addChild(childNode);
			}
		}
		return childrenNode;
	}

	protected Node createGraphNode(final IGraphNode node)
			throws ConfigurationException {
		try {
			final Node configNode = createNode(node);
			final Node sendToNode = createSendToSubNode(node);
			final Node childrenNode = createChildrenSubNode(node);
			final Node propertiesNode = createPropertiesSubNode(node);
			configNode.addChild(sendToNode);
			configNode.addChild(childrenNode);
			configNode.addChild(propertiesNode);
			return configNode;
		} catch (final NullPointerException e) {
			throw new ConfigurationException(e);
		}
	}

	protected Node createNode(final IGraphNode node)
			throws ConfigurationException {
		final Node configNode = new Node(NODE);
		final Node idAttr = new Node(NODE_ID_ATTRIBUTE, node.getNodeId());
		idAttr.setAttribute(true);
		final Node hashAttr = new Node(NODE_HASH_ATTRIBUTE, node.hashCode());
		hashAttr.setAttribute(true);
		final Node parentAttr = new Node(NODE_PARENT_ID_ATTRIBUTE,
				node.getParentNode() != null ? node.getParentNode().getNodeId()
						: "");
		parentAttr.setAttribute(true);
		final Node nameAttr = new Node(NODE_NAME_ATTRIBUTE, node.getName());
		nameAttr.setAttribute(true);
		final Node typeAttr = new Node(NODE_TYPE_ATTRIBUTE, node.getType()
				.toString());
		typeAttr.setAttribute(true);
		configNode.addAttribute(idAttr);
		configNode.addAttribute(parentAttr);
		configNode.addAttribute(hashAttr);
		configNode.addAttribute(nameAttr);
		configNode.addAttribute(typeAttr);
		return configNode;
	}

	protected Node createPropertiesSubNode(final IGraphNode node)
			throws ConfigurationException {
		final Node propertiesNode = new Node(PROPERTIES_NODE);
		final Set<String> keySet = node.getProperties().keySet();
		for (final String key : keySet) {
			final Node propertyNode = new Node(PROPERTY_NODE);
			final Node keyAttr = new Node(PROPERTY_NODE_KEY_ATTRIBUTE, key);
			keyAttr.setAttribute(true);
			final Node valueAttr = new Node(PROPERTY_NODE_VALUE_ATTRIBUTE, node
					.getProperties().get(key));
			valueAttr.setAttribute(true);
			propertyNode.addAttribute(keyAttr);
			propertyNode.addAttribute(valueAttr);
			propertiesNode.addChild(propertyNode);
		}
		return propertiesNode;
	}

	protected Node createSendToSubNode(final IGraphNode node)
			throws ConfigurationException {
		final Node sendToNode = new Node(SEND_TO_NODE);
		for (final IGraphNode wfNode : node.getFollowingNodes()) {
			final Node followingNode = new Node(FOLLOWING_NODE);
			final Node followingIdAttr = new Node(FOLLOWING_NODE_ID_ATTRIBUTE,
					wfNode.getNodeId());
			followingIdAttr.setAttribute(true);
			followingNode.addAttribute(followingIdAttr);
			sendToNode.addChild(followingNode);
		}
		return sendToNode;
	}

	@Override
	public File getConfigurationFile() {
		return configFile;
	}

	@Override
	public String getProjectName() throws ConfigurationException {
		String projectName;
		final List<ConfigurationNode> attrList = config.getRoot()
				.getAttributes(ROOT_NODE_NAME_ATTRIBUTE);
		if (attrList.size() > 0) {
			projectName = (String) attrList.get(0).getValue();
			return projectName;
		} else {
			throw new ConfigurationException("no attribute '"
					+ ROOT_NODE_NAME_ATTRIBUTE
					+ "' in root node of the configuration");
		}
	}

	protected void linkGraphNodes(final List<IGraphNode> nodes)
			throws ConfigurationException {
		for (final ConfigurationNode node : config.getRoot().getChildren(NODE)) {
			final List<ConfigurationNode> idAttrList = node
					.getAttributes(NODE_ID_ATTRIBUTE);
			final List<ConfigurationNode> parentIdAttrList = node
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
				for (final IGraphNode pn : nodes) {
					if (pn.getNodeId().equalsIgnoreCase(id)) {
						graphNode = pn;
						break;
					}
				}
			}
			if (parentId != null) {
				IGraphNode parentNode = null;
				for (final IGraphNode pn : nodes) {
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
					for (final ConfigurationNode followingNode : followingNodes) {
						final List<ConfigurationNode> idList = followingNode
								.getAttributes(FOLLOWING_NODE_ID_ATTRIBUTE);
						if (idList.size() > 0) {
							final String followingId = (String) idList.get(0)
									.getValue();
							IGraphNode followingProjectNode = null;
							for (final IGraphNode pn : nodes) {
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
	public List<IGraphNode> loadGraph() throws ConfigurationException {
		return loadGraph(configFile);
	}

	@Override
	public List<IGraphNode> loadGraph(final File file)
			throws ConfigurationException {
		try {
			config.setFile(file);
			config.load();
			// config.validate();//TODO attach schema
			return loadGraphFromXML();
		} catch (final ConfigurationException e) {
			LOG.severe("KH: could not load graph from file: " + file.getPath()
					+ " " + e.getMessage());
			throw e;
		}
	}

	protected List<IGraphNode> loadGraphFromXML() throws ConfigurationException {
		// initial creation of project nodes
		final List<IGraphNode> nodes = loadGraphNodes();
		// linking project nodes with one another
		linkGraphNodes(nodes);
		return nodes;
	}

	protected IGraphNode loadGraphNode(final ConfigurationNode node)
			throws ConfigurationException {
		String id = null, name = null;
		GraphNodeType type = null;

		final List<ConfigurationNode> idAttrList = node
				.getAttributes(NODE_ID_ATTRIBUTE);
		final List<ConfigurationNode> nameAttrList = node
				.getAttributes(NODE_NAME_ATTRIBUTE);
		final List<ConfigurationNode> typeAttrList = node
				.getAttributes(NODE_TYPE_ATTRIBUTE);

		if (idAttrList.size() > 0)
			id = (String) idAttrList.get(0).getValue();
		if (nameAttrList.size() > 0)
			name = (String) nameAttrList.get(0).getValue();
		if (typeAttrList.size() > 0)
			type = GraphNodeType.getType((String) typeAttrList.get(0)
					.getValue());

		IGraphNode graphNode;
		IGraphNodeBuilder gnBuilder = RepositoryLoaderService.getInstance()
				.createGraphNodeBuilder();
		try {
			graphNode = gnBuilder.setType(type).setId(id).setName(name).build();
		} catch (final GraphNodeBuilderException e) {
			e.printStackTrace();
			throw new ConfigurationException(e);
		}
		return graphNode;
	}

	protected Map<String, Object> loadGraphNodeProperties(
			final ConfigurationNode node) throws ConfigurationException {
		final Map<String, Object> props = new HashMap<String, Object>();

		List<ConfigurationNode> propertiesList = node
				.getChildren(PROPERTIES_NODE);
		if (propertiesList.size() > 0) {
			final ConfigurationNode propsNode = propertiesList.get(0);
			propertiesList = propsNode.getChildren(PROPERTY_NODE);
			for (final ConfigurationNode prop : propertiesList) {
				String key;
				Object value;

				final List<ConfigurationNode> keyAttrList = prop
						.getAttributes(PROPERTY_NODE_KEY_ATTRIBUTE);
				final List<ConfigurationNode> valueAttrList = prop
						.getAttributes(PROPERTY_NODE_VALUE_ATTRIBUTE);

				if (keyAttrList.size() > 0) {
					key = (String) keyAttrList.get(0).getValue();
				} else {
					throw new ConfigurationException(
							"KH: no required 'key' attribute in "
									+ PROPERTY_NODE + " node");
				}

				if (valueAttrList.size() > 0) {
					value = valueAttrList.get(0).getValue();
				} else {
					throw new ConfigurationException(
							"KH: no required 'value' attribute in "
									+ PROPERTY_NODE + " node");
				}

				props.put(key, value);
			}
		}
		return props;
	}

	protected List<IGraphNode> loadGraphNodes() throws ConfigurationException {
		final List<IGraphNode> nodes = new ArrayList<IGraphNode>();
		for (final ConfigurationNode node : config.getRoot().getChildren(NODE)) {
			final IGraphNode graphNode = loadGraphNode(node);
			final Map<String, Object> nodeProperties = loadGraphNodeProperties(node);
			graphNode.setProperties(nodeProperties);
			nodes.add(graphNode);
		}
		return nodes;
	}

	@Override
	public void save() throws ConfigurationException {
		save(configFile);
	}

	@Override
	public void save(final File file) throws ConfigurationException {
		config.save(file);
	}

	@Override
	public void saveGraph(final List<IGraphNode> nodes)
			throws ConfigurationException {
		saveGraph(nodes, configFile);
	}

	@Override
	public void saveGraph(final List<IGraphNode> nodes, final File file)
			throws ConfigurationException {
		final XMLConfiguration tempConfig = (XMLConfiguration) config.clone();
		try {
			config.clear();
			config.setRootNode(tempConfig.getRootNode());
			config.getRootNode().removeChildren();
			for (final IGraphNode node : nodes) {
				config.getRoot().addChild(createGraphNode(node));
			}
			config.save(file);
		} catch (final ConfigurationException e) {
			config = tempConfig;
			config.save(file);
			throw e;
		}
	}

	@Override
	public void setConfigurationFile(final File file) {
		this.configFile = file;
	}

	@Override
	public void setProjectName(final String name) throws ConfigurationException {
		final List<ConfigurationNode> attributes = config.getRoot()
				.getAttributes(ROOT_NODE_NAME_ATTRIBUTE);
		if (attributes.size() > 0) {
			attributes.get(0).setValue(name);
		} else {
			final Node attr = new Node(ROOT_NODE_NAME_ATTRIBUTE);
			attr.setAttribute(true);
			attr.setValue(name);
			config.getRoot().addAttribute(attr);
		}
	}
}
