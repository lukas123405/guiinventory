package me.Inted.guiinventory;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.Inted.guiinventory.listener.commandlistener;
import me.Inted.guiinventory.listener.inventoryclicklistener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;


public class main extends JavaPlugin{

	public List<guiinventory> inventories;
	File file;
	public FileConfiguration cfg;
	@Override
	public void onDisable() {
	}
	
	@Override
	public void onEnable() {
		loadConfig();
		new commandlistener(this);
		new inventoryclicklistener(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("guiinventory")){
			if(args.length == 0)
			{
				sender.sendMessage("Not included yet."); //Todo
				return true;
			}
			if(args[0].equalsIgnoreCase("reload")){
				if(!sender.hasPermission("guiinventory.reload"))
				{
					sender.sendMessage(ChatColor.RED + "Du hast keine Rechte das zu tun.");
					return true;
				}
				loadConfig();
				sender.sendMessage(ChatColor.GREEN + "[GuiInventory] " + ChatColor.WHITE + "Das Plugin wurde neu geladen");
				return true;
			}
			if(args[0].equalsIgnoreCase("console")){
				if(!sender.hasPermission("guiinventory.console")){
					sender.sendMessage(ChatColor.RED + "Du hast keine Rechte das zu tun.");
					return true;
				}
				if(args.length < 3){
					sender.sendMessage("Zu wenig Argumente");
					return true;
				}
				if(args.length > 3){
					sender.sendMessage("Zu viele Argumente");
					return true;
				}
				if(!this.getServer().getPlayer(args[1]).hasPermission("guiinventory." + args[2])){
					sender.sendMessage("Der Spieler hat keine Rechte das zu tun.");
					return true;
				}
				this.getServer().getPlayer(args[1]).chat("/" + args[2]);
				
				
			}
			
		}
		return false;
		
	}
	
	public void saveConfig(){
		try {
			cfg.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadConfig(){
		inventories = new ArrayList<guiinventory>();
		file = new File(this.getDataFolder(), "inventories.yml");
		cfg = YamlConfiguration.loadConfiguration(file);
		if(!file.exists()){
		cfg.addDefault("inventories.help.title", "Hilfe");
		cfg.addDefault("inventories.help.command", "help");
		cfg.addDefault("inventories.help.size", 1);
		cfg.options().copyDefaults(true);
		}
		saveConfig();
		
		for(String inventory : cfg.getConfigurationSection("inventories").getKeys(false)){
			
			cfg.addDefault("inventories." + inventory + ".title", inventory);
			cfg.addDefault("inventories." + inventory + ".command", inventory);
			cfg.addDefault("inventories." + inventory + ".size", 1);
			cfg.options().copyDefaults(true);
			saveConfig();
			String title = cfg.getString("inventories." + inventory + ".title");
			String command = cfg.getString("inventories." + inventory + ".command");
			int size = cfg.getInt("inventories." + inventory + ".size");
			
			if(size > 10 || size < 1)
			{
				size = 1;
				cfg.set("inventories." + inventory + ".size", 1);
				saveConfig();
			}
			guiinventory tmp = new guiinventory(this,inventory, title, command, size);
			inventories.add(tmp);
		}
	}
	
	
	
	
	
}