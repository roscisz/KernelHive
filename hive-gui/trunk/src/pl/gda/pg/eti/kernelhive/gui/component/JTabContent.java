package pl.gda.pg.eti.kernelhive.gui.component;

import java.io.File;

import javax.swing.JPanel;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public abstract class JTabContent extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4471004674246615418L;
	private JTabPanel tabPanel;
	private MainFrame frame;
	private boolean dirty;
	
	public JTabContent(MainFrame frame){
		this.frame = frame;
		dirty = false;
	}
	public abstract boolean saveContent(File file);
	public abstract boolean loadContent(File file);
	public abstract void redoAction();
	public abstract void undoAction();
	public abstract void cut();
	public abstract void copy();
	public abstract void paste();
	public abstract void selectAll();
	public abstract void refresh();
	
	public void setTabPanel(JTabPanel panel){
		tabPanel = panel;
	}
	public JTabPanel getTabPanel(){
		return tabPanel;
	}
	
	protected MainFrame getFrame(){
		return frame;
	}
	
	public boolean isDirty(){
		return dirty;
	}
	
	protected void setDirty(boolean dirty){
		this.dirty = dirty;
	}
}