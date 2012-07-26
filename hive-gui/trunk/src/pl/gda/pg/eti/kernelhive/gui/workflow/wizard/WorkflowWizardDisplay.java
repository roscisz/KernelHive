package pl.gda.pg.eti.kernelhive.gui.workflow.wizard;

import java.awt.Dialog;
import java.awt.Frame;
import java.net.MalformedURLException;
import java.net.URL;

import pl.gda.pg.eti.kernelhive.gui.project.IProject;
import pl.gda.pg.eti.kernelhive.gui.wizard.Wizard;
import pl.gda.pg.eti.kernelhive.gui.wizard.WizardPanelNotFoundException;

public class WorkflowWizardDisplay implements IWorkflowWizardDisplay {

	private Wizard wizard;
	private GraphValidationPanelDescriptor desc1;
	private InputDataPanelDescriptor desc2;
	private UserCredentialsPanelDescriptor desc3;
	
	public WorkflowWizardDisplay(Frame frame, String title, IProject project){
		wizard = new Wizard(frame);
		initWizard(title, project);
	}
	
	public WorkflowWizardDisplay(Dialog dialog, String title, IProject project){
		wizard = new Wizard(dialog);
		initWizard(title, project);
	}
	
	private void initWizard(String title, IProject project){
		wizard.getDialog().setTitle(title);
		desc1 = new GraphValidationPanelDescriptor(
				project);
		wizard.registerWizardPanel(
				GraphValidationPanelDescriptor.IDENTIFIER, desc1);
		desc2 = new InputDataPanelDescriptor();
		wizard.registerWizardPanel(InputDataPanelDescriptor.IDENTIFIER,
				desc2);
		desc3 = new UserCredentialsPanelDescriptor();
		wizard.registerWizardPanel(
				UserCredentialsPanelDescriptor.IDENTIFIER, desc3);
	}
	
	@Override
	public int displayWizard() throws WorkflowWizardDisplayException {
		try {
			wizard.setCurrentPanel(GraphValidationPanelDescriptor.IDENTIFIER);
		} catch (WizardPanelNotFoundException e) {
			throw new WorkflowWizardDisplayException(e);
		}
		return wizard.showNonModalDialog();
	}
	
	@Override
	public String getUsername(){
		return ((UserCredentialsPanel)desc3.getPanelComponent()).getUsername();
	}
	
	@Override
	public char[] getPassword(){
		return ((UserCredentialsPanel)desc3.getPanelComponent()).getPassword();
	}
	
	@Override
	public URL getInputDataUrl(){
		try {
			return new URL(((InputDataPanel)desc2.getPanelComponent()).getInputDataUrlString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
