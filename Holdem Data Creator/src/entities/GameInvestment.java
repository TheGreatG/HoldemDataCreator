package entities;

public class GameInvestment {
	private Double preFlopInvestment = 0.0;
	private Double flopInvestment = 0.0; 
	private Double turnInvestment = 0.0;
	private Double riverInvestment = 0.0;
	private Double win = 0.0;
	
	public Double getPreFlopInvestment() {
		return preFlopInvestment;
	}
	public void setPreFlopInvestment(Double preFlopInvestment) {
		this.preFlopInvestment = preFlopInvestment;
	}
	public Double getFlopInvestment() {
		return flopInvestment;
	}
	public void setFlopInvestment(Double flopInvestment) {
		this.flopInvestment = flopInvestment;
	}
	public Double getTurnInvestment() {
		return turnInvestment;
	}
	public void setTurnInvestment(Double turnInvestment) {
		this.turnInvestment = turnInvestment;
	}
	public Double getRiverInvestment() {
		return riverInvestment;
	}
	public void setRiverInvestment(Double riverInvestment) {
		this.riverInvestment = riverInvestment;
	}
	public Double getWin() {
		return win;
	}
	public void setWin(Double win) {
		this.win = win;
	}
	
	
}
