package pl.gda.pg.eti.kernelhive.common.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class DataDownloader {
	
	public static byte[] downloadData(String hostname, int portNumber, int dataID) {
		System.out.println("Downloading data from hostname " + hostname + ", port " + portNumber + ", dataID " + dataID);
		Socket clientSocket;
		try {
			clientSocket = new Socket(hostname, portNumber);
			ByteBuffer outBuffer = TCPServer.prepareEmptyBuffer();
			
			outBuffer.putInt(8);			
			outBuffer.putInt(1);
			outBuffer.putInt(dataID);

			writeBufferToStream(outBuffer, clientSocket.getOutputStream());
			ByteBuffer inBuffer = TCPServer.prepareEmptyBuffer();
			for(int i = 0; i != 4; i++)
				inBuffer.put((byte) clientSocket.getInputStream().read());
			
			inBuffer.rewind();
			int dataSize = inBuffer.getInt();
			
			ByteBuffer outBuffer2 = TCPServer.prepareEmptyBuffer();
			outBuffer2.putInt(8);
			outBuffer2.putInt(2);
			outBuffer2.putInt(dataID);
		
			writeBufferToStream(outBuffer2, clientSocket.getOutputStream());
			
			ByteBuffer inBuffer2 = TCPServer.prepareEmptyBuffer();
			for(int i = 0; i != dataSize; i++)
				inBuffer2.put((byte) clientSocket.getInputStream().read());
			
			byte[] data = new byte[dataSize];
			for(int i = 0; i != dataSize; i++) {
				data[i] = inBuffer2.get();
			}
			
			return data;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static int readLittleEndianInt(InputStream inputStream) throws IOException {
		ByteBuffer inBuffer = TCPServer.prepareEmptyBuffer();		
		byte[] cmdSize = new byte[4];
		inputStream.read(cmdSize);
		inBuffer.put(cmdSize);
		inBuffer.rewind();
		return inBuffer.getInt();
	}

	private static void writeBufferToStream(ByteBuffer buffer, OutputStream stream) throws IOException {
		buffer.flip();
		int i =0;
		while(buffer.hasRemaining())
			stream.write(buffer.get());
	}
	
	public static void main(String[] args) {
		byte[] abc = downloadData("127.0.0.1", 31339, 13);
	}

}
