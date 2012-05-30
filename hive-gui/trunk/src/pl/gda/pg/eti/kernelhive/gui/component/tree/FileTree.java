package pl.gda.pg.eti.kernelhive.gui.component.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import pl.gda.pg.eti.kernelhive.gui.frame.MainFrame;

//TODO i18n
public class FileTree extends JTree implements ActionListener {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8862701840838830716L;
	private static Logger LOG = Logger.getLogger(FileTree.class.getName());
	
	private JPopupMenu dirPopup;
	private JPopupMenu filePopup;
	private JPopupMenu blankPopup;
	private final MainFrame frame;
	
	public FileTree(final MainFrame frame, TreeModel model){
		super(model);
		this.frame = frame;
		setupBlankPopupMenu();
		setupDirectoryPopupMenu();
		setupFilePopupMenu();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e){
				if(e.getButton()==3){//PPM
					TreePath tr = getPathForLocation(e.getX(), e.getY());
					if(tr==null){//no node selected
						blankPopup.show((JComponent)e.getSource(), e.getX(), e.getY());
					} else{
						File f = (File)tr.getLastPathComponent();
						if(f.isDirectory()){
							dirPopup.show((JComponent)e.getSource(), e.getX(), e.getY());
						} else{
							filePopup.show((JComponent)e.getSource(), e.getX(), e.getY());
						}
						setSelectionPath(tr);
					}
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getButton()==1){//LPM
					if(e.getClickCount()==2&&!e.isConsumed()){
						e.consume();
						TreePath tr = getPathForLocation(e.getX(), e.getY());
						if(tr!=null){
							File f = (File)tr.getLastPathComponent();
							if(f.isFile()){
								frame.getController().openTab(f);
							}
						}
					}
				}
			}
		});
	}
	
	private void setupDirectoryPopupMenu(){
		dirPopup = new JPopupMenu();
		JMenuItem mi = new JMenuItem("New File");
		mi.addActionListener(this);
		mi.setActionCommand("newfile");
		dirPopup.add(mi);
		mi = new JMenuItem("New Directory");
		mi.addActionListener(this);
		mi.setActionCommand("newdir");
		dirPopup.add(mi);
		mi = new JMenuItem("Cut");
		mi.addActionListener(this);
		mi.setActionCommand("cut");
		dirPopup.add(mi);
		mi = new JMenuItem("Copy");
		mi.addActionListener(this);
		mi.setActionCommand("copy");
		dirPopup.add(mi);
		mi = new JMenuItem("Paste");
		mi.addActionListener(this);
		mi.setActionCommand("paste");
		dirPopup.add(mi);
		mi = new JMenuItem("Delete");
		mi.addActionListener(this);
		mi.setActionCommand("delete");
		dirPopup.add(mi);
		mi = new JMenuItem("Refresh");
		mi.addActionListener(this);
		mi.setActionCommand("refresh");
		dirPopup.add(mi);
	}
	
	private void setupFilePopupMenu(){
		filePopup = new JPopupMenu();
		JMenuItem mi = new JMenuItem("Open");
		mi.addActionListener(this);
		mi.setActionCommand("open");
		filePopup.add(mi);
		mi = new JMenuItem("Cut");
		mi.addActionListener(this);
		mi.setActionCommand("cut");
		filePopup.add(mi);
		mi = new JMenuItem("Copy");
		mi.addActionListener(this);
		mi.setActionCommand("copy");
		filePopup.add(mi);
		mi = new JMenuItem("Paste");
		mi.addActionListener(this);
		mi.setActionCommand("paste");
		filePopup.add(mi);
		mi = new JMenuItem("Delete");
		mi.addActionListener(this);
		mi.setActionCommand("delete");
		filePopup.add(mi);
		mi = new JMenuItem("Refresh");
		mi.addActionListener(this);
		mi.setActionCommand("refresh");
		filePopup.add(mi);
	}
	
	private void setupBlankPopupMenu(){
		blankPopup = new JPopupMenu();
		JMenuItem mi = new JMenuItem("New File");
		mi.addActionListener(this);
		mi.setActionCommand("newfile");
		blankPopup.add(mi);
		mi = new JMenuItem("New Directory");
		mi.addActionListener(this);
		mi.setActionCommand("newdir");
		blankPopup.add(mi);
		mi = new JMenuItem("Paste");
		mi.addActionListener(this);
		mi.setActionCommand("paste");
		blankPopup.add(mi);
		mi = new JMenuItem("Refresh");
		mi.addActionListener(this);
		mi.setActionCommand("refresh");
		blankPopup.add(mi);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {//TODO
		
		TreePath tp = getSelectionPath();
		File file = null;
		if(tp!=null){
			file = (File) tp.getLastPathComponent();
		}
		
		if(ae.getActionCommand().equalsIgnoreCase("open")){
			frame.getController().openTab(file);
		} else if(ae.getActionCommand().equalsIgnoreCase("newfile")){
			
		} else if(ae.getActionCommand().equalsIgnoreCase("newdir")){
			
		} else if(ae.getActionCommand().equalsIgnoreCase("paste")){
			
		} else if(ae.getActionCommand().equalsIgnoreCase("copy")){
			
		} else if(ae.getActionCommand().equalsIgnoreCase("cut")){
			
		} else if(ae.getActionCommand().equalsIgnoreCase("delete")){
			
		} else if(ae.getActionCommand().equalsIgnoreCase("refresh")){
			this.setModel(this.getModel());
			this.updateUI();
		} else {
			
		}
	}

}
