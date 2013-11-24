package pl.gda.pg.eti.kernelhive.gui.component;

import java.io.File;

import javax.swing.JPanel;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

/**
 *
 * @author mschally
 *
 */
public abstract class JTabContent extends JPanel {

	private static final long serialVersionUID = -4471004674246615418L;
	protected JTabPanel tabPanel;
	protected MainFrame frame;
	protected boolean dirty;
	protected File file;

	public JTabContent(final MainFrame frame) {
		this.frame = frame;
		dirty = false;
	}

	/**
	 * Saves the contents to specified File
	 *
	 * @param file File
	 * @return true on success, false on failure
	 */
	public abstract boolean saveContent(File file);

	/**
	 * saves content
	 *
	 * @return true on success, false on failure
	 */
	public abstract boolean saveContent();

	/**
	 * Loads the content from specified file
	 *
	 * @param file File
	 * @return true on success, false on failure
	 */
	public abstract boolean loadContent(File file);

	/**
	 * loads content
	 *
	 * @return true on success, false on failure
	 */
	public abstract boolean loadContent();

	/**
	 * redo the last action
	 */
	public abstract void redoAction();

	/**
	 * undo the last action
	 */
	public abstract void undoAction();

	/**
	 * cut the selected content
	 */
	public abstract void cut();

	/**
	 * copy the selected content
	 */
	public abstract void copy();

	/**
	 * paste the copied/cut content
	 */
	public abstract void paste();

	/**
	 * select all content
	 */
	public abstract void selectAll();

	public abstract void clearSelection();

	/**
	 * refresh the content
	 */
	public abstract void refresh();

	public boolean find(final String toFind, final boolean matchCase,
			final boolean wholeWord, final boolean isRegex,
			final boolean searchBack) {
		return true;
	}

	public boolean replace(final String toFind, final String toReplace,
			final boolean matchCase, final boolean wholeWord,
			final boolean isRegex, final boolean searchBack) {
		return true;
	}

	public int replaceAll(final String toFind, final String toReplace,
			final boolean matchCase, final boolean wholeWord,
			final boolean isRegex, final boolean searchBack) {
		return 0;
	}

	/**
	 * sets the corresponding JTabPanel
	 *
	 * @param panel JTabPanel
	 */
	public void setTabPanel(final JTabPanel panel) {
		tabPanel = panel;
		tabPanel.setTabContent(this);
	}

	/**
	 * gets the corresponding JTabPanel
	 *
	 * @return
	 */
	public JTabPanel getTabPanel() {
		return tabPanel;
	}

	/**
	 * gets the MainFrame object
	 *
	 * @return MainFrame object
	 */
	protected MainFrame getFrame() {
		return frame;
	}

	/**
	 * checks if the content is dirty (unsaved)
	 *
	 * @return true if the content is dirty, otherwise false
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * sets the content state (if it's dirty)
	 *
	 * @param dirty boolean
	 */
	protected void setDirty(final boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * gets file
	 *
	 * @return File
	 */
	public File getFile() {
		return file;
	}

	/**
	 * sets file
	 *
	 * @param file File
	 */
	public void setFile(final File file) {
		this.file = file;
	}

	public void goToLine(final int line) {
	}

	public void onClose() {
	}
}