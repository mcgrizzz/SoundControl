package me.mcgrizzz.soundcontrol.action;

public enum ActionType {
	
	DAMAGED(new ActionDamaged()),
	DEATH(new ActionDeath()),
	KILL(new ActionKill()),
	ENCHANT(new ActionEnchant()),
	DAMAGE(new ActionDamage()),
	JOIN(new ActionJoin()),
	LEAVE(new ActionLeave()),
	LEAVE_REGION(new ActionLeaveRegion()),
	JOIN_REGION(new ActionJoinRegion()),
	MESSAGE_SENT(new ActionMessageSent()),
	MESSAGE_RECEIVE(new ActionMessageReceive()),
	CMD(new ActionCmd());
	
	private Action action;
	
	ActionType(Action a){
		this.action = a;
	}
	
	public Action getAction(){
		return this.action;
	}
	
	public static ActionType fromString(String s){
		for(ActionType type : values()){
			if(type.name().equalsIgnoreCase(s)){
				return type;
			}
		}
		return null;
	}
	

}
