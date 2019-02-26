package useCases;

import java.util.HashMap;

import entities.Game;
import entities.ValueInBB;

public class NormalizeValues {
	
	public Double normalizeFeature(HashMap<Game, Boolean> feature){
		int positiveCount = 0;
		int allCount = 0; 
		for(Game g : feature.keySet()){
			Boolean tempValue = feature.get(g);
			if(tempValue != null){
				allCount++;
				if(tempValue){
					positiveCount++;
				}
			}
		}
		
		Double result = 0.0;
		if(allCount != 0){
			result = ((double)positiveCount/(double)allCount);
		}
		
		return result; 
	}
	
	public Double featureAVGDouble(HashMap<Game, Double> feature){
		
		Double sum = 0.0;
		for(Game g : feature.keySet()){
			sum = sum + feature.get(g);
		}			
		
		return sum/feature.size(); 
	}
	
	public Double featureAVG(HashMap<Game, ValueInBB> feature){
		
		Double sum = 0.0;
		for(Game g : feature.keySet()){
			sum = sum + feature.get(g).getValueInBB();
		}			
		
		return sum/feature.size(); 
	}
	
	
}
