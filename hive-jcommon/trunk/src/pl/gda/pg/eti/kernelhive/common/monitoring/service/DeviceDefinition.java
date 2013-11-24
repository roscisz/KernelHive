package pl.gda.pg.eti.kernelhive.common.monitoring.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for deviceDefinition complex type.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="deviceDefinition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="clock" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="clusterId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="computeUnitsNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="deviceId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="globalMemoryBytes" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="localMemoryBytes" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unitId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="vendor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="workGroupSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deviceDefinition", propOrder = {
	"clock",
	"clusterId",
	"computeUnitsNumber",
	"deviceId",
	"globalMemoryBytes",
	"id",
	"localMemoryBytes",
	"name",
	"unitId",
	"vendor",
	"workGroupSize"
})
public class DeviceDefinition {

	protected int clock;
	protected int clusterId;
	protected int computeUnitsNumber;
	protected int deviceId;
	protected Long globalMemoryBytes;
	protected Long id;
	protected Long localMemoryBytes;
	protected String name;
	protected int unitId;
	protected String vendor;
	protected int workGroupSize;

	/**
	 * Gets the value of the clock property.
	 *
	 */
	public int getClock() {
		return clock;
	}

	/**
	 * Sets the value of the clock property.
	 *
	 */
	public void setClock(int value) {
		this.clock = value;
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
	 * Gets the value of the computeUnitsNumber property.
	 *
	 */
	public int getComputeUnitsNumber() {
		return computeUnitsNumber;
	}

	/**
	 * Sets the value of the computeUnitsNumber property.
	 *
	 */
	public void setComputeUnitsNumber(int value) {
		this.computeUnitsNumber = value;
	}

	/**
	 * Gets the value of the deviceId property.
	 *
	 */
	public int getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the value of the deviceId property.
	 *
	 */
	public void setDeviceId(int value) {
		this.deviceId = value;
	}

	/**
	 * Gets the value of the globalMemoryBytes property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public Long getGlobalMemoryBytes() {
		return globalMemoryBytes;
	}

	/**
	 * Sets the value of the globalMemoryBytes property.
	 *
	 * @param value allowed object is {@link Long }
	 *
	 */
	public void setGlobalMemoryBytes(Long value) {
		this.globalMemoryBytes = value;
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
	 * Gets the value of the localMemoryBytes property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public Long getLocalMemoryBytes() {
		return localMemoryBytes;
	}

	/**
	 * Sets the value of the localMemoryBytes property.
	 *
	 * @param value allowed object is {@link Long }
	 *
	 */
	public void setLocalMemoryBytes(Long value) {
		this.localMemoryBytes = value;
	}

	/**
	 * Gets the value of the name property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setName(String value) {
		this.name = value;
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

	/**
	 * Gets the value of the vendor property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * Sets the value of the vendor property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setVendor(String value) {
		this.vendor = value;
	}

	/**
	 * Gets the value of the workGroupSize property.
	 *
	 */
	public int getWorkGroupSize() {
		return workGroupSize;
	}

	/**
	 * Sets the value of the workGroupSize property.
	 *
	 */
	public void setWorkGroupSize(int value) {
		this.workGroupSize = value;
	}
}
