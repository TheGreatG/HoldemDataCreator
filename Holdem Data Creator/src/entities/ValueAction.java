package entities;

public class ValueAction extends Action {
	
	private ValueInBB betValue; //value of bet
	private ValueInBB stack;	//player´s stack before proceed action
	private ValueInBB actualPotValue; //pot size before proceed action
	private ValueOfAction valueOfActionToStackRatio;	//value of new action compared to player stack
	private ValueOfAction valueOfActionToPotRatio; //value of action compared to pot
	
	public ValueAction(ActionType actionType, Player player, GameStateType gameStateType, ValueInBB betValue) {
		super(actionType, player, gameStateType);
		this.betValue = betValue;
		
	}
	
	public ValueAction(ActionType actionType, Player player, GameStateType gameStateType, ValueInBB betValue, Game game, int noOfPlayers) {
		super(actionType, player, gameStateType, noOfPlayers);
		this.betValue = betValue; 
		this.stack = getActualStackSize(game, player);
		this.actualPotValue = getActualPotValue(game, player);
		this.valueOfActionToStackRatio = getValueOfActionToStackRatio(game);
		this.valueOfActionToPotRatio = getValueOfActionToPotRatio(game);		
	}
	
	public ValueInBB getActualStackSize(Game game, Player player){
		ValueInBB stack = new ValueInBB(player.getStacks().get(game).getValueInBB());
		double lastActionValue = 0.0;
		GameStateType gst = GameStateType.INIT;
		
		for (Action a : game.getActions()) {
			if (a.getPlayer() == player && a.getActionType() != ActionType.FOLD && a.getActionType() != ActionType.CHECK) {
				if (a.getGameStateType() != gst) {
					lastActionValue = 0.0;
					gst = a.getGameStateType();
				}
				ValueAction va = (ValueAction) a;
				stack.setValueInBB(stack.getValueInBB() - va.getBetValue().getValueInBB() + lastActionValue);
				lastActionValue = va.getBetValue().getValueInBB();
			}		
		}
		
		return stack;
	}

	public ValueInBB getActualPotValue(Game game, Player player){
		ValueInBB potValue = new ValueInBB(0);
		
		for (Action a : game.getActions()) {
			if (a.getActionType() != ActionType.FOLD && a.getActionType() != ActionType.CHECK) {
				ValueAction va = (ValueAction) a;
				potValue.setValueInBB(potValue.getValueInBB() + va.getBetValue().getValueInBB());
			}		
		}		
		
		return potValue;		
	}
	
	public ValueOfAction getValueOfStackToPotRatio(Game game){
		if (this.getGameStateType() == GameStateType.INIT) {
			return new ValueOfAction();
		}
		if (this.getGameStateType() == GameStateType.PREFLOP) {
			return new ValueOfAction(getEffectiveStackByPlayers(game, this.stack.getValueInBB()).getValueInBB(), getActualPotValue().getValueInBB());
		}
		return new ValueOfAction(getEffectiveStackByActions(game, this.stack.getValueInBB()).getValueInBB(), getActualPotValue().getValueInBB());		
	}	
	
	public ValueOfAction getValueOfActionToLastAction(Game game){
		double valueForCompare = 0.0;
		
		for (int i = game.getActions().size() - 1; i >= 0; i--) {
			if (isValueAction(game.getActions().get(i).getActionType())) {
				if (this.getGameStateType() == game.getActions().get(i).getGameStateType()) {
					ValueAction va = (ValueAction) game.getActions().get(i);
					valueForCompare = va.getBetValue().getValueInBB();
					return new ValueOfAction(getEffectiveStackByActions(game,this.betValue.getValueInBB()).getValueInBB(), valueForCompare);
				}				
			}			
		}
		
		return new ValueOfAction();		
	}
	
