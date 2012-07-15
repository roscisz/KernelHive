package pl.gda.pg.eti.kernelhive.gui.component.repository.viewer;

import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;


public class RepositoryViewer<E> extends JList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5736151002744175310L;
	
	public RepositoryViewer(){
		super();
	}
	
	public RepositoryViewer(E[] array){
		super(array);
	}
	
	public RepositoryViewer(ListModel<E> model){
		super(model);
	}
	
	public RepositoryViewer(Vector<? extends E> vector){
		super(vector);
	}

}
