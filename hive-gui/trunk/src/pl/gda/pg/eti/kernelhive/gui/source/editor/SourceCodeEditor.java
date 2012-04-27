package pl.gda.pg.eti.kernelhive.gui.source.editor;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class SourceCodeEditor {

	public static JPanel createNewEditor(){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		RSyntaxTextArea textarea = new RSyntaxTextArea();
		textarea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
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
