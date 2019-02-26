package useCases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import configuration.Configuration;
import dataprovider.FileLineProvider;
import dataprovider.ILineProvider;
import entities.ActionType;
import entities.Card;
import entities.CardsCombination;
import entities.Game;
import entities.GameStateType;
import entities.Player;
import entities.ValueAction;
import entities.ValueInBB;



public class StreamProcessor {
	
	private ILineProvider iLineProvider;
	
	public StreamProcessor(){
		this.iLineProvider = new FileLineProvider();
	}
	
	public StreamProcessor(ILineProvider iLineProvider){
		this.iLineProvider = iLineProvider;
	}
	
	public ILineProvider getiLineProvider() {
		return iLineProvider;
	}

	public void setiLineProvider(ILineProvider iLineProvider) {
		this.iLineProvider = iLineProvider;
	}
	
	public List<String> getPlayersFromFile(String filePath){
		List<String> playersFromFile = new ArrayList<String>();
		
		this.iLineProvider.openSource(filePath);
		String line;
		
		while((line = this.iLineProvider.getLine()) != null){
			playersFromFile.add(line);
		}
		
		return playersFromFile;
	}
	
	public List<Game> processSourceForPrediction(){
		
		List<Game> games = new ArrayList<Game>();
		
		this.iLineProvider.openSource(Configuration.getInputFilePath());
		
		String line;
		Game currentGame = null;
		int buttonNumber = -1;
		int buttonNumberInList = -1;
		List<Player> currentPlayers = new ArrayList<Player>();
		int numOfPlayers = 0;
		GameStateType state = GameStateType.END;
		Map<String,Player> allPlayers = new HashMap<String, Player>();
		boolean collected = false;
		int numericID = 0;
		boolean isShowDown = false;
		List<Card> cards = new ArrayList<Card>();
		String firstCard;
		String secondCard;
		String thirdCard;
		Card firstC;
		Card secondC;
		Card thirdC;
		boolean isFlop = false;
		boolean isAllinPreflop = false;
		String lastLine = null;
		
		
		while((line = this.iLineProvider.getLine()) != null){
			
			String euroSign = new String("€");
			String poundSign = new String("£");
			//Replace all EURO Symbols to $
			line = line.replace( euroSign, "$" );
			line = line.replace( poundSign, "$" );
			
			//Replace all EURO Symbols to $
			line = line.replace( '\u00A3', '$' );
			
			
			if(line.contains(" said,")){
				continue;
			}
			
			if(line.startsWith("*** SUMMARY ***")){
				state = GameStateType.END;
				numOfPlayers = 0;
			}
		
			if(line.startsWith("*** FLOP ***") || line.startsWith("*** FIRST FLOP ***")){
				state = GameStateType.FLOP;
				cards = new ArrayList<Card>();
				isFlop = true;
			}
			
			if(line.startsWith("*** TURN ***") || line.startsWith("*** FIRST TURN ***")){
				state = GameStateType.TURN;
				if (lastLine.contains("*** FLOP ***") || line.startsWith("*** FIRST FLOP ***")) { //if no actions between flop mark and turn mark, than all in was preflop
					isAllinPreflop = true;
				}
			}
			
			if(line.startsWith("*** RIVER ***") || line.startsWith("*** FIRST RIVER ***")){
				state = GameStateType.RIVER;
			}
			
	
			// new hand (game)
			if(state == GameStateType.END) {
				if(line.contains("PokerStars Hand") || line.contains("PokerStars Zoom Hand")) {
					
					if (isAllinPreflop == true || isShowDown == false || currentGame.getActions().size() > Configuration.getMaximumActionInTheGame()) { //if hand was all in preflop or cards was not revealed or number of action > 40, then dont store that hand
						games.remove(currentGame);
					}
				
					currentGame = new Game();
					isAllinPreflop = false;
					isShowDown = false;
					collected = false;

					currentGame.setID(line.substring(line.indexOf("#") + 1, line.indexOf(":")));
					currentGame.setDateString(line.substring(line.indexOf("-") + 1, line.indexOf("ET")).trim());
					
					//find big blind
					String temp = line.substring(line.indexOf("/") + 2);
					String temp2 = temp.substring(0, temp.indexOf(" "));
					temp2 = temp2.replaceAll("[^\\d.]", "");
					currentGame.setBigBlind(Double.parseDouble(temp2));
					
					//find small blind
					temp = line.substring(line.indexOf("($")+2, line.indexOf("/"));
					temp = temp.replaceAll("[^\\d.]", "");
					currentGame.setSmallBlind(Double.parseDouble(temp));
					
					
					state = GameStateType.INIT;
				}
				
				if(line.contains(" showed [")){
					
					isShowDown = true;
					
					String playerName = line.substring(line.indexOf(":") + 1, line.indexOf(" showed [")).trim();
									
					if(playerName.contains("(button)") || playerName.contains("(small blind)")
							|| playerName.contains("(big blind)")){
						String[] playerNameSplit = playerName.split(" \\(");
						playerName = playerNameSplit[0].trim();
					}
					if (!isAllinPreflop) {
						String first = line.substring(line.indexOf(" showed [") + 9, line.indexOf(" showed [") + 11);
						String second = line.substring(line.indexOf(" showed [") + 12, line.indexOf(" showed [") + 14);
						allPlayers.get(playerName).getCardsCombinations().put(currentGame, new CardsCombination(first, second));	
					}	

				}
				
				if(line.contains(" mucked [")){
					
					isShowDown = true;
					
					String playerName = line.substring(line.indexOf(":") + 1, line.indexOf(" mucked [")).trim();
					
									
					if(playerName.contains("(button)") || playerName.contains("(small blind)")
							|| playerName.contains("(big blind)")){
						String[] playerNameSplit = playerName.split(" \\(");
						playerName = playerNameSplit[0].trim();
					}
					if (!isAllinPreflop) {
						String first = line.substring(line.indexOf(" mucked [") + 9, line.indexOf(" mucked [") + 11);
						String second = line.substring(line.indexOf(" mucked [") + 12, line.indexOf(" mucked [") + 14);
						allPlayers.get(playerName).getCardsCombinations().put(currentGame, new CardsCombination(first, second));
					}
					
				}
				
			}
			
			if(state == GameStateType.INIT){

				if(line.startsWith("Table ")){
					buttonNumber = Integer.parseInt(line.substring(line.indexOf("#") + 1, line.indexOf("#")+2));
				}
				
				if (line.startsWith("Seat ")){
					String playerName = line.substring(line.indexOf(":") + 1, line.indexOf("($")).trim();										
					Double playerStack = Double.parseDouble(line.substring(line.indexOf("($") + 2, line.indexOf("in chips")).trim());									
					
					if (!allPlayers.containsKey(playerName)) {
						allPlayers.put(playerName, new Player(playerName, numericID));
						numericID++;
					}
					
					allPlayers.get(playerName).setActive(true);
					currentPlayers.add(allPlayers.get(playerName));
					allPlayers.get(playerName).getStacks().put(currentGame, new ValueInBB(playerStack, currentGame.getBigBlind()));
					numOfPlayers++;				
					
					if(Integer.parseInt(line.substring(line.indexOf(":") - 1, line.indexOf(":"))) == buttonNumber){					
						buttonNumberInList = currentPlayers.indexOf(allPlayers.get(playerName));
					}
				}

				if (!line.startsWith("Seat ") && !line.startsWith("Table ") && !line.contains("PokerStars Hand") && !line.contains("PokerStars Zoom Hand") && line.contains(": posts ")) {
					String playerName = line.substring(0 , line.indexOf(": posts "));
					String delimeter = new String(" blind $");
					if (line.contains("posts small & big blinds")) {
						delimeter = "blinds $";
					}
					if (line.contains("the ante ")) {
						delimeter = "the ante $";
					}
					String value = line.substring(line.indexOf(delimeter) + delimeter.length(), line.length());
					currentGame.getActions().add(new ValueAction(ActionType.RAISE, allPlayers.get(playerName), state, new ValueInBB(Double.parseDouble(value), currentGame.getBigBlind()), currentGame, numOfPlayers));
				}
				
				if(line.startsWith("*** HOLE CARDS ***")){
					
					int currentPosition = buttonNumberInList;
					for (int i = 0; i < numOfPlayers; i++) {
						currentGame.getPlayers().add(currentPlayers.get(currentPosition));
						
						if(currentPlayers.size()-1 < ++currentPosition){
							currentPosition = 0;
						}
						
					}
					
					currentPlayers.clear();
					games.add(currentGame);
					state = GameStateType.PREFLOP;
				}
			}
			
			if(state == GameStateType.PREFLOP || state == GameStateType.FLOP){	
								
				if (line.contains("*** [")) {
					firstCard = line.substring(line.indexOf("[") + 1, line.indexOf("[") + 3);
					secondCard = line.substring(line.indexOf("[") + 4, line.indexOf("[") + 6);
					thirdCard = line.substring(line.indexOf("[") + 7, line.indexOf("[") + 9);
					firstC = new Card(firstCard);
					secondC = new Card(secondCard);
					thirdC = new Card(thirdCard);
					cards.add(firstC);
					cards.add(secondC);
					cards.add(thirdC);
					currentGame.setBoardCards(cards);
				}
				
				if(line.contains(" folds ")){
					
					currentGame.getActions().add(new ValueAction(ActionType.FOLD, allPlayers.get(line.substring(0, line.indexOf(" folds")-1)), state, new ValueInBB(0.0, currentGame.getBigBlind()), currentGame, numOfPlayers));
					
					allPlayers.get(line.substring(0, line.indexOf(" folds")-1)).setActive(false);
					numOfPlayers--;
				}
								
				if(line.contains(" raises ")){
					Double value = Double.parseDouble(line.substring(line.lastIndexOf("$")+1).split(" ")[0]);
					currentGame.getActions().add(new ValueAction(ActionType.RAISE, allPlayers.get(line.substring(0, line.indexOf(" raises")-1)), state, new ValueInBB(value, currentGame.getBigBlind()), currentGame, numOfPlayers));
				}
				
				if(line.contains(" calls ")){
					Double value = Double.parseDouble(line.substring(line.lastIndexOf("$")+1).split(" ")[0]);
					currentGame.getActions().add(new ValueAction(ActionType.CALL, allPlayers.get(line.substring(0, line.indexOf(" calls")-1)), state, new ValueInBB(value, currentGame.getBigBlind()), currentGame, numOfPlayers));
				}
				
				if(line.contains(" bets ")){
					Double value = Double.parseDouble(line.substring(line.lastIndexOf("$")+1).split(" ")[0]);
					currentGame.getActions().add(new ValueAction(ActionType.BET, allPlayers.get(line.substring(0, line.indexOf(" bets")-1)), state, new ValueInBB(value, currentGame.getBigBlind()), currentGame, numOfPlayers));
				}
				
				if(line.contains(" checks ")){
					currentGame.getActions().add(new ValueAction(ActionType.CHECK, allPlayers.get(line.substring(0, line.indexOf(" checks")-1)), state, new ValueInBB(0.0, currentGame.getBigBlind()), currentGame, numOfPlayers));
				}
				
			}
			
			lastLine = new String(line);			
		}
		return games;
	}
		
	
		public List<Game> processSourceForInit(){
			
			List<Game> games = new ArrayList<Game>();
			
			this.iLineProvider.openSource(Configuration.getInputFilePath());
			
			String line;
			Game currentGame = null;
			int buttonNumber = -1;
			int buttonNumberInList = -1;
			List<Player> currentPlayers = new ArrayList<Player>();
			int numOfPlayers = 0;
			GameStateType state = GameStateType.END;
			Map<String,Player> allPlayers = new HashMap<String, Player>();
			boolean collected = false;
			int numericID = 0;
			
			while((line = this.iLineProvider.getLine()) != null){
				
				String euroSign = new String("€");
				String poundSign = new String("£");
				//Replace all EURO Symbols to $
				line = line.replace( euroSign, "$" );
				line = line.replace( poundSign, "$" );
				
				//Replace all EURO Symbols to $
				line = line.replace( '\u00A3', '$' );
				
				//System.out.println(line);
				if(line.contains(" said,")){
					continue;
				}
				
				if(line.contains("collected $") && !collected){
					
					String playerName = line.substring(0, line.indexOf("collected $")).trim();					
					String temp2 = line.substring(line.indexOf("collected $") + 11, line.indexOf(" from "));
					Double winSum = Double.parseDouble(temp2);
					
					ValueAction va = new ValueAction(ActionType.WIN, allPlayers.get(playerName), GameStateType.END, new ValueInBB(winSum, currentGame.getBigBlind()));
					
					currentGame.getActions().add(va);
					collected = true;
				}
				
				
				// new hand (game)
				if(state == GameStateType.END) {
					if(line.contains("PokerStars Hand") || line.contains("PokerStars Zoom Hand")) {
						
						currentGame = new Game();
						collected = false;

						currentGame.setID(line.substring(line.indexOf("#") + 1, line.indexOf(":")));
						currentGame.setDateString(line.substring(line.indexOf("-") + 1, line.indexOf("ET")).trim());
						
						//find big blind
						String temp = line.substring(line.indexOf("/") + 2);
						String temp2 = temp.substring(0, temp.indexOf(" "));
						temp2 = temp2.replaceAll("[^\\d.]", "");
						currentGame.setBigBlind(Double.parseDouble(temp2));
						
						//find small blind
						temp = line.substring(line.indexOf("($")+2, line.indexOf("/"));
						temp = temp.replaceAll("[^\\d.]", "");
						currentGame.setSmallBlind(Double.parseDouble(temp));
						
						
						state = GameStateType.INIT;
					}
					
					if(line.contains("collected")){
						
						String playerName = line.substring(line.indexOf(":") + 1, line.indexOf("collected")).trim();
						
										
						if(playerName.contains("(button)") || playerName.contains("(small blind)")
								|| playerName.contains("(big blind)")){
							String[] playerNameSplit = playerName.split(" \\(");
							playerName = playerNameSplit[0].trim();
						}
						
						
						String temp = line.substring(line.indexOf("collected"));
						String temp2 = temp.substring(temp.indexOf("(") + 2, temp.indexOf(")"));
						Double winSum = Double.parseDouble(temp2);
						
						ValueAction va = new ValueAction(ActionType.WIN, allPlayers.get(playerName), GameStateType.END, new ValueInBB(winSum, currentGame.getBigBlind()));
						
						currentGame.getActions().add(va);
					}
					
					if(line.contains(" showed [")){
						
						String playerName = line.substring(line.indexOf(":") + 1, line.indexOf(" showed [")).trim();
						
										
						if(playerName.contains("(button)") || playerName.contains("(small blind)")
								|| playerName.contains("(big blind)")){
							String[] playerNameSplit = playerName.split(" \\(");
							playerName = playerNameSplit[0].trim();
						}
						
						
						String first = line.substring(line.indexOf(" showed [") + 9, line.indexOf(" showed [") + 11);
						String second = line.substring(line.indexOf(" showed [") + 12, line.indexOf(" showed [") + 14);
						allPlayers.get(playerName).getCardsCombinations().put(currentGame, new CardsCombination(first, second));

					}
					
					if(line.contains(" mucked [")){
						
						String playerName = line.substring(line.indexOf(":") + 1, line.indexOf(" mucked [")).trim();
						
										
						if(playerName.contains("(button)") || playerName.contains("(small blind)")
								|| playerName.contains("(big blind)")){
							String[] playerNameSplit = playerName.split(" \\(");
							playerName = playerNameSplit[0].trim();
						}
						
						
						String first = line.substring(line.indexOf(" mucked [") + 9, line.indexOf(" mucked [") + 11);
						String second = line.substring(line.indexOf(" mucked [") + 12, line.indexOf(" mucked [") + 14);
						allPlayers.get(playerName).getCardsCombinations().put(currentGame, new CardsCombination(first, second));

					}
					
				}
				
				if(state == GameStateType.INIT){

					if(line.startsWith("Table ")){
						buttonNumber = Integer.parseInt(line.substring(line.indexOf("#") + 1, line.indexOf("#")+2));
					}
					
					if (line.startsWith("Seat ")) {
						String playerName = line.substring(line.indexOf(":") + 1, line.indexOf("($")).trim();										
						Double playerStack = Double.parseDouble(line.substring(line.indexOf("($") + 2, line.indexOf("in chips")).trim());									
						
						if (!allPlayers.containsKey(playerName)) {
							allPlayers.put(playerName, new Player(playerName, numericID));
							numericID++;
						}
						
						allPlayers.get(playerName).setActive(true);
						currentPlayers.add(allPlayers.get(playerName));
						allPlayers.get(playerName).getStacks().put(currentGame, new ValueInBB(playerStack, currentGame.getBigBlind()));
						numOfPlayers++;				
						
						if(Integer.parseInt(line.substring(line.indexOf(":") - 1, line.indexOf(":"))) == buttonNumber){					
							buttonNumberInList = currentPlayers.indexOf(allPlayers.get(playerName));
						}
					}

					if (!line.startsWith("Seat ") && !line.startsWith("Table ") && !line.contains("PokerStars Hand") && !line.contains("PokerStars Zoom Hand") && line.contains(": posts ")) {
						String playerName = line.substring(0 , line.indexOf(": posts "));
						String delimeter = new String(" blind $");
						if (line.contains("posts small & big blinds")) {
							delimeter = "blinds $";
						}
						if (line.contains("the ante ")) {
							delimeter = "the ante $";
						}
						String value = line.substring(line.indexOf(delimeter) + delimeter.length(), line.length());
						currentGame.getActions().add(new ValueAction(ActionType.RAISE, allPlayers.get(playerName), state, new ValueInBB(Double.parseDouble(value), currentGame.getBigBlind()), currentGame, numOfPlayers));
					}
					
					if(line.startsWith("*** HOLE CARDS ***")){
						
						int currentPosition = buttonNumberInList;
						for (int i = 0; i < numOfPlayers; i++) {
							currentGame.getPlayers().add(currentPlayers.get(currentPosition));
							
							if(currentPlayers.size()-1 < ++currentPosition){
								currentPosition = 0;
							}
							
						}
						
						currentPlayers.clear();
						games.add(currentGame);
						state = GameStateType.PREFLOP;
					}
				}
				
				if(state == GameStateType.PREFLOP || state == GameStateType.FLOP || state == GameStateType.RIVER || state == GameStateType.TURN){
					
					if(line.contains(" folds ")){
						currentGame.getActions().add(new ValueAction(ActionType.FOLD, allPlayers.get(line.substring(0, line.indexOf(" folds")-1)), state, new ValueInBB(0.0, currentGame.getBigBlind()), currentGame, numOfPlayers));
						allPlayers.get(line.substring(0, line.indexOf(" folds")-1)).setActive(false);
						numOfPlayers--;
					}
									
					if(line.contains(" raises ")){
						Double value = Double.parseDouble(line.substring(line.lastIndexOf("$")+1).split(" ")[0]);
						currentGame.getActions().add(new ValueAction(ActionType.RAISE, allPlayers.get(line.substring(0, line.indexOf(" raises")-1)), state, new ValueInBB(value, currentGame.getBigBlind()), currentGame, numOfPlayers));
					}
					
					if(line.contains(" calls ")){
						Double value = Double.parseDouble(line.substring(line.lastIndexOf("$")+1).split(" ")[0]);
						currentGame.getActions().add(new ValueAction(ActionType.CALL, allPlayers.get(line.substring(0, line.indexOf(" calls")-1)), state, new ValueInBB(value, currentGame.getBigBlind()), currentGame, numOfPlayers));
					}
					
					if(line.contains(" bets ")){
						Double value = Double.parseDouble(line.substring(line.lastIndexOf("$")+1).split(" ")[0]);
						currentGame.getActions().add(new ValueAction(ActionType.BET, allPlayers.get(line.substring(0, line.indexOf(" bets")-1)), state, new ValueInBB(value, currentGame.getBigBlind()), currentGame, numOfPlayers));
					}
					
					if(line.contains(" checks ")){
						currentGame.getActions().add(new ValueAction(ActionType.CHECK, allPlayers.get(line.substring(0, line.indexOf(" checks")-1)), state, new ValueInBB(0.0, currentGame.getBigBlind()), currentGame, numOfPlayers));
						
					}
					
				}
				
				if(line.startsWith("*** SUMMARY ***")){
					state = GameStateType.END;
					numOfPlayers = 0;
				}
			
				if(line.startsWith("*** FLOP ***")){
					state = GameStateType.FLOP;
				}
				
				if(line.startsWith("*** TURN ***")){
					state = GameStateType.TURN;
				}
				
				if(line.startsWith("*** RIVER ***")){
					state = GameStateType.RIVER;
				}
				
			}
		
		return games;
	}
	
	
}
