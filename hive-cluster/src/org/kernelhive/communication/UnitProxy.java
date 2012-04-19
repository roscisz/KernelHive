package org.kernelhive.communication;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.List;

public class UnitProxy {
	private static String commandSeparator = " ";
	private static String deviceSeparator = ";";
	
	private SocketChannel socketChannel;
	private List<Device> devices = new ArrayList<Device>();
	
	public UnitProxy(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public void processMessage(String message) {
		String[] command = message.split(commandSeparator, 2);
		
		if(command[0].equals("UPDATE")) 
			update(command[1]);
		else
			// TODO: error handling
			System.out.println("No such command.");
				
	}

	private void update(String devicesString) {
		String[] devicesArray = devicesString.split(deviceSeparator);
	
		int devicesCount = Integer.parseInt(devicesArray[0]);
		
		devices.clear();
		for(int i = 1; i <= devicesCount; i++)
			devices.add(new Device(devicesArray[i]));		
	}

	@Override
	public String toString() {
		return "UnitProxy [socketChannel=" + socketChannel + ", devices="
				+ devices + "]";
	}		
}
