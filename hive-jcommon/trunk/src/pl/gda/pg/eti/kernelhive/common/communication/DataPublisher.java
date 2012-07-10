package pl.gda.pg.eti.kernelhive.common.communication;

import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class DataPublisher implements TCPServerListener {
	private static String commandSeparator = " ";
	
	private TCPServer server;
	private Integer prevId = 0;
	private HashMap<Integer, String> data = new HashMap<Integer, String>();
	
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
	
	public int publish(String entity) {
		int id = generateId();
		publish(id, entity);
		return id;
	}
	
	public void publish(int id, String entity) {
		if(data.containsKey(id))
			data.put(id, data.get(id) + entity);
		else data.put(id, entity);		
	}

	@Override
	public void onConnection(SocketChannel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTCPMessage(SocketChannel channel, String message) {
		System.out.println("Publisher got message: " + message);
		message = message.split("\r")[0];
		message = message.split("\n")[0];
		String[] command = message.split(commandSeparator, 2);
		
		String answer = "UNDEFINED ERROR";

		try {
			answer = processMessage(Integer.parseInt(command[0]), command[1]);				
		}
		catch(NumberFormatException nfe) {
			answer = "Bad command or argument: " + nfe.getLocalizedMessage();
		}
		catch(ArrayIndexOutOfBoundsException ex) {
			answer = "No such command: " + ex.getLocalizedMessage();
		}
		catch(NullPointerException npe) {
			answer = "No such ID";
		}
		
		TCPServer.sendMessage(channel, answer);
	}
	
	@Override
	public void onDisconnection(SocketChannel channel) {
		// TODO Auto-generated method stub

	}

	private String processMessage(int command, String params) {		
		switch(Command.values()[command]) {
		case ALLOCATE: return allocateData(params);
		case PUT: return putData(params);
		case GETSIZE: return getSize(params);
		case GET: return getData(params);
		case DELETE: return deleteData(params);
		default: return "No such command";
		}		
	}
	
	private String allocateData(String params) {
		return generateId() + "";
	}

	private String putData(String params) {
		String[] paramsArray = params.split(commandSeparator, 3);
		int id = Integer.parseInt(paramsArray[0]);
		publish(id, paramsArray[2]);
		return "OK";
	}
	
	private String getSize(String params) {
		Integer id = Integer.parseInt(params);
		return data.get(id).getBytes().length + "";
	}

	private Integer generateId() {
		return ++prevId;
	}

	private String getData(String params) {
		Integer index = Integer.parseInt(params);
		return data.get(index);
	}

	private String deleteData(String params) {
		Integer index = Integer.parseInt(params);
		data.remove(index);
		return "OK";
	}

}
