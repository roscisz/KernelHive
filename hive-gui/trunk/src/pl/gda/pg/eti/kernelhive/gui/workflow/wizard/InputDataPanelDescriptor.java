/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
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
		// FIXME: turned off validation
		getWizard().setNextFinishButtonEnabled(true);
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
			isInvalid = false;
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
