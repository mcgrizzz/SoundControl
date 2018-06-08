package me.mcgrizzz.soundcontrol.action;

import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class ActionEnchant extends Action {

	public ActionEnchant() {
		super(ActionType.ENCHANT);
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent e){
		checkConditions(e.getEnchanter());
			
	}
	
	

}