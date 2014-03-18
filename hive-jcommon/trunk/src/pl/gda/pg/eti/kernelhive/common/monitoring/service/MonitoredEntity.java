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
 * <p>Java class for monitoredEntity complex type.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="monitoredEntity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="clusterId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="deviceId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="type" type="{http://monitoring.engine.kernelhive.eti.pg.gda.pl/}monitoredEntityType" minOccurs="0"/>
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
@XmlType(name = "monitoredEntity", propOrder = {
	"clusterId",
	"deviceId",
	"id",
	"type",
	"unitId"
})
public class MonitoredEntity {

	protected int clusterId;
	protected Integer deviceId;
	protected int id;
	protected MonitoredEntityType type;
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
	 * Gets the value of the deviceId property.
	 *
	 * @return possible object is {@link Integer }
	 *
	 */
	public Integer getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the value of the deviceId property.
	 *
	 * @param value allowed object is {@link Integer }
	 *
	 */
	public void setDeviceId(Integer value) {
		this.deviceId = value;
	}

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
	 * Gets the value of the type property.
	 *
	 * @return possible object is {@link MonitoredEntityType }
	 *
	 */
	public MonitoredEntityType getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 *
	 * @param value allowed object is {@link MonitoredEntityType }
	 *
	 */
	public void setType(MonitoredEntityType value) {
		this.type = value;
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
