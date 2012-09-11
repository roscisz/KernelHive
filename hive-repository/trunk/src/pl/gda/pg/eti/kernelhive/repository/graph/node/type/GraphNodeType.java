package pl.gda.pg.eti.kernelhive.repository.graph.node.type;

import pl.gda.pg.eti.kernelhive.repository.graph.node.type.IGraphNodeType;

public class GraphNodeType implements IGraphNodeType {

	private String type;
	
	public GraphNodeType(String type){
		this.type = type;
	}
	
	@Override
	public String toString(){
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphNodeType other = (GraphNodeType) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
