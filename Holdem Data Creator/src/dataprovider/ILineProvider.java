package dataprovider;

public interface ILineProvider {
	public void openSource(String sourceFile);
	public String getLine();
	public String getSourcePath();
}
