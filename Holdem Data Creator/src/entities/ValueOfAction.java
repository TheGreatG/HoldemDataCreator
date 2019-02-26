package entities;

public class ValueOfAction {
	private double valueOfAction = 0.0;
	
	public ValueOfAction(){
		
	}

	public ValueOfAction(double valueOfMyAction, double valueForCompare) {
		this.valueOfAction = valueOfMyAction/valueForCompare;
	}

	public double getValueOfAction() {
		return valueOfAction;
	}

	public void setValueOfAction(double valueOfAction) {
		this.valueOfAction = valueOfAction;
	}
	
}
