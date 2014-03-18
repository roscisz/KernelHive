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
package pl.gda.pg.eti.kernelhive.common.clusterService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for reportPreview complex type.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="reportPreview">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="arg1" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reportPreview", propOrder = {
	"arg0",
	"arg1"
})
public class ReportPreview {

	protected int arg0;
	@XmlElementRef(name = "arg1", type = JAXBElement.class, required = false)
	protected JAXBElement<byte[]> arg1;

	/**
	 * Gets the value of the arg0 property.
	 *
	 */
	public int getArg0() {
		return arg0;
	}

	/**
	 * Sets the value of the arg0 property.
	 *
	 */
	public void setArg0(int value) {
		this.arg0 = value;
	}

	/**
	 * Gets the value of the arg1 property.
	 *
	 * @return possible object is
	 * {@link JAXBElement }{@code <}{@link byte[]}{@code >}
	 *
	 */
	public JAXBElement<byte[]> getArg1() {
		return arg1;
	}

	/**
	 * Sets the value of the arg1 property.
	 *
	 * @param value allowed object is
	 * {@link JAXBElement }{@code <}{@link byte[]}{@code >}
	 *
	 */
	public void setArg1(JAXBElement<byte[]> value) {
		this.arg1 = value;
	}
}
