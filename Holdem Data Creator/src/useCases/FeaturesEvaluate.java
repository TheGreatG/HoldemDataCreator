package useCases;

import entities.*;

public class FeaturesEvaluate {

	public void evaluateVPIP(Game game){
		
		
		for (Action action : game.getActions()) {
			
			if (action.getGameStateType() == GameStateType.PREFLOP) {
				
				if (action.getActionType() == ActionType.CALL || action.getActionType() == ActionType.RAISE) {
					action.getPlayer().getHandStats().getVpip().put(game, true);
				}else{
					if(!action.getPlayer().getHandStats().getVpip().containsKey(game)){
						action.getPlayer().getHandStats().getVpip().put(game, false);
					}				
				}
			}	
			
		}
		
		for(Player p : game.getPlayers()){
			
			if(!p.getHandStats().getVpip().containsKey(game)){
				p.getHandStats().getVpip().put(game, null);
			}
		}
				
	}
	
	public void evaluatePFR(Game game){
		
		double highestBet = 0.0; 
		
		for (Action action : game.getActions()) {	

			if (action.getGameStateType() == GameStateType.PREFLOP) {
				
				if (action.getActionType() == ActionType.RAISE) {
					action.getPlayer().getHandStats().getPfr().put(game, true);
					highestBet = ((ValueAction)action).getBetValue().getValueInBB();
				}else{
					
					if(highestBet >= action.getPlayer().getStacks().get(game).getValueInBB()){
						action.getPlayer().getHandStats().getPfr().put(game, null);
					}
					else {
						action.getPlayer().getHandStats().getPfr().put(game, false);
					}
					
				}
				
			}			
		}

		for (Player p : game.getPlayers()) {

			if (!p.getHandStats().getPfr().containsKey(game)) {
				p.getHandStats().getPfr().put(game, null);
			}
		}
		
	}
	
	public void evaluateTriBet(Game game){
		
		int raiseCount = 0; 
				
		for (Action action : game.getActions()) {

			if (action.getGameStateType() == GameStateType.PREFLOP) {
				
				if (action.getActionType() == ActionType.RAISE) {
					if (raiseCount == 1) {
						raiseCount++;
						action.getPlayer().getHandStats().getThreeBet().put(game, true);
					}
					else {
						raiseCount++;
						action.getPlayer().getHandStats().getThreeBet().put(game, null);
					}
				} else {
					if (raiseCount != 1) {
						action.getPlayer().getHandStats().getThreeBet().put(game, null);
					}
					else {
						action.getPlayer().getHandStats().getThreeBet().put(game, false);
					}

				}
			}
			
		}
	
		for (Player p : game.getPlayers()) {

			if (!p.getHandStats().getThreeBet().containsKey(game)) {
				p.getHandStats().getThreeBet().put(game, null);
			}
		}
	
	}
	
	public void evaluateCBet(Game game){
		
		Player lastRaiser = null;
		
		for(Action a : game.getActions()){
			
			if(a.getGameStateType() == GameStateType.PREFLOP){
				
				if(a.getActionType() == ActionType.RAISE){
					lastRaiser = a.getPlayer();
				}
				
			}
			
			if(a.getGameStateType() == GameStateType.FLOP && lastRaiser != null){
				
				if(a.getActionType() == ActionType.BET ){
					if(a.getPlayer() == lastRaiser){
						a.getPlayer().getHandStats().getcBet().put(game, true);
						break;
					}					
				}
				
				if(a.getPlayer() == lastRaiser && a.getActionType() != ActionType.BET){
					a.getPlayer().getHandStats().getcBet().put(game, false);
					break;
				}
				
			}
			
			for (Player p : game.getPlayers()) {

				if (!p.getHandStats().getcBet().containsKey(game)) {
					p.getHandStats().getcBet().put(game, null);
				}
			}
			
		}
		
		
	}
	
	public void evaluateDonkBet(Game game){
		
		Player lastRaiser = null;
		
		for(Action a : game.getActions()){
			
			if(a.getGameStateType() == GameStateType.PREFLOP){
				
				if(a.getActionType() == ActionType.RAISE){
					lastRaiser = a.getPlayer();
				}
				
			}
			
			if(a.getGameStateType() == GameStateType.FLOP && lastRaiser != null){
				
				if(a.getActionType() == ActionType.BET){
					if(a.getPlayer() != lastRaiser){
						a.getPlayer().getHandStats().getDonkBet().put(game, true);
						break;
					}					
				}
				
				if(a.getActionType() != ActionType.BET){
					if(a.getPlayer() != lastRaiser){
						a.getPlayer().getHandStats().getDonkBet().put(game, false);
					}
				}
				
				if(a.getPlayer() == lastRaiser){
					break;
				}
				
			}
			
			for (Player p : game.getPlayers()) {

				if (!p.getHandStats().getDonkBet().containsKey(game)) {
					p.getHandStats().getDonkBet().put(game, null);
				}
			}
			
		}
		
	}
	
