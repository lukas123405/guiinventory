package me.Inted.guiinventory.listener;

import java.io.File;

import me.Inted.guiinventory.main;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;



public class signclicklistener implements Listener{

	 main plugin;
	 File file;
	 YamlConfiguration cfg;
	
	
	public signclicklistener(main plugin){
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.isCancelled())
		{
			return;
		}
        Block b = event.getClickedBlock();
        Material m = b.getType();
        if(!(m == Material.WALL_SIGN || m == Material.SIGN_POST)) {
           return;
        }
        Sign sign = (Sign)b.getState();
        if(!sign.getLine(0).endsWith("[Promote]")){
        	return;
        }
        Player p = (Player)event.getPlayer();
        String rang = sign.getLine(1);
        if(!(p.hasPermission("guiinventory.promote." + rang) || p.hasPermission("guiinventory.promote.*") || p.hasPermission("guiinventory.*") || p.hasPermission("*"))){
        	event.getPlayer().sendMessage(ChatColor.RED + "Du hast keine Rechte das zu tun.");
			event.setCancelled(true);
			return;
        }
        if(p.getGameMode().equals(GameMode.CREATIVE)){
        	return;
        }
        event.setCancelled(true);     
        int amount = Integer.parseInt(sign.getLine(2));
        
        try {
			if(!Economy.hasEnough(p.getName(), amount)){
				p.sendMessage("Du hast nicht genug Geld.");
				return;
			}
			try {
				Economy.subtract(p.getName(), amount);
			} catch (NoLoanPermittedException e) {
				e.printStackTrace();
			}
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		}
        
        

        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "setrank " + p.getName() + " " + rang);
		p.sendMessage("Du wurdest zum " + rang + " promotet.");
		System.out.println("[Guiinventory] " + p.getName() + " wurde die Gruppe " + rang + " zugewiesen.");
    }
}
