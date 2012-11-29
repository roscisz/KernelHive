package pl.gda.pg.eti.kernelhive.gui.component.workflow.editor;

public enum WorkflowGraphLayoutType {
	VERTICAL_HIERARCHICAL("verticalHierarchical"), HORIZONTAL_HIERARCHICAL(
			"horizontalHierarchical"), VERTICAL_TREE("verticalTree"), HORIZONTAL_TREE(
			"horizontalTree"), PARALLEL_EDGES("parallelEdges"), PLACE_EDGE_LABELS(
			"placeEdgeLabels"), ORGANIC_LAYOUT("organicLayout"), VERTICAL_PARTITION(
			"verticalPartition"), HORIZONTAL_PARTITION("horizontalPartition"), VERTICAL_STACK(
			"verticalStack"), HORIZONTAL_STACK("horizontalStack"), CIRCLE_LAYOUT(
			"circleLayout");

	private final String ident;

	private WorkflowGraphLayoutType(final String ident) {
		this.ident = ident;
	}

	public String getIdent() {
		return ident;
	}
}
