package entities;

import java.util.ArrayList;
import java.util.List;

public enum CardsCombinationType {
	VALUE_HAND("0"),BROADWAY("1"),MIDDLE_VALUE("2"),CONNECTORS("3"),SMALL_PAIR("4"),BLUFF("5");
	
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
	
	CardsCombinationType(String value){
		this.value = value;
	}
	
}
