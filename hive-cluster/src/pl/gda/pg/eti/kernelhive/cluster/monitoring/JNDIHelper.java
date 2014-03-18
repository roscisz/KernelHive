/**
 * Copyright (c) 2014 Gdansk University of Technology
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
package pl.gda.pg.eti.kernelhive.cluster.monitoring;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import pl.gda.pg.eti.kernelhive.cluster.HiveCluster;

public class JNDIHelper {

	InitialContext initialContext = null;

	public JNDIHelper(InitialContext initialContext) {
		if (initialContext == null) {
			throw new IllegalArgumentException("initialContext cannot be null");
		}
		this.initialContext = initialContext;
	}

	public JNDIHelper(Properties jndiProperties) throws NamingException {
		initialContext = new InitialContext(jndiProperties);
	}

	public JNDIHelper() throws NamingException {
		initialContext = new InitialContext();
	}

	public <T> T getResource(String name) {
		try {
			return (T) initialContext.lookup(name);
		} catch (NamingException ex) {
			Logger.getLogger(JNDIHelper.class.getName()).log(Level.SEVERE, "Error while resource lookup", ex);
			return null;
		}
	}

	public static Properties getJMSProperties() {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial",
				"com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs",
				"com.sun.enterprise.naming");
		props.setProperty("java.naming.factory.state",
				"com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");

		// optional.  Defaults to localhost.  Only needed if web server is running 
		// on a different host than the appserver    
		props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");

		// optional.  Defaults to 3700.  Only needed if target orb port is not 3700.
		props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

		return props;
	}

	public static Properties getAMQProperties() {
		Properties props = new Properties();
		props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		props.setProperty(Context.PROVIDER_URL, "tcp://" + HiveCluster.getEngineHostname() + ":61616");
		props.setProperty("queue.monitoringAMQQueue", "monitoringAMQQueue");
		return props;
	}
}
