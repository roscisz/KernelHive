package pl.gda.pg.eti.kernelhive.common.graph.configuration.impl;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.configuration.IEngineGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;

public class EngineGraphConfiguration extends AbstractGraphConfiguration implements IEngineGraphConfiguration {

	public EngineGraphConfiguration(){
		super();
	}
	
	public EngineGraphConfiguration(File file){
		super(file);
	}
	
	@Override
	public List<EngineGraphNodeDecorator> loadGraphForEngine()
			throws ConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EngineGraphNodeDecorator> loadGraphForEngine(File file)
			throws ConfigurationException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
