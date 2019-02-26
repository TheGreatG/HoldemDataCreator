package entities;

public enum Suit {
	SPADE("s"), HEART("h"), CLUB("c"), DIAMOND("d");
	
	private String suit;
	
	public String getResponse() {
        return suit;
    }
	
	Suit(String suit){
		this.suit = suit;
	}	
	
}
