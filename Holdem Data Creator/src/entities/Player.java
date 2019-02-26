package entities;

import java.util.HashMap;
import java.util.List;

public class Player {
	private String ID; //Name
	private int numID;
	private List<Integer> onHotID;
	private HashMap<Game, ValueInBB> Stacks = new HashMap<Game, ValueInBB>();
	private HashMap<Game, CardsCombination> cardsCombinations = new HashMap<Game, CardsCombination>();
	private HashMap<Game, Position> positions = new HashMap<Game, Position>(); 
	private HandStats handStats = new HandStats();
	private boolean active = true; 
	private GameInvestment gameInvestment = new GameInvestment(); //for one game - temp variable
	private PlayerType playerType;
	
	public Player(String id){
		this.ID = id;
	}
	
	public Player(String id, int numID){
		this.ID = id;
		this.numID = numID;
	}
		
	public List<Integer> getOnHotID() {
		return onHotID;
	}

	public void setOnHotID(List<Integer> onHotID) {
		this.onHotID = onHotID;
	}
	
	public int getIsMyProPlayer(String playerName) {
		if (this.ID.equals(playerName)) return 1;
		return 0;
	}

	public int getNumID() {
		return numID;
	}

	public void setNumID(int numID) {
		this.numID = numID;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public void setPlayerType(PlayerType playerType) {
		this.playerType = playerType;
	}
	
	public void setStacks(HashMap<Game, ValueInBB> stacks) {
		Stacks = stacks;
	}

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	public HandStats getHandStats() {
		return handStats;
	}
	public HashMap<Game, CardsCombination> getCardsCombinations() {
		return cardsCombinations;
	}

	public void setCardsCombinations(HashMap<Game, CardsCombination> cardsCombinations) {
		this.cardsCombinations = cardsCombinations;
	}

	public HashMap<Game, Position> getPositions() {
		return positions;
	}

	public void setPositions(HashMap<Game, Position> positions) {
		this.positions = positions;
	}

	public void setHandStats(HandStats handStats) {
		this.handStats = handStats;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}	
	
	public HashMap<Game, ValueInBB> getStacks() {
		return Stacks;
	}

	public String toString(){
		return this.getID();
	}

	public GameInvestment getGameInvestment() {
		return gameInvestment;
	}

	public void setGameInvestment(GameInvestment gameInvestment) {
		this.gameInvestment = gameInvestment;
	}
	
	public void resetGameInvestment(){
		this.setGameInvestment(new GameInvestment()); 
	}
	
	public String toString(Game game){
		CardsCombinationTypeFinder cctf = new CardsCombinationTypeFinder();
		
		if(this.getCardsCombinations().get(game) != null){
			return "" + this.getID() + " -> " + this.getStacks().get(game).getValueInBB() + "--" 
			+ this.getCardsCombinations().get(game).getCards().get(0).toString() + "--" 
			+ this.getCardsCombinations().get(game).getCards().get(1).toString() 
			+ " category: " + cctf.getCardsCombinationType(this.getCardsCombinations().get(game));
		}
		return "" + this.getID() + " -> " + this.getStacks().get(game).getValueInBB();
	}
	
	public String toStringForDP2(Game game){ //vypise meno a kombinaciu zaradenu do skupiny
		CardsCombinationTypeFinder cctf = new CardsCombinationTypeFinder();
		
		if(this.getCardsCombinations().get(game) != null){
			return "" + this.getID() + " category: " + cctf.getCardsCombinationType(this.getCardsCombinations().get(game));
		}
		return "" + this.getID();
	}
}
