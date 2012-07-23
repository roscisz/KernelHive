package pl.gda.pg.eti.kernelhive.common.structure;

public class HasID {
	
	private static int lastID = 0;
	
	public int ID;
	
	public HasID() {
		this.ID = HasID.generateID();		
	}
	
	private static int generateID() {
		return ++lastID;		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof HasID))
			return false;
		HasID other = (HasID) obj;
		if (ID != other.ID)
			return false;
		return true;
	}		

}
