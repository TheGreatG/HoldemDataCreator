package entities;

import java.util.ArrayList;
import java.util.List;

public class CardsCombination {
	private List<Card> cards = new ArrayList<Card>();

	public CardsCombination(String first, String second) {	
		super();
		this.cards.add(new Card(first));
		this.cards.add(new Card(second));
	}
	
	public CardsCombination(List<Card> cards){
		this.cards = cards;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	public List<Card> suitControl(){ //remake cards to suited or off suited
		if (cards.get(0).getSuit() == cards.get(1).getSuit()) {	//if suited - cards are both managed as spade suit
			cards.get(0).setSuit(Suit.SPADE);
			cards.get(1).setSuit(Suit.SPADE);
			return cards;
		}
		else{
			cards.get(0).setSuit(Suit.HEART);	//if not suited - cards are managed as heart suit
			cards.get(1).setSuit(Suit.HEART);
			return cards;
		}		
	}
		
}
