package pl.gda.pg.eti.kernelhive.gui.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;

/**
 * graph node properties dialog
 * @author marcel
 *
 */
public class NodePropertiesDialog extends JDialog {

	private static final long serialVersionUID = -7313937306855473619L;
	
	private JTextField textFieldName;
	private JTextField textFieldType;
	private JTextField textFieldId;
	private JLabel lblSourceFiles;
	private JList<ISourceFile> list;
	private MainFrame frame;
	private IGraphNode node;
	
	public NodePropertiesDialog(MainFrame frame, IGraphNode node){
		super(frame);
		this.frame = frame;
		this.node = node;
		this.setBounds(this.getParent().getWidth()/2, this.getParent().getHeight()/2, 450, 300);
		getContentPane().setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(12, 41, 46, 15);
		getContentPane().add(lblName);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(76, 39, 232, 19);
		getContentPane().add(textFieldName);
		textFieldName.setColumns(10);
		textFieldName.setText(node.getName());
		
		JLabel lblType = new JLabel("Type");
		lblType.setBounds(12, 68, 46, 15);
		getContentPane().add(lblType);
		
		textFieldType = new JTextField();
		textFieldType.setEditable(false);
		textFieldType.setBounds(76, 66, 232, 19);
		getContentPane().add(textFieldType);
		textFieldType.setColumns(10);
		textFieldType.setText(node.getType().toString());
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(12, 12, 46, 15);
		getContentPane().add(lblId);
		
		textFieldId = new JTextField();
		textFieldId.setEditable(false);
		textFieldId.setBounds(76, 10, 232, 19);
		getContentPane().add(textFieldId);
		textFieldId.setColumns(10);
		textFieldId.setText(node.getNodeId());
		
		lblSourceFiles = new JLabel("Source Files");
		lblSourceFiles.setBounds(12, 107, 92, 15);
		getContentPane().add(lblSourceFiles);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(355, 230, 81, 25);
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		getContentPane().add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(262, 230, 81, 25);
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				close();
			}
		});
		getContentPane().add(btnSave);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(115, 107, 193, 96);
		getContentPane().add(scrollPane);
		
		list = new JList<ISourceFile>();
		scrollPane.setViewportView(list);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(319, 102, 92, 25);
		btnAdd.setEnabled(false);
		getContentPane().add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(320, 139, 91, 25);
		btnRemove.setEnabled(false);
		getContentPane().add(btnRemove);
		
		fillSourceFilesList(node.getSourceFiles());
		
	}
	
	private void save(){
		node.setName(textFieldName.getText());
	}
	
	private void close(){
		this.setVisible(false);
		this.dispose();
	}
	
	private void fillSourceFilesList(List<ISourceFile> sourceFiles){
		ListModel<ISourceFile> model = new SourceFilesListModel<ISourceFile>(sourceFiles);
		list.setModel(model);
		list.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==2){
					e.consume();
					ISourceFile file = list.getSelectedValue();
					frame.getController().openTab(file.getFile());
				}
			}
			
		});
	}
	
	
	private class SourceFilesListModel<E> implements ListModel<E>{

		List<E> list;
		List<ListDataListener> listDataListeners;
		
		public SourceFilesListModel(List<E> list){
			this.list = list;
			listDataListeners = new ArrayList<ListDataListener>();
		}
		
		@Override
		public int getSize() {
			if(list!=null){
				return list.size();
			} else{
				return -1;
			}
		}

		@Override
		public E getElementAt(int index) {
			if(list!=null){
				return list.get(index);
			} else{
				return null;
			}
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			if(!listDataListeners.contains(l)){
				listDataListeners.add(l);
			}
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			if(listDataListeners.contains(l)){
				listDataListeners.remove(l);
			}
		}
	}
}
