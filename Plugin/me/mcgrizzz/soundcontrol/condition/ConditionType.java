package me.mcgrizzz.soundcontrol.condition;

public enum ConditionType {
	
	DEFAULT,
	RAIN,
	FIGHT,
	REGION;
	
	public static ConditionType fromString(String s){
		for(ConditionType t : values()){
			if(t.name().equalsIgnoreCase(s)){
				return t;
			}
		}
		
		return null;
		
	}
	

}
