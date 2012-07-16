package pl.gda.pg.eti.kernelhive.common.kernel.repository.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;

import pl.gda.pg.eti.kernelhive.common.graph.node.GraphNodeType;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.IKernelRepository;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelPathEntry;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;

/**
 * 
 * @author mschally
 *
 */
public class KernelRepository implements IKernelRepository {

	private static String ROOT_NODE = "kh:repository";
	private static String ENTRY_NODE = "kh:entry";
	private static String ENTRY_NODE_TYPE_ATTRIBUTE = "type";
	private static String ENTRY_NODE_DESCRIPTION_ATTRIBUTE = "description";
	private static String KERNEL_NODE = "kh:kernel";
	private static String KERNEL_NODE_NAME_ATTRIBUTE = "name";
	private static String KERNEL_NODE_SRC_ATTRIBUTE = "src";
	private static String KERNEL_PROPERTY_NODE = "kh:property";
	private static String KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE = "key";
	private static String KERNEL_PROPERTY_NODE_VALUE_ATTRIBUTE = "value";
	
	private XMLConfiguration config;
	private URL resource;
	
	public KernelRepository(URL resource) {
		config = new XMLConfiguration();
		this.resource = resource;
	}
	
	@Override
	public List<KernelRepositoryEntry> getEntries()
			throws ConfigurationException {
		config.load(resource);
		
		List<KernelRepositoryEntry> entries = new ArrayList<KernelRepositoryEntry>();
		
		List<ConfigurationNode> entryNodes = config.getRoot().getChildren(ENTRY_NODE);
		for(ConfigurationNode node : entryNodes){
			entries.add(getEntryFromConfigurationNode(node));
		}		
		return entries;
	}

	@Override
	public KernelRepositoryEntry getEntryForGraphNodeType(GraphNodeType type)
			throws ConfigurationException {
		config.load(resource);
		
		List<ConfigurationNode> entryNodes = config.getRootNode().getChildren(ENTRY_NODE);
		for(ConfigurationNode node : entryNodes){
			List<ConfigurationNode> typeAttrList = node.getAttributes(ENTRY_NODE_TYPE_ATTRIBUTE);
			
			if(typeAttrList.size()>0){
				String val = (String) typeAttrList.get(0).getValue();
				
				if(type.equals(GraphNodeType.getType(val))){
					return getEntryFromConfigurationNode(node);
				}
			} else{
				throw new ConfigurationException("KH: no required 'type' attribute in <kh:entry> node");
			}
		}
		
		return null;
	}
	
	private KernelRepositoryEntry getEntryFromConfigurationNode(ConfigurationNode node) throws ConfigurationException{
		List<ConfigurationNode> typeAttrList = node.getAttributes(ENTRY_NODE_TYPE_ATTRIBUTE);
		List<ConfigurationNode> descAttrList = node.getAttributes(ENTRY_NODE_DESCRIPTION_ATTRIBUTE);
		String typeStr;
		String desc = null;
		
		if(typeAttrList.size()>0){
			typeStr = (String) typeAttrList.get(0).getValue();
		} else{
			throw new ConfigurationException("KH: no required 'type' attribute in <kh:entry> node");
		}
		
		if(descAttrList.size()>0){
			desc = (String) descAttrList.get(0).getValue();
		}
		
		List<KernelPathEntry> kernelPathEntries = getKernelPathEntries(node);
		
		return new KernelRepositoryEntry(GraphNodeType.getType(typeStr), desc, kernelPathEntries);
	}
	
	private List<KernelPathEntry> getKernelPathEntries(ConfigurationNode node) throws ConfigurationException{
		List<ConfigurationNode> kernelsList = node.getChildren(KERNEL_NODE);
		List<KernelPathEntry> list = new ArrayList<KernelPathEntry>(kernelsList.size());
		for(ConfigurationNode kNode : kernelsList){
			String name;
			URL src;
			
			List<ConfigurationNode> nameAttrList = kNode.getAttributes(KERNEL_NODE_NAME_ATTRIBUTE);
			List<ConfigurationNode> srcAttrList = kNode.getAttributes(KERNEL_NODE_SRC_ATTRIBUTE);
			
			if(nameAttrList.size()>0){
				name = (String) nameAttrList.get(0).getValue();
			} else{
				throw new ConfigurationException("KH: no required 'name' attribute in <kh:kernel> node");
			}
			
			if(srcAttrList.size()>0){
				src = KernelRepository.class.getResource((String) srcAttrList.get(0).getValue());
				if(src==null){
					throw new ConfigurationException("KH: the resource path: '"+srcAttrList.get(0).getValue()+"' cound not be found");
				}
			} else{
				throw new ConfigurationException("KH: no required 'src' attribute in <kh:kernel> node");
			}
			
			Map<String, String> properties = getKernelProperties(kNode);
			
			list.add(new KernelPathEntry(name, src, properties));
		}
		return list;
	}
	
	private Map<String, String> getKernelProperties(ConfigurationNode node) throws ConfigurationException{
		List<ConfigurationNode> propertiesNodeList = node.getChildren(KERNEL_PROPERTY_NODE);
		Map<String, String> properties = new HashMap<String, String>(propertiesNodeList.size());
		
		for(ConfigurationNode pNode : propertiesNodeList){
			String key, value;
			
			List<ConfigurationNode> keyAttrList = pNode.getAttributes(KERNEL_PROPERTY_NODE_KEY_ATTRIBUTE);
			List<ConfigurationNode> valueAttrList = pNode.getAttributes(KERNEL_PROPERTY_NODE_VALUE_ATTRIBUTE);
			
			if(keyAttrList.size()>0){
				key = (String) keyAttrList.get(0).getValue();
			} else{
				throw new ConfigurationException("KH: no required 'key' attribute in <kh:property> node");
			}
			
			if(valueAttrList.size()>0){
				value = (String) valueAttrList.get(0).getValue();
			} else{
				throw new ConfigurationException("KH: no required 'value' attribute in <kh:property> node");
			}
			
			properties.put(key, value);
		}
		return properties;
	}
}
