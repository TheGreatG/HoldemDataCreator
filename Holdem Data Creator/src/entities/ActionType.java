package entities;


public enum ActionType {
	//0.0       0.2			0.4			0.6			0.8	      1.0
	CALL("0"), RAISE("1"), BET("2"), FOLD("3"), CHECK("4"), WIN("5");	
	
	private String value;
	
	public String getResponse() {
		float returnValue = Float.parseFloat(value);
		String returnString = String.valueOf(returnValue/5); //normalize value into (0,1)
		return returnString;
    }

	ActionType(String value){
		this.value = value;
	}
}
