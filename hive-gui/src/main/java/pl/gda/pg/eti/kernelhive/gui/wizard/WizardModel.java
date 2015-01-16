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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

/**
 * The model for the Wizard component, which tracks the text, icons, and enabled
 * state of each of the buttons, as well as the current panel that is displayed.
 */
public class WizardModel {

	/**
	 * Identification string for the current panel.
	 */
	public static final String CURRENT_PANEL_DESCRIPTOR_PROPERTY = "currentPanelDescriptorProperty";

	/**
	 * Property identification String for the Back button's text
	 */
	public static final String BACK_BUTTON_TEXT_PROPERTY = "backButtonTextProperty";
	/**
	 * Property identification String for the Back button's icon
	 */
	public static final String BACK_BUTTON_ICON_PROPERTY = "backButtonIconProperty";
	/**
	 * Property identification String for the Back button's enabled state
	 */
	public static final String BACK_BUTTON_ENABLED_PROPERTY = "backButtonEnabledProperty";

	/**
	 * Property identification String for the Next button's text
	 */
	public static final String NEXT_FINISH_BUTTON_TEXT_PROPERTY = "nextButtonTextProperty";
	/**
	 * Property identification String for the Next button's icon
	 */
	public static final String NEXT_FINISH_BUTTON_ICON_PROPERTY = "nextButtonIconProperty";
	/**
	 * Property identification String for the Next button's enabled state
	 */
	public static final String NEXT_FINISH_BUTTON_ENABLED_PROPERTY = "nextButtonEnabledProperty";

	/**
	 * Property identification String for the Cancel button's text
	 */
	public static final String CANCEL_BUTTON_TEXT_PROPERTY = "cancelButtonTextProperty";
	/**
	 * Property identification String for the Cancel button's icon
	 */
	public static final String CANCEL_BUTTON_ICON_PROPERTY = "cancelButtonIconProperty";
	/**
	 * Property identification String for the Cancel button's enabled state
	 */
	public static final String CANCEL_BUTTON_ENABLED_PROPERTY = "cancelButtonEnabledProperty";

	protected WizardPanelDescriptor currentPanel;

	protected Map<Object, WizardPanelDescriptor> panelHashmap;

	protected Map<String, String> buttonTextHashmap;
	protected Map<String, Icon> buttonIconHashmap;
	protected Map<String, Boolean> buttonEnabledHashmap;

	protected PropertyChangeSupport propertyChangeSupport;

	/**
	 * Default constructor.
	 */
	public WizardModel() {

		panelHashmap = new HashMap<Object, WizardPanelDescriptor>();

		buttonTextHashmap = new HashMap<String, String>();
		buttonIconHashmap = new HashMap<String, Icon>();
		buttonEnabledHashmap = new HashMap<String, Boolean>();

		propertyChangeSupport = new PropertyChangeSupport(this);

	}

	/**
	 * Returns the currently displayed WizardPanelDescriptor.
	 * 
	 * @return The currently displayed WizardPanelDescriptor
	 */
	WizardPanelDescriptor getCurrentPanelDescriptor() {
		return currentPanel;
	}

	/**
	 * Registers the WizardPanelDescriptor in the model using the
	 * Object-identifier specified.
	 * 
	 * @param id
	 *            Object-based identifier
	 * @param descriptor
	 *            WizardPanelDescriptor that describes the panel
	 */
	void registerPanel(Object id, WizardPanelDescriptor descriptor) {

		// Place a reference to it in a hashtable so we can access it later
		// when it is about to be displayed.

		panelHashmap.put(id, descriptor);

	}

	/**
	 * Sets the current panel to that identified by the Object passed in.
	 * 
	 * @param id
	 *            Object-based panel identifier
	 * @return boolean indicating success or failure
	 * @throws WizardPanelNotFoundException
	 */
	boolean setCurrentPanel(Object id) throws WizardPanelNotFoundException {

		// First, get the hashtable reference to the panel that should
		// be displayed.

		WizardPanelDescriptor nextPanel = (WizardPanelDescriptor) panelHashmap
				.get(id);

		// If we couldn't find the panel that should be displayed, return
		// false.

		if (nextPanel == null)
			throw new WizardPanelNotFoundException();

		WizardPanelDescriptor oldPanel = currentPanel;
		currentPanel = nextPanel;

		if (oldPanel != currentPanel)
			firePropertyChange(CURRENT_PANEL_DESCRIPTOR_PROPERTY, oldPanel,
					currentPanel);

		return true;

	}

	String getBackButtonText() {
		return buttonTextHashmap.get(BACK_BUTTON_TEXT_PROPERTY);
	}

	void setBackButtonText(String newText) {

		String oldText = getBackButtonText();
		if (newText!=null&&!newText.equals(oldText)) {
			buttonTextHashmap.put(BACK_BUTTON_TEXT_PROPERTY, newText);
			firePropertyChange(BACK_BUTTON_TEXT_PROPERTY, oldText, newText);
		} else if(newText==null&&oldText!=null){
			buttonTextHashmap.put(BACK_BUTTON_TEXT_PROPERTY, newText);
			firePropertyChange(BACK_BUTTON_TEXT_PROPERTY, oldText, newText);
		}
	}

	String getNextFinishButtonText() {
		return buttonTextHashmap.get(NEXT_FINISH_BUTTON_TEXT_PROPERTY);
	}

