package pl.gda.pg.eti.kernelhive.common.monitoring.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for getGraphPath complex type.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="getGraphPath">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://monitoring.engine.kernelhive.eti.pg.gda.pl/}monitoredEntity" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getGraphPath", propOrder = {
	"arg0"
})
public class GetGraphPath {

	protected MonitoredEntity arg0;

	/**
	 * Gets the value of the arg0 property.
	 *
	 * @return possible object is {@link MonitoredEntity }
	 *
	 */
	public MonitoredEntity getArg0() {
		return arg0;
	}

	/**
	 * Sets the value of the arg0 property.
	 *
	 * @param value allowed object is {@link MonitoredEntity }
	 *
	 */
	public void setArg0(MonitoredEntity value) {
		this.arg0 = value;
	}
}
