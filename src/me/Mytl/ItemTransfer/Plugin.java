package me.Mytl.ItemTransfer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
	
	@Override
	public void onEnable()
	{

 	  FileConfiguration config = this.getConfig(); 	   
	  config.options().copyDefaults(true);
	  this.saveDefaultConfig();

			PluginManager pm = getServer().getPluginManager();
			EventListener SendListener = new EventListener(config, this, "ItemTransfer.Send");
			EventListener ReceiveListener = new EventListener(config, this, "ItemTransfer.Receive");
			pm.registerEvents(SendListener, this);
			pm.registerEvents(ReceiveListener, this);
	}
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("IT"))
    	{
    		if(!sender.hasPermission("ItemTransfer.ReturnInventory"))
    			return true;
    		
    		if(args.length == 1 && args[0].equalsIgnoreCase("ReturnInventory"))
    		{
    			if (sender instanceof Player)
    			{
    				Player player = (Player) sender;
    				
    				if (player.getInventory().getContents() == null)
    					return true;
    				
    				
    				int index = 0;
    				for(ItemStack IT : player.getInventory().getContents())
    				{
        				if (IT == null)
        					player.sendMessage("Slot " + index + " - Empty");
        				else
        					player.sendMessage("Slot " + index + " - " + IT.toString());
        				
    					index++;
    				}
    				
    				player.sendMessage("---");
    				
    				return true;
    			}
    		}
    		else if(args.length == 2 && args[0].equalsIgnoreCase("ReturnInventory"))
    		{
    			if(!isOnline(args[1]))
    				return true;
    			
    				Player player = getServer().getPlayer(args[1]);
    			
    				
    				if (player.getInventory().getContents() == null)
    					return true;
    				
    				
    				int index = 0;
    				for(ItemStack IT : player.getInventory().getContents())
    				{
        				if (IT == null)
        					player.sendMessage("Slot " + index + "- Empty");
    					
    					player.sendMessage("Slot " + index + " - " + IT.toString());
    					index++;
    				}
    				
    				player.sendMessage("---");
    				
    				return true;
    		}
    		
    	}
    	
    	return false; 
    }
    
    public boolean isOnline(String PlayerName)
    {
    	for(Player player : getServer().getOnlinePlayers())
    	{
    		if(player.getName() == PlayerName)
    		{
    			return true;
    		}
    	}
    	
    	return false;
    }

}
