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
package pl.gda.pg.eti.kernelhive.engine.monitoring;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

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
