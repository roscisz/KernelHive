/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 * Copyright (c) 2014 Szymon Bultrowicz
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

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import pl.gda.pg.eti.kernelhive.gui.wizard.WizardPanelDescriptor;

public class UserCredentialsPanelDescriptor extends WizardPanelDescriptor implements CaretListener {

	public static final String IDENTIFIER = "USER_CREDENTIALS_PANEL";

	public UserCredentialsPanelDescriptor() {
		super(IDENTIFIER, new UserCredentialsPanel());

		((UserCredentialsPanel) this.getPanelComponent()).addUsernameTextFieldCaretListener(this);
		((UserCredentialsPanel) this.getPanelComponent()).addPasswordFieldCaretListener(this);
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
		getWizard().setNextFinishButtonEnabled(true);
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
		if (ucp.getUsername().length() > 0 && ucp.getPassword().length > 0) {
			getWizard().setNextFinishButtonEnabled(true);
		} else {
			getWizard().setNextFinishButtonEnabled(true);
		}
	}
}
