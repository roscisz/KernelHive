/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring.messages;

import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoringMessageType;

/**
 *
 * @author szymon
 */
public interface MonitoringMessage {

	MonitoringMessageType getType();

	int getUnitId();

	int getClusterId();
}
