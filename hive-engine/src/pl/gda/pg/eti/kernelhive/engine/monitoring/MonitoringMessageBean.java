/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.gda.pg.eti.kernelhive.engine.monitoring.messages.MonitoringMessage;

/**
 *
 * @author szymon
 */
@MessageDriven(mappedName = "monitoringAMQQueue", activationConfig = {
	@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "destination", propertyValue = "monitoringAMQQueue")
})
public class MonitoringMessageBean implements MessageListener {

	private static final int MAX_MSG_SIZE = 1024 * 100; // 1KB
	private static final Logger logger = Logger.getLogger(MonitoringMessageBean.class.getName());

	@Override
	public void onMessage(Message message) {
		//logger.info("Got monitoring message");
		MonitoringStorage storage = new MonitoringStorage();
		try {
			byte[] buff = new byte[MAX_MSG_SIZE];
			((BytesMessage) message).readBytes(buff);
			MonitoringMessage parsedMessage = new MessageParser().parseMessage(buff);
			storage.storeMessage(parsedMessage);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error while processing monitoring message", ex);
		}
	}
}
