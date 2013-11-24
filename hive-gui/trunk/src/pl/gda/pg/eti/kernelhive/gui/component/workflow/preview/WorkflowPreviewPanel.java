/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.component.workflow.preview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.PreviewObject;

/**
 *
 * @author szymon
 */
public class WorkflowPreviewPanel extends JPanel {

	private List<PreviewObject> data;
	private IPreviewProvider previewProvider;

	public WorkflowPreviewPanel(IPreviewProvider previewProvider) {
		this.previewProvider = previewProvider;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		clearBackground(g);

		if (data != null && data.size() > 0) {
			previewProvider.paintData(g, data, getWidth(), getHeight());
		} else {
			g.setColor(Color.YELLOW);
			g.drawString("No data", 10, 10);
		}
	}

	private void clearBackground(Graphics g) {
		BufferedImage bImage = new BufferedImage(getWidth(),
				getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics imageGraphics = bImage.getGraphics();
		imageGraphics.setColor(Color.BLACK);
		imageGraphics.fillRect(0, 0, getWidth(), getHeight());

		g.drawImage(bImage, 0, 0, null);

		imageGraphics.dispose();
	}

	public void setPreviewData(List<PreviewObject> data) {
		this.data = data;
		repaint();
		revalidate();
	}
}
