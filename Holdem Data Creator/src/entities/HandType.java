package entities;

import java.util.ArrayList;
import java.util.List;

public enum HandType {
	MADE_HAND("0"),STRAIGHT_DRAW("1"),FLUSH_DRAW("2"),COMBO_DRAW("3"),MADE_HAND_W_FLUSH_DRAW("4"),NOTHING("5");
	
	private String value;
	
	public String getResponse() {
        return value;
    }
	
	public ArrayList<String> getArrayResponse() {
        List<String> list = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			if (this.getResponse().equals(String.valueOf(i))) {
				list.add("1");
			}
			else list.add("0");
		}
		return (ArrayList<String>) list;
    }
	
	HandType(String value){
		this.value = value;
	}
	
}