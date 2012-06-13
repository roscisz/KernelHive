package pl.gda.pg.eti.kernelhive.gui.component.source;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import pl.gda.pg.eti.kernelhive.gui.component.JTabContent;
import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class SourceCodeEditor extends JTabContent implements DocumentListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5474455832346699476L;
	private static Logger LOG = Logger.getLogger(SourceCodeEditor.class.getName());
	
	public static enum SyntaxStyle{
		CPLUSPLUS(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS, "cpp"),
		C(SyntaxConstants.SYNTAX_STYLE_C, "c"),
		XML(SyntaxConstants.SYNTAX_STYLE_XML, "xml"),
		PROPERTIES(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE, "properties"),
		NONE(SyntaxConstants.SYNTAX_STYLE_NONE, "");
		
		private final String style;
		private final String filetype;
		
		SyntaxStyle(String style, String filetype){
			this.style = style;
			this.filetype = filetype;
		}
		
		public String getStyle(){
			return this.style;
		}
		
		public String getFileType(){
			return this.filetype;
		}
	}
	
	private RSyntaxTextArea textarea;
	private String name;
	
	public SourceCodeEditor(MainFrame frame, String name){
		super(frame);
		this.name = name;
		setName(name);
		setLayout(new BorderLayout());
		textarea = new RSyntaxTextArea();
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
	
	public String getText(){
		return textarea.getText();
	}
	
	public void setText(String text){
		textarea.setText(text);
	}
	
	public String getName(){
		return name;
	}
	
	public void setSyntaxStyle(SyntaxStyle style){
		textarea.setSyntaxEditingStyle(style.getStyle());
		textarea.repaint();
	}
	
	@Override
	public boolean saveContent(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); 
			osw.write(getText());
			osw.flush();
			osw.close();
			name = file.getName();
			getTabPanel().getLabel().setText(name);
			setDirty(false);
			return true;
		} catch (FileNotFoundException e) {
			LOG.warning("KH: file not found: "+file.getPath());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			LOG.warning("KH: unsupported encoding");
			e.printStackTrace();
		} catch (IOException e) {
			LOG.warning("KH: could not save to file: "+file.getPath());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean loadContent(File file) {
		try{
			FileInputStream fis = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(dis));
			StringBuilder sb = new StringBuilder();
			String strLine;
			while((strLine = br.readLine()) !=null){
				sb.append(strLine);
				sb.append("\n");
			}
			if(sb.length()!=0){
				sb.deleteCharAt(sb.length()-1);
			}
			setText(sb.toString());
			dis.close();
			return true;
		} catch(IOException e){
			LOG.warning("KH: could not load specified file: "+file.getPath());
			e.printStackTrace();
		}
		return false;
	}
	
	public static SyntaxStyle resolveSyntaxStyle(String filetype){
		if(filetype.equalsIgnoreCase(SyntaxStyle.C.getFileType())){
			return SyntaxStyle.C;
		} else if(filetype.equalsIgnoreCase(SyntaxStyle.CPLUSPLUS.getFileType())){
			return SyntaxStyle.CPLUSPLUS;
		} else if(filetype.equalsIgnoreCase(SyntaxStyle.XML.getFileType())){
			return SyntaxStyle.XML;
		} else if(filetype.equalsIgnoreCase(SyntaxStyle.PROPERTIES.getFileType())){
			return SyntaxStyle.PROPERTIES;
		} else{
			return SyntaxStyle.NONE;
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		if(getTabPanel()!=null){
			setDirty(true);
			getTabPanel().getLabel().setText("*"+name);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		if(getTabPanel()!=null){
			setDirty(true);
			getTabPanel().getLabel().setText("*"+name);
		}
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		if(getTabPanel()!=null){
			setDirty(true);
			getTabPanel().getLabel().setText("*"+name);
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
}