package pl.gda.pg.eti.kernelhive.repository.graph.node.type;

/**
 * 
 * @author mschally
 *
 */
public enum GraphNodeType {
	GENERIC("generic"), 
	MERGER("DataMerger"), 
	PARTITIONER("DataPartitioner"), 
	PROCESSOR("DataProcessor"), 
	COMPOSITE("composite"), 
	MASTERSLAVE("masterslave"), 
	DAC("dac");
	
	private String type;
	
	GraphNodeType(String str){
		this.type = str;
	}
	
	@Override
	public String toString(){
		return type;
	}
	
	public static GraphNodeType getType(String type){
		GraphNodeType[] types = GraphNodeType.values();
		for(GraphNodeType t : types){
			if(t.toString().equalsIgnoreCase(type)){
				return t;
			}
		}
		return null;
	}	
}