package pl.gda.pg.eti.kernelhive.gui.configuration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Configuration class (Singleton pattern)
 * 
 * @author mschally
 * 
 */
public class AppConfiguration {

	private static final String AVAILABLE_LANG_PACKS = "available.language.packs";
	private static final String LANGUAGE = "language.bundle";
	private static final String PREVIOUS_INPUT_DATA_URLS = "previous.input.data.urls";

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
				LOG.severe("KH: " + e.getMessage());
				e.printStackTrace();
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
	public void reloadConfiguration() throws ConfigurationException {
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
	 * @param key
	 *            {@link String}
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
}
