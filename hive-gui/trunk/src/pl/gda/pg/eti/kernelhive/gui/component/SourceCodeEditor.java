package pl.gda.pg.eti.kernelhive.gui.component;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

public class SourceCodeEditor extends JTabContent implements KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5474455832346699476L;
	
	public static enum SyntaxStyle{
		CPLUSPLUS(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS),
		C(SyntaxConstants.SYNTAX_STYLE_C),
		XML(SyntaxConstants.SYNTAX_STYLE_XML),
		PROPERTIES(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
		
		private final String style;
		
		SyntaxStyle(String style){
			this.style = style;
		}
		
		public String getStyle(){
			return this.style;
		}
	}
	
	private RSyntaxTextArea textarea = new RSyntaxTextArea();
	private String name;
	
	public SourceCodeEditor(MainFrame frame, String name, SyntaxStyle style){
		super(frame);
		this.name = name;
		setName(name);
		setLayout(new BorderLayout());
		textarea.setSyntaxEditingStyle(style.getStyle());
		textarea.setCodeFoldingEnabled(true);
		textarea.setAnimateBracketMatching(true);
		textarea.setAutoIndentEnabled(true);
		textarea.setBracketMatchingEnabled(true);
		textarea.setAntiAliasingEnabled(true);
		textarea.addKeyListener(this);
		
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
	
	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		setDirty(true);
		getTabPanel().getLabel().setText("*"+name);
	}

	@Override
	public boolean saveContent(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); 
			osw.write(getText());
			osw.flush();
			osw.close();			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}