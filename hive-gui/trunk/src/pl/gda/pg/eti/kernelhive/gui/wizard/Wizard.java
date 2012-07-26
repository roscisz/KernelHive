package pl.gda.pg.eti.kernelhive.gui.wizard;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

/**
 * 
 *
 */
public class Wizard extends WindowAdapter implements PropertyChangeListener {

	/**
	 * 
	 */
	public static final int FINISH_RETURN_CODE = 0;
	/**
	 * 
	 */
	public static final int CANCEL_RETURN_CODE = 1;
	/**
	 * 
	 */
	public static final int ERROR_RETURN_CODE = 2;

	static String BACK_TEXT = "Back";
	static String NEXT_TEXT = "Next";
	static String FINISH_TEXT = "Finish";
	static String CANCEL_TEXT = "Cancel";

	static Icon BACK_ICON;
	static Icon NEXT_ICON;
	static Icon FINISH_ICON;
	static Icon CANCEL_ICON;

	private WizardModel model;
	private WizardController controller;
	private WizardDialog dialog;
	private int returnCode;

	public Wizard() {
		this((Frame) null);
	}

	public Wizard(Dialog owner) {
		model = new WizardModel();
		model.addPropertyChangeListener(this);
		controller = new WizardController(this);
		dialog = new WizardDialog(owner, controller);
	}

	public Wizard(Frame owner) {
		model = new WizardModel();
		model.addPropertyChangeListener(this);
		controller = new WizardController(this);
		dialog = new WizardDialog(owner, controller);
	}

	void close(int code) {
		returnCode = code;
		dialog.dispose();
	}

	/**
	 * 
	 * @return
	 */
	public WizardDialog getDialog() {
		return dialog;
	}

	/**
	 * 
	 * @return
	 */
	public WizardModel getModel() {
		return model;
	}

	/**
	 * 
	 * @return
	 */
	public int getReturnCode() {
		return returnCode;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(
				WizardModel.CURRENT_PANEL_DESCRIPTOR_PROPERTY)) {
			controller.resetButtonsToPanelRules();
		} else if (evt.getPropertyName().equals(
				WizardModel.NEXT_FINISH_BUTTON_TEXT_PROPERTY)) {
			dialog.getNextButton().setText(evt.getNewValue().toString());
		} else if (evt.getPropertyName().equals(
				WizardModel.BACK_BUTTON_TEXT_PROPERTY)) {
			dialog.getBackButton().setText(evt.getNewValue().toString());
		} else if (evt.getPropertyName().equals(
				WizardModel.CANCEL_BUTTON_TEXT_PROPERTY)) {
			dialog.getCancelButton().setText(evt.getNewValue().toString());
		} else if (evt.getPropertyName().equals(
				WizardModel.NEXT_FINISH_BUTTON_ENABLED_PROPERTY)) {
			dialog.getNextButton().setEnabled(((Boolean) evt.getNewValue()).booleanValue());
		} else if (evt.getPropertyName().equals(
				WizardModel.BACK_BUTTON_ENABLED_PROPERTY)) {
			dialog.getBackButton().setEnabled(((Boolean) evt.getNewValue()).booleanValue());
		} else if (evt.getPropertyName().equals(
				WizardModel.CANCEL_BUTTON_ENABLED_PROPERTY)) {
			dialog.getCancelButton().setEnabled(((Boolean) evt.getNewValue())
					.booleanValue());
		} else if (evt.getPropertyName().equals(
				WizardModel.NEXT_FINISH_BUTTON_ICON_PROPERTY)) {
			dialog.getNextButton().setIcon((Icon) evt.getNewValue());
		} else if (evt.getPropertyName().equals(
				WizardModel.BACK_BUTTON_ICON_PROPERTY)) {
			dialog.getBackButton().setIcon((Icon) evt.getNewValue());
		} else if (evt.getPropertyName().equals(
				WizardModel.CANCEL_BUTTON_ICON_PROPERTY)) {
			dialog.getCancelButton().setIcon((Icon) evt.getNewValue());
		}
	}

	/**
	 * 
	 * @param id
	 * @param panel
	 */
	public void registerWizardPanel(Object id, WizardPanelDescriptor panel) {
		dialog.getCardPanel().add(panel.getPanelComponent(), id.toString());
		panel.setWizard(this);
		model.registerPanel(id, panel);
	}

	/**
	 * 
	 * @param id
	 * @throws WizardPanelNotFoundException
	 */
	public void setCurrentPanel(Object id) throws WizardPanelNotFoundException {
		if (id == null) {
			close(ERROR_RETURN_CODE);
		}

		WizardPanelDescriptor oldPanelDescriptor = model
				.getCurrentPanelDescriptor();
		if (oldPanelDescriptor != null) {
			oldPanelDescriptor.aboutToHidePanel();
		}

		model.setCurrentPanel(id);
		model.getCurrentPanelDescriptor().aboutToDisplayPanel();
		
		dialog.getCardLayout().show(dialog.getCardPanel(), id.toString());
		model.getCurrentPanelDescriptor().displayingPanel();
	}

	/**
	 * 
	 * @return
	 */
	public int showModalDialog() {
		dialog.setModal(true);
		dialog.pack();
		dialog.setVisible(true);
		return returnCode;
	}
	
	public void setNextFinishButtonEnabled(boolean enabled){
		model.setNextFinishButtonEnabled(enabled);
	}
	
	/**
	 * 
	 * @return
	 */
	public int showNonModalDialog(){
		dialog.setModal(false);
		dialog.pack();
		dialog.setVisible(true);
		return returnCode;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		returnCode = CANCEL_RETURN_CODE;
	}
}
