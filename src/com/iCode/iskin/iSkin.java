package com.iCode.iskin;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.player.SpoutPlayer;

public class iSkin extends JavaPlugin{

	
	Logger log = Logger.getLogger("Minecraft");
	protected Configuration config;
	
	public void onEnable(){
	
		setupConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.CUSTOM_EVENT, new iSkinSpoutListener(this), Priority.Normal, this);

	}
	
	public void onDisable(){
		log.severe("[iSkin] is being shut down by admin trololol!");
	}
	
	public void setupConfig() {
		
		config = getConfiguration();
		config.setHeader("#iSkin Configuration file");
		config.getString("skinlist.test.url", "http://www.minecraft.net/skin/Hoddie54.png");
		config.getString("players.test.url", "http://www.minecraft.net/skin/Hoddie54.png");
		config.save();		
	}
    public boolean onCommand(CommandSender cs, Command cmd, String aliases, String[] args){
		
		if((cmd.getName().equalsIgnoreCase("iskin") && (cs.hasPermission("iSkin.reload") || cs.isOp()))) {
			
			if(args.length == 0) {
				return false;
			}
			/*
			 * reload command
			 */
		if(args[0].equalsIgnoreCase("reload")) {
				
				log.info("initiating plugin reload...");
				config = getConfiguration();
				config.load();
				
				Player[] pl = getServer().getOnlinePlayers();
				
				for(int i=0; i < pl.length; i++) {
					
					SpoutPlayer sp = (SpoutPlayer)pl[i];
					if(!sp.isSpoutCraftEnabled()) {
						continue;
					}
					Event ev = new SpoutCraftEnableEvent(sp);
						getServer().getPluginManager().callEvent(ev); //Calls this event
				}
				log.info("reload successful");
				cs.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.GREEN+"MCHardcore"+ChatColor.DARK_RED+"] has been reloaded");
				
			return true;
			}
		/*
		 * help command
		 */
		else if ((args[0].equalsIgnoreCase("help")) && (cs.hasPermission("iskin.help") || cs.isOp())) {
			
			cs.sendMessage(ChatColor.GOLD+"-----------iSkin-----------");
			cs.sendMessage(ChatColor.GREEN+"/iskin setself [url]");
			cs.sendMessage(ChatColor.GREEN+"/iskin setgroup [name] [url]");
			cs.sendMessage(ChatColor.GREEN+"/iskin setplayer [name] [url]");
			cs.sendMessage(ChatColor.GOLD+"Created by iCode");
				return true;
		}
		/*
		 * setself command
		 */
		else if ((args[0].equalsIgnoreCase("setself")) && (cs.hasPermission("iskin.setself") || cs.isOp())) {
			
			if (args.length !=2) {
				return false;
			}
			Player player = (Player)cs;
			String url = args[1];
			
		if (!url.endsWith(".png")) {
			player.sendMessage(ChatColor.RED+"URL must end with .png");
			return false;
		}
		config.setProperty("players." + player.getName() + ".url", url);
		config.save();
		checkSpoutEnabled(cs);
			return true;
	}
		/*
		 * setplayer command
		 */
		else if ((args[0].equalsIgnoreCase("setplayer") && (cs.hasPermission("iskin.setplayer") || cs.isOp()))) {
			
			if (args.length !=3) {
				return false;
			}
			
			Player player = (Player)cs;
			String playerName = args[1];
			String url = args[2];
			
		if (!url.endsWith(".png")) {
			player.sendMessage(ChatColor.RED+"URL must end with .png");
			return false;
		}
		config.setProperty("players." + playerName + ".url", url);
		config.save();
		checkSpoutEnabled(cs);
			return true;
		}
		
		/*
		 * setgroup command
		 */
		else if ((args[0].equalsIgnoreCase("setgroup")) && (cs.hasPermission("iskin.setgroup") || cs.isOp())) {
			if (args.length !=3) {
				return false;
			}
			Player player = (Player)cs;
			String groupName = args[1];
			String url = args[2];
			
		if (!url.endsWith(".png")) {
			player.sendMessage(ChatColor.RED+"URL must end with .png");
			return false;
		}
		config.setProperty("skinlist."+ groupName + ".url", url);
		config.save();
		checkSpoutEnabled(cs);
			return true;
		}
	}
		return false;
}	
		
	public String forceSkin(Player player){

		List<String> a = config.getKeys("skinlist");
	    
		if(a == null || a.size() < 1){
			return null; 
			//Returns nothing if the player has not been forced.
		}
		for(int i = 0; i < a.size(); i++){
			
			String skinname = a.get(i);
			String url = config.getString("skinlist." + skinname + ".url", "http://www.minecraft.net/skin/" + player.getName() + ".png");
			
			if(player.hasPermission("iskin." + skinname) && !player.hasPermission("*")) return url; 
			//Gets the URL for that skinlist
		}
		return null;
	}
	
	public void checkSpoutEnabled(CommandSender cs) {
		
		Player[] pl = getServer().getOnlinePlayers();
		   for(int i = 0; i < pl.length; i++){ 
			    SpoutPlayer sp = (SpoutPlayer) pl[i];
			    if(!sp.isSpoutCraftEnabled()){
			     continue;
			}
			 Event ev = new SpoutCraftEnableEvent(sp);
			 	getServer().getPluginManager().callEvent(ev);
	    }
	}
}