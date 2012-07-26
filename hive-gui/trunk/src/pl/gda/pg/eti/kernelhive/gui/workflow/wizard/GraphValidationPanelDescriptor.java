package pl.gda.pg.eti.kernelhive.gui.workflow.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
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
		List<IGraphNode> graphNodes = new ArrayList<IGraphNode>();
		for(GUIGraphNodeDecorator g : guiGraphNodes){
			graphNodes.add(g.getGraphNode());
		}
		List<ValidationResult> validationResults = GraphValidator.validateGraph(graphNodes);
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
