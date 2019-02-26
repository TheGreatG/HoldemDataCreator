package entities;

public class CardsCombinationTypeFinder {
	
	public CardsCombinationType getCardsCombinationType(CardsCombination cc){
		Card c1 = cc.getCards().get(0);
		Card c2 = cc.getCards().get(1);
		
		if (isPair(c1, c2) && (isBroadway(c1) || isMiddle(c1))) return CardsCombinationType.VALUE_HAND;	
		if ((isAce(c1) && isBroadway(c2)) || (isBroadway(c1) && isAce(c2) )) return CardsCombinationType.VALUE_HAND;
		if (isBroadway(c1) && isBroadway(c2)) return CardsCombinationType.BROADWAY;
		if (isBroadwayAndMiddle(c1) && isBroadwayAndMiddle(c2) || isAce(c1) || isAce(c2)) return CardsCombinationType.MIDDLE_VALUE;
		if (isMiddle(c1) && isMiddle(c2)) return CardsCombinationType.CONNECTORS;
		if (!isBroadway(c1) && !isMiddle(c1) && isPair(c1, c2)) return CardsCombinationType.SMALL_PAIR;
		return CardsCombinationType.BLUFF;
		
	}
	
	public boolean isAce(Card c){
		if (c.getValue() == Value.ACE) return true;
		return false;
	}
	public boolean isBroadway(Card c){
		if (c.getValue() == Value.ACE || c.getValue() == Value.KING || c.getValue() == Value.QUEEN || c.getValue() == Value.JACK) return true;
		return false;
	}
	public boolean isBroadwayAndMiddle(Card c){
		if (c.getValue() == Value.KING || c.getValue() == Value.QUEEN || c.getValue() == Value.JACK || c.getValue() == Value.TEN || c.getValue() == Value.NINE) return true;
		return false;
	}
	public boolean isMiddle(Card c){
		if (c.getValue() == Value.TEN || c.getValue() == Value.NINE || c.getValue() == Value.EIGHT || c.getValue() == Value.SEVEN || c.getValue() == Value.SIX) return true;
		return false;
	}
	public boolean isSuited(Card c1, Card c2){
		if(c1.getSuit() == c2.getSuit()) return true;
		return false;
	}
	public boolean isPair(Card c1, Card c2){
		if(c1.getValue() == c2.getValue()) return true;
		return false;
	}
	

}
