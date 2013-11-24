/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.cluster;

import java.util.Arrays;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBean;
import pl.gda.pg.eti.kernelhive.common.communication.CommunicationException;
import pl.gda.pg.eti.kernelhive.common.communication.UDPMessage;
import pl.gda.pg.eti.kernelhive.common.communication.UDPServer;
import pl.gda.pg.eti.kernelhive.common.communication.UDPServerListener;

/**
 *
 * @author szymon
 */
public class RunnerServer implements UDPServerListener {

	private int port;
	private UDPServer udpServer;
	private ClusterBean clusterBean;
	private static final Logger LOGGER = Logger.getLogger(RunnerServer.class.getName());

	public RunnerServer(int port, ClusterBean clusterBean) {
		this.port = port;
		this.clusterBean = clusterBean;
		udpServer = new UDPServer(port, this);
	}

	public void start() throws CommunicationException {
		udpServer.start();
	}

	public void stop() {
		udpServer.stop();
	}

	@Override
	public void onUDPMessage(UDPMessage message) {
		String[] report = message.getString().split(" ", 4);
		Command command = Command.fromValue(Integer.parseInt(report[0]));
		final int jobId = Integer.parseInt(report[1]);

		LOGGER.info(String.format("UDP message: %s", message.getString()));

		switch (command) {
			case PROGRESS:
				final int percent = Integer.parseInt(report[2]);
				new Thread() {
					@Override
					public void run() {
						LOGGER.info(String.format("Job %d progress: %d%%", jobId, percent));
						clusterBean.reportProgress(jobId, percent);
					}
				}.start();
				break;
			case PREVIEW:
				int size = Integer.parseInt(report[2]);
				int spaceToOmit = 3;
				int offset = 0;
				for (byte b : message.getBytes()) {
					if (b == 0x20) {
						spaceToOmit--;
					}
					offset++;
					if (spaceToOmit == 0) {
						break;
					}
				}
				final byte[] buffer = Arrays.copyOfRange(message.getBytes(), offset, size);
				/*StringBuilder sb = new StringBuilder(String.format("Preview buffer of size %d: \n", size));
				 for(int i = 0; i < buffer.length; i++) {
				 sb.append(String.format("%02X ", buffer[i]));
				 }
				 Loger.getLogger(getClass().getName()).info(sb.toString());*/

				new Thread() {
					@Override
					public void run() {
						LOGGER.info(String.format("Sending job preview for job %d", jobId));
						clusterBean.reportPreview(jobId, buffer);
					}
				}.start();
				break;
		}


		// TODO: Update progress in Job representation
		//System.out.println("JobID " + id + ": " + percent + "% done.");
	}

	enum Command {

		PROGRESS(1),
		PREVIEW(2);
		private final int value;

		Command(int value) {
			this.value = value;
		}

		public static Command fromValue(int value) {
			switch (value) {
				case 1:
					return PROGRESS;
				case 2:
					return PREVIEW;
				default:
					return null;
			}
		}
	}
}
