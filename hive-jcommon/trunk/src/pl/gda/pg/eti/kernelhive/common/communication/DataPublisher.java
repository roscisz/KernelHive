package pl.gda.pg.eti.kernelhive.common.communication;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashMap;

public class DataPublisher implements TCPServerListener {
	private TCPServer server;
	private Integer prevId = 0;
	private HashMap<Integer, byte[]> data = new HashMap<Integer, byte[]>();
	
	private enum Command {
		ALLOCATE, // 0		
		GETSIZE, // 1
		GET, // 2
		DELETE,	// 3
		PUT, // 4
	}	

	public DataPublisher(NetworkAddress serverAddress) {

		try {
			server = new TCPServer(serverAddress, this);
		} catch (CommunicationException e) {
			e.printStackTrace();
		}
	}
	
	public int publish(byte[] entity) {
		int id = generateId();
		publish(id, entity);
		return id;
	}
	
	public void publish(int id, byte[] entity) {
		if(data.containsKey(id))
			data.put(id, concatArrays(data.get(id), entity));
		else data.put(id, entity);		
	}

	private byte[] concatArrays(byte[] first, byte[] second) {
		byte[] ret = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, ret, first.length, second.length);
		return ret;
	}

	@Override
	public void onConnection(SocketChannel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTCPMessage(SocketChannel channel, ByteBuffer input) {		
		ByteBuffer output = TCPServer.prepareEmptyBuffer();		
		int command = input.getInt();
		
		System.out.println("Publisher got command: " + command);
		
		String error = null;

		try {
			processMessage(command, input, output, channel);				
		}
		catch(NumberFormatException nfe) {
			error = "Bad command or argument: " + nfe.getLocalizedMessage();
		}
		catch(ArrayIndexOutOfBoundsException ex) {
			error = "No such command: " + ex.getLocalizedMessage();
		}
		catch(NullPointerException npe) {
			error = "No such ID";
		}
		
		if(error == null) {
			if(output.position() > 0)
				TCPServer.sendMessage(channel, output);
		}
		else
			TCPServer.sendMessage(channel, Decoder.encode(error));
		
	}
	
	@Override
	public void onDisconnection(SocketChannel channel) {
		// TODO Auto-generated method stub

	}

	private void processMessage(int command, ByteBuffer input, ByteBuffer output, SocketChannel channel) {	
		switch(Command.values()[command]) {
		case ALLOCATE: allocateData(input, output); break;
		case PUT: putData(input, output); break;
		case GETSIZE: getSize(input, output); break;
		case GET: getData(input, channel); break;
		case DELETE: deleteData(input, output); break;
		}		
	}
	
	private void allocateData(ByteBuffer input, ByteBuffer output) {
		int size = input.getInt();
		output.putInt(generateId());
	}

	private void putData(ByteBuffer input, ByteBuffer output) {		
		int id = input.getInt();
		int size = input.getInt();		
		byte[] entity = new byte[size];
		
		try {
			input.get(entity);			
		}		
		catch(BufferUnderflowException bue) {			
		}
		publish(id, entity);
		
		System.out.println("Bytes sent into the data server: ");
		ByteBuffer buf = ByteBuffer.wrap(entity);
		while(true) {
			try {
				System.out.println("" + buf.get() + " " + buf.get() + " " + buf.get() + " " + buf.get());
			}
			catch(BufferUnderflowException bue) {
				break;
			}
		}
	}
	
	private void getSize(ByteBuffer input, ByteBuffer output) {
		int id = input.getInt();
		int length = data.get(id).length;
		output.putInt(length);
	}

	private Integer generateId() {
		return ++prevId;
	}

	private void getData(ByteBuffer input, SocketChannel channel) {
		int id = input.getInt();
		byte[] entity = data.get(id);
		
		ByteBuffer outputBuffer = TCPServer.prepareEmptyBuffer();
		int entityOffset = 0;

		while((entity.length - entityOffset) >= TCPServer.MAX_MESSAGE_BYTES) {
			outputBuffer.rewind();
			outputBuffer.put(entity, entityOffset, TCPServer.MAX_MESSAGE_BYTES);
			TCPServer.sendMessage(channel, outputBuffer);
			entityOffset += TCPServer.MAX_MESSAGE_BYTES;
		}
		outputBuffer.rewind();
		outputBuffer.put(entity, entityOffset, entity.length - entityOffset);
		TCPServer.sendMessage(channel, outputBuffer);
	}

	private void deleteData(ByteBuffer input, ByteBuffer output) {
		int id = input.getInt();
		data.remove(id);
	}

}
