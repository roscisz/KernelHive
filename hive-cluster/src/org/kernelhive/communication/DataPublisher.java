package org.kernelhive.communication;

import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class DataPublisher implements TCPServerListener {
	private static String commandSeparator = " ";
	private static String successMessage = "OK";
	
	private TCPServer server;
	private Integer prevId = 0;
	private HashMap<Integer, String> data = new HashMap<Integer, String>();
	
	private enum Command {
		PUT, // 0
		GETSIZE, // 1
		GET, // 2
		DELETE	// 3		
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
		data.put(id, entity);		
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
			answer = "No such ID.";
		}
		
		TCPServer.sendMessage(channel, answer);
	}
	
	@Override
	public void onDisconnection(SocketChannel channel) {
		// TODO Auto-generated method stub

	}

	private String processMessage(int command, String params) {		
		switch(Command.values()[command]) {
		case PUT: return putData(params);
		case GETSIZE: return getSize(params);
		case GET: return getData(params);
		case DELETE: return deleteData(params);
		default: return "No such command";
		}		
	}

	private String putData(String params) {
		Integer id = generateId();
		String[] paramsArray = params.split(commandSeparator, 2);
		data.put(id, paramsArray[1]);
		return id.toString();
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
		return successMessage;
	}

}
