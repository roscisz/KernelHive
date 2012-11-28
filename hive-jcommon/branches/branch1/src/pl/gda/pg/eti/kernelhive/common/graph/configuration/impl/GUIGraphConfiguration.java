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

import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.IGUIGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.source.IKernelFile;
import pl.gda.pg.eti.kernelhive.common.source.KernelFile;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;

/**
 * {@link GUIGraphConfiguration} object is responsible for persisting project
 * graph to the underlying file layer (XML)
 * 
 * @author mschally
 * 
 */
public class GUIGraphConfiguration extends AbstractGraphConfiguration implements IGUIGraphConfiguration {

	private static final String NODE_X_ATTRIBUTE = "x";
	private static final String NODE_Y_ATTRIBUTE = "y";
	private static final String NODE_SOURCE_FILES = "kh:node-source-files";
	private static final String SOURCE_FILE = "kh:source-file";
	private static final String SOURCE_FILE_SRC_ATTRIBUTE = "src";
	private static final String SOURCE_FILE_ID_ATTRIBUTE = "id";
	private static final String SOURCE_FILE_PROPERTY_NODE = "kh:property";
	private static final String SOURCE_FILE_PROPERTY_NODE_KEY_ATTRIBUTE = "key";
	private static final String SOURCE_FILE_PROPERTY_NODE_VALUE_ATTRIBUTE = "value";
	
	private static final Logger LOG = Logger
			.getLogger(GUIGraphConfiguration.class.getName());

	public GUIGraphConfiguration() {
		super();
	}

	public GUIGraphConfiguration(File configFile) {
		super(configFile);
	}

	private Node createGraphNodeForGUI(GUIGraphNodeDecorator node, File file)
			throws ConfigurationException {
		try {
			Node configNode = createNodeForGUI(node);
			Node sendToNode = createSendToSubNode(node.getGraphNode());
			Node childrenNode = createChildrenSubNode(node.getGraphNode());
			Node propertiesNode = createPropertiesSubNode(node.getGraphNode());
			Node sourcesNode = createSourceFilesSubNode(node, file);
			configNode.addChild(childrenNode);
			configNode.addChild(sendToNode);
			configNode.addChild(propertiesNode);
			configNode.addChild(sourcesNode);
			return configNode;
		} catch (NullPointerException e) {
			throw new ConfigurationException(e);
		}
	}

	private Node createNodeForGUI(GUIGraphNodeDecorator node)
			throws ConfigurationException {
		Node configNode = createNode(node.getGraphNode());
		Node xAttr = new Node(NODE_X_ATTRIBUTE, node.getX());
		xAttr.setAttribute(true);
		Node yAttr = new Node(NODE_Y_ATTRIBUTE, node.getY());
		yAttr.setAttribute(true);
		configNode.addAttribute(xAttr);
		configNode.addAttribute(yAttr);
		return configNode;
	}

	private Node createSourceFilesSubNode(GUIGraphNodeDecorator node, File file)
			throws ConfigurationException {
		Node sourcesNode = new Node(NODE_SOURCE_FILES);
		for (IKernelFile f : node.getSourceFiles()) {
			Node sourceNode = new Node(SOURCE_FILE);
			Node srcAttr = new Node(
					SOURCE_FILE_SRC_ATTRIBUTE,
					(new File(FileUtils.translateAbsoluteToRelativePath(file
							.getAbsolutePath(), f.getFile().getAbsolutePath()))));
			Node srcIdAttr = new Node(SOURCE_FILE_ID_ATTRIBUTE, f.getId());
			srcIdAttr.setAttribute(true);
			srcAttr.setAttribute(true);
			sourceNode.addAttribute(srcIdAttr);
			sourceNode.addAttribute(srcAttr);
			Set<String> srcKeySet = f.getProperties().keySet();
			for (String key : srcKeySet) {
				Object val = f.getProperties().get(key);
				Node propNode = new Node(SOURCE_FILE_PROPERTY_NODE);
				Node keyNode = new Node(SOURCE_FILE_PROPERTY_NODE_KEY_ATTRIBUTE);
				keyNode.setAttribute(true);
				keyNode.setValue(key);
				Node valNode = new Node(
						SOURCE_FILE_PROPERTY_NODE_VALUE_ATTRIBUTE);
				valNode.setAttribute(true);
				valNode.setValue(val);
				propNode.addAttribute(keyNode);
				propNode.addAttribute(valNode);
				sourceNode.addChild(propNode);
			}
			sourcesNode.addChild(sourceNode);
		}
		return sourcesNode;
	}

	
	@Override
	public List<GUIGraphNodeDecorator> loadGraphForGUI()
			throws ConfigurationException {
		return loadGraphForGUI(configFile);
	}

