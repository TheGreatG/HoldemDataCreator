package entities;

public enum Value{
	TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("T"), JACK("J"), QUEEN("Q"), KING("K"), ACE("A");
	
	private String value;
	
	public String getResponse() {
        return value;
    }
	
	Value(String value){
		this.value = value;
	}	
	
}
