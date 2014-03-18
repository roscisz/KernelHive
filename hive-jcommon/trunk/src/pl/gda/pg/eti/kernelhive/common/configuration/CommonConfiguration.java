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
