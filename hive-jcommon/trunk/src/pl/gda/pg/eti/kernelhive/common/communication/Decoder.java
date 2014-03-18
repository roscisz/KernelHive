/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
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
package pl.gda.pg.eti.kernelhive.common.communication;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class Decoder {
	
	private static Charset charset = Charset.forName("UTF-8");
	private static CharsetDecoder decoder = charset.newDecoder();
	private static CharsetEncoder encoder = charset.newEncoder();
	
	public static String decode(ByteBuffer buffer) {		
		//buffer.flip();		
		// FIXME: Setting the charset somewhere
		
		CharsetDecoder decoder = charset.newDecoder();
		CharBuffer charBuffer;
		try {
			charBuffer = decoder.decode(buffer);
			String message = charBuffer.toString();
			message = message.split("\n")[0];
			message = message.split("\r")[0];
			return message;
		}
		catch(CharacterCodingException cce) {
			cce.printStackTrace();
		}
		
		return "";
	}
	
	public static ByteBuffer encode(String message) {
		try {
			ByteBuffer ret = encoder.encode(CharBuffer.wrap(message));
			ret.position(ret.limit());
			return ret;
		}
		catch(CharacterCodingException cce) {
			cce.printStackTrace();
			return null;
		}
	}
				
}
