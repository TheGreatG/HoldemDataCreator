package entities;

import java.util.HashMap;

public class HandStats {
	private HashMap<Game, Boolean> vpip = new HashMap<Game, Boolean>();
	private HashMap<Game, Boolean> rfi = new HashMap<Game, Boolean>();
	private HashMap<Game, Boolean> pfr = new HashMap<Game, Boolean>();
	private HashMap<Game, Boolean> threeBet = new HashMap<Game, Boolean>();
	private HashMap<Game, Boolean> cBet = new HashMap<Game, Boolean>();
	private HashMap<Game, Boolean> donkBet = new HashMap<Game, Boolean>();
	private HashMap<Game, Double>  oneGameWinRate = new HashMap<Game, Double>();
	
	public HashMap<Game, Boolean> getVpip() {
		return vpip;
	}
	public void setVpip(HashMap<Game, Boolean> vpip) {
		this.vpip = vpip;
	}
	public HashMap<Game, Boolean> getRfi() {
		return rfi;
	}
	public void setRfi(HashMap<Game, Boolean> rfi) {
		this.rfi = rfi;
	}
	
	public HashMap<Game, Boolean> getThreeBet() {
		return threeBet;
	}
	public void setThreeBet(HashMap<Game, Boolean> threeBet) {
		this.threeBet = threeBet;
	}
	public HashMap<Game, Boolean> getcBet() {
		return cBet;
	}
	public void setcBet(HashMap<Game, Boolean> cBet) {
		this.cBet = cBet;
	}
	public HashMap<Game, Boolean> getDonkBet() {
		return donkBet;
	}
	public void setDonkBet(HashMap<Game, Boolean> donkBet) {
		this.donkBet = donkBet;
	}
	public HashMap<Game, Boolean> getPfr() {
		return pfr;
	}
	public void setPfr(HashMap<Game, Boolean> pfr) {
		this.pfr = pfr;
	}
	public HashMap<Game, Double> getOneGameWinRate() {
		return oneGameWinRate;
	}
	public void setOneGameWinRate(HashMap<Game, Double> oneGameWinRate) {
		this.oneGameWinRate = oneGameWinRate;
	}	
	
	
}
