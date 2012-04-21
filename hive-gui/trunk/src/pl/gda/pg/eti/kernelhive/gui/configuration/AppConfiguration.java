package pl.gda.pg.eti.kernelhive.gui.configuration;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Configuration class (Singleton pattern)
 * @author marcel
 *
 */
public class AppConfiguration {

	private static Logger LOG = Logger.getLogger(AppConfiguration.class.getName());
	private static AppConfiguration _appconfig = null;

	private PropertiesConfiguration config;

	public static AppConfiguration getInstance(){
		if (_appconfig == null) {
			try{
				_appconfig = new AppConfiguration();
				_appconfig.reloadConfiguration();
				return _appconfig;
			} catch(ConfigurationException e){
				LOG.severe("KH: "+e.getMessage());
				e.printStackTrace();
				return null;
			}			
		} else {
			return _appconfig;
		}
	}

	public void reloadConfiguration() throws ConfigurationException {
		this.config = new PropertiesConfiguration("config.properties");
	}

	public ResourceBundle getLanguageResourceBundle() {
		String prop = this.config.getString("language.bundle");
		if (prop != null) {
			String[] locale = prop.split("_");
			if (locale.length >= 2) {
				return ResourceBundle.getBundle("messages", new Locale(
						locale[0], locale[1]));
			}
		}
		return null;
	}
}
