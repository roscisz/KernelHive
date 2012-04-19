package pl.gda.pg.eti.kernelhive.gui.controller;

import javax.swing.JFrame;

/**
 * Swing implementation for GuiController
 * @see GuiController
 * @author mschally
 * @version 1.0
 * 
 */
public class SwingGuiController implements GuiController {

	private JFrame frame;
	
	public SwingGuiController(){
		frame = null;
	}
	
	public SwingGuiController(JFrame frame){
		this.frame = frame;
	}

	
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	
}