	@Override
	public List<GUIGraphNodeDecorator> loadGraphForGUI(File file)
			throws ConfigurationException {
		XMLConfiguration temp = (XMLConfiguration) config.clone();
		try {
			config.clear();
			config.setFile(file);
			config.load();
			// config.validate();//TODO attach schema
			return loadGraphFromXMLForGUI();
		} catch (ConfigurationException e) {
			LOG.severe("KH: could not load gui graph from file: "
					+ file.getPath() + " " + e.getMessage());
			config = temp;
			config.save();
			throw e;
		}
	}

	private List<GUIGraphNodeDecorator> loadGraphFromXMLForGUI()
			throws ConfigurationException {
		List<GUIGraphNodeDecorator> guiNodes = loadGraphNodesForGUI();
		List<IGraphNode> nodes = new ArrayList<IGraphNode>();
		for (GUIGraphNodeDecorator n : guiNodes) {
			nodes.add(n.getGraphNode());
		}
		linkGraphNodes(nodes);
		return guiNodes;
	}

	private GUIGraphNodeDecorator loadGraphNodeForGUI(ConfigurationNode node)
			throws ConfigurationException {
		
		IGraphNode graphNode = loadGraphNode(node);
		int x = -1, y = -1;
		
		List<ConfigurationNode> xAttrList = node
				.getAttributes(NODE_X_ATTRIBUTE);
		List<ConfigurationNode> yAttrList = node
				.getAttributes(NODE_Y_ATTRIBUTE);
		if (xAttrList.size() > 0)
			x = Integer.parseInt((String) xAttrList.get(0).getValue());
		if (yAttrList.size() > 0)
			y = Integer.parseInt((String) yAttrList.get(0).getValue());
		
		GUIGraphNodeDecorator guiNode = new GUIGraphNodeDecorator(graphNode);
		guiNode.setX(x);
		guiNode.setY(y);

		return guiNode;
	}	

	private List<GUIGraphNodeDecorator> loadGraphNodesForGUI()
			throws ConfigurationException {
		List<GUIGraphNodeDecorator> nodes = new ArrayList<GUIGraphNodeDecorator>();
		for (ConfigurationNode node : config.getRoot().getChildren(NODE)) {
			GUIGraphNodeDecorator guiNode = loadGraphNodeForGUI(node);
			List<IKernelFile> sourceFiles = loadSourceFiles(node);
			guiNode.setSourceFiles(sourceFiles);
			Map<String, Object> nodeProperties = loadGraphNodeProperties(node);
			guiNode.getGraphNode().setProperties(nodeProperties);
			nodes.add(guiNode);
		}
		return nodes;
	}

	private IKernelFile loadSourceFile(ConfigurationNode src)
			throws ConfigurationException {
		File file;
		String srcId;

		List<ConfigurationNode> idSourceAttrs = src
				.getAttributes(SOURCE_FILE_ID_ATTRIBUTE);
		if (idSourceAttrs.size() > 0) {
			srcId = (String) idSourceAttrs.get(0).getValue();
		} else {
			throw new ConfigurationException("KH: no required attribute '"
					+ SOURCE_FILE_ID_ATTRIBUTE + "' found in " + SOURCE_FILE
					+ " node");
		}

		List<ConfigurationNode> srcAttrs = src
				.getAttributes(SOURCE_FILE_SRC_ATTRIBUTE);
		if (srcAttrs.size() > 0) {
			String absolutePath = FileUtils.translateRelativeToAbsolutePath(
					config.getBasePath(), (String) srcAttrs.get(0).getValue());
			if (absolutePath == null) {
				throw new ConfigurationException(
						"KH: could not found source file with a stored filepath: "
								+ srcAttrs.get(0).getValue()
								+ ", basepath of the config file: "
								+ config.getBasePath());
			}
			file = new File(absolutePath);
			if (!file.exists()) {
				throw new ConfigurationException(
						"KH: could not found source file with a filepath: "
								+ absolutePath);
			}
		} else {
			throw new ConfigurationException(
					"KH: no required attribute 'src' in " + SOURCE_FILE
							+ " node");
		}

		Map<String, Object> properties = loadSourceFileProperties(src);

		return new KernelFile(file, srcId, properties);
	}

	private Map<String, Object> loadSourceFileProperties(ConfigurationNode node)
			throws ConfigurationException {
		List<ConfigurationNode> propList = node
				.getChildren(SOURCE_FILE_PROPERTY_NODE);
		Map<String, Object> properties = new HashMap<String, Object>();
		for (ConfigurationNode propNode : propList) {
			String key;
			Object value;

			List<ConfigurationNode> keyAttrList = propNode
					.getAttributes(SOURCE_FILE_PROPERTY_NODE_KEY_ATTRIBUTE);
			if (keyAttrList.size() > 0) {
				key = (String) keyAttrList.get(0).getValue();
			} else {
				throw new ConfigurationException("KH: no required attribute '"
						+ SOURCE_FILE_PROPERTY_NODE_KEY_ATTRIBUTE + "' in "
						+ SOURCE_FILE_PROPERTY_NODE + " node");
			}
			List<ConfigurationNode> valueAttrList = propNode
					.getAttributes(SOURCE_FILE_PROPERTY_NODE_VALUE_ATTRIBUTE);
			if (valueAttrList.size() > 0) {
				value = valueAttrList.get(0).getValue();
			} else {
				throw new ConfigurationException("KH: no required attribute '"
						+ SOURCE_FILE_PROPERTY_NODE_VALUE_ATTRIBUTE + "' in "
						+ SOURCE_FILE_PROPERTY_NODE + " node");
			}
			properties.put(key, value);
		}
		return properties;
	}

	private List<IKernelFile> loadSourceFiles(ConfigurationNode node)
			throws ConfigurationException {
		List<IKernelFile> sourceFiles = new ArrayList<IKernelFile>();

		List<ConfigurationNode> sourceFilesList = node
				.getChildren(NODE_SOURCE_FILES);
		if (sourceFilesList.size() > 0) {
			ConfigurationNode sourcesNode = sourceFilesList.get(0);
			sourceFilesList = sourcesNode.getChildren(SOURCE_FILE);
			for (ConfigurationNode src : sourceFilesList) {
				sourceFiles.add(loadSourceFile(src));
			}
		}
		return sourceFiles;
	}

	@Override
	public void saveGraphForGUI(List<GUIGraphNodeDecorator> guiGraphNode)
			throws ConfigurationException {
		saveGraphForGUI(guiGraphNode, configFile);
	}

	@Override
	public void saveGraphForGUI(List<GUIGraphNodeDecorator> guiGraphNodes,
			File file) throws ConfigurationException {
		XMLConfiguration tempConfig = (XMLConfiguration) config.clone();
		try {
			config.clear();
			config.setRootNode(tempConfig.getRootNode());
			for (GUIGraphNodeDecorator guiNode : guiGraphNodes) {
				config.getRoot().addChild(createGraphNodeForGUI(guiNode, file));
			}
			config.save(file);
		} catch (ConfigurationException e) {
			config = tempConfig;
			config.save(file);
			throw e;
		}
	}	
}
