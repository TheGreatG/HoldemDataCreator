package entities;

import java.util.HashMap;
import java.util.Map;

public class RangeTable {
	HashMap<CardsCombination, Integer> rangeTable = new HashMap<>();

	public HashMap<CardsCombination, Integer> getRangeTable() {
		return rangeTable;
	}

	public void setRangeTable(HashMap<CardsCombination, Integer> rangeTable) {
		this.rangeTable = rangeTable;
	}
	
	public RangeTable(Player p){
		for(Map.Entry<Game, CardsCombination> cardCombinations : p.getCardsCombinations().entrySet()) {
		    CardsCombination value = new CardsCombination(cardCombinations.getValue().suitControl()); //remake to suited or not suited
		    Boolean cardsAdded = false;
		    
		    for(Map.Entry<CardsCombination, Integer> cardsCombinationInTable : rangeTable.entrySet()) {
		    	if (compareCardsCombinations(cardsCombinationInTable.getKey(), value)) {
		    		rangeTable.put(cardsCombinationInTable.getKey(), cardsCombinationInTable.getValue() +1);
		    		cardsAdded = true;
				}
		    }
		    
		    if (!cardsAdded) {
		    	rangeTable.put(value, 1);
			}
	    
		}
	}
	
	
	public void printRangeTable(){
		for(Map.Entry<CardsCombination, Integer> combinationCounter : this.rangeTable.entrySet()) {
		    CardsCombination key = combinationCounter.getKey();
		    Integer value = combinationCounter.getValue();
		    
		    System.out.println("Card Combination: " + key.getCards().get(0).toString() + key.getCards().get(1).toString() + " count: " + value);
		}
	}
	
	public Boolean compareCardsCombinations(CardsCombination cc1, CardsCombination cc2){
		if (compareCards(cc1.getCards().get(0), cc2.getCards().get(0)) && compareCards(cc1.getCards().get(1), cc2.getCards().get(1))) {
			return true;
		}
		if (compareCards(cc1.getCards().get(0), cc2.getCards().get(1)) && compareCards(cc1.getCards().get(1), cc2.getCards().get(0))) {
			return true;
		}
		return false;
	}
	
	public Boolean compareCards(Card c1, Card c2){
		if (c1.getSuit() == c2.getSuit() && c1.getValue() == c2.getValue()) {
			return true;
		}
		return false;
	}
}
