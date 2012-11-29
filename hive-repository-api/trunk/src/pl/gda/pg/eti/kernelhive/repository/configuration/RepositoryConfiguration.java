package pl.gda.pg.eti.kernelhive.repository.configuration;

import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class RepositoryConfiguration {

	private static Logger LOG = Logger.getLogger(RepositoryConfiguration.class
			.getName());
	private static RepositoryConfiguration _commonConfig = null;

	private PropertiesConfiguration config;

	/**
	 * gets instance
	 * 
	 * @return {@link AppConfiguration} instance
	 */
	public static RepositoryConfiguration getInstance() {
		if (_commonConfig == null) {
			try {
				_commonConfig = new RepositoryConfiguration();
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

	protected RepositoryConfiguration() {
	}

	/**
	 * reload the configuration file
	 * 
	 * @throws ConfigurationException
	 */
	public void reloadConfiguration() throws ConfigurationException {
		this.config = new PropertiesConfiguration("repo.properties");
	}

	/**
	 * get the {@link URL} to the Kernel Repository
	 * 
	 * @return {@link URL}
	 */
	public URL getKernelRepositoryDescriptorFileURL() {
		final String prop = this.config
				.getString("kernel.repository.descriptor.file.path");
		if (prop != null) {
			return RepositoryConfiguration.class.getResource(prop);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getKernelRepositoryJarURL() {
		final String prop = this.config.getString("kernel.repository.url.path");
		return prop;
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
