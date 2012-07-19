package pl.gda.pg.eti.kernelhive.common.graph.node;

import java.util.List;

import pl.gda.pg.eti.kernelhive.common.file.FileUtils;
import pl.gda.pg.eti.kernelhive.common.source.IKernelString;
import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;
import pl.gda.pg.eti.kernelhive.common.source.KernelString;

public class GraphNodeDecoratorConverter {
	
	public static EngineGraphNodeDecorator convertGuiToEngine(GUIGraphNodeDecorator node){
		EngineGraphNodeDecorator engineNode = new EngineGraphNodeDecorator(node.getGraphNode());
		List<ISourceFile> sources = node.getSourceFiles();
		for(ISourceFile source : sources){
			String src = FileUtils.readFileToString(source.getFile());
			IKernelString kernel = new KernelString(source.getId(), src, source.getProperties());
			engineNode.getKernels().add(kernel);
		}
		return engineNode;
	}
	
}
