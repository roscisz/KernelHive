/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.cluster;

import java.nio.channels.SocketChannel;

/**
 *
 * @author szymon
 */
public class ClusterHelpers {

	private ClusterHelpers() {
	}

	public static String getHostName(SocketChannel channel) {
		return channel.socket().getInetAddress().getHostName();
	}
}
