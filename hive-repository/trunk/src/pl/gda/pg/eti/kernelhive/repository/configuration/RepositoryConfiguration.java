package pl.gda.pg.eti.kernelhive.repository.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class RepositoryConfiguration {

	// private static final Logger LOG = Logger
	// .getLogger(RepositoryConfiguration.class.getName());
	private static RepositoryConfiguration _config;
	private static boolean isInitialized = false;
	private PropertiesConfiguration propConfig;

	public static RepositoryConfiguration getInstance() {
		if (!isInitialized) {
			_config = new RepositoryConfiguration();
			isInitialized = true;
		}
		return _config;
	}

	protected RepositoryConfiguration() {
	}

	private void reloadConfiguration(final File jarFileLocation)
			throws ConfigurationException {
		try {
			propConfig = new PropertiesConfiguration("jar:"
					+ jarFileLocation.toURI().toURL() + "!/"
					+ "repository.properties");
		} catch (final MalformedURLException e) {
			throw new ConfigurationException(e);
		}
	}

	/**
	 * get the {@link URL} to the Kernel Repository
	 * 
	 * @return {@link URL}
	 * @throws ConfigurationException
	 */
	public URL getKernelRepositoryDescriptorFileURL(final File jarFileLocation)
			throws ConfigurationException {
		reloadConfiguration(jarFileLocation);
		final String prop = this.propConfig
				.getString("kernel.repository.descriptor.file.path");
		if (prop != null) {
			return RepositoryConfiguration.class.getResource(prop);
		} else {
			return null;
		}
	}

}