	public void evaluateOneGameWinRate(Game game){
		
		
		for(Player p : game.getPlayers()){
			
			p.getHandStats().getOneGameWinRate().put(game, 0.0);
			p.resetGameInvestment();
			
			if(game.getPlayers().indexOf(p) == 1){
				p.getGameInvestment().setPreFlopInvestment(- game.getSmallBlind());
			}
			
			if(game.getPlayers().indexOf(p) == 2){
				p.getGameInvestment().setPreFlopInvestment( - game.getBigBlind());
			}
			
		}
		
		for(Action a : game.getActions()){
			
			if(a instanceof ValueAction){
				
				ValueAction va = (ValueAction) a;
				
				if(a.getActionType() == ActionType.WIN){
					
					a.getPlayer().getGameInvestment().setWin(va.getBetValue().getValueInBB());
				
				}else{
					
					
					if(a.getActionType() == ActionType.RAISE || a.getActionType() == ActionType.BET){
						
						if(a.getActionType() == ActionType.RAISE && getLastAggresiveAction(game, va) != null){
							
							
							if(!(isLastBetInGame(game, a))){
								setGameInvestment(a.getPlayer(), va, false);
							}else{
								setGameInvestment(a.getPlayer(), getLastAggresiveAction(game, va), true);
							}
						}else{
						
							if(!(isLastBetInGame(game, a))){
								setGameInvestment(a.getPlayer(), va, false);
							}
						}
						
					}else{
						setGameInvestment(a.getPlayer(), va, true);
					}
				}
			}
			
		}
		
		for(Player p : game.getPlayers()){
			
			Double temp = p.getGameInvestment().getWin();
			
			temp = temp + p.getGameInvestment().getPreFlopInvestment();
			temp = temp + p.getGameInvestment().getFlopInvestment();
			temp = temp + p.getGameInvestment().getTurnInvestment();
			temp = temp + p.getGameInvestment().getRiverInvestment();
			
			p.getHandStats().getOneGameWinRate().put(game, temp/game.getBigBlind());
		}
		
		
	}
	
	private static boolean isLastBetInGame(Game g, Action a){
				
		int i = g.getActions().indexOf(a);
		i++;
		
		for(; i < g.getActions().size(); i++){
			Action action = g.getActions().get(i);
			if(!(action.getActionType() == ActionType.FOLD || action.getActionType() == ActionType.WIN)){
				return false;
			}
		}
		
		return true;
	}
	
	private static void setGameInvestment(Player p, ValueAction va, boolean isSum){
		
		if(va.getGameStateType() == GameStateType.PREFLOP){
			if(isSum){
				p.getGameInvestment().setPreFlopInvestment(- va.getBetValue().getValueInBB() + p.getGameInvestment().getPreFlopInvestment());
			}else{
				p.getGameInvestment().setPreFlopInvestment(- va.getBetValue().getValueInBB());
			}
		}
		
		if(va.getGameStateType() == GameStateType.FLOP){
			if(isSum){
				p.getGameInvestment().setFlopInvestment(- va.getBetValue().getValueInBB() + p.getGameInvestment().getFlopInvestment());
			}else{
				p.getGameInvestment().setFlopInvestment(- va.getBetValue().getValueInBB());
			}
		}
		
		if(va.getGameStateType() == GameStateType.TURN){
			if(isSum){
				p.getGameInvestment().setTurnInvestment(- va.getBetValue().getValueInBB() + p.getGameInvestment().getTurnInvestment());
			}else{
				p.getGameInvestment().setTurnInvestment(- va.getBetValue().getValueInBB());
			}
		}
		
		if(va.getGameStateType() == GameStateType.RIVER){
			if(isSum){
				p.getGameInvestment().setRiverInvestment(- va.getBetValue().getValueInBB() + p.getGameInvestment().getRiverInvestment());
			}else{
				p.getGameInvestment().setRiverInvestment(- va.getBetValue().getValueInBB());
			}
		}
	}
	
	private ValueAction getLastAggresiveAction(Game g, Action a){
		
			
		int i = g.getActions().indexOf(a);
		
		for(i = g.getActions().indexOf(a) - 1; i > 0; i--){
			
			Action currentAction = g.getActions().get(i);
			
			if(currentAction.getGameStateType() != a.getGameStateType()){
				break;
			}
			
			if(currentAction.getActionType() == ActionType.RAISE || currentAction.getActionType() == ActionType.BET){
				return (ValueAction) currentAction; 
				
			}			
			
		}
		
		return null;
	}
	
}
