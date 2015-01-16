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
		return wizard.showModalDialog();
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
	public String getInputDataUrl(){
		return ((InputDataPanel)desc2.getPanelComponent()).getInputDataUrlString();
	}
}
