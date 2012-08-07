package pl.gda.pg.eti.kernelhive.common.graph.node.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.graph.node.EngineGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.graph.node.GUIGraphNodeDecorator;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;
import pl.gda.pg.eti.kernelhive.common.source.KernelString;

public class GraphNodeDecoratorConverter {
	
	public static EngineGraphNodeDecorator convertGuiToEngine(GUIGraphNodeDecorator node) throws GraphNodeDecoratorConverterException{
		try{
			EngineGraphNodeDecorator engineNode = new EngineGraphNodeDecorator(node.getGraphNode());
			List<ISourceFile> sources = node.getSourceFiles();
			for(ISourceFile source : sources){
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
