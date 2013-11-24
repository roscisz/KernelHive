/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.component.workflow.progress;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pl.gda.pg.eti.kernelhive.common.clientService.JobProgress;

/**
 *
 * @author szymon
 */
public class WorkflowProgressViewerPanel extends JPanel {

	JLabel doneLabel = new JLabel();
	JLabel processingLabel = new JLabel();
	JLabel pendingLabel = new JLabel();
	JPanel header = new JPanel();
	JPanel progressPanel = new JPanel();
	GridLayout progressLayout = new GridLayout();

	public WorkflowProgressViewerPanel() {
		setLayout(new BorderLayout());

		header.add(new JLabel("Jobs done: "));
		header.add(doneLabel);
		header.add(new JLabel("Jobs processing: "));
		header.add(processingLabel);
		header.add(new JLabel("Jobs pending: "));
		header.add(pendingLabel);
		add(header, BorderLayout.NORTH);

		progressPanel.setLayout(progressLayout);
		add(progressPanel, BorderLayout.CENTER);
	}

	public void setProgress(List<JobProgress> progress) {
		int total = progress.size();
		int size = (int) Math.ceil(Math.sqrt((double) total));
		progressLayout.setColumns(size);
		progressLayout.setRows(size);
		progressLayout.setHgap(Math.max(progressPanel.getWidth() / (size * 10), 1));
		progressLayout.setVgap(Math.max(progressPanel.getHeight() / (size * 10), 1));

		progressPanel.removeAll();
		for (JobProgress jobProgress : progress) {
			JLabel square;
			switch (jobProgress.getState()) {
				case FINISHED:
					square = createSquare(String.format("%d: %s",
							jobProgress.getId(),
							jobProgress.getType().toString()),
							new Color(76, 148, 255));
					break;
				case PROCESSING:
				case SCHEDULED:
					square = createSquare(String.format("%d: %s (%d%%)",
							jobProgress.getId(),
							jobProgress.getType().toString(),
							jobProgress.getProgress()),
							new Color(64, 178, 64));
					break;
				default:
					square = createSquare(String.format("%d: %s",
							jobProgress.getId(),
							jobProgress.getType().toString()),
							new Color(153, 153, 153));
			}
			progressPanel.add(square);
		}
		progressPanel.revalidate();
	}

	private JLabel createSquare(String text, Color color) {
		JLabel cell = new JLabel(text);
		cell.setBackground(color);
		cell.setOpaque(true);
		return cell;
	}
}
