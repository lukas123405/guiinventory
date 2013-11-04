package me.Inted.guiinventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class guiinventory {

	main plugin;
	 String name;
	 String title;
	 String command;
	 int size;
	 Inventory inv;
	 File file;
	 YamlConfiguration cfg;
	
	public guiinventory(main plugin, String name, String title, String command, int size){
		this.plugin = plugin;
		this.name = name;
		this.title = title;
		this.command = command;
		this.size = size;
		inv = Bukkit.createInventory(null, size * 9, title);
		loadConfig();
	}
	
	
	public Inventory getInventory(){
		return inv;
	}
	
	public String getName(){
		return name;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getCommand(){
		return command;
	}
	
	@SuppressWarnings("deprecation")
	public void loadConfig(){
		//inventories = new ArrayList<guiinventory>();
		file = new File(plugin.getDataFolder() + "/inventories", name + ".yml");
		cfg = YamlConfiguration.loadConfiguration(file);
		if(!file.exists()){
		cfg.addDefault(name + ".slot1.item", "277 1 name:Spawn lore:&4Teleportiert_dich_zum_spawn");
		cfg.addDefault(name + ".slot1.action", "command: /spawn");
		cfg.options().copyDefaults(true);
		}
		saveConfig();
		for(String slot : cfg.getConfigurationSection(name).getKeys(false)){
			if(slot.length() <5 || slot.length() >7){
				System.out.println(ChatColor.RED + "[GuiInventory] Der " + slot + " ist beschädigt.");
				return;
			}
			int slotid = -1;
			
			try{
				slotid = Integer.parseInt(slot.substring(4));
				}catch (NumberFormatException nfe)
				{
				System.out.println(ChatColor.RED + "[GuiInventory] Der " + slot + " ist beschädigt.");
				return;
				}
			
			cfg.addDefault(name + "." + slot + ".item", "277 1 name:Spawn lore:&4Teleportiert_dich_zum_spawn");
			cfg.addDefault(name + "." + slot + ".action", "command: /spawn");
			cfg.options().copyDefaults(true);
			saveConfig();
			String item = cfg.getString(name + "." + slot + ".item");
			String[] iteminfos =item.split(" ");
			if(iteminfos.length < 2)
			{
				System.out.println(ChatColor.RED + "[GuiInventory] Der " + slot + " ist beschädigt.");
				return;
			}
			
			ItemStack is = null;
			try{
				String[] data = iteminfos[0].split("_");
				if(data.length == 2){
					is = new ItemStack(Material.getMaterial(Integer.parseInt(data[0])), Integer.parseInt(iteminfos[1]), Short.parseShort(data[1]));
				}
				else
				{
					is = new ItemStack(Material.getMaterial(Integer.parseInt(iteminfos[0])), Integer.parseInt(iteminfos[1]));
				}
			}catch (NumberFormatException nfe)
			{
			System.out.println(ChatColor.RED + "[GuiInventory] Der " + slot + " ist beschädigt.");
			return;
			}
			
			for(int i = 2; i<iteminfos.length;i++)
			{
				if(!addMeta(is, iteminfos[i])){
					System.out.println(ChatColor.RED + "[GuiInventory] Der " + slot + " ist beschädigt.");
					return;
				}
			}
			inv.setItem(slotid, is);
			
			}
		}
		
		public void saveConfig(){
			try {
				cfg.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public boolean addMeta(ItemStack is, String metainfo){
			ItemMeta im = is.getItemMeta();
			String[] itemmetainfo = metainfo.split(":");
			String meta = itemmetainfo[0];
			String info = itemmetainfo[1];
			for(int i = 2; i < itemmetainfo.length; i++)
			{
				info += ":" + itemmetainfo[i];
			}
			if(meta.equalsIgnoreCase("name")){
				im.setDisplayName(replace(info));
				is.setItemMeta(im);
				return true;
			}
			if(meta.equalsIgnoreCase("lore")){
				List<String>lore  = new ArrayList<String>();
				lore.add(replace(info));
				im.setLore(lore);
				is.setItemMeta(im);
				return true;
			}
			
			int information = 0;
			try{
				information = Integer.parseInt(info);
				}catch (NumberFormatException nfe)
				{
				return false;
				}
			if(information > 127)
			{
				information = 127;
			}
			if(meta.equalsIgnoreCase("efficiency")){
				
				if(information <= 5){
					is.addEnchantment(Enchantment.DIG_SPEED, information);
				}
				else{
					
					is.addUnsafeEnchantment(Enchantment.DIG_SPEED, information);
				}
				return true;
			}

			if(meta.equalsIgnoreCase("unbreaking")){
				if(information <= 3){
					is.addEnchantment(Enchantment.DURABILITY, information);
				}
				else{
					is.addUnsafeEnchantment(Enchantment.DURABILITY, information);
				}
				return true;
			}
			
			return false;
		}
		
		public String replace(String text){
		return ChatColor.translateAlternateColorCodes('&', text.replace('_', ' '));
		}
}
