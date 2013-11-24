/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author szymon
 */
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
