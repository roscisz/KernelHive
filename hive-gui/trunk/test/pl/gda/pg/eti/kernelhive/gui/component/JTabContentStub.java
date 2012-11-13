package pl.gda.pg.eti.kernelhive.gui.component;

import java.io.File;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class JTabContentStub extends JTabContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3956468341219132050L;

	public JTabContentStub(final MainFrame frame) {
		super(frame);
	}

	@Override
	public boolean saveContent(final File file) {
		return false;
	}

	@Override
	public boolean loadContent(final File file) {
		return false;
	}

	@Override
	public void redoAction() {

	}

	@Override
	public void undoAction() {

	}

	@Override
	public void cut() {

	}

	@Override
	public void copy() {

	}

	@Override
	public void paste() {

	}

	@Override
	public void selectAll() {

	}

	@Override
	public void refresh() {

	}

	@Override
	public boolean saveContent() {
		return false;
	}

	@Override
	public boolean loadContent() {
		return false;
	}

	@Override
	public void clearSelection() {

	}

}
