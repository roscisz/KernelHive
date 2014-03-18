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
 * <p>Java class for device complex type.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="device">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vendor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="computeUnitsNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="clock" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="globalMemoryBytes" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="localMemoryBytes" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="workGroupSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="busy" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "device", propOrder = {
	"id",
	"name",
	"vendor",
	"computeUnitsNumber",
	"clock",
	"globalMemoryBytes",
	"localMemoryBytes",
	"workGroupSize",
	"busy"
})
public class Device {

	protected int id;
	protected String name;
	protected String vendor;
	protected int computeUnitsNumber;
	protected int clock;
	protected Long globalMemoryBytes;
	protected Long localMemoryBytes;
	protected int workGroupSize;
	protected boolean busy;

	/**
	 * Gets the value of the id property.
	 *
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 *
	 */
	public void setId(int value) {
		this.id = value;
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

	/**
	 * Gets the value of the busy property.
	 *
	 */
	public boolean isBusy() {
		return busy;
	}

	/**
	 * Sets the value of the busy property.
	 *
	 */
	public void setBusy(boolean value) {
		this.busy = value;
	}
}
