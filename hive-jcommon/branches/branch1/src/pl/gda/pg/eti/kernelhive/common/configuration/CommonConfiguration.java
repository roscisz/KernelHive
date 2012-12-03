package pl.gda.pg.eti.kernelhive.common.configuration;

import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class CommonConfiguration {

	private static Logger LOG = Logger.getLogger(CommonConfiguration.class
			.getName());
	private static CommonConfiguration _commonConfig = null;

	private PropertiesConfiguration config;

	/**
	 * gets instance
	 * 
	 * @return {@link AppConfiguration} instance
	 */
	public static CommonConfiguration getInstance() {
		if (_commonConfig == null) {
			try {
				_commonConfig = new CommonConfiguration();
				_commonConfig.reloadConfiguration();
				return _commonConfig;
			} catch (final ConfigurationException e) {
				LOG.severe("KH: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		} else {
			return _commonConfig;
		}
	}

	protected CommonConfiguration() {
	}

	/**
	 * reload the configuration file
	 * 
	 * @throws ConfigurationException
	 */
	public void reloadConfiguration() throws ConfigurationException {
		this.config = new PropertiesConfiguration("common.properties");
	}

	/**
	 * get the property using the given key
	 * 
	 * @param key
	 *            {@link String}
	 * @return {@link String}
	 */
	public String getProperty(final String key) {
		return this.config.getString(key);
	}
}
