package pl.gda.pg.eti.kernelhive.gui.workflow.wizard;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListDataListener;

/**
 * 
 * @author marcel
 * 
 */
public class InputDataPanel extends JPanel {

	private static final long serialVersionUID = 3430605808965461808L;
	private JLabel lblUrlIsInvalid;
	private JButton btnValidate;
	private JComboBox comboBox;

	public InputDataPanel(List<String> previouslyUsedURLs) {
		setSize(new Dimension(900, 300));
		setPreferredSize(new Dimension(900, 300));
		setMinimumSize(new Dimension(450, 300));
		setLayout(null);

		JLabel lblInputDataUrl = new JLabel("Input data URL");
		lblInputDataUrl.setBounds(12, 12, 105, 15);
		add(lblInputDataUrl);

		lblUrlIsInvalid = new JLabel("URL is invalid!");
		lblUrlIsInvalid.setVisible(false);
		lblUrlIsInvalid.setBounds(12, 70, 105, 15);
		add(lblUrlIsInvalid);

		btnValidate = new JButton("Validate");
		btnValidate.setBounds(335, 34, 93, 25);
		add(btnValidate);

		comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setBounds(12, 34, 311, 24);
		comboBox.setModel(new URLComboBoxModel(previouslyUsedURLs));
		add(comboBox);
	}

	public void addValidateButtonActionListener(ActionListener l) {
		btnValidate.addActionListener(l);
	}

	public void setInvalidUrlLabelVisible(boolean visible) {
		lblUrlIsInvalid.setVisible(visible);
	}

	public String getInputDataUrlString() {
		return comboBox.getSelectedItem().toString();
	}

	private class URLComboBoxModel implements ComboBoxModel {

		private List<Object> items;
		private Object selectedItem;
		private List<ListDataListener> listeners;

		public URLComboBoxModel(List<String> urls) {
			listeners = new ArrayList<ListDataListener>();
			items = new ArrayList<Object>();
			for (String s : urls) {
				items.add(s);
			}
			selectedItem = items.get(0);
		}

		@Override
		public int getSize() {
			return items.size();
		}

		@Override
		public Object getElementAt(int index) {
			return items.get(index);
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			listeners.add(l);
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			listeners.remove(l);
		}

		@Override
		public void setSelectedItem(Object anItem) {
			selectedItem = anItem;
		}

		@Override
		public Object getSelectedItem() {
			return selectedItem;
		}
	}
}
