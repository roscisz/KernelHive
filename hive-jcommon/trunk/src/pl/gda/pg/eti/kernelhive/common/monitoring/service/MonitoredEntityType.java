package pl.gda.pg.eti.kernelhive.common.monitoring.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for monitoredEntityType.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="monitoredEntityType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CPU_SPEED"/>
 *     &lt;enumeration value="CPU_USAGE"/>
 *     &lt;enumeration value="MEMORY"/>
 *     &lt;enumeration value="GPU_USAGE"/>
 *     &lt;enumeration value="FAN"/>
 *     &lt;enumeration value="GPU_GLOBAL_MEMORY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "monitoredEntityType")
@XmlEnum
public enum MonitoredEntityType {

	CPU_SPEED,
	CPU_USAGE,
	MEMORY,
	GPU_USAGE,
	FAN,
	GPU_GLOBAL_MEMORY;

	public String value() {
		return name();
	}

	public static MonitoredEntityType fromValue(String v) {
		return valueOf(v);
	}
}
