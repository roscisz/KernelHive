package pl.gda.pg.eti.kernelhive.gui.workflow.wizard;

import java.net.URL;

import pl.gda.pg.eti.kernelhive.gui.wizard.Wizard;

public interface IWorkflowWizardDisplay {
	public static final int WIZARD_FINISH_RETURN_CODE = Wizard.FINISH_RETURN_CODE;
	public static final int WIZARD_CANCEL_RETURN_CODE = Wizard.CANCEL_RETURN_CODE;
	public static final int WIZARD_ERROR_RETURN_CODE = Wizard.ERROR_RETURN_CODE;

	int displayWizard() throws WorkflowWizardDisplayException;
	char[] getPassword();
	String getUsername();
	URL getInputDataUrl();
	
}
