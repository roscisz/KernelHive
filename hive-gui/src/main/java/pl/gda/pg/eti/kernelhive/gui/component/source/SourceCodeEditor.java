/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.gui.component.source;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class SourceCodeEditor extends JTabContent implements DocumentListener {

	private static final long serialVersionUID = 5474455832346699476L;
	private static Logger LOG = Logger.getLogger(SourceCodeEditor.class
			.getName());

	/**
	 *
	 * @author marcel
	 *
	 */
	public static enum SyntaxStyle {

		CPLUSPLUS(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS, "cpp"),
		C(SyntaxConstants.SYNTAX_STYLE_C, "c"),
		XML(SyntaxConstants.SYNTAX_STYLE_XML, "xml"),
		PROPERTIES(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE, "properties"),
		NONE(SyntaxConstants.SYNTAX_STYLE_NONE, ""),
		OPENCL(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS, "cl"),
		JAVA(SyntaxConstants.SYNTAX_STYLE_JAVA, "java");
		private final String style;
		private final String filetype;

		SyntaxStyle(String style, String filetype) {
			this.style = style;
			this.filetype = filetype;
		}

		public String getStyle() {
			return this.style;
		}

		public String getFileType() {
			return this.filetype;
		}

		public static SyntaxStyle getSyntaxStyle(String mimeType) {
			SyntaxStyle[] styles = SyntaxStyle.values();
			for (SyntaxStyle s : styles) {
				if (s.getStyle().equals(mimeType)) {
					return s;
				}
			}
			return null;
		}

		public static SyntaxStyle resolveSyntaxStyle(String filetype) {
			SyntaxStyle[] styles = SyntaxStyle.values();
			for (SyntaxStyle s : styles) {
				if (s.getFileType().equalsIgnoreCase(filetype)) {
					return s;
				}
			}
			return SyntaxStyle.NONE;
		}
	}
	private TextEditorPane textarea;
	private String fileName;

	/**
	 *
	 * @param frame
	 * @param name
	 */
	public SourceCodeEditor(MainFrame frame, String name) {
		super(frame);
		this.fileName = name;
		setName(name);
		setLayout(new BorderLayout());
		textarea = new TextEditorPane();
		textarea.setCodeFoldingEnabled(true);
		textarea.setAnimateBracketMatching(true);
		textarea.setAutoIndentEnabled(true);
		textarea.setBracketMatchingEnabled(true);
		textarea.setAntiAliasingEnabled(true);
		textarea.getDocument().addDocumentListener(this);

		RTextScrollPane scrollpane = new RTextScrollPane(textarea);
		scrollpane.setFoldIndicatorEnabled(true);
		add(scrollpane);
	}

	/**
	 * gets text.
	 *
	 * @return text
	 */
	public final String getText() {
		return textarea.getText();
	}

	/**
	 * sets text
	 *
	 * @param text String
	 */
	public final void setText(String text) {
		textarea.setText(text);
	}

	/**
	 * gets file name
	 *
	 * @return file name
	 */
	public final String getFileName() {
		return fileName;
	}

	/**
	 * sets file name
	 *
	 * @param name String
	 */
	public final void setFileName(String name) {
		this.fileName = name;
	}

	/**
	 * sets syntax style
	 *
	 * @param style {@link SyntaxStyle}
	 */
	public final void setSyntaxStyle(SyntaxStyle style) {
		textarea.setSyntaxEditingStyle(style.getStyle());
		textarea.repaint();
	}

	/**
	 * gets syntax style
	 *
	 * @return {@link SyntaxStyle}
	 */
	public final SyntaxStyle getSyntaxStyle() {
		String style = textarea.getSyntaxEditingStyle();
		return SyntaxStyle.getSyntaxStyle(style);
	}

	@Override
	public final boolean saveContent(final File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(getText());
			osw.flush();
			osw.close();
			fileName = file.getName();

			try {
				getTabPanel().getLabel().setText(fileName);
			} catch (NullPointerException e) {
				LOG.warning("KH: no TabPanel object associated!");
				e.printStackTrace();
				return false;
			}

			setDirty(false);
			return true;
		} catch (FileNotFoundException e) {
			LOG.warning("KH: file not found: " + file.getPath());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			LOG.warning("KH: unsupported encoding");
			e.printStackTrace();
		} catch (IOException e) {
			LOG.warning("KH: could not save to file: " + file.getPath());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public final boolean loadContent(final File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			StringBuilder sb = new StringBuilder();
			String strLine;
			while ((strLine = br.readLine()) != null) {
				sb.append(strLine);
				sb.append("\n");
			}
			if (sb.length() != 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			setText(sb.toString());
			dis.close();
			return true;
		} catch (IOException e) {
			LOG.warning("KH: could not load specified file: " + file.getPath());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public final void changedUpdate(final DocumentEvent arg0) {
		if (getTabPanel() != null) {
			setDirty(true);
			getTabPanel().getLabel().setText("*" + fileName);
		}
	}

	@Override
	public final void insertUpdate(final DocumentEvent arg0) {
		if (getTabPanel() != null) {
			setDirty(true);
			getTabPanel().getLabel().setText("*" + fileName);
		}
	}

	@Override
	public final void removeUpdate(final DocumentEvent arg0) {
		if (getTabPanel() != null) {
			setDirty(true);
			getTabPanel().getLabel().setText("*" + fileName);
		}
	}

	@Override
	public final void redoAction() {
		textarea.redoLastAction();
	}

	@Override
	public final void undoAction() {
		textarea.undoLastAction();
	}

	@Override
	public final void cut() {
		textarea.cut();
	}

	@Override
	public final void copy() {
		textarea.copy();
	}

	@Override
	public final void paste() {
		textarea.paste();
	}

	@Override
	public final void selectAll() {
		textarea.selectAll();
	}

	@Override
	public final void refresh() {
		if (getFile() != null) {
			loadContent(getFile());
		}
		setDirty(false);
		getTabPanel().getLabel().setText(fileName);
	}

	@Override
	public final boolean saveContent() {
		return saveContent(getFile());
	}

	@Override
	public final boolean loadContent() {
		return loadContent(getFile());
	}

	@Override
	public final boolean find(String toFind, boolean matchCase,
			boolean wholeWorld, boolean isRegex, boolean searchBack) {
		if (toFind.length() == 0) {
			return false;
		}
		SearchContext context = new SearchContext();
		context.setSearchFor(toFind);
		context.setMatchCase(matchCase);
		context.setRegularExpression(isRegex);
		context.setWholeWord(wholeWorld);
		context.setSearchForward(!searchBack);

		return SearchEngine.find(textarea, context);
	}

	@Override
	public final boolean replace(String toFind, String toReplace,
			boolean matchCase, boolean wholeWorld, boolean isRegex,
			boolean searchBack) {
		if (toFind.length() == 0) {
			return false;
		}
		SearchContext context = new SearchContext();
		context.setSearchFor(toFind);
		context.setReplaceWith(toReplace);
		context.setMatchCase(matchCase);
		context.setRegularExpression(isRegex);
		context.setWholeWord(wholeWorld);
		context.setSearchForward(!searchBack);

		return SearchEngine.replace(textarea, context);
	}

	@Override
	public final int replaceAll(String toFind, String toReplace,
			boolean matchCase, boolean wholeWorld, boolean isRegex,
			boolean searchBack) {
		if (toFind.length() == 0) {
			return -1;
		}
		SearchContext context = new SearchContext();
		context.setSearchFor(toFind);
		context.setReplaceWith(toReplace);
		context.setMatchCase(matchCase);
		context.setRegularExpression(isRegex);
		context.setWholeWord(wholeWorld);
		context.setSearchForward(!searchBack);

		return SearchEngine.replaceAll(textarea, context);
	}

	@Override
	public final void clearSelection() {
		textarea.setSelectionStart(textarea.getCaretPosition());
		textarea.setSelectionEnd(textarea.getCaretPosition());
	}

	@Override
	public final void goToLine(final int line) {
		try {
			textarea.scrollRectToVisible(textarea.modelToView(textarea
					.getLineStartOffset(line - 1)));
			textarea.setCaretPosition(textarea.getLineStartOffset(line - 1));
			textarea.setActiveLineRange(line, line);
		} catch (BadLocationException e) {
			LOG.warning("KH: bad line number: " + line);
		}
	}
}
