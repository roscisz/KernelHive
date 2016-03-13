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
package pl.gda.pg.eti.kernelhive.gui.project.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.common.graph.configuration.IGUIGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.graph.configuration.impl.GUIGraphConfiguration;
import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.source.IKernelFile;
import pl.gda.pg.eti.kernelhive.gui.project.IProject;

public class KernelHiveProject implements Serializable, IProject {

	private static final long serialVersionUID = -4797108604024696381L;
	private static final Logger LOG = Logger.getLogger(KernelHiveProject.class
			.getName());
	private String projectName;
	private File projectDir;
	private File projectFile;
	private List<GUIGraphNodeDecorator> nodes = new ArrayList<GUIGraphNodeDecorator>();
	private transient IGUIGraphConfiguration config;

	public KernelHiveProject(File projectDir, String projectName) {
		this.projectName = projectName;
		this.projectDir = projectDir;
		config = new GUIGraphConfiguration();
	}

	@Override
	public void addProjectNode(GUIGraphNodeDecorator node) {
		if (!nodes.contains(node)) {
			nodes.add(node);
		}
	}

	@Override
	public File getProjectDirectory() {
		return projectDir;
	}

	@Override
	public File getProjectFile() {
		return projectFile;
	}

	@Override
	public String getProjectName() {
		return projectName;
	}

	@Override
	public List<GUIGraphNodeDecorator> getProjectNodes() {
		return nodes;
	}

	@Override
	public void initProject() throws ConfigurationException {
		setProjectName(projectName);
		save();
	}

	@Override
	public void load() throws ConfigurationException {
		load(projectFile);
	}

	@Override
	public void load(File file) throws ConfigurationException {
		config.setConfigurationFile(file);
		nodes = config.loadGraphForGUI();
		projectName = config.getProjectName();
	}

	@Override
	public void removeProjectNode(GUIGraphNodeDecorator node, boolean removeFromDisc) {
		if (nodes.contains(node)) {
			nodes.remove(node);
			if (removeFromDisc) {
				List<IKernelFile> srcFiles = node.getSourceFiles();
				for (IKernelFile f : srcFiles) {
					f.getFile().delete();
				}
			}
		}
	}

	@Override
	public void save() throws ConfigurationException {
		if ((projectFile == null) || (!projectFile.exists())) {
			projectFile = new File(
					Paths.get(projectDir.getPath().toString(), "project.xml")
					.toUri());
		}
		save(projectFile);
	}

	@Override
	public void save(File file) throws ConfigurationException {
		File f;
		if (!file.exists()) {
			try {
				f = FileUtils.createNewFile(file.getPath());
				file = f;
			} catch (IOException e) {
				LOG.severe("KH: error creating new file");
				e.printStackTrace();
				throw new ConfigurationException(e);
			}
		}
		config.setProjectName(projectName);
		config.setConfigurationFile(file);
		config.saveGraphForGUI(this.nodes);
	}

	@Override
	public void setProjectDirectory(File dir) {
		//FIXME not working
		boolean result = this.projectDir.renameTo(dir);
	}

	@Override
	public void setProjectFile(File file) {
		projectFile = file;
	}

	@Override
	public void setProjectName(String name) {
		this.projectName = name;
	}

	@Override
	public void setProjectNodes(List<GUIGraphNodeDecorator> nodes) {
		this.nodes = nodes;
	}
}
