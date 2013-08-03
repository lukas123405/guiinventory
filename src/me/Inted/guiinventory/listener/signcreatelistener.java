package me.Inted.guiinventory.listener;

import java.io.File;

import me.Inted.guiinventory.main;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class signcreatelistener implements Listener{

	 main plugin;
	 File file;
	 YamlConfiguration cfg;
	
	
	public signcreatelistener(main plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
        if(!event.getLine(0).equalsIgnoreCase("[promote]")){
        	return;
        }
        Player p = (Player)event.getPlayer();
        if(!(p.hasPermission("guiinventory.promote.create") || p.hasPermission("guiinventory.promote.*") || p.hasPermission("guiinventory.*") || p.hasPermission("*"))){
        	event.getPlayer().sendMessage(ChatColor.RED + "Du hast keine Rechte das zu tun.");
			event.setCancelled(true);
			return;
        }
        event.setLine(0, ChatColor.GREEN + "[Promote]");
        try  
        {  
        	Integer.parseInt(event.getLine(2));
         } catch(NumberFormatException nfe)  
         {  
        	 p.sendMessage("Das ist ein ungültiger Preis.");
        	 event.setCancelled(true);
             return;
         }
        System.out.println("[Guiinventory] Promote-Sign erstellt");
        
    }
}
