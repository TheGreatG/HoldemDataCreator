package configuration;

import java.io.File;

public class Configuration {
//	private static String handHistory = "C:/PokerPredictor/HandHistory/";
//	private static String pokerPredictorFolderPath = "C:/PokerPredictor/";
//	private static String clusteringFolderPath = "C:/PokerPredictor/Clustering/";
//	private static String clusteringFilePath = "C:/PokerPredictor/Clustering/clusteringInputForR.csv";
//	private static String clusteredPlayersGroupsDirectoryPath = "C:/PokerPredictor/ClusteringGroups";
//	private static String playersOver15000FilePath = "C:/PokerPredictor/Clustering/playersOver15000.csv";
//	private static String statsForPredictionRegularsDirectoryPath = "C:/PokerPredictor/Prediction/"; 
//	private static String statsForPredictionGroupsDirectoryPath = "C:/PokerPredictor/PredictionGroups/";
//	private static String handToPredictDirectoryPath = "C:/PokerPredictor/HandToPredict/";
//	private static String modelSaverDirectoryPath = "C:/PokerPredictor/ModelSaver/";
//	private static String modelSaverEmptyDirectoryPath = "C:/PokerPredictor/ModelSaverEmpty/";
//	private static String modelsPlotDirectoryPath = "C:/PokerPredictor/ModelsPlots/";
	
	private static String handHistory = "Data/HH jan jul 2016/";
	private static String pokerPredictorFolderPath = "Data/PokerPredictor/";
	private static String clusteringFolderPath = "Data/PokerPredictor/Clustering/";
	private static String clusteringFilePath = "Data/PokerPredictor/Clustering/clusteringInputForR.csv";
	private static String clusteredPlayersGroupsDirectoryPath = "Data/PokerPredictor/ClusteringGroups";
	private static String playersOver15000FilePath = "Data/PokerPredictor/Clustering/playersOver15000.csv";
	private static String statsForPredictionRegularsDirectoryPath = "Data/PokerPredictor/Prediction/"; 
	private static String statsForPredictionGroupsDirectoryPath = "Data/PokerPredictor/PredictionGroups/";
	private static String handToPredictDirectoryPath = "Data/PokerPredictor/HandToPredict/";
	private static String modelSaverDirectoryPath = "Data/PokerPredictor/ModelSaver/";
	private static String modelSaverEmptyDirectoryPath = "Data/PokerPredictor/ModelSaverEmpty/";
	private static String modelsPlotDirectoryPath = "Data/PokerPredictor/ModelsPlots/";
	
	private static int maximumActionsInTheGame = 40; //dont store hands with more actions
	
	public static int getMaximumActionInTheGame(){
		return maximumActionsInTheGame;
	}
	
	public static String getClusteringFolderPath() {
		return clusteringFolderPath;
	}
	
	public static String getPokerPredictorFolderPath() {
		return pokerPredictorFolderPath;
	}

	public static String getClusteringFilePath() {
		return clusteringFilePath;
	}

	public static String getInputFilePath() {
		return handHistory;
	}
	
	public static String getClusteredPlayersGroupsDirectoryPath() {
		return clusteredPlayersGroupsDirectoryPath;
	}

	public static String getPlayersOver15000FilePath() {
		return playersOver15000FilePath;
	}	

	public static String getStatsForPredictionRegularsDirectoryPath() {
		return statsForPredictionRegularsDirectoryPath;
	}
	
	public static String getStatsForPredictionGroupsDirectoryPath() {
		return statsForPredictionGroupsDirectoryPath;
	}

	public static void createFoldersTempClustering(){
		File dir = new File(pokerPredictorFolderPath);
		if (!dir.exists()) {
			new File(pokerPredictorFolderPath).mkdir();
		}
		new File(clusteringFolderPath).mkdir();
		new File(clusteredPlayersGroupsDirectoryPath).mkdir();
	}
	
	public static void createFoldersTempPrediction(){
		File dir = new File(pokerPredictorFolderPath);
		if (!dir.exists()) {
			new File(pokerPredictorFolderPath).mkdir();
		}
		
		dir = new File(handToPredictDirectoryPath);
		if (!dir.exists()) {			
			new File(handToPredictDirectoryPath).mkdir();
		}
		
		dir = new File(modelSaverDirectoryPath);
		if (!dir.exists()) {
			new File(modelSaverDirectoryPath).mkdir();
		}
		
		dir = new File(modelSaverEmptyDirectoryPath);
		if (!dir.exists()) {
			new File(modelSaverEmptyDirectoryPath).mkdir();
		}
		
		dir = new File(modelsPlotDirectoryPath);
		if (!dir.exists()) {
			new File(modelsPlotDirectoryPath).mkdir();
		}
		
		new File(statsForPredictionRegularsDirectoryPath).mkdir();
		new File(statsForPredictionGroupsDirectoryPath).mkdir();
	}
	
}
