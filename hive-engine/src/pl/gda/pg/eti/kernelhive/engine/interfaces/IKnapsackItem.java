package pl.gda.pg.eti.kernelhive.engine.interfaces;

public interface IKnapsackItem extends Comparable<IKnapsackItem> {
	
	public int getWeight();
	public int getValue();	
	
}
