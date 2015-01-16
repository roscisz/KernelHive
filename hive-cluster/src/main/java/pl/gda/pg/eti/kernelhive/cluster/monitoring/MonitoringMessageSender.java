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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.NamingException;

public class MonitoringMessageSender {

	Session session = null;
	Queue queue = null;
	private final static String CONNECTION_FACTORY_NAME = "ConnectionFactory";
	private final static String QUEUE_NAME = "monitoringAMQQueue";
	private final static Logger logger = Logger.getLogger(MonitoringMessageSender.class.getName());

	public MonitoringMessageSender() throws NamingException {
		JNDIHelper jndiHelper = new JNDIHelper(JNDIHelper.getAMQProperties());
		ConnectionFactory connectionFactory = jndiHelper.getResource(CONNECTION_FACTORY_NAME);
		if (connectionFactory == null) {
			logger.log(Level.SEVERE, "Error while fetching connectionFactory");
		} else {
			logger.log(Level.FINE, "Fetched connectionFactory");
			try {
				Connection connection = connectionFactory.createConnection();
				session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			} catch (JMSException ex) {
				Logger.getLogger(MonitoringMessageSender.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		queue = jndiHelper.getResource(QUEUE_NAME);
		if (queue == null) {
			logger.log(Level.SEVERE, "Error while fetching queue");
		} else {
			logger.log(Level.FINE, "Fetched queue");
		}
	}

	public void send(byte[] message) {
		if (session == null) {
			logger.warning("Session is null - sending monitoring message aborted");
		}
		if (queue == null) {
			logger.warning("Queue is null - sending monitoring message aborted");
		}
		try {
			BytesMessage bytesMessage = session.createBytesMessage();
			bytesMessage.writeBytes(message);
			session.createProducer(queue).send(bytesMessage);
		} catch (JMSException ex) {
			Logger.getLogger(MonitoringMessageSender.class.getName()).log(Level.SEVERE, "Message sending error", ex);
		}
		//logger.info("Sent monitoring message");
	}
}
