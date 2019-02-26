package entities;

import java.util.ArrayList;
import java.util.List;


public class Game{
	
	private boolean flag = false;
	private String ID;
	private List<Player> players = new ArrayList<Player>(); //ordered from SB, BB, UTG, ... button last position
	private List<Action> actions = new ArrayList<Action>();
	private String dateString;
	private double bigBlind; 
	private double smallBlind;
	private int noOfPlayers;
	private CardsVector boardCards;	
	
	public Player isPlayerInTheGame(String playerName){
		for (Player player : players) {
			if (player.getID().equals(playerName)) {
				return player;
			}
		}
		return null;
	}
		
	public CardsVector getBoardCards() {
		return boardCards;
	}
	public void setBoardCards(List<Card> cards) {
		this.boardCards = new CardsVector(cards);
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public double getNoOfPlayers() {
		return noOfPlayers;
	}
	public void setNoOfPlayers(int noOfPlayers) {
		this.noOfPlayers = noOfPlayers;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public double getBigBlind() {
		return bigBlind;
	}
	public void setBigBlind(double bigBlind) {
		this.bigBlind = bigBlind;
	}
	public double getSmallBlind() {
		return smallBlind;
	}
	public void setSmallBlind(double smallBlind) {
		this.smallBlind = smallBlind;
	}
	
	
}
