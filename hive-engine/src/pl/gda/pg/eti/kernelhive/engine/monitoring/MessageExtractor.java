/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

/**
 *
 * @author szymon
 */
public class MessageExtractor {

	private final byte[] message;
	private int offset = 0;

	public MessageExtractor(byte[] message) {
		this.message = message;
		/*String tmp = "bytes: ";
		 for(int i = 0; i < Math.min(50, message.length); i++) {
		 tmp += String.format("%02X", message[offset+i]);
		 }
		 Logger.getLogger(getClass().getName()).info(tmp);*/
	}

	public int extractInt(IntegerSize type) {
		ByteBuffer buffer = ByteBuffer.allocate(IntegerSize.INT.getValue());
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		buffer.put(message, offset, type.getValue());
		offset += type.getValue();
		buffer.position(0);
		return buffer.getInt();
	}
}
