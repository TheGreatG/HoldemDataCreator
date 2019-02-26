package entities;

public class Action {
	private ActionType actionType;
	private Player player;
	private GameStateType gameStateType;
	private int noOfPlyers;

	public Action(ActionType actionType,Player player, GameStateType gameStateType) {
		super();
		this.actionType = actionType;
		this.player = player;
		this.gameStateType = gameStateType;
	}
	
	public Action(ActionType actionType,Player player, GameStateType gameStateType, int noOfPlayers) {
		super();
		this.actionType = actionType;
		this.player = player;
		this.gameStateType = gameStateType;
		this.noOfPlyers = noOfPlayers;
	}
	
	public int getNoOfPlyers() {
		return noOfPlyers;
	}
	public void setNoOfPlyers(int noOfPlyers) {
		this.noOfPlyers = noOfPlyers;
	}
	public ActionType getActionType() {
		return actionType;
	}
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public GameStateType getGameStateType() {
		return gameStateType;
	}
	public void setGameStateType(GameStateType gameStateType) {
		this.gameStateType = gameStateType;
	}

	public String toString(Game game){
		return "" + this.getPlayer().getID() + " -> " + this.getActionType().toString() + " (" + this.getGameStateType().toString()+")" 
		+ " " + this.getPlayer().getStacks().get(game).getValueInBB() + " pocet hracov: " + getNoOfPlyers();
	}
	
	public double fourAfterDecimal(double d){
		return (int)Math.round(d * 10000)/(double)10000;
	}
	
	public String toStringForDP2(Game game){		
		if(this.getActionType() == ActionType.WIN){
			return new String("");
		}
		return "" + this.getPlayer().getID() + 
		" stav hry: " + this.getGameStateType().toString() + 
		" typ akcie: " + this.getActionType().toString() +
		" pocet hracov: " + this.getNoOfPlyers();
	}
}
