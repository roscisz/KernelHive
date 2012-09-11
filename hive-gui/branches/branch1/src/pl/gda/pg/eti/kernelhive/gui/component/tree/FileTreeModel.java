package pl.gda.pg.eti.kernelhive.gui.component.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * 
 * @author mschally
 *
 */
public class FileTreeModel implements TreeModel {

	protected File root;
	protected List<TreeModelListener> treeModelListeners;
	
	
	public FileTreeModel(File root){
		this.root = root;
		treeModelListeners = new ArrayList<TreeModelListener>();
	}
	
	@Override
	public void addTreeModelListener(TreeModelListener tml) {
		treeModelListeners.add(tml);
	}

	@Override
	public Object getChild(Object node, int index) {
		String[] chilren = ((File)node).list();
		if((chilren==null)||(index>=chilren.length)) return null;
		else return new File((File)node, chilren[index]);
	}

	@Override
	public int getChildCount(Object node) {
		String[] children = ((File) node).list();
		if(children==null) return 0;
		else return children.length;
	}

	@Override
	public int getIndexOfChild(Object node, Object child) {
		String[] children = ((File)node).list();
		if(children==null) return -1;
		String childname = ((File)child).getName();
		for(int i=0; i<children.length; i++){
			if(children[i].equalsIgnoreCase(childname)){
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object node) {
		return ((File)node).isFile();
	}

	@Override
	public void removeTreeModelListener(TreeModelListener tml) {
		treeModelListeners.remove(tml);
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
