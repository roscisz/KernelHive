package pl.gda.pg.eti.kernelhive.gui.source.editor;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class SourceCodeEditor {
	
	public static final String CPLUSPLUS = SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS;
	public static final String C = SyntaxConstants.SYNTAX_STYLE_C;
	public static final String XML = SyntaxConstants.SYNTAX_STYLE_XML;
	public static final String PROPERTIES = SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE;

	public static JPanel createNewEditor(String syntax){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		RSyntaxTextArea textarea = new RSyntaxTextArea();
		textarea.setSyntaxEditingStyle(syntax);
		textarea.setCodeFoldingEnabled(true);
		textarea.setAnimateBracketMatching(true);
		textarea.setAutoIndentEnabled(true);
		textarea.setBracketMatchingEnabled(true);
		textarea.setAntiAliasingEnabled(true);
		RTextScrollPane scrollpane = new RTextScrollPane(textarea);
		scrollpane.setFoldIndicatorEnabled(true);
		panel.add(scrollpane);
		return panel;		
	}
}
