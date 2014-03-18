/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 * Copyright (c) 2014 Szymon Bultrowicz
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
package pl.gda.pg.eti.kernelhive.gui.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class AppConfiguration {

	private static final String AVAILABLE_LANG_PACKS = "available.language.packs";
	private static final String LANGUAGE = "language.bundle";
	private static final String PREVIOUS_INPUT_DATA_URLS = "previous.input.data.urls";
	private static final String ENGINE_ADDRESS = "engine.address";
	private static Logger LOG = Logger.getLogger(AppConfiguration.class
			.getName());
	private static AppConfiguration _appconfig = null;
	private PropertiesConfiguration config;

	/**
	 * gets instance
	 *
	 * @return {@link AppConfiguration} instance
	 */
	public static AppConfiguration getInstance() {
		if (_appconfig == null) {
			try {
				_appconfig = new AppConfiguration();
				_appconfig.reloadConfiguration();
				return _appconfig;
			} catch (ConfigurationException e) {
				LOG.log(Level.SEVERE, null, e);
				return null;
			} catch (URISyntaxException e) {
				LOG.log(Level.SEVERE, null, e);
				return null;
			}
		} else {
			return _appconfig;
		}
	}

	protected AppConfiguration() {
	}

	/**
	 * reload the configuration file
	 *
	 * @throws ConfigurationException
	 */
	public void reloadConfiguration() throws ConfigurationException, URISyntaxException {
		config = new PropertiesConfiguration("config.properties");
	}

	/**
	 * get the language resource bundle
	 *
	 * @return {@link ResourceBundle}
	 */
	public ResourceBundle getLanguageResourceBundle() {
		String prop = config.getString(LANGUAGE);
		if (prop != null) {
			String[] locale = prop.split("_");
			if (locale.length >= 2) {
				return ResourceBundle.getBundle("messages", new Locale(
						locale[0], locale[1]));
			}
		}
		return null;
	}

	public String getSelectedLanguage() {
		String prop = config.getString(LANGUAGE);
		return prop;
	}

	public void setLanguage(String language) {
		config.setProperty(LANGUAGE, language);
	}

	/**
	 *
	 * @return
	 */
	public List<String> getAvailableLanguageResourceBundles() {
		config.setListDelimiter(',');
		List<Object> languages = config.getList(AVAILABLE_LANG_PACKS);
		List<String> result = new ArrayList<String>();
		for (Object o : languages) {
			result.add((String) o);
		}
		return result;
	}

	public List<String> getPreviousInputDataURLs() {
		config.setListDelimiter(',');
		List<Object> urls = config.getList(PREVIOUS_INPUT_DATA_URLS);
		List<String> result = new ArrayList<String>();
		for (Object o : urls) {
			if (!((String) o).equalsIgnoreCase("")) {
				result.add((String) o);
			}
		}
		return result;
	}

	/**
	 * get the {@link URL} to the Kernel Repository
	 *
	 * @return {@link URL}
	 */
	public URL getKernelRepositoryURL() {
		String prop = config.getString("kernel.repository.file.path");
		if (prop != null) {
			return AppConfiguration.class.getResource(prop);
		} else {
			return null;
		}
	}

	/**
	 * get the property using the given key
	 *
	 * @param key {@link String}
	 * @return {@link String}
	 */
	public String getProperty(String key) {
		return this.config.getString(key);
	}

	public void addURLToPreviousURLs(String url) {
		boolean isNew = true;
		config.setListDelimiter(',');
		List<Object> list = config.getList(PREVIOUS_INPUT_DATA_URLS);
		List<String> urls = new ArrayList<String>();
		for (Object o : list) {
			urls.add((String) o);
		}
		for (String u : urls) {
			if (u.equalsIgnoreCase(url)) {
				isNew = false;
				break;
			}
		}
		if (isNew) {
			list.add(url);
			config.clearProperty(PREVIOUS_INPUT_DATA_URLS);
			config.addProperty(PREVIOUS_INPUT_DATA_URLS, list);
			try {
				config.save();
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	public URL getEngineAddress() {
		String address = (String) config.getProperty(ENGINE_ADDRESS);
		if (address != null) {
			try {
				return new URL(address);
			} catch (MalformedURLException ex) {
				Logger.getLogger(AppConfiguration.class.getName()).log(Level.SEVERE, null, ex);
				return null;
			}
		} else {
			return null;
		}
	}

	public void setEngineAddress(String address) {
		config.setProperty(ENGINE_ADDRESS, address);
	}
}
