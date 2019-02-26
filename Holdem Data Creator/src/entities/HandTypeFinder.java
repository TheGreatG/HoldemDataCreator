package entities;

public class HandTypeFinder {
	private HandType ht = HandType.NOTHING;
	private HandType3Ways ht3ways = HandType3Ways.NOTHING;

	public HandTypeFinder(CardsCombination myHandCC, CardsVector desk) {
		
		CardsVector myHand = new CardsVector(myHandCC.getCards());
		
		boolean pair = false;
		boolean flush = false;
		boolean straight = false;
		boolean straightDraw = false;
		int straightDrawCounter;
		boolean flushDraw = false;
		
		for (int i = 0; i < 13; i++) { //go along all cards values (no suites) - pair finder
			if (myHand.getVector()[i] > 0 && desk.getVector()[i] > 0 ) {
				pair = true;
			}
			if (myHand.getVector()[i] > 0.3) {
				pair = true;
			}			
		}
		if (pair) { //if pair is third pair on board or lower, than no pair hand 
			if (myHand.findHighest(myHand) <= desk.findLowest(desk)) {
				pair = false;
			}
		}
		for (int i = 0; i < 10; i++) { //go along all cards values start up to TEN - straightDraw finder
			straightDrawCounter = 0;
			if (myHand.getVector()[i] > 0 || desk.getVector()[i] > 0 ) {
				straightDrawCounter++;
			}
			if (myHand.getVector()[i+1] > 0 || desk.getVector()[i+1] > 0 ) {
				straightDrawCounter++;
			}
			if (myHand.getVector()[i+2] > 0 || desk.getVector()[i+2] > 0 ) {
				straightDrawCounter++;
			}
			if (myHand.getVector()[i+3] > 0 || desk.getVector()[i+3] > 0 ) {
				straightDrawCounter++;
			}
			if (myHand.getVector()[(i+4)%13] > 0 || desk.getVector()[(i+4)%13] > 0 ) {
				straightDrawCounter++;
			}
			if (straightDrawCounter == 4) {
				straightDraw = true;
			}
			if (straightDrawCounter == 5) {
				straight = true;
			}
		}
		if (straight == true) { //check if there is straight completed, there is no straightDraw anymore
			straightDraw = false;
		}
		for (int i = 13; i < 17; i++) { //go along all cards suites - flush and flushDraw finder
			if (myHand.getVector()[i] + desk.getVector()[i] == 1.2) {
				flushDraw = true;
			}
			if (myHand.getVector()[i] + desk.getVector()[i] == 1.5) {
				flush = true;
			}
		}
		
		if (pair || straight || flush) {
			this.ht = HandType.MADE_HAND;
			this.ht3ways = HandType3Ways.MADE_HAND;
		}		
		if (straightDraw) {
			this.ht = HandType.STRAIGHT_DRAW;
			this.ht3ways = HandType3Ways.DRAW;
		}
		if (flushDraw) {
			this.ht = HandType.FLUSH_DRAW;
			this.ht3ways = HandType3Ways.DRAW;
		}
		if (straightDraw && flushDraw) {
			this.ht = HandType.COMBO_DRAW;
			this.ht3ways = HandType3Ways.DRAW;
		}
		if (pair && (straightDraw || flushDraw)) {
			this.ht = HandType.MADE_HAND_W_FLUSH_DRAW;
			this.ht3ways = HandType3Ways.DRAW;
		}
		
	}

	public HandType getHt() {
		return ht;
	}

	public void setHt(HandType ht) {
		this.ht = ht;
	}

	public HandType3Ways getHt3ways() {
		return ht3ways;
	}

	public void setHt3ways(HandType3Ways ht3ways) {
		this.ht3ways = ht3ways;
	}
	
	

}
