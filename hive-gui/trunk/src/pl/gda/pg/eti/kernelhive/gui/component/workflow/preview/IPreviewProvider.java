/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.component.workflow.preview;

import java.awt.Graphics;
import java.util.List;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.PreviewObject;

/**
 *
 * @author szymon
 */
public interface IPreviewProvider {

	void paintData(Graphics g, List<PreviewObject> data, int areaWidth, int areaHeight);
}
