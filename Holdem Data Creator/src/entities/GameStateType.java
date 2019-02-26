package entities;

public enum GameStateType {
	INIT("0"), PREFLOP("1"), FLOP("2"), TURN("3"), RIVER("4"), END("5");
	
	private String value;
	
	public String getResponse() {
		float returnValue = Float.parseFloat(value);
		String returnString = String.valueOf(returnValue/5); //normalize value into (0,1)
		return returnString;
    }
	
	GameStateType(String value){
		this.value = value;
	}
}
