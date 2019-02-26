package dataprovider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileLineProvider implements ILineProvider{
	
	private BufferedReader br = null;
	private List<File> inputFiles = new ArrayList<File>();
	private int index = 0;
	
	
	@Override
	public void openSource(String sourceFile) {
		
			File inputFile = new File(sourceFile);
			
			if(inputFile.isDirectory()){
				
				for(File f : inputFile.listFiles()){
					this.inputFiles.add(f);
				}
				
			}else{
				this.inputFiles.add(inputFile);
			}
		
		
	}

	@Override
	public String getLine() {		
	    
		try {
			
			if(this.br == null){
				this.br = new BufferedReader(new FileReader(this.inputFiles.get(index)));
				
				index++;
			}
			
			String readLine = br.readLine();
			
			if(readLine == null){
				if(index < this.inputFiles.size()){
					this.br = new BufferedReader(new FileReader(this.inputFiles.get(index)));
					index++;
					
					
					readLine = br.readLine();
				}
			}
			
	    	return readLine;
		
		} catch (IOException e) {
			System.out.println("Cant load files from: " + this.inputFiles);
			System.exit(0);
		}
		return null;
	}

	@Override
	public String getSourcePath() {
		return this.inputFiles.get(this.index).toString();
	}
	
	
	
}
