package entities;

public class ValueInBB {
	private double valueInBB;
	
	public ValueInBB(double value){
		this.valueInBB = value;
	}

	public ValueInBB(double value, double bigBlind) {
		this.valueInBB = value/bigBlind;
	}

	public double getValueInBB() {
		return valueInBB;
	}

	public void setValueInBB(double stackValueBB) {
		this.valueInBB = stackValueBB;
	}
	
}
