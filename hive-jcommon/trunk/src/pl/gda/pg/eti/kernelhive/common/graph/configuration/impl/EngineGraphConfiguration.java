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

import pl.gda.pg.eti.kernelhive.common.graph.configuration.IEngineGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.common.source.KernelString;

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

	public EngineGraphConfiguration() {
		super();
	}

	public EngineGraphConfiguration(File file) {
		super(file);
	}

	private Node createGraphNodeForEngine(EngineGraphNodeDecorator node) throws ConfigurationException {
		try {
			Node configNode = createNodeForEngine(node);
			Node sendToNode = createSendToSubNode(node.getGraphNode());
			Node childrenNode = createChildrenSubNode(node.getGraphNode());
			Node propertiesNode = createPropertiesSubNode(node.getGraphNode());
			Node sourcesNode = createKernelsSubNode(node);
			configNode.addChild(childrenNode);
			configNode.addChild(sendToNode);
			configNode.addChild(propertiesNode);
			configNode.addChild(sourcesNode);
			return configNode;
		} catch (NullPointerException e) {
			throw new ConfigurationException(e);
		}
	}

	private Node createKernelsSubNode(EngineGraphNodeDecorator node) throws ConfigurationException {
		Node sourcesNode = new Node(NODE_KERNELS);
		for (IKernelString s : node.getKernels()) {
			Node sourceNode = new Node(KERNEL);
			Node srcAttr = new Node(
					KERNEL_SRC_ATTRIBUTE,
					s.getKernel());
			Node srcIdAttr = new Node(KERNEL_ID_ATTRIBUTE, s.getId());
			srcIdAttr.setAttribute(true);
			srcAttr.setAttribute(true);
			sourceNode.addAttribute(srcIdAttr);
			sourceNode.addAttribute(srcAttr);
			Set<String> srcKeySet = s.getProperties().keySet();
			for (String key : srcKeySet) {
				Object val = s.getProperties().get(key);
				Node propNode = new Node(KERNEL_PROPERTY_NODE);
				Node keyNode = new Node(KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE);
				keyNode.setAttribute(true);
				keyNode.setValue(key);
				Node valNode = new Node(
						KERNEL_PROPERTY_NODE_VALUE_ATTRIBUTE);
				valNode.setAttribute(true);
				keyNode.setValue(val);
				propNode.addAttribute(keyNode);
				propNode.addAttribute(valNode);
				sourceNode.addChild(propNode);
			}
			sourcesNode.addChild(sourceNode);
		}
		return sourcesNode;
	}

	private Node createNodeForEngine(EngineGraphNodeDecorator node) throws ConfigurationException {
		Node configNode = createNode(node.getGraphNode());
		return configNode;
	}

	@Override
	public String getInputDataURL() throws ConfigurationException {
		List<ConfigurationNode> dataNodes = config.getRoot().getChildren(
				INPUT_DATA_NODE);
		if(dataNodes.size()>0){
			List<ConfigurationNode> urlAttrList = dataNodes.get(0).getAttributes(INPUT_DATA_NODE_URL_ATTRIBUTE);
			if(urlAttrList.size()>0){
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
	public List<EngineGraphNodeDecorator> loadGraphForEngine(File file)
			throws ConfigurationException {
		try {
			config.setFile(file);
			config.load();
			// config.validate();//TODO attach schema
			return loadGraphFromXMLForEngine();
		} catch (ConfigurationException e) {
			LOG.severe("KH: could not load engine graph from file: "
					+ file.getPath() + " " + e.getMessage());
			throw e;
		}
	}

	private List<EngineGraphNodeDecorator> loadGraphFromXMLForEngine()
			throws ConfigurationException {
		List<EngineGraphNodeDecorator> engineNodes = loadGraphNodesForEngine();
		List<IGraphNode> nodes = new ArrayList<IGraphNode>();
		for (EngineGraphNodeDecorator n : engineNodes) {
			nodes.add(n.getGraphNode());
		}
		linkGraphNodes(nodes);
		return engineNodes;
	}

	private EngineGraphNodeDecorator loadGraphNodeForEngine(
			ConfigurationNode node) throws ConfigurationException {
		IGraphNode graphNode = loadGraphNode(node);
		EngineGraphNodeDecorator engineNode = new EngineGraphNodeDecorator(
				graphNode);
		return engineNode;
	}

	private List<EngineGraphNodeDecorator> loadGraphNodesForEngine()
			throws ConfigurationException {
		List<EngineGraphNodeDecorator> nodes = new ArrayList<EngineGraphNodeDecorator>();
		for (ConfigurationNode node : config.getRoot().getChildren(NODE)) {
			EngineGraphNodeDecorator engineNode = loadGraphNodeForEngine(node);
			List<IKernelString> kernels = loadKernels(node);
			engineNode.setKernels(kernels);
			Map<String, Object> nodeProperties = loadGraphNodeProperties(node);
			engineNode.getGraphNode().setProperties(nodeProperties);
			nodes.add(engineNode);
		}
		return nodes;
	}

	private IKernelString loadKernel(ConfigurationNode node)
			throws ConfigurationException {
		String src;
		String srcId;

		List<ConfigurationNode> idSourceAttrs = node
				.getAttributes(KERNEL_ID_ATTRIBUTE);
		if (idSourceAttrs.size() > 0) {
			srcId = (String) idSourceAttrs.get(0).getValue();
		} else {
			throw new ConfigurationException("KH: no required attribute '"
					+ KERNEL_ID_ATTRIBUTE + "' found in " + KERNEL + " node");
		}

		List<ConfigurationNode> srcAttrs = node
				.getAttributes(KERNEL_SRC_ATTRIBUTE);
		if (srcAttrs.size() > 0) {
			src = (String) srcAttrs.get(0).getValue();
		} else {
			throw new ConfigurationException(
					"KH: no required attribute 'src' in " + KERNEL + " node");
		}

		Map<String, Object> properties = loadKernelProperties(node);
		return new KernelString(srcId, src, properties);
	}

	private Map<String, Object> loadKernelProperties(ConfigurationNode node)
			throws ConfigurationException {
		List<ConfigurationNode> propList = node
				.getChildren(KERNEL_PROPERTY_NODE);
		Map<String, Object> properties = new HashMap<String, Object>();
		for (ConfigurationNode propNode : propList) {
			String key;
			Object value;

			List<ConfigurationNode> keyAttrList = propNode
					.getAttributes(KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE);
			if (keyAttrList.size() > 0) {
				key = (String) keyAttrList.get(0).getValue();
			} else {
				throw new ConfigurationException("KH: no required attribute '"
						+ KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE + "' in "
						+ KERNEL_PROPERTY_NODE + " node");
			}
			List<ConfigurationNode> valueAttrList = propNode
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

	private List<IKernelString> loadKernels(ConfigurationNode node)
			throws ConfigurationException {
		List<IKernelString> kernels = new ArrayList<IKernelString>();

		List<ConfigurationNode> kernelNodes = node.getChildren(NODE_KERNELS);
		if (kernelNodes.size() > 0) {
			ConfigurationNode kernelNode = kernelNodes.get(0);
			kernelNodes = kernelNode.getChildren(KERNEL);
			for (ConfigurationNode n : kernelNodes) {
				kernels.add(loadKernel(n));
			}
		}
		return kernels;
	}

	@Override
	public void saveGraphForEngine(List<EngineGraphNodeDecorator> graphNodes)
			throws ConfigurationException {
		saveGraphForEngine(graphNodes, configFile);
	}

	@Override
	public void saveGraphForEngine(List<EngineGraphNodeDecorator> graphNodes,
			File file) throws ConfigurationException {
		XMLConfiguration tempConfig = (XMLConfiguration) config.clone();
		try {
			config.clear();
			for (EngineGraphNodeDecorator engineNode : graphNodes) {
				config.getRoot().addChild(createGraphNodeForEngine(engineNode));
			}
			config.save(file);
		} catch (ConfigurationException e) {
			config = tempConfig;
			config.save(file);
			throw e;
		}

	}

	@Override
	public void setInputDataURL(String inputDataUrl) throws ConfigurationException {
		List<ConfigurationNode> dataNodes = config.getRoot().getChildren(
				INPUT_DATA_NODE);
		if (dataNodes.size() > 0) {
			List<ConfigurationNode> attrList = dataNodes.get(0).getChildren(INPUT_DATA_NODE_URL_ATTRIBUTE);
			if(attrList.size()>0){
				attrList.get(0).setValue(inputDataUrl);
			}
		} else {
			Node data = new Node(INPUT_DATA_NODE);
			Node url = new Node(INPUT_DATA_NODE_URL_ATTRIBUTE);
			url.setAttribute(true);
			url.setValue(inputDataUrl);
			data.addAttribute(url);
			config.getRoot().addChild(data);
		}
	}
}
