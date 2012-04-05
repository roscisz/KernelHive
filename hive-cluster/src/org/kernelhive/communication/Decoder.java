package org.kernelhive.communication;

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
	
	public static String decode(ByteBuffer buffer) throws CharacterCodingException {		
		buffer.flip();		
		// FIXME: Setting the charset somewhere
		
		CharsetDecoder decoder = charset.newDecoder();
		CharBuffer charBuffer;
		charBuffer = decoder.decode(buffer);
		return charBuffer.toString();
	}
	
	public static ByteBuffer encode(String message) throws CharacterCodingException {
		return encoder.encode(CharBuffer.wrap(message));		
	}
				
}