package entities;

public class PositionFinder {
	private Position position;

	public PositionFinder(int playerCount, int actualPlayerPosition) {
		super();
		this.position = findPosition(playerCount, actualPlayerPosition);
	}
	
	public Position findPosition(int playerCount, int actualPlayerPosition){
		
		if (playerCount==9) {
			switch(actualPlayerPosition){  
		    case 1: return Position.BLINDS;  
		    case 2: return Position.BLINDS;
		    case 3: return Position.EARLY;
		    case 4: return Position.EARLY;
		    case 5: return Position.MIDDLE;
		    case 6: return Position.MIDDLE;
		    case 7: return Position.MIDDLE;
		    case 8: return Position.LATE;
		    case 9: return Position.LATE;
		    }  
		}
		
		if (playerCount==8) {
			switch(actualPlayerPosition){  
		    case 1: return Position.BLINDS;  
		    case 2: return Position.BLINDS;
		    case 3: return Position.EARLY;
		    case 4: return Position.EARLY;
		    case 5: return Position.MIDDLE;
		    case 6: return Position.MIDDLE;
		    case 7: return Position.LATE;
		    case 8: return Position.LATE;
		    }  
		}
		
		if (playerCount==7 || playerCount==6) {
			switch(actualPlayerPosition){  
		    case 1: return Position.BLINDS;  
		    case 2: return Position.BLINDS;
		    case 3: return Position.EARLY;
		    case 4: return Position.MIDDLE;
		    case 5: return Position.MIDDLE;
		    case 6: return Position.LATE;
		    case 7: return Position.LATE;
		    }  
		}
		
		if (playerCount==5) {
			switch(actualPlayerPosition){  
		    case 1: return Position.BLINDS;  
		    case 2: return Position.BLINDS;
		    case 3: return Position.EARLY;
		    case 4: return Position.MIDDLE;
		    case 5: return Position.LATE;
		    }  
		}
		
		if (playerCount==4) {
			switch(actualPlayerPosition){  
		    case 1: return Position.BLINDS;  
		    case 2: return Position.BLINDS;
		    case 3: return Position.EARLY;
		    case 5: return Position.LATE;
		    }  
		}
		
		if (playerCount==3 || playerCount==2) {
			switch(actualPlayerPosition){  
		    case 1: return Position.BLINDS;  
		    case 2: return Position.BLINDS;
		    case 5: return Position.LATE;
		    }  
		}
		
		
		return position;		
	}
	
}
