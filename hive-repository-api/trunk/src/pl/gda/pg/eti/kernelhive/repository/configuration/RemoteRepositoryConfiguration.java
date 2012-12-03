package pl.gda.pg.eti.kernelhive.repository.configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class RemoteRepositoryConfiguration {

	private static Logger LOG = Logger
			.getLogger(RemoteRepositoryConfiguration.class.getName());
	private static RemoteRepositoryConfiguration _commonConfig = null;

	private PropertiesConfiguration config;

	/**
	 * gets instance
	 * 
	 * @return {@link AppConfiguration} instance
	 */
	public static RemoteRepositoryConfiguration getInstance() {
		if (_commonConfig == null) {
			try {
				_commonConfig = new RemoteRepositoryConfiguration();
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

	protected RemoteRepositoryConfiguration() {
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
	 * 
	 * @return
	 */
	public URL getKernelRepositoryJarURL() throws ConfigurationException {
		final String prop = this.config.getString("kernel.repository.url.path");
		try {
			final URL url = new URL(prop);
			return url;
		} catch (final MalformedURLException e) {
			throw new ConfigurationException(
					"malformed url of 'kernel.repository.url.path'", e);
		}
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
