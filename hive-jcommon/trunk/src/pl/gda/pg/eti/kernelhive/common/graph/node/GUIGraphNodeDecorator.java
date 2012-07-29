package pl.gda.pg.eti.kernelhive.common.graph.node;

import java.util.ArrayList;
import java.util.List;

import pl.gda.pg.eti.kernelhive.common.source.ISourceFile;
import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;

public class GUIGraphNodeDecorator extends AbstractGraphNodeDecorator {

	List<ISourceFile> sourceFiles;
	int x, y;
	
	public GUIGraphNodeDecorator(IGraphNode node){
		super(node);
		this.sourceFiles = new ArrayList<ISourceFile>();
	}
	
	public GUIGraphNodeDecorator(IGraphNode node, List<ISourceFile> sourceFiles){
		super(node);
		this.sourceFiles = sourceFiles;
	}

	@Override
	public List<ValidationResult> validate() {
		List<ValidationResult> result = node.validate();
//		List<String> sourceIds = node.getRequiredSourceFilesIds();
//		int count = 0;
//		for(ISourceFile s : sourceFiles){
//			if(sourceIds.contains(s.getId())){
//				count++;
//			}
//		}
//		if(count==sourceIds.size()){
//			result.add(new ValidationResult("Source Files ok", ValidationResultType.VALID));
//		} else{
//			result.add(new ValidationResult("Missing some source files", ValidationResultType.INVALID));
//		}
		return result;
	}
	
	public int getX(){
		return x;		
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public List<ISourceFile> getSourceFiles() {
		return sourceFiles;
	}
	
	public void setSourceFiles(List<ISourceFile> sourceFiles){
		this.sourceFiles = sourceFiles;
	}
	
	@Override
	public String toString() {
		return node.toString();
	}
}
