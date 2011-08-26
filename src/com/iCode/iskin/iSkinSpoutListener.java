package com.iCode.iskin;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.event.spout.SpoutListener;
import org.getspout.spoutapi.player.AppearanceManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class iSkinSpoutListener extends SpoutListener {

public static iSkin plugin;
	
	public iSkinSpoutListener(iSkin instance) {
		plugin = instance;
	}
	
	
	public void onSpoutCraftEnable(SpoutCraftEnableEvent ev){
		
		SpoutPlayer sp = SpoutManager.getPlayer(ev.getPlayer());
		AppearanceManager am = SpoutManager.getAppearanceManager();
		
		if(!sp.isSpoutCraftEnabled()){
			return;
		}
		//TODO Change it from testing mode
		String url = plugin.config.getString("players." + sp.getName() + ".url", "http://www.minecraft.net/skin/" + sp.getName() + ".png");
		Player[] players = plugin.getServer().getOnlinePlayers();
		
		if(url == "http://www.minecraft.net/skin/" + sp.getName() + ".png" ) plugin.config.save(); //Saves default value
		
		
       for(int i =0; i < players.length; i++){
			
			try{@SuppressWarnings("unused")
			SpoutPlayer p = (SpoutPlayer)players[i];} //Tests if the player is actually a SpoutPlayer
			catch(Exception e){continue;}
			
			if(plugin.forceSkin((Player) sp) != null
					&& url != "http://www.minecraft.net/skin/" + sp.getName() + ".png"){
				url = plugin.forceSkin((Player) sp);
			}
			sp.sendMessage(url);
			am.setPlayerSkin( (SpoutPlayer) players[i], (HumanEntity) sp, url); //Set skin
			
			
			
		   
            }
       
	
	}
	
	
	
}
