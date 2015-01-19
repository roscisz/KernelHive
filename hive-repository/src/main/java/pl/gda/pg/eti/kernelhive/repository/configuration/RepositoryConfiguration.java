/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
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
