package configuration;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import entities.Game;
import entities.Player;
import useCases.DateExport;
import useCases.FeaturesEvaluate;
import useCases.StreamProcessor;

public class App {

	
	public static void main(String[] args) throws Exception {
						
		System.out.println("If you would like to get clustering input data press: 1");
		System.out.println("If you would like to get predictor input data: 2");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String userChoice = scanner.nextLine();
		boolean deleteAllfilesInDirectory = true;
		
		DateExport de;
		
		if (userChoice.equals("1")) {
			Configuration.createFoldersTempClustering();
			System.out.println("Processing data. Please wait.");
			List<Game> games = (new StreamProcessor()).processSourceForInit();
			FeaturesEvaluate fe = new FeaturesEvaluate();
			Set<Player> players = new HashSet<Player>();
			de = new DateExport();
			
			for(Game game : games){
				fe.evaluateVPIP(game);
				fe.evaluatePFR(game);
				fe.evaluateTriBet(game);
				fe.evaluateCBet(game);
				fe.evaluateOneGameWinRate(game);
				
				for(Player p : game.getPlayers()){
					players.add(p);
				}
			}			
			de = new DateExport();
			de.writeCSVForClustering(players);
			System.out.println("Data stored in: " + Configuration.getClusteringFilePath());
		}
		
		if (userChoice.equals("2")) {
			Configuration.createFoldersTempPrediction();
			List<String> playersOver15000 = new StreamProcessor().getPlayersFromFile(Configuration.getPlayersOver15000FilePath());
			System.out.println("Loading and processing data for prediction.");
			List<Game> games = (new StreamProcessor()).processSourceForPrediction();
			
			for (String playerName : playersOver15000) {
				System.out.println("Creating files for prediction for player " + playerName + ".");

				de = new DateExport(); //each player has own counter for inputFileForPredictor
				
				String pathForInputPrediction = Configuration.getStatsForPredictionRegularsDirectoryPath() + playerName + "/";
				new File(pathForInputPrediction).mkdir();
				new File(pathForInputPrediction + "Input/").mkdir();
				String patForOutputPrediction = pathForInputPrediction + "output.csv";
				de.writeCsvForDP3(pathForInputPrediction + "Input/", patForOutputPrediction, games, playerName, deleteAllfilesInDirectory);
				
			}
			
			File[] directories = new File(Configuration.getClusteredPlayersGroupsDirectoryPath()).listFiles(File::isFile);
			
			for (File file : directories) {
				List<String> playersNamesFromGroup = new StreamProcessor().getPlayersFromFile(file.toString());
				playersNamesFromGroup.remove(0);
				String groupName = file.toString().substring(file.toString().lastIndexOf("\\") + 1).replace(".csv", "");
				
				String pathForInputPrediction = Configuration.getStatsForPredictionGroupsDirectoryPath() + groupName + "/";
				new File(pathForInputPrediction).mkdir();
				new File(pathForInputPrediction + "Input/").mkdir();
				String patForOutputPrediction = pathForInputPrediction + "output.csv";
				deleteAllfilesInDirectory = true;
				de = new DateExport(); //each group of amateur players have own counter for inputFilesForPredictor
				System.out.println("Creating files for prediction for players grouped as " + groupName + ".");
				
				for (String playerName : playersNamesFromGroup) {
					de.writeCsvForDP3(pathForInputPrediction + "Input/", patForOutputPrediction, games, playerName.substring(1, playerName.length()-1), deleteAllfilesInDirectory);
					deleteAllfilesInDirectory = false;
				}
			}
			
			deleteAllfilesInDirectory = true;
			
			System.out.println("Players and groups data for prediction are stored in folders: " 
					+ Configuration.getStatsForPredictionRegularsDirectoryPath() + " and " 
					+ Configuration.getStatsForPredictionGroupsDirectoryPath() );
		}
	}

}
