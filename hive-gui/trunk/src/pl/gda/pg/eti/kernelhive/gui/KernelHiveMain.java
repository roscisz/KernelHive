package pl.gda.pg.eti.kernelhive.gui;

import java.awt.EventQueue;
import java.util.logging.Logger;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

/**
 * Main Class
 * @author marcel
 *
 */
public class KernelHiveMain {

	private static final Logger LOG = Logger.getLogger(KernelHiveMain.class.getName());
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					LOG.severe("KH: "+e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}
}