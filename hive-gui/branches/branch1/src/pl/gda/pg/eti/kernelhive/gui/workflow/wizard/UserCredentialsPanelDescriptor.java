package pl.gda.pg.eti.kernelhive.gui.workflow.wizard;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import pl.gda.pg.eti.kernelhive.gui.wizard.WizardPanelDescriptor;

public class UserCredentialsPanelDescriptor extends WizardPanelDescriptor implements CaretListener {

	public static final String IDENTIFIER = "USER_CREDENTIALS_PANEL";
	
	public UserCredentialsPanelDescriptor(){
		super(IDENTIFIER, new UserCredentialsPanel());
		
		((UserCredentialsPanel)this.getPanelComponent()).addUsernameTextFieldCaretListener(this);
		((UserCredentialsPanel)this.getPanelComponent()).addPasswordFieldCaretListener(this);
	}
	@Override
	public Object getNextPanelDescriptor() {
		return WizardPanelDescriptor.FINISH;
	}

	@Override
	public Object getBackPanelDescriptor() {
		return InputDataPanelDescriptor.IDENTIFIER;
	}

	@Override
	public void aboutToDisplayPanel() {
		getWizard().setNextFinishButtonEnabled(false);
	}

	@Override
	public void displayingPanel() {
		
	}

	@Override
	public void aboutToHidePanel() {
		
	}
	
	@Override
	public void caretUpdate(CaretEvent e) {
		UserCredentialsPanel ucp = (UserCredentialsPanel) getPanelComponent();
		if(ucp.getUsername().length()>0&&ucp.getPassword().length>0){
			getWizard().setNextFinishButtonEnabled(true);
		} else{
			getWizard().setNextFinishButtonEnabled(false);
		}
	}

}
