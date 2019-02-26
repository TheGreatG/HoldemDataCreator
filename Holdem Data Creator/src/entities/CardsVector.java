package entities;

import java.util.List;

public class CardsVector {
	private double[] vector = new double[17];

	public CardsVector() {
		super();
	}

	public CardsVector(List<Card> cards) {
		for (Card card : cards) {
			switch (card.getValue().toString()) {
			case "ACE": this.vector[0] += 0.3;break;
			case "TWO": this.vector[1] += 0.3;break;
			case "THREE": this.vector[2] += 0.3;break;
			case "FOUR": this.vector[3] += 0.3;break;
			case "FIVE": this.vector[4] += 0.3;break;
			case "SIX": this.vector[5] += 0.3;break;
			case "SEVEN": this.vector[6] += 0.3;break;
			case "EIGHT": this.vector[7] += 0.3;break;
			case "NINE": this.vector[8] += 0.3;break;
			case "TEN": this.vector[9] += 0.3;break;
			case "JACK": this.vector[10] += 0.3;break;
			case "QUEEN": this.vector[11] += 0.3;break;
			case "KING": this.vector[12] += 0.3;break;
			default:
				break;
			}
			switch (card.getSuit().toString()) {
			case "SPADE": this.vector[13] += 0.3;break;
			case "HEART": this.vector[14] += 0.3;break;
			case "DIAMOND": this.vector[15] += 0.3;break;
			case "CLUB": this.vector[16] += 0.3;break;
			default:
				break;
			}
		}
	}

	public double[] getVector() {
		return vector;
	}

	public void setVector(double[] vector) {
		this.vector = vector;
	}
	
	public int findHighest(CardsVector hand){
		int highest = 0;
		for (int i = 0; i < vector.length; i++) {
			if (hand.getVector()[i] != 0.0) {
				highest = i;
			}
		}		
		return highest;
	}
	
	public int findLowest(CardsVector hand){
		int lowest = 13;
		for (int i = vector.length -1; i <= 0; i--) {
			if (hand.getVector()[i] != 0.0) {
				lowest = i;
			}
		}		
		return lowest;
	}
	
}