	void setNextFinishButtonText(String newText) {

		String oldText = getNextFinishButtonText();
		if (newText!=null&&!newText.equals(oldText)) {
			buttonTextHashmap.put(NEXT_FINISH_BUTTON_TEXT_PROPERTY, newText);
			firePropertyChange(NEXT_FINISH_BUTTON_TEXT_PROPERTY, oldText,
					newText);
		} else if(newText==null&&oldText!=null){
			buttonTextHashmap.put(NEXT_FINISH_BUTTON_TEXT_PROPERTY, newText);
			firePropertyChange(NEXT_FINISH_BUTTON_TEXT_PROPERTY, oldText, newText);
		}
	}

	String getCancelButtonText() {
		return buttonTextHashmap.get(CANCEL_BUTTON_TEXT_PROPERTY);
	}

	void setCancelButtonText(String newText) {

		String oldText = getCancelButtonText();
		if (newText!=null&&!newText.equals(oldText)) {
			buttonTextHashmap.put(CANCEL_BUTTON_TEXT_PROPERTY, newText);
			firePropertyChange(CANCEL_BUTTON_TEXT_PROPERTY, oldText, newText);
		} else if(newText==null&&oldText!=null){
			buttonTextHashmap.put(CANCEL_BUTTON_TEXT_PROPERTY, newText);
			firePropertyChange(CANCEL_BUTTON_TEXT_PROPERTY, oldText, newText);
		}
	}

	Icon getBackButtonIcon() {
		return (Icon) buttonIconHashmap.get(BACK_BUTTON_ICON_PROPERTY);
	}

	void setBackButtonIcon(Icon newIcon) {

		Object oldIcon = getBackButtonIcon();
		if (newIcon!=null&&!newIcon.equals(oldIcon)) {
			buttonIconHashmap.put(BACK_BUTTON_ICON_PROPERTY, newIcon);
			firePropertyChange(BACK_BUTTON_ICON_PROPERTY, oldIcon, newIcon);
		} else if(newIcon==null&&oldIcon!=null){
			buttonIconHashmap.put(BACK_BUTTON_ICON_PROPERTY, newIcon);
			firePropertyChange(BACK_BUTTON_ICON_PROPERTY, oldIcon, newIcon);
		}
	}

	Icon getNextFinishButtonIcon() {
		return (Icon) buttonIconHashmap.get(NEXT_FINISH_BUTTON_ICON_PROPERTY);
	}

	void setNextFinishButtonIcon(Icon newIcon) {

		Object oldIcon = getNextFinishButtonIcon();
		if (newIcon!=null&&!newIcon.equals(oldIcon)) {
			buttonIconHashmap.put(NEXT_FINISH_BUTTON_ICON_PROPERTY, newIcon);
			firePropertyChange(NEXT_FINISH_BUTTON_ICON_PROPERTY, oldIcon,
					newIcon);
		} else if(newIcon==null&&oldIcon!=null){
			buttonIconHashmap.put(NEXT_FINISH_BUTTON_ICON_PROPERTY, newIcon);
			firePropertyChange(NEXT_FINISH_BUTTON_ICON_PROPERTY, oldIcon, newIcon);
		}
	}

	Icon getCancelButtonIcon() {
		return (Icon) buttonIconHashmap.get(CANCEL_BUTTON_ICON_PROPERTY);
	}

	void setCancelButtonIcon(Icon newIcon) {

		Icon oldIcon = getCancelButtonIcon();
		if (newIcon!=null&&!newIcon.equals(oldIcon)) {
			buttonIconHashmap.put(CANCEL_BUTTON_ICON_PROPERTY, newIcon);
			firePropertyChange(CANCEL_BUTTON_ICON_PROPERTY, oldIcon, newIcon);
		} else if(newIcon==null&&oldIcon!=null){
			buttonIconHashmap.put(CANCEL_BUTTON_ICON_PROPERTY, newIcon);
			firePropertyChange(CANCEL_BUTTON_ICON_PROPERTY, oldIcon, newIcon);
		}
	}

	Boolean getBackButtonEnabled() {
		return (Boolean) buttonEnabledHashmap.get(BACK_BUTTON_ENABLED_PROPERTY);
	}

	void setBackButtonEnabled(Boolean newValue) {

		Boolean oldValue = getBackButtonEnabled();
		if (newValue != oldValue) {
			buttonEnabledHashmap.put(BACK_BUTTON_ENABLED_PROPERTY, newValue);
			firePropertyChange(BACK_BUTTON_ENABLED_PROPERTY, oldValue, newValue);
		}
	}

	Boolean getNextFinishButtonEnabled() {
		return (Boolean) buttonEnabledHashmap
				.get(NEXT_FINISH_BUTTON_ENABLED_PROPERTY);
	}

	void setNextFinishButtonEnabled(Boolean newValue) {

		Boolean oldValue = getNextFinishButtonEnabled();
		if (newValue != oldValue) {
			buttonEnabledHashmap.put(NEXT_FINISH_BUTTON_ENABLED_PROPERTY,
					newValue);
			firePropertyChange(NEXT_FINISH_BUTTON_ENABLED_PROPERTY, oldValue,
					newValue);
		}
	}

	Boolean getCancelButtonEnabled() {
		return (Boolean) buttonEnabledHashmap
				.get(CANCEL_BUTTON_ENABLED_PROPERTY);
	}

	void setCancelButtonEnabled(Boolean newValue) {

		Boolean oldValue = getCancelButtonEnabled();
		if (newValue != oldValue) {
			buttonEnabledHashmap.put(CANCEL_BUTTON_ENABLED_PROPERTY, newValue);
			firePropertyChange(CANCEL_BUTTON_ENABLED_PROPERTY, oldValue,
					newValue);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener p) {
		propertyChangeSupport.addPropertyChangeListener(p);
	}

	public void removePropertyChangeListener(PropertyChangeListener p) {
		propertyChangeSupport.removePropertyChangeListener(p);
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}

}