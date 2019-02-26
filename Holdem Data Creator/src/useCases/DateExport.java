package useCases;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import configuration.Configuration;
import entities.Action;
import entities.ActionType;
import entities.CardsCombination;
import entities.Game;
import entities.GameStateType;
import entities.HandTypeFinder;
import entities.Player;
import entities.ValueAction;

public class DateExport {
	private int fileNumber = 0;

	ArrayList<String> headerForClustering = new ArrayList<String>() {{
		add("NAME");
	    add("VPIP");
	    add("PFR");
	    add("3BET");
	    add("CBET");
	}};
	
	ArrayList<String> headerPrediction23 = new ArrayList<String>() {{
		add("NAME_FLAG");
		add("GAME_STATE");
		add("ACTION_TYPE");
	    add("NO_OF_PLAYERS");
	    add("ASR");
	    add("APR");
	    add("17_CARD_VECTOR");
	}};
	
	
	ArrayList<String> handType = new ArrayList<String>() {{
		add("HAND_TYPE");
	}};
			
	
    public void writeCSVForClustering(Set<Player> players) throws Exception{

		FileWriter writer = new FileWriter(Configuration.getClusteringFilePath());
		NormalizeValues nv = new NormalizeValues();
		double winRateHelper = 0;
		List<String> playersOver15000 = new ArrayList<String>();
		
		CSVUtil.writeLine(writer, headerForClustering);
		
		List<String> values = null;
		for(Player p : players){
			
			values = new ArrayList<String>();
			
			//filter player with name contains ",", keep only amateur players
			if(p.getID().contains(",") || p.getStacks().size() > 15000 || p.getStacks().size() < 100){
				if (p.getStacks().size() > 15000) {
					playersOver15000.add(p.getID());
				}
				continue;
			}
			winRateHelper = nv.featureAVGDouble(p.getHandStats().getOneGameWinRate());
			if (winRateHelper > 0) {
				continue;
			}
			
			values.add(p.getID().trim());
			values.add(String.valueOf(twoAfterDecimal(nv.normalizeFeature(p.getHandStats().getVpip()))));
			values.add(String.valueOf(twoAfterDecimal(nv.normalizeFeature(p.getHandStats().getPfr()))));
			values.add(String.valueOf(twoAfterDecimal(nv.normalizeFeature(p.getHandStats().getThreeBet()))));
			values.add(String.valueOf(twoAfterDecimal(nv.normalizeFeature(p.getHandStats().getcBet()))));
				
			CSVUtil.writeLine(writer, values);
		}
		
		writer.flush();
		writer.close();
		
		writer = new FileWriter(Configuration.getPlayersOver15000FilePath());
		
		for(String playerName : playersOver15000){
			values = new ArrayList<String>();
			values.add(playerName);
			CSVUtil.writeLine(writer, values);
		}	
				
		writer.flush();
		writer.close();
	}
    
    public void writeCsvForDP3(String csvFileForInput, String csvFileForOutput, List<Game> games, String playerName, boolean deleteAllFiles) throws Exception{
    	writeInputDP3(csvFileForInput, games, playerName, deleteAllFiles);
    	writeOutputDP3HandType(csvFileForOutput, games, playerName, deleteAllFiles);   	
    }
    
