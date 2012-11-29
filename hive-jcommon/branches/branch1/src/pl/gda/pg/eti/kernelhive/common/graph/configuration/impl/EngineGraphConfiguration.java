package pl.gda.pg.eti.kernelhive.common.graph.configuration.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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

import pl.gda.pg.eti.kernelhive.common.graph.configuration.IEngineGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.common.source.KernelString;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNode;

/**
 * 
 * @author mschally
 * 
 */
public class EngineGraphConfiguration extends AbstractGraphConfiguration
		implements IEngineGraphConfiguration {

	private static final String INPUT_DATA_NODE = "kh:input-data";
	private static final String INPUT_DATA_NODE_URL_ATTRIBUTE = "url";
	private static final String NODE_KERNELS = "kh:kernels";
	private static final String KERNEL = "kh:kernel";
	private static final String KERNEL_ID_ATTRIBUTE = "id";
	private static final String KERNEL_SRC_ATTRIBUTE = "src";
	private static final String KERNEL_PROPERTY_NODE = "kh:property";
	private static final String KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE = "key";
	private static final String KERNEL_PROPERTY_NODE_VALUE_ATTRIBUTE = "value";

	private static Logger LOG = Logger.getLogger(EngineGraphConfiguration.class
			.getName());

	public EngineGraphConfiguration(final String serializedConf) {
		super();
		// File inputFile = putStringIntoFile(serializedConf);
		// readFromFile(inputFile);
		// inputFile.delete();
	}

	/**
	 * to read/write graph from/to string, saveGraphForEngine/loadGraphForEngine
	 * with (respective) {@link Reader} and {@link Writer} as an parameter
	 */
	@Deprecated
	public static File putStringIntoFile(final String serializedConf) {
		final File ret = new File("tempConf.xml");
		FileWriter fw;
		try {
			fw = new FileWriter(ret);
			fw.write(serializedConf);
			fw.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public EngineGraphConfiguration() {
		super();
	}

	public EngineGraphConfiguration(final File file) {
		super(file);
	}

	private Node createGraphNodeForEngine(final EngineGraphNodeDecorator node)
			throws ConfigurationException {
		try {
			final Node configNode = createNodeForEngine(node);
			final Node sendToNode = createSendToSubNode(node.getGraphNode());
			final Node childrenNode = createChildrenSubNode(node.getGraphNode());
			final Node propertiesNode = createPropertiesSubNode(node
					.getGraphNode());
			final Node sourcesNode = createKernelsSubNode(node);
			configNode.addChild(childrenNode);
			configNode.addChild(sendToNode);
			configNode.addChild(propertiesNode);
			configNode.addChild(sourcesNode);
			return configNode;
		} catch (final NullPointerException e) {
			throw new ConfigurationException(e);
		}
	}

	private Node createKernelsSubNode(final EngineGraphNodeDecorator node)
			throws ConfigurationException {
		final Node sourcesNode = new Node(NODE_KERNELS);
		for (final IKernelString s : node.getKernels()) {
			final Node sourceNode = new Node(KERNEL);
			final Node srcAttr = new Node(KERNEL_SRC_ATTRIBUTE, s.getKernel());
			final Node srcIdAttr = new Node(KERNEL_ID_ATTRIBUTE, s.getId());
			srcIdAttr.setAttribute(true);
			srcAttr.setAttribute(true);
			sourceNode.addAttribute(srcIdAttr);
			sourceNode.addAttribute(srcAttr);
			final Set<String> srcKeySet = s.getProperties().keySet();
			for (final String key : srcKeySet) {
				final Object val = s.getProperties().get(key);
				final Node propNode = new Node(KERNEL_PROPERTY_NODE);
				final Node keyNode = new Node(
						KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE);
				keyNode.setAttribute(true);
				keyNode.setValue(key);
				final Node valNode = new Node(
						KERNEL_PROPERTY_NODE_VALUE_ATTRIBUTE);
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

	private Node createNodeForEngine(final EngineGraphNodeDecorator node)
			throws ConfigurationException {
		final Node configNode = createNode(node.getGraphNode());
		return configNode;
	}

	@Override
	public String getInputDataURL() throws ConfigurationException {
		final List<ConfigurationNode> dataNodes = config.getRoot().getChildren(
				INPUT_DATA_NODE);
		if (dataNodes.size() > 0) {
			final List<ConfigurationNode> urlAttrList = dataNodes.get(0)
					.getAttributes(INPUT_DATA_NODE_URL_ATTRIBUTE);
			if (urlAttrList.size() > 0) {
				return (String) urlAttrList.get(0).getValue();
			}
		}
		return null;
	}

	@Override
	public List<EngineGraphNodeDecorator> loadGraphForEngine()
			throws ConfigurationException {
		return loadGraphForEngine(configFile);
	}

	@Override
	public List<EngineGraphNodeDecorator> loadGraphForEngine(final File file)
			throws ConfigurationException {
		XMLConfiguration temp = null;
		try {
			temp = (XMLConfiguration) config.clone();
			config.clear();
			config.setFile(file);
			config.load();
			// config.validate();//TODO attach schema
			return loadGraphFromXMLForEngine();
		} catch (final ConfigurationException e) {
			LOG.severe("KH: could not load engine graph from file: "
					+ file.getPath() + " " + e.getMessage());
			config = temp;
			config.save();
			throw e;
		}
	}

	@Override
	public List<EngineGraphNodeDecorator> loadGraphForEngine(final Reader reader)
			throws ConfigurationException {
		config.clear();
		config.load(reader);
		// config.validate();//TODO attach schema
		return loadGraphFromXMLForEngine();
	}

	private List<EngineGraphNodeDecorator> loadGraphFromXMLForEngine()
			throws ConfigurationException {
		final List<EngineGraphNodeDecorator> engineNodes = loadGraphNodesForEngine();
		final List<IGraphNode> nodes = new ArrayList<IGraphNode>();
		for (final EngineGraphNodeDecorator n : engineNodes) {
			nodes.add(n.getGraphNode());
		}
		linkGraphNodes(nodes);
		return engineNodes;
	}

	private EngineGraphNodeDecorator loadGraphNodeForEngine(
			final ConfigurationNode node) throws ConfigurationException {
		final IGraphNode graphNode = loadGraphNode(node);
		final EngineGraphNodeDecorator engineNode = new EngineGraphNodeDecorator(
				graphNode);
		return engineNode;
	}

	private List<EngineGraphNodeDecorator> loadGraphNodesForEngine()
			throws ConfigurationException {
		final List<EngineGraphNodeDecorator> nodes = new ArrayList<EngineGraphNodeDecorator>();
		for (final ConfigurationNode node : config.getRoot().getChildren(NODE)) {
			final EngineGraphNodeDecorator engineNode = loadGraphNodeForEngine(node);
			final List<IKernelString> kernels = loadKernels(node);
			engineNode.setKernels(kernels);
			final Map<String, Object> nodeProperties = loadGraphNodeProperties(node);
			engineNode.getGraphNode().setProperties(nodeProperties);
			nodes.add(engineNode);
		}
		return nodes;
	}

	private IKernelString loadKernel(final ConfigurationNode node)
			throws ConfigurationException {
		String src;
		String srcId;

		final List<ConfigurationNode> idSourceAttrs = node
				.getAttributes(KERNEL_ID_ATTRIBUTE);
		if (idSourceAttrs.size() > 0) {
			srcId = (String) idSourceAttrs.get(0).getValue();
		} else {
			throw new ConfigurationException("KH: no required attribute '"
					+ KERNEL_ID_ATTRIBUTE + "' found in " + KERNEL + " node");
		}

		final List<ConfigurationNode> srcAttrs = node
				.getAttributes(KERNEL_SRC_ATTRIBUTE);
		if (srcAttrs.size() > 0) {
			src = (String) srcAttrs.get(0).getValue();
		} else {
			throw new ConfigurationException(
					"KH: no required attribute 'src' in " + KERNEL + " node");
		}

		final Map<String, Object> properties = loadKernelProperties(node);
		return new KernelString(srcId, src, properties);
	}

	private Map<String, Object> loadKernelProperties(
			final ConfigurationNode node) throws ConfigurationException {
		final List<ConfigurationNode> propList = node
				.getChildren(KERNEL_PROPERTY_NODE);
		final Map<String, Object> properties = new HashMap<String, Object>();
		for (final ConfigurationNode propNode : propList) {
			String key;
			Object value;

			final List<ConfigurationNode> keyAttrList = propNode
					.getAttributes(KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE);
			if (keyAttrList.size() > 0) {
				key = (String) keyAttrList.get(0).getValue();
			} else {
				throw new ConfigurationException("KH: no required attribute '"
						+ KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE + "' in "
						+ KERNEL_PROPERTY_NODE + " node");
			}
			final List<ConfigurationNode> valueAttrList = propNode
					.getAttributes(KERNEL_PROPERTY_NODE_VALUE_ATTRIBUTE);
			if (valueAttrList.size() > 0) {
				value = valueAttrList.get(0).getValue();
			} else {
				throw new ConfigurationException("KH: no required attribute '"
						+ KERNEL_PROPERTY_NODE_VALUE_ATTRIBUTE + "' in "
						+ KERNEL_PROPERTY_NODE + " node");
			}
			properties.put(key, value);
		}
		return properties;
	}

	private List<IKernelString> loadKernels(final ConfigurationNode node)
			throws ConfigurationException {
		final List<IKernelString> kernels = new ArrayList<IKernelString>();

		List<ConfigurationNode> kernelNodes = node.getChildren(NODE_KERNELS);
		if (kernelNodes.size() > 0) {
			final ConfigurationNode kernelNode = kernelNodes.get(0);
			kernelNodes = kernelNode.getChildren(KERNEL);
			for (final ConfigurationNode n : kernelNodes) {
				kernels.add(loadKernel(n));
			}
		}
		return kernels;
	}

	@Override
	public void saveGraphForEngine(
			final List<EngineGraphNodeDecorator> graphNodes)
			throws ConfigurationException {
		saveGraphForEngine(graphNodes, configFile);
	}

	@Override
	public void saveGraphForEngine(
			final List<EngineGraphNodeDecorator> graphNodes, final File file)
			throws ConfigurationException {
		final XMLConfiguration tempConfig = (XMLConfiguration) config.clone();
		try {
			config.clear();
			config.setRootNode(tempConfig.getRootNode());
			config.getRootNode().removeChildren();
			final List<ConfigurationNode> inputDataURLNodes = tempConfig
					.getRootNode().getChildren(INPUT_DATA_NODE);
			for (final ConfigurationNode node : inputDataURLNodes) {
				config.getRootNode().addChild(node);
			}
			for (final EngineGraphNodeDecorator engineNode : graphNodes) {
				config.getRoot().addChild(createGraphNodeForEngine(engineNode));
			}
			config.save(file);
		} catch (final ConfigurationException e) {
			config = tempConfig;
			config.save(file);
			throw e;
		}

	}

	@Override
	public void saveGraphForEngine(
			final List<EngineGraphNodeDecorator> graphNodes, final Writer writer)
			throws ConfigurationException {
		final XMLConfiguration tempConfig = (XMLConfiguration) config.clone();
		final File tempFile = config.getFile();
		try {
			config.clear();
			final List<ConfigurationNode> rootAttrs = tempConfig.getRootNode()
					.getAttributes();
			config.setRootNode((ConfigurationNode) tempConfig.getRootNode()
					.clone());
			config.getRootNode().removeChildren();
			for (final ConfigurationNode n : rootAttrs) {
				config.getRootNode()
						.addAttribute((ConfigurationNode) n.clone());
			}
			final List<ConfigurationNode> inputDataURLNodes = tempConfig
					.getRoot().getChildren(INPUT_DATA_NODE);
			for (final ConfigurationNode node : inputDataURLNodes) {
				config.getRootNode().addChild(node);
			}
			for (final EngineGraphNodeDecorator engineNode : graphNodes) {
				config.getRoot().addChild(createGraphNodeForEngine(engineNode));
			}
			config.save(writer);
		} catch (final ConfigurationException e) {
			config = tempConfig;
			config.save(tempFile);
		}

	}

	@Override
	public void setInputDataURL(final String inputDataUrl)
			throws ConfigurationException {
		final List<ConfigurationNode> dataNodes = config.getRoot().getChildren(
				INPUT_DATA_NODE);
		if (dataNodes.size() > 0) {
			final List<ConfigurationNode> attrList = dataNodes.get(0)
					.getChildren(INPUT_DATA_NODE_URL_ATTRIBUTE);
			if (attrList.size() > 0) {
				attrList.get(0).setValue(inputDataUrl);
			}
		} else {
			final Node data = new Node(INPUT_DATA_NODE);
			final Node url = new Node(INPUT_DATA_NODE_URL_ATTRIBUTE);
			url.setAttribute(true);
			url.setValue(inputDataUrl);
			data.addAttribute(url);
			config.getRoot().addChild(data);
		}
	}
}
