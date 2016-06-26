/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
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
package pl.gda.pg.eti.kernelhive.gui.component.workflow.editor;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.graph.builder.IGraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.common.graph.builder.impl.GraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.IGraphNode;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.NodeIdGenerator;
import pl.gda.pg.eti.kernelhive.common.graph.node.util.NodeNameGenerator;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelPathEntry;
import pl.gda.pg.eti.kernelhive.common.kernel.repository.KernelRepositoryEntry;
import pl.gda.pg.eti.kernelhive.common.source.IKernelFile;
import pl.gda.pg.eti.kernelhive.common.source.KernelFile;
import pl.gda.pg.eti.kernelhive.gui.component.repository.viewer.TransferableKernelRepositoryEntry;
import pl.gda.pg.eti.kernelhive.gui.dialog.MessageDialog;

public class WorkflowEditorDropTargetListener extends DropTargetAdapter {

	private static Logger LOG = Logger
			.getLogger(WorkflowEditorDropTargetListener.class.getName());
	protected DropTarget target;
	protected WorkflowEditor editor;

	public WorkflowEditorDropTargetListener(final WorkflowEditor editor) {
		this.editor = editor;
		editor.graphComponent.setDragEnabled(false);
		target = new DropTarget(editor.graphComponent,
				DnDConstants.ACTION_COPY, this, true, null);
	}

	@Override
	public void drop(final DropTargetDropEvent dtde) {
		File dir = null;
		GUIGraphNodeDecorator guiNode = null;
		try {
			final KernelRepositoryEntry kre = (KernelRepositoryEntry) dtde
					.getTransferable().getTransferData(
					TransferableKernelRepositoryEntry.entryFlavour);

			if (dtde.isDataFlavorSupported(TransferableKernelRepositoryEntry.entryFlavour)) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY);

				final JFileChooser fc = new JFileChooser(
						editor.project.getProjectDirectory());
				fc.setDialogTitle("Choose directory...");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setAcceptAllFileFilterUsed(false);
				fc.setMultiSelectionEnabled(false);
				if (fc.showDialog(editor, "Select") == JFileChooser.APPROVE_OPTION) {
					// 1. select directory to store the copied kernels
					final String nodeId = NodeIdGenerator.generateId();
					dir = new File(fc.getSelectedFile().getAbsolutePath()
							+ System.getProperty("file.separator") + nodeId);
					dir.mkdirs();

					// 2. get input streams to the kernel templates, save the
					// content to new files
					final List<IKernelFile> sourceFiles = copyKernelsFromTemplates(
							kre, dir);

					// 3. create appropriate graph node
					final IGraphNodeBuilder graphNodeBuilder = new GraphNodeBuilder();
					final IGraphNode node = graphNodeBuilder
							.setType(kre.getGraphNodeType()).setId(nodeId).setProperties(kre.getProperties())
							.setName(NodeNameGenerator.generateName()).build();
					guiNode = new GUIGraphNodeDecorator(node, sourceFiles);
					guiNode.setX(dtde.getLocation().x);
					guiNode.setY(dtde.getLocation().y);

					// 4. add it to project
					editor.getProject().addProjectNode(guiNode);

					// 5. refresh graph
					editor.refresh();

					dtde.dropComplete(true);

				} else {
					MessageDialog.showMessageDialog(editor, "",
							"You have to choose a directory to place kernel files!");
					dtde.rejectDrop();
				}
			} else {
				dtde.rejectDrop();
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Error while dropping", e);
			if (guiNode != null && dir != null) {
				for (final IKernelFile s : guiNode.getSourceFiles()) {
					s.getFile().delete();
				}
				dir.delete();
			}
			dtde.rejectDrop();
		}
	}

	private List<IKernelFile> copyKernelsFromTemplates(
			final KernelRepositoryEntry kre, final File rootDir)
			throws SecurityException, IOException {
		final List<IKernelFile> sourceFiles = new ArrayList<IKernelFile>(kre
				.getKernelPaths().size());
		for (final KernelPathEntry kpe : kre.getKernelPaths()) {
			final BufferedReader br = new BufferedReader(new InputStreamReader(
					kpe.getPath().openConnection().getInputStream()));

			final File file = FileUtils.createNewFile(rootDir.getAbsolutePath()
					+ System.getProperty("file.separator") + kpe.getName());

			final BufferedWriter bw = new BufferedWriter(new FileWriter(file));

			String line = br.readLine();
			while (line != null) {
				bw.write(line);
				bw.newLine();
				line = br.readLine();
			}
			bw.flush();
			bw.close();
			br.close();

			sourceFiles.add(new KernelFile(file, kpe.getId(), kpe
					.getProperties()));
		}
		return sourceFiles;
	}
}
