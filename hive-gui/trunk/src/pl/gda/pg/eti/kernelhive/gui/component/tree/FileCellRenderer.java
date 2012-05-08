package pl.gda.pg.eti.kernelhive.gui.component.tree;

import java.awt.Component;
import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

public class FileCellRenderer implements TreeCellRenderer {

	protected TreeCellRenderer renderer;

	public FileCellRenderer(TreeCellRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Object root = tree.getModel().getRoot();
		String newvalue;
		if(root.equals(value)){
			newvalue = ((File) value).getPath(); 
		} else{
			newvalue = ((File) value).getName();
		}
		
		return renderer.getTreeCellRendererComponent(tree, newvalue, selected,
				expanded, leaf, row, hasFocus);
	}

}
