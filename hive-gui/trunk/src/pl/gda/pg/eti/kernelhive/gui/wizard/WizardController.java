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
package pl.gda.pg.eti.kernelhive.gui.wizard;

public class WizardController {
	
	private Wizard wizard;
	
	public WizardController(Wizard wizard){
		this.wizard = wizard;
	}
	
	public Wizard getWizard(){
		return wizard;
	}

	/**
	 * 
	 * @throws WizardPanelNotFoundException
	 */
	public void executeBack() throws WizardPanelNotFoundException {
		WizardModel model = wizard.getModel();
		WizardPanelDescriptor descriptor = model.getCurrentPanelDescriptor();
		
		Object backPanelDescriptor = descriptor.getBackPanelDescriptor();        
        wizard.setCurrentPanel(backPanelDescriptor);
	}

	/**
	 * 
	 * @throws WizardPanelNotFoundException
	 */
	public void executeNext() throws WizardPanelNotFoundException {
		WizardModel model = wizard.getModel();
		WizardPanelDescriptor descriptor = model.getCurrentPanelDescriptor();
		
		Object nextPanelDescriptor = descriptor.getNextPanelDescriptor();
		
		if(nextPanelDescriptor instanceof WizardPanelDescriptor.FinishIdentifier){
			wizard.close(Wizard.FINISH_RETURN_CODE);
		} else{
			wizard.setCurrentPanel(nextPanelDescriptor);
		}
		
	}

	/**
	 * 
	 */
	public void executeCancel() {
		wizard.close(Wizard.CANCEL_RETURN_CODE);
	}
	
	void resetButtonsToPanelRules() {
	    
        //  Reset the buttons to support the original panel rules,
        //  including whether the next or back buttons are enabled or
        //  disabled, or if the panel is finishable.
        
        WizardModel model = wizard.getModel();
        WizardPanelDescriptor descriptor = model.getCurrentPanelDescriptor();
        
        model.setCancelButtonText(Wizard.CANCEL_TEXT);
        model.setCancelButtonIcon(Wizard.CANCEL_ICON);
        
        //  If the panel in question has another panel behind it, enable
        //  the back button. Otherwise, disable it.
        
        model.setBackButtonText(Wizard.BACK_TEXT);
        model.setBackButtonIcon(Wizard.BACK_ICON);
        
        if (descriptor.getBackPanelDescriptor() != null)
            model.setBackButtonEnabled(Boolean.TRUE);
        else
            model.setBackButtonEnabled(Boolean.FALSE);

        //  If the panel in question has one or more panels in front of it,
        //  enable the next button. Otherwise, disable it.
 
        if (descriptor.getNextPanelDescriptor() != null)
            model.setNextFinishButtonEnabled(Boolean.TRUE);
        else
            model.setNextFinishButtonEnabled(Boolean.FALSE);
 
        //  If the panel in question is the last panel in the series, change
        //  the Next button to Finish. Otherwise, set the text back to Next.
        
        if (descriptor.getNextPanelDescriptor() instanceof WizardPanelDescriptor.FinishIdentifier) {
            model.setNextFinishButtonText(Wizard.FINISH_TEXT);
            model.setNextFinishButtonIcon(Wizard.FINISH_ICON);
        } else {
            model.setNextFinishButtonText(Wizard.NEXT_TEXT);
            model.setNextFinishButtonIcon(Wizard.NEXT_ICON);
        }
        
    }

}
