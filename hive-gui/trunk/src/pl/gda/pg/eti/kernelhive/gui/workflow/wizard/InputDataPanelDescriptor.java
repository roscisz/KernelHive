package pl.gda.pg.eti.kernelhive.gui.workflow.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pl.gda.pg.eti.kernelhive.gui.configuration.AppConfiguration;
import pl.gda.pg.eti.kernelhive.gui.wizard.WizardPanelDescriptor;

public class InputDataPanelDescriptor extends WizardPanelDescriptor implements
		ActionListener {

	public static final String IDENTIFIER = "INPUT_DATA_PANEL";

	public InputDataPanelDescriptor() {
		super(IDENTIFIER, new InputDataPanel(AppConfiguration.getInstance()
				.getPreviousInputDataURLs()));

		((InputDataPanel) getPanelComponent())
				.addValidateButtonActionListener(this);
	}

	@Override
	public Object getNextPanelDescriptor() {
		return UserCredentialsPanelDescriptor.IDENTIFIER;
	}

	@Override
	public Object getBackPanelDescriptor() {
		return GraphValidationPanelDescriptor.IDENTIFIER;
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

	private void showURLValidationResult() {
		boolean isInvalid = true;
		try {
			URL url = new URL(
					((InputDataPanel) getPanelComponent())
							.getInputDataUrlString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int code = conn.getResponseCode();
			if (code == 200) {
				isInvalid = false;
				addTypedURLToPreviousURLs(url.toExternalForm());
			} else {
				isInvalid = true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			((InputDataPanel) getPanelComponent())
					.setInvalidUrlLabelVisible(isInvalid);
			getWizard().setNextFinishButtonEnabled(!isInvalid);
		}
	}

	private void addTypedURLToPreviousURLs(String url) {
		AppConfiguration.getInstance().addURLToPreviousURLs(url);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		showURLValidationResult();
	}
}
