package entities;

import java.util.ArrayList;
import java.util.List;

public enum HandType3Ways {
	MADE_HAND("0"),DRAW("1"),NOTHING("2");
	
	private String value;
	
	public String getResponse() {
        return value;
    }
	
	public ArrayList<String> getArrayResponse() {
        List<String> list = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			if (this.getResponse().equals(String.valueOf(i))) {
				list.add("1");
			}
			else list.add("0");
		}
		return (ArrayList<String>) list;
    }
	
	HandType3Ways(String value){
		this.value = value;
	}
	
}
