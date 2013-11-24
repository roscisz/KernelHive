package pl.gda.pg.eti.kernelhive.common.clusterService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for updateResponse complex type.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="updateResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateResponse", propOrder = {
	"_return"
})
public class UpdateResponse {

	@XmlElement(name = "return")
	protected int _return;

	/**
	 * Gets the value of the return property.
	 *
	 */
	public int getReturn() {
		return _return;
	}

	/**
	 * Sets the value of the return property.
	 *
	 */
	public void setReturn(int value) {
		this._return = value;
	}
}
