/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.common.communication;

/**
 *
 * @author szymon
 */
public class UDPMessage {

	byte[] message;

	public UDPMessage(byte[] message) {
		this.message = message;
	}

	public byte[] getBytes() {
		return message;
	}

	public String getString() {
		String text = new String(message);
		text = text.split("\n")[0];
		text = text.split("\r")[0];
		return text;
	}
}
