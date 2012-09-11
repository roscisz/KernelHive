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

/**
 * 
 * @author mschally
 * 
 */
public class SourceCodeEditor extends JTabContent implements DocumentListener {

	private static final long serialVersionUID = 5474455832346699476L;
	private static Logger LOG = Logger.getLogger(SourceCodeEditor.class
			.getName());

	public static enum SyntaxStyle {
		CPLUSPLUS(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS, "cpp"), C(
				SyntaxConstants.SYNTAX_STYLE_C, "c"), XML(
				SyntaxConstants.SYNTAX_STYLE_XML, "xml"), PROPERTIES(
				SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE, "properties"), NONE(
				SyntaxConstants.SYNTAX_STYLE_NONE, ""), OPENCL(
				SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS, "cl");

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
				if (s.getStyle() == mimeType) {
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
	 * gets text
	 * 
	 * @return text
	 */
	public String getText() {
		return textarea.getText();
	}

	/**
	 * sets text
	 * 
	 * @param text
	 *            String
	 */
	public void setText(String text) {
		textarea.setText(text);
	}

	/**
	 * gets file name
	 * 
	 * @return file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * sets file name
	 * 
	 * @param name
	 *            String
	 */
	public void setFileName(String name) {
		this.fileName = name;
	}

	/**
	 * sets syntax style
	 * 
	 * @param style
	 *            {@link SyntaxStyle}
	 */
	public void setSyntaxStyle(SyntaxStyle style) {
		textarea.setSyntaxEditingStyle(style.getStyle());
		textarea.repaint();
	}

	/**
	 * gets syntax style
	 * 
	 * @return {@link SyntaxStyle}
	 */
	public SyntaxStyle getSyntaxStyle() {
		String style = textarea.getSyntaxEditingStyle();
		return SyntaxStyle.getSyntaxStyle(style);
	}

	@Override
	public boolean saveContent(File file) {
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
	public boolean loadContent(File file) {
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
	public void changedUpdate(DocumentEvent arg0) {
		if (getTabPanel() != null) {
			setDirty(true);
			getTabPanel().getLabel().setText("*" + fileName);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		if (getTabPanel() != null) {
			setDirty(true);
			getTabPanel().getLabel().setText("*" + fileName);
		}
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		if (getTabPanel() != null) {
			setDirty(true);
			getTabPanel().getLabel().setText("*" + fileName);
		}
	}

	@Override
	public void redoAction() {
		textarea.redoLastAction();
	}

	@Override
	public void undoAction() {
		textarea.undoLastAction();
	}

	@Override
	public void cut() {
		textarea.cut();
	}

	@Override
	public void copy() {
		textarea.copy();
	}

	@Override
	public void paste() {
		textarea.paste();
	}

	@Override
	public void selectAll() {
		textarea.selectAll();
	}

	@Override
	public void refresh() {
		if (getFile() != null) {
			loadContent(getFile());
		}
		setDirty(false);
		getTabPanel().getLabel().setText(fileName);
	}

	@Override
	public boolean saveContent() {
		return saveContent(getFile());
	}

	@Override
	public boolean loadContent() {
		return loadContent(getFile());
	}
	
	@Override
	public boolean find(String toFind, boolean matchCase, boolean wholeWorld,
			boolean isRegex, boolean searchBack){
		if(toFind.length()==0){
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
	public boolean replace(String toFind, String toReplace, boolean matchCase,
			boolean wholeWorld, boolean isRegex, boolean searchBack) {
		if(toFind.length()==0){
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
	public int replaceAll(String toFind, String toReplace, boolean matchCase,
			boolean wholeWorld, boolean isRegex, boolean searchBack) {
		if(toFind.length()==0){
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
	public void clearSelection() {
		textarea.setSelectionStart(textarea.getCaretPosition());
		textarea.setSelectionEnd(textarea.getCaretPosition());
	}
	
	@Override
	public void goToLine(int line) {
		try {
			textarea.scrollRectToVisible(textarea.modelToView(textarea.getLineStartOffset(line-1)));
			textarea.setCaretPosition(textarea.getLineStartOffset(line-1));
			textarea.setActiveLineRange(line, line);
		} catch (BadLocationException e) {
			LOG.warning("KH: bad line number: "+line);
		}		
	}
}