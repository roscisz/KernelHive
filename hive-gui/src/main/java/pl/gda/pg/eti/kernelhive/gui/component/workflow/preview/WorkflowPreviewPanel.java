/**
 * Copyright (c) 2014 Gdansk University of Technology
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
package pl.gda.pg.eti.kernelhive.gui.component.workflow.preview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.PreviewObject;

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
