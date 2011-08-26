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
	
		
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Type.CUSTOM_EVENT, new iSkinSpoutListener(this), Priority.Normal, this); //Register Events
		
		makeConfig();
		
		
		config.setHeader("#iSkin Configuration file");
		config.setProperty("skinlist.test.url", "http://www.minecraft.net/skin/Hoddie54.png");
		config.setProperty("players.test.url", "http://www.minecraft.net/skin/Hoddie54.png");
		config.save();
		
		//Make default examples
	}
	
	public void onDisable(){
		log.severe("NOOB Y U CLOSE OUR PLUGIN. BACON");
	}
	
    public boolean onCommand(CommandSender sender, Command cmd, String aliases, String[] args){
		
		if(cmd.getName().equalsIgnoreCase("iskin") && (sender.hasPermission("iSkin.reload") || sender.isOp())){
			
			if(args.length == 0) return false;
			if(args[0].equalsIgnoreCase("help")) {sender.sendMessage("Type /iskin reload to reload the config"); return true;}
			if(!args[0].equalsIgnoreCase("reload")) return false; //Handling commands
			
			log.info("[iSkin] Initiating Plugin Reload");
			config = getConfiguration();
			config.load(); //Reloads configuration file
			
			
			Player[] pl = getServer().getOnlinePlayers();
			
			for(int i = 0; i < pl.length; i++){	
				
				SpoutPlayer sp = (SpoutPlayer) pl[i];
				if(!sp.isSpoutCraftEnabled()){
					continue;
		    }
				
            Event ev = new SpoutCraftEnableEvent(sp);
	        getServer().getPluginManager().callEvent(ev); //Call the SpoutCraftEnable Event for every SpoutPlayer
	        
		}
			log.info("[iSkin] Reload Successful");
			sender.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.GREEN+"iSkin"+ChatColor.DARK_RED+"] has been reloaded");
			
			return true;
	}
		
		return false;
}
	
	

	private void makeConfig(){
		
		config = getConfiguration();
	}
	
	public String forceSkin(Player player){
		
		//Insert pun about method name
		
		List<String> a = config.getKeys("skinlist");
	    
		if(a == null || a.size() < 1){
			return null; //Returns nothing if the player has not been forced.
		}
		
		for(int i = 0; i < a.size(); i++){
			
			String skinname = a.get(i);
			String url = config.getString("skinlist." + skinname + ".url", "http://www.minecraft.net/skin/" + player.getName() + ".png");
			
			if(player.hasPermission("iskin." + skinname) && !player.hasPermission("*")) return url; //Gets the URL for that skinlist
			
		}
		
		return null;
	}
	
}
