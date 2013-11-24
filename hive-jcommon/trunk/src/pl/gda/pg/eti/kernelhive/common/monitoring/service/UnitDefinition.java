package pl.gda.pg.eti.kernelhive.common.monitoring.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for unitDefinition complex type.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="unitDefinition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="clusterId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="cpuCores" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="devicesCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="hostname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
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
@XmlType(name = "unitDefinition", propOrder = {
	"clusterId",
	"cpuCores",
	"devicesCount",
	"hostname",
	"id",
	"memorySize",
	"unitId"
})
public class UnitDefinition {

	protected int clusterId;
	protected int cpuCores;
	protected int devicesCount;
	protected String hostname;
	protected Long id;
	protected int memorySize;
	protected int unitId;

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
	 * Gets the value of the devicesCount property.
	 *
	 */
	public int getDevicesCount() {
		return devicesCount;
	}

	/**
	 * Sets the value of the devicesCount property.
	 *
	 */
	public void setDevicesCount(int value) {
		this.devicesCount = value;
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
