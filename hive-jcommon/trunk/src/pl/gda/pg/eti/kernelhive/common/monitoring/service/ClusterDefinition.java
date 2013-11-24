package pl.gda.pg.eti.kernelhive.common.monitoring.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for clusterDefinition complex type.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="clusterDefinition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataPort" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="hostname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="tcpPort" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="udpPort" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clusterDefinition", propOrder = {
	"dataPort",
	"hostname",
	"id",
	"tcpPort",
	"udpPort"
})
public class ClusterDefinition {

	protected int dataPort;
	protected String hostname;
	protected Long id;
	protected int tcpPort;
	protected int udpPort;

	/**
	 * Gets the value of the dataPort property.
	 *
	 */
	public int getDataPort() {
		return dataPort;
	}

	/**
	 * Sets the value of the dataPort property.
	 *
	 */
	public void setDataPort(int value) {
		this.dataPort = value;
	}

	/**
	 * Gets the value of the hostname property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Sets the value of the hostname property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setHostname(String value) {
		this.hostname = value;
	}

	/**
	 * Gets the value of the id property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 *
	 * @param value allowed object is {@link Long }
	 *
	 */
	public void setId(Long value) {
		this.id = value;
	}

	/**
	 * Gets the value of the tcpPort property.
	 *
	 */
	public int getTcpPort() {
		return tcpPort;
	}

	/**
	 * Sets the value of the tcpPort property.
	 *
	 */
	public void setTcpPort(int value) {
		this.tcpPort = value;
	}

	/**
	 * Gets the value of the udpPort property.
	 *
	 */
	public int getUdpPort() {
		return udpPort;
	}

	/**
	 * Sets the value of the udpPort property.
	 *
	 */
	public void setUdpPort(int value) {
		this.udpPort = value;
	}
}
