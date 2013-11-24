package pl.gda.pg.eti.kernelhive.common.clusterService;

public class HasID {

	private static int lastID = 0;
	private int id;

	public HasID() {
		this.id = HasID.generateID();
	}

	private static int generateID() {
		return ++lastID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof HasID)) {
			return false;
		}
		HasID other = (HasID) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	public int getId() {
		return id;
	}
}
