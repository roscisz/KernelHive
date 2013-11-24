/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring;

/**
 *
 * @author szymon
 */
public enum MonitoringMessageType {

	INITIAL(0),
	SEQUENTIAL(1);
	private int value;

	private MonitoringMessageType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static MonitoringMessageType fromValue(int value) {
		for (MonitoringMessageType type : MonitoringMessageType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return null;
	}
}
