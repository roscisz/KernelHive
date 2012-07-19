package pl.gda.pg.eti.kernelhive.common.configuration;

import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class CommonConfiguration {

	private static Logger LOG = Logger.getLogger(CommonConfiguration.class.getName());
	private static CommonConfiguration _commonConfig = null;

	private PropertiesConfiguration config;

	/**
	 * gets instance
	 * @return {@link AppConfiguration} instance
	 */
	public static CommonConfiguration getInstance(){
		if (_commonConfig == null) {
			try{
				_commonConfig = new CommonConfiguration();
				_commonConfig.reloadConfiguration();
				return _commonConfig;
			} catch(ConfigurationException e){
				LOG.severe("KH: "+e.getMessage());
				e.printStackTrace();
				return null;
			}			
		} else {
			return _commonConfig;
		}
	}
	
	protected CommonConfiguration() {	}

	/**
	 * reload the configuration file
	 * @throws ConfigurationException
	 */
	public void reloadConfiguration() throws ConfigurationException {
		this.config = new PropertiesConfiguration("common.properties");
	}
	
	/**
	 * get the {@link URL} to the Kernel Repository
	 * @return {@link URL}
	 */
	public URL getKernelRepositoryURL(){
		String prop = this.config.getString("kernel.repository.file.path");
		if(prop!=null){
			return CommonConfiguration.class.getResource(prop);
		} else{
			return null;
		}
	}
	
	/**
	 * get the property using the given key
	 * @param key {@link String}
	 * @return {@link String}
	 */
	public String getProperty(String key){
		return this.config.getString(key);
	}
}
