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
