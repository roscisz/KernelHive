package pl.gda.pg.eti.kernelhive.repository.kernel.repository;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;

import pl.gda.pg.eti.kernelhive.repository.configuration.RepositoryConfiguration;
import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;

/**
 * 
 * @author mschally
 * 
 */
public class KernelRepository implements IKernelRepository {

	// private static String ROOT_NODE = "kh:repository";
	private static String ENTRY_NODE = "kh:entry";
	private static String ENTRY_NODE_TYPE_ATTRIBUTE = "type";
	private static String ENTRY_NODE_DESCRIPTION_ATTRIBUTE = "description";
	private static String KERNEL_NODE = "kh:kernel";
	private static String KERNEL_NODE_NAME_ATTRIBUTE = "name";
	private static String KERNEL_NODE_SRC_ATTRIBUTE = "src";
	private static String KERNEL_NODE_ID_ATTRIBUTE = "id";
	private static String KERNEL_PROPERTY_NODE = "kh:property";
	private static String KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE = "key";
	private static String KERNEL_PROPERTY_NODE_VALUE_ATTRIBUTE = "value";

	private final XMLConfiguration config;
	private URL resource;
	private final RepositoryConfiguration repoConfig;
	private File jarFileLocation;

	public KernelRepository() {
		repoConfig = RepositoryConfiguration.getInstance();
		config = new XMLConfiguration();
	}

	@Override
	public List<IKernelRepositoryEntry> getEntries()
			throws KernelRepositoryException {
		try {
			resource = repoConfig
					.getKernelRepositoryDescriptorFileURL(jarFileLocation);
			config.load(resource);

			final List<IKernelRepositoryEntry> entries = new ArrayList<IKernelRepositoryEntry>();

			final List<ConfigurationNode> entryNodes = config.getRoot()
					.getChildren(ENTRY_NODE);
			for (final ConfigurationNode node : entryNodes) {
				entries.add(getEntryFromConfigurationNode(node));
			}
			return entries;
		} catch (final ConfigurationException e) {
			throw new KernelRepositoryException(e);
		}
	}

	@Override
	public IKernelRepositoryEntry getEntryForGraphNodeType(
			final GraphNodeType type) throws KernelRepositoryException {
		try {
			resource = repoConfig
					.getKernelRepositoryDescriptorFileURL(jarFileLocation);
			config.load(resource);
			final List<ConfigurationNode> entryNodes = config.getRootNode()
					.getChildren(ENTRY_NODE);
			for (final ConfigurationNode node : entryNodes) {
				final List<ConfigurationNode> typeAttrList = node
						.getAttributes(ENTRY_NODE_TYPE_ATTRIBUTE);

				if (typeAttrList.size() > 0) {
					final String val = (String) typeAttrList.get(0).getValue();

					if (type.equals(GraphNodeType.getType(val))) {
						return getEntryFromConfigurationNode(node);
					}
				} else {
					throw new ConfigurationException("KH: no required '"
							+ ENTRY_NODE_TYPE_ATTRIBUTE + "' attribute in "
							+ ENTRY_NODE + " node");
				}
			}
			return null;
		} catch (final ConfigurationException e) {
			throw new KernelRepositoryException(e);
		}
	}

	private IKernelRepositoryEntry getEntryFromConfigurationNode(
			final ConfigurationNode node) throws ConfigurationException {
		final List<ConfigurationNode> typeAttrList = node
				.getAttributes(ENTRY_NODE_TYPE_ATTRIBUTE);
		final List<ConfigurationNode> descAttrList = node
				.getAttributes(ENTRY_NODE_DESCRIPTION_ATTRIBUTE);
		String typeStr;
		String desc = null;

		if (typeAttrList.size() > 0) {
			typeStr = (String) typeAttrList.get(0).getValue();
		} else {
			throw new ConfigurationException("KH: no required '"
					+ ENTRY_NODE_TYPE_ATTRIBUTE + "' attribute in "
					+ ENTRY_NODE + " node");
		}

		if (descAttrList.size() > 0) {
			desc = (String) descAttrList.get(0).getValue();
		}

		final List<IKernelPathEntry> kernelPathEntries = getKernelPathEntries(node);

		return new KernelRepositoryEntry(GraphNodeType.getType(typeStr), desc,
				kernelPathEntries);
	}

	private List<IKernelPathEntry> getKernelPathEntries(
			final ConfigurationNode node) throws ConfigurationException {
		final List<ConfigurationNode> kernelsList = node
				.getChildren(KERNEL_NODE);
		final List<IKernelPathEntry> list = new ArrayList<IKernelPathEntry>(
				kernelsList.size());
		for (final ConfigurationNode kNode : kernelsList) {
			String name;
			URL src;
			String id;

			final List<ConfigurationNode> idAttrList = kNode
					.getAttributes(KERNEL_NODE_ID_ATTRIBUTE);
			final List<ConfigurationNode> nameAttrList = kNode
					.getAttributes(KERNEL_NODE_NAME_ATTRIBUTE);
			final List<ConfigurationNode> srcAttrList = kNode
					.getAttributes(KERNEL_NODE_SRC_ATTRIBUTE);

			if (nameAttrList.size() > 0) {
				name = (String) nameAttrList.get(0).getValue();
			} else {
				throw new ConfigurationException("KH: no required '"
						+ KERNEL_NODE_NAME_ATTRIBUTE + "' attribute in "
						+ KERNEL_NODE + " node");
			}

			if (srcAttrList.size() > 0) {
				src = null;
				// try {
				// src = new URL("jar:" + jarFileLocation.toURI().toURL()
				// + "!/" + ((String) srcAttrList.get(0).getValue()));
				// } catch (final MalformedURLException e) {
				// e.printStackTrace();
				// }
				src = KernelRepository.class.getResource((String) srcAttrList
						.get(0).getValue());
				if (src == null) {
					throw new ConfigurationException("KH: the resource path: '"
							+ srcAttrList.get(0).getValue()
							+ "' cound not be found");
				}
			} else {
				throw new ConfigurationException("KH: no required '"
						+ KERNEL_NODE_SRC_ATTRIBUTE + "' attribute in "
						+ KERNEL_NODE + " node");
			}

			if (idAttrList.size() > 0) {
				id = (String) idAttrList.get(0).getValue();
			} else {
				throw new ConfigurationException("KH: no required '"
						+ KERNEL_NODE_ID_ATTRIBUTE + "' attribute in "
						+ KERNEL_NODE + " node");
			}

			final Map<String, Object> properties = getKernelProperties(kNode);

			list.add(new KernelPathEntry(id, name, src, properties));
		}
		return list;
	}

	private Map<String, Object> getKernelProperties(final ConfigurationNode node)
			throws ConfigurationException {
		final List<ConfigurationNode> propertiesNodeList = node
				.getChildren(KERNEL_PROPERTY_NODE);
		final Map<String, Object> properties = new HashMap<String, Object>(
				propertiesNodeList.size());

		for (final ConfigurationNode pNode : propertiesNodeList) {
			String key;
			Object value;

			final List<ConfigurationNode> keyAttrList = pNode
					.getAttributes(KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE);
			final List<ConfigurationNode> valueAttrList = pNode
					.getAttributes(KERNEL_PROPERTY_NODE_VALUE_ATTRIBUTE);

			if (keyAttrList.size() > 0) {
				key = (String) keyAttrList.get(0).getValue();
			} else {
				throw new ConfigurationException("KH: no required '"
						+ KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE
						+ "' attribute in " + KERNEL_PROPERTY_NODE + " node");
			}

			if (valueAttrList.size() > 0) {
				value = valueAttrList.get(0).getValue();
			} else {
				throw new ConfigurationException("KH: no required '"
						+ KERNEL_PROPERTY_NODE_VALUE_ATTRIBUTE
						+ "' attribute in " + KERNEL_PROPERTY_NODE + " node");
			}

			properties.put(key, value);
		}
		return properties;
	}

	@Override
	public File getJarFileLocation() {
		return jarFileLocation;
	}

	@Override
	public void setJarFileLocation(final File file) {
		jarFileLocation = file;
	}
}
