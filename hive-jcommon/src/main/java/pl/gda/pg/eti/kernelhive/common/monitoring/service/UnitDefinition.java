/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Szymon Bultrowicz
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
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