	public ValueOfAction getValueOfActionToStackRatio(Game game){
		if (this.getGameStateType() == GameStateType.INIT) {
			return new ValueOfAction();
		}
		if (this.getGameStateType() == GameStateType.PREFLOP && this.betValue.getValueInBB() >= getEffectiveStackByPlayers(game, this.stack.getValueInBB()).getValueInBB()) {
			return new ValueOfAction(1.0 , 1.0);
		}
		return new ValueOfAction(getEffectiveStackByActions(game,this.betValue.getValueInBB()).getValueInBB(), getEffectiveStackByActions(game, this.stack.getValueInBB()).getValueInBB());
	}
	
	public ValueOfAction getValueOfActionToPotRatio(Game game){
		if (this.getGameStateType() == GameStateType.INIT) {
			return new ValueOfAction();
		}
		if (this.getGameStateType() == GameStateType.PREFLOP) {
			return new ValueOfAction(getEffectiveStackByPlayers(game, this.betValue.getValueInBB()).getValueInBB(), getActualPotValue().getValueInBB());
		}
		return new ValueOfAction(getEffectiveStackByActions(game,this.betValue.getValueInBB()).getValueInBB(), this.actualPotValue.getValueInBB());
	}
	
	public ValueInBB getEffectiveStackByActions(Game game, double comparator){
		double effectiveStack = 0;
		
		for (Player player : game.getPlayers()) {
			if (!player.isActive()) {
				continue;
			}
			double effStackPlayerHelper = 0;
			for (Action playerAction : game.getActions()) {
				ValueAction valAct = (ValueAction) playerAction;
				if (valAct.getPlayer().getID().equals(player.getID())) {
					if (effStackPlayerHelper == 0) {
						effStackPlayerHelper = valAct.getStack().getValueInBB();
					}else {
						effStackPlayerHelper -= valAct.getBetValue().getValueInBB();
					}
				}
			}
			if (effStackPlayerHelper > effectiveStack) {
				effectiveStack = effStackPlayerHelper;
			}
		}
		
		if(effectiveStack > comparator){ //if value of action of player who makes action 
																// is less than effective stack of all other active players, then return active player´s value of action
			return new ValueInBB(comparator);
		}
		
		return new ValueInBB(effectiveStack);
	}
	
	public ValueInBB getEffectiveStackByPlayers(Game game, double comparator){
		ValueInBB effectiveStack = new ValueInBB(0.0);
		
		for (Player p : game.getPlayers()) {	//find the highest stack in active players, except of player who is making action			
				if(p.isActive() && !p.getID().equals(this.getPlayer().getID()) && effectiveStack.getValueInBB() <  p.getStacks().get(game).getValueInBB())
					effectiveStack = p.getStacks().get(game);
		}
		
		if(effectiveStack.getValueInBB() > comparator){ //if value of action of player who makes action 
																// is less than effective stack of all other active players, then return active player´s value of action
			return new ValueInBB(comparator);
		}
		
		return effectiveStack;
	}		

	public ValueInBB getActualPotValue() {
		return actualPotValue;
	}

	public void setActualPotValue(ValueInBB actualPotValue) {
		this.actualPotValue = actualPotValue;
	}
	
	public ValueInBB getStack() {
		return stack;
	}

	public void setStack(ValueInBB stack) {
		this.stack = stack;
	}

	public ValueOfAction getValueOfActionToStack() {
		return valueOfActionToStackRatio;
	}

	public void setValueOfActionToStack(ValueOfAction valueOfActionToStack) {
		this.valueOfActionToStackRatio = valueOfActionToStack;
	}

	public ValueOfAction getValueOfActionToPot() {
		return valueOfActionToPotRatio;
	}

	public void setValueOfActionToPot(ValueOfAction valueOfActionToPot) {
		this.valueOfActionToPotRatio = valueOfActionToPot;
	}
	
	public ValueInBB getBetValue() {
		return betValue;
	}

	public void setBetValue(ValueInBB betValue) {
		this.betValue = betValue;
	}
	
	public boolean isValueAction(ActionType at){
		if(at == ActionType.BET || at == ActionType.RAISE || at == ActionType.CALL) return true;
		return false;
	}
	
	public double fourAfterDecimal(double d){
		return (int)Math.round(d * 10000)/(double)10000;
	}

}
