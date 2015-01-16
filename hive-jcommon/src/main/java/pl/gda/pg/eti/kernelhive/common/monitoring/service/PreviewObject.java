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
 * <p>Java class for previewObject complex type.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="previewObject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="f1" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="f2" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="f3" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "previewObject", propOrder = {
	"f1",
	"f2",
	"f3"
})
public class PreviewObject {

	protected float f1;
	protected float f2;
	protected float f3;

	/**
	 * Gets the value of the f1 property.
	 *
	 */
	public float getF1() {
		return f1;
	}

	/**
	 * Sets the value of the f1 property.
	 *
	 */
	public void setF1(float value) {
		this.f1 = value;
	}

	/**
	 * Gets the value of the f2 property.
	 *
	 */
	public float getF2() {
		return f2;
	}

	/**
	 * Sets the value of the f2 property.
	 *
	 */
	public void setF2(float value) {
		this.f2 = value;
	}

	/**
	 * Gets the value of the f3 property.
	 *
	 */
	public float getF3() {
		return f3;
	}

	/**
	 * Sets the value of the f3 property.
	 *
	 */
	public void setF3(float value) {
		this.f3 = value;
	}
}
