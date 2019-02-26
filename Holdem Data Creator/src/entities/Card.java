package entities;

public class Card {
	private Suit suit;
	private Value value;
	
	public Card(String cardString){

		switch (cardString.substring(0, 1)) {
		case "2": this.value = Value.TWO;break;
		case "3": this.value = Value.THREE;break;
		case "4": this.value = Value.FOUR;break;
		case "5": this.value = Value.FIVE;break;
		case "6": this.value = Value.SIX;break;
		case "7": this.value = Value.SEVEN;break;
		case "8": this.value = Value.EIGHT;break;
		case "9": this.value = Value.NINE;break;
		case "T": this.value = Value.TEN;break;
		case "J": this.value = Value.JACK;break;
		case "Q": this.value = Value.QUEEN;break;
		case "K": this.value = Value.KING;break;
		case "A": this.value = Value.ACE;break;
		default:
			break;
		}
		switch (cardString.substring(1, 2)) {
		case "s": this.suit = Suit.SPADE;break;
		case "h": this.suit = Suit.HEART;break;
		case "d": this.suit = Suit.DIAMOND;break;
		case "c": this.suit = Suit.CLUB;break;
		default:
			break;
		}
	}
	
	public Suit getSuit() {
		return suit;
	}
	public void setSuit(Suit suit) {
		this.suit = suit;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	
	public String toString(){
		return "" + this.getValue().getResponse() + this.getSuit().getResponse();
	}
		
}
