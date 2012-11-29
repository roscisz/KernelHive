package pl.gda.pg.eti.kernelhive.repository.graph.node.type;

/**
 * 
 * @author mschally
 * 
 */
public enum GraphNodeType {
	GENERIC("generic"), MERGER("DataMerger"), PARTITIONER("DataPartitioner"), PROCESSOR(
			"DataProcessor"), COMPOSITE("composite"), MASTERSLAVE("masterslave"), DAC(
			"dac"), EXPANDABLE("expandable");

	private String type;

	GraphNodeType(final String str) {
		this.type = str;
	}

	@Override
	public String toString() {
		return type;
	}

	public static GraphNodeType getType(final String type) {
		final GraphNodeType[] types = GraphNodeType.values();
		for (final GraphNodeType t : types) {
			if (t.toString().equalsIgnoreCase(type)) {
				return t;
			}
		}
		return null;
	}
}