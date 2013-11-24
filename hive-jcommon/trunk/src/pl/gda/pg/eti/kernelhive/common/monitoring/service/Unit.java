package pl.gda.pg.eti.kernelhive.common.monitoring.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for unit complex type.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="unit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="devices" type="{http://monitoring.engine.kernelhive.eti.pg.gda.pl/}device" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="clusterId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="connected" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="cpuCores" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="hostname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="memorySize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="unitId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unit", propOrder = {
	"devices",
	"clusterId",
	"connected",
	"cpuCores",
	"hostname",
	"memorySize",
	"unitId"
})
public class Unit {

	@XmlElement(nillable = true)
	protected List<Device> devices;
	protected int clusterId;
	protected boolean connected;
	protected int cpuCores;
	protected String hostname;
	protected int memorySize;
	protected int unitId;

	/**
	 * Gets the value of the devices property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the devices property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getDevices().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Device }
	 *
	 *
	 */
	public List<Device> getDevices() {
		if (devices == null) {
			devices = new ArrayList<Device>();
		}
		return this.devices;
	}

	/**
	 * Gets the value of the clusterId property.
	 *
	 */
	public int getClusterId() {
		return clusterId;
	}

	/**
	 * Sets the value of the clusterId property.
	 *
	 */
	public void setClusterId(int value) {
		this.clusterId = value;
	}

	/**
	 * Gets the value of the connected property.
	 *
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Sets the value of the connected property.
	 *
	 */
	public void setConnected(boolean value) {
		this.connected = value;
	}

	/**
	 * Gets the value of the cpuCores property.
	 *
	 */
	public int getCpuCores() {
		return cpuCores;
	}

	/**
	 * Sets the value of the cpuCores property.
	 *
	 */
	public void setCpuCores(int value) {
		this.cpuCores = value;
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
	 * Gets the value of the memorySize property.
	 *
	 */
	public int getMemorySize() {
		return memorySize;
	}

	/**
	 * Sets the value of the memorySize property.
	 *
	 */
	public void setMemorySize(int value) {
		this.memorySize = value;
	}

	/**
	 * Gets the value of the unitId property.
	 *
	 */
	public int getUnitId() {
		return unitId;
	}

	/**
	 * Sets the value of the unitId property.
	 *
	 */
	public void setUnitId(int value) {
		this.unitId = value;
	}
}
