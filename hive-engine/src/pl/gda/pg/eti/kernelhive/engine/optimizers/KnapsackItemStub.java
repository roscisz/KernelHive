package pl.gda.pg.eti.kernelhive.engine.optimizers;

import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;

public class KnapsackItemStub implements IKnapsackItem {

	private int weight;
	private int value;
	private String name;
	
	public KnapsackItemStub(int weight, int value, String name) {
		this.weight = weight;
		this.value = value;
		this.name = name;
	}
	
	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public int getValue() {
		return value;		
	}
	
	public String getName() { 
		return name;
	}
	
	@Override
	public int compareTo(IKnapsackItem o) {
		Float myDensity = (float)this.getValue() / (float)this.getWeight();
		Float oDensity = (float)o.getValue() / (float)o.getWeight();
		return -myDensity.compareTo(oDensity);
		
		//Integer myValue = (Integer) value;
		//Integer oValue = (Integer) o.getValue();
		//return -myValue.compareTo(oValue);
		
		//Integer myWeight = (Integer) weight;
		//Integer oWeight = (Integer) o.getWeight();
		//return myWeight.compareTo(oWeight);
	}
}
