/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.component.workflow.viewer;

import pl.gda.pg.eti.kernelhive.common.clientService.WorkflowInfo;

/**
 *
 * @author szymon
 */
public interface WorkflowViewerHandler {

	void showProgress(WorkflowInfo workflowInfo);

	void previewWork(WorkflowInfo workflowInfo);

	void terminate(WorkflowInfo workflowInfo);
}
