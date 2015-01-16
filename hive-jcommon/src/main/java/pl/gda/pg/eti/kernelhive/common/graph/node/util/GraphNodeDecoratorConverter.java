/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 * Copyright (c) 2014 Pawel Rosciszewski
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
package pl.gda.pg.eti.kernelhive.common.graph.node.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.source.IKernelFile;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.common.source.KernelString;

public class GraphNodeDecoratorConverter {
	
	public static EngineGraphNodeDecorator convertGuiToEngine(GUIGraphNodeDecorator node) throws GraphNodeDecoratorConverterException{
		try{
			EngineGraphNodeDecorator engineNode = new EngineGraphNodeDecorator(node.getGraphNode());
			List<IKernelFile> sources = node.getSourceFiles();
			for(IKernelFile source : sources){
				String src = FileUtils.readFileToString(source.getFile());
				IKernelString kernel = new KernelString(source.getId(), src, source.getProperties());
				engineNode.getKernels().add(kernel);
			}
			return engineNode;
		} catch(IOException e){
			throw new GraphNodeDecoratorConverterException(e, node);
		}
	}
	
	public static List<EngineGraphNodeDecorator> convertGuiToEngine(List<GUIGraphNodeDecorator> guiNodes) throws GraphNodeDecoratorConverterException{
		List<EngineGraphNodeDecorator> engineNodes = new ArrayList<EngineGraphNodeDecorator>();
		for (GUIGraphNodeDecorator g : guiNodes) {
			engineNodes.add(GraphNodeDecoratorConverter
						.convertGuiToEngine(g));
		}
		return engineNodes;
	}
	
}
