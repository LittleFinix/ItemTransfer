package me.Mytl.ItemTransfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class InventoryFile
{
	public static String Save(Player player)
	{
		File PlayerFile = new File("plugins/ItemTransfer/Transfers/" + player.getName() + ".inv");
		if(PlayerFile.exists())
		{
			return ChatColor.DARK_RED + "You've already stored an inventory!";
		}
		else
		{
			try {
				PlayerFile.createNewFile();
			} catch (IOException e) {}
		}
		YamlConfiguration save = new YamlConfiguration();
		int index = 0;
		for(ItemStack IS : player.getInventory().getContents())
		{
			if(IS != null)
			{
				save.set("Inventory.Slot_" + index + ".Type", IS.getTypeId());
				save.set("Inventory.Slot_" + index + ".DamageValue", IS.getDurability());
				save.set("Inventory.Slot_" + index + ".Amount", IS.getAmount());
				save.set("TEST", IS.getData().toString());
			}
			else
			{
				save.set("Inventory.Slot_" + index + ".Type", 0);
				save.set("Inventory.Slot_" + index + ".Amount", 0);
			}
			
			index++;
		}
		
		try {
			save.save(PlayerFile);
			player.getInventory().clear();
		} catch (IOException e) {}
		
		return ChatColor.AQUA + "The spell was successfull!";
		
	}
	
	public static String Load(Player player, FileConfiguration config)
	{
		File PlayerFile = new File("plugins/ItemTransfer/Transfers/" + player.getName() + ".inv");
		if(!PlayerFile.exists())
		{
			return ChatColor.DARK_RED + "You haven't stored an inventory!";
		}
		
		YamlConfiguration load = new YamlConfiguration();
		try {
			load.load(PlayerFile);
		} catch (FileNotFoundException e) {} catch (IOException e) {} catch (InvalidConfigurationException e) {}
		
		ItemStack[] inv = new ItemStack[36];
		
		for(int i = 0; i < 36; i++)
		{
				inv[i] = new ItemStack(0, 0);
		}
		
		if(config.getBoolean("ItemTransfer.Receive.NeedsClearInventory"))
			for(ItemStack IS : player.getInventory().getContents())
			{
				if(IS != null)
					return ChatColor.DARK_RED + "Your inventory must be empty!";
			}
		
		for(int i = 0; i < 36; i++)
		{
			
			if(load.getInt("Inventory.Slot_" + i + ".Type") != 0 && load.getInt("Inventory.Slot_" + i + ".Amount") > 0)
			{
				inv[i].setAmount(load.getInt("Inventory.Slot_" + i + ".Amount"));
				inv[i].setTypeId(load.getInt("Inventory.Slot_" + i + ".Type"));
				inv[i].setDurability((short) load.getInt("Inventory.Slot_" + i + ".DamageValue"));
			}
			else
			{
				inv[i] = null;
			}
		}
		
		PlayerFile.delete();
		player.getInventory().setContents(inv);
		
		return ChatColor.AQUA + "The spell was successfull!";
	}
}