    public void writeInputDP3(String csvFile, List<Game> games, String playerName, boolean deleteAllFiles) throws Exception{
    	
    	if (playerName.contains(",")) {
			return;
		}
    	
    	File directory = new File(csvFile); 
    	if (deleteAllFiles && directory.list().length > 0) {
    		for(File file: directory.listFiles()) //delete files in directory
        	    if (!file.isDirectory()) 
        	        file.delete();
		}    	
		
		int gameIndex = 0;
		Double noOfPlayersInTheGame;
		
		Player dreamPlayer = null;
    	while (dreamPlayer == null) {
    		for (Player player : games.get(gameIndex).getPlayers()) {
    			if (player.getID().equals(playerName)) {
    				dreamPlayer = player;
    			}
    		}
    		if (gameIndex < games.size()-1) {
				gameIndex++;
			}else {
				System.out.println("Cannot find player " + playerName + " in stored data.");
				System.exit(0);
			}
		}
				
		List<String> values = null;
		
		for (Map.Entry<Game, CardsCombination> entry : dreamPlayer.getCardsCombinations().entrySet()) {
					
			String fileAddress = new StringBuilder().append(csvFile).append(String.format("%08d", fileNumber++)).append("_").append(entry.getKey().getID().toString()).append(".csv").toString();
			FileWriter writer = new FileWriter(fileAddress);
			CSVUtil.writeLine(writer, headerPrediction23);
			noOfPlayersInTheGame = new Double(entry.getKey().getPlayers().size());
			
			for(Action a : entry.getKey().getActions()){
				
				//filter player with name contains ","
				if(a.getPlayer().getID().contains(",")){
					continue;
				}
				values = new ArrayList<String>();								
				
				ValueAction va = (ValueAction) a;
				
				values.add(String.valueOf(va.getPlayer().getIsMyProPlayer(playerName))); //flag if action is made by myProPlayer or no
				values.add(va.getGameStateType().getResponse()); //game state - number
				values.add(va.getActionType().getResponse()); //action type - number
				values.add(String.valueOf(fourAfterDecimal(noOfPlayersInTheGame/10))); //number of players in the game (normalize into (0,1) - number of players / 10)
				values.add(String.valueOf(fourAfterDecimal(va.getValueOfActionToStack().getValueOfAction()/10))); //value of this action to effective stack ratio (normalize - divided by 10)
				values.add(String.valueOf(fourAfterDecimal(va.getValueOfActionToPot().getValueOfAction()/10))); //value of effective action to pot ratio (normalize - divided by 10)
				if (va.getGameStateType() == GameStateType.FLOP) {
					for (int j = 0; j < entry.getKey().getBoardCards().getVector().length; j++) { //vector cards
						values.add(String.valueOf(entry.getKey().getBoardCards().getVector()[j]));
					}
				}
				else{
					for (int j = 0; j < entry.getKey().getBoardCards().getVector().length; j++) { //vector cards
						values.add("0.0");
					}
				}
				
				if (va.getActionType() == ActionType.FOLD) {
					noOfPlayersInTheGame -= 1;
				}				
									
				CSVUtil.writeLine(writer, values);
			}
			writer.flush();
			writer.close();
			
		}
	}
	
	@SuppressWarnings("resource")
	public void writeOutputDP3HandType(String csvFile, List<Game> games, String playerName, boolean deleteEverythingFromFile) throws Exception{
		if (playerName.contains(",")) {
			return;
		}
		FileWriter writer = new FileWriter(csvFile, true);
		
		if (deleteEverythingFromFile) {
			writer = new FileWriter(csvFile);
		}		
    	
    	int gameIndex = 0;
		
    	Player dreamPlayer = null;
    	while (dreamPlayer == null) {
    		for (Player player : games.get(gameIndex).getPlayers()) {
    			if (player.getID().equals(playerName)) {
    				dreamPlayer = player;
    			}
    		}
    		if (gameIndex < games.size()) {
				gameIndex++;
			}else {
				System.out.println("Cannot find player " + playerName + " in stored data.");
				System.exit(0);
			}
		}    	 
		
    	if (deleteEverythingFromFile) {
    		CSVUtil.writeLine(writer, handType);
		}		
				
		List<String> values = null;
		for (Map.Entry<Game, CardsCombination> entry : dreamPlayer.getCardsCombinations().entrySet()) {
			values = new ArrayList<String>();
			HandTypeFinder handTypeFinder = new HandTypeFinder(dreamPlayer.getCardsCombinations().get(entry.getKey()), entry.getKey().getBoardCards()); 
			values.add(String.valueOf(handTypeFinder.getHt().getResponse()));
			values.add(entry.getKey().getID());
			CSVUtil.writeLine(writer, values); 				
		}
			
		writer.flush();
		writer.close();
    }    

	public double fourAfterDecimal(double d){
		return (int)Math.round(d * 10000)/(double)10000;
	}
    public double twoAfterDecimal(double d){
		return (int)Math.round(d * 100)/(double)100;
	}
}