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
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.validation.GraphValidator;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult.ValidationResultType;
import pl.gda.pg.eti.kernelhive.gui.project.IProject;
import pl.gda.pg.eti.kernelhive.gui.wizard.WizardPanelDescriptor;

public class GraphValidationPanelDescriptor extends WizardPanelDescriptor implements ActionListener {

	public static final String IDENTIFIER = "GRAPH_VALIDATION_PANEL";
	private IProject project;

	public GraphValidationPanelDescriptor(IProject project){
		super(IDENTIFIER, new GraphValidationPanel());
		this.project = project;
		((GraphValidationPanel)this.getPanelComponent()).addButtonActionListener(this);
	}
	
	@Override
	public void aboutToDisplayPanel() {
		reloadGraphValidation();
	}
	
	@Override
	public void aboutToHidePanel() {
		reloadGraphValidation();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		reloadGraphValidation();
	}

	@Override
	public void displayingPanel() {
		
	}

	private List<ValidationResult> executeGraphValidation(){
		List<GUIGraphNodeDecorator> guiGraphNodes = project.getProjectNodes();
		List<ValidationResult> validationResults = GraphValidator.validateGraphForGUI(guiGraphNodes);
		return validationResults;	
	}

	@Override
	public Object getBackPanelDescriptor() {
		return null;
	}
	
	@Override
	public Object getNextPanelDescriptor() {
		return InputDataPanelDescriptor.IDENTIFIER;
	}

	private void reloadGraphValidation(){
		List<ValidationResult> validationResults = executeGraphValidation();
		((GraphValidationPanel)this.getPanelComponent()).displayGraphValidationResults(validationResults);
		boolean passed = true;
		for(ValidationResult r : validationResults){
			if(r.getType()==ValidationResultType.INVALID){
				passed = false;
				break;
			}
		}
		getWizard().setNextFinishButtonEnabled(passed);
	}
}
