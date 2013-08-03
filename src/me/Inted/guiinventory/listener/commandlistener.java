package me.Inted.guiinventory.listener;


import me.Inted.guiinventory.main;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class commandlistener implements Listener{
	
	main plugin;
	
	
	public commandlistener(main plugin){
		this.plugin = plugin;
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		String[] tmp = event.getMessage().substring(1).split(" ");
		String cmd = tmp[0];
		for(int i = 0; i < plugin.inventories.size(); i++){
			if(plugin.inventories.get(i).getCommand().equalsIgnoreCase(cmd) && !cmd.equalsIgnoreCase(""))
			{
				if(!(event.getPlayer().hasPermission("guiinventory." + cmd) || event.getPlayer().hasPermission("guiinventory.*")|| event.getPlayer().hasPermission("*"))){
					event.getPlayer().sendMessage(ChatColor.RED + "Du hast keine Rechte das zu tun.");
					event.setCancelled(true);
					return;
				}
				event.getPlayer().openInventory(plugin.inventories.get(i).getInventory());
				event.setCancelled(true);
				return;
			}
		}
		event.setCancelled(false);
	}
	
	public String replace (String text, Player p){
		return ChatColor.translateAlternateColorCodes('&', text).replaceAll("@p", p.getDisplayName());
	}
}
