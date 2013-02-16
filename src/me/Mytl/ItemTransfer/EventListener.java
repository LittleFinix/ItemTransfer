package me.Mytl.ItemTransfer;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EventListener implements Listener
{
	public FileConfiguration config;
	
	public Plugin plugin;
	
	public String ConfigNode;
	
	List<PlayerProfile> taskIDs = new ArrayList<PlayerProfile>();
	
	public EventListener(FileConfiguration config, Plugin plugin, String ConfigNode)
	{
		this.config = config;
		this.plugin = plugin;
		this.ConfigNode = ConfigNode;
	}
	
	@EventHandler
	public void onBlockRightClick(final PlayerInteractEvent event)
	{
		if(event.getClickedBlock() == null)
			return;
		
		List<Integer> Position = config.getIntegerList(ConfigNode + ".Block.LocationXYZ");
		if(!event.getClickedBlock().getWorld().getWorldFolder().getName().equalsIgnoreCase(config.getString(ConfigNode + ".Block.World")) || !event.getClickedBlock().getLocation().equals(new Location(event.getClickedBlock().getWorld(), Position.get(0), Position.get(1), Position.get(2))))
			return;
		
		event.getPlayer().sendMessage(ChatColor.AQUA + "Warming up the Transfer spell...");
		event.getPlayer().sendMessage(ChatColor.AQUA + "Don't move!");
		
		final PlayerProfile taskID = new PlayerProfile(event.getPlayer(), plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		    @Override  
		    public void run() {
		    	if(ConfigNode == "ItemTransfer.Send")
		    	{
		    	event.getPlayer().sendMessage(InventoryFile.Save(event.getPlayer()));
		    	}
		    	else
		    	{
		    	event.getPlayer().sendMessage(InventoryFile.Load(event.getPlayer(), config));
		    	}
		    	
		    }
		}, config.getLong(ConfigNode + ".WarmUp") * 20));
		
		taskIDs.add(taskID);		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		List<Integer> Position = config.getIntegerList(ConfigNode + ".Block.LocationXYZ");
		if(!event.getBlock().getWorld().getWorldFolder().getName().equalsIgnoreCase(config.getString(ConfigNode + ".Block.World")) || !event.getBlock().getLocation().equals(new Location(event.getBlock().getWorld(), Position.get(0), Position.get(1), Position.get(2))))
			return;
		
		if(config.getBoolean(ConfigNode  + ".Block.Unbreakable"))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if(event.getFrom().getWorld().getName() == "res" && !event.getPlayer().hasPermission("Teleport.Allow"))
		{
		event.setCancelled(true);
		event.getPlayer().sendMessage(ChatColor.DARK_RED + "You do not have enough power!");
		event.getPlayer().setExhaustion(event.getPlayer().getExhaustion() - 5f);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		int index = 0;
		for(PlayerProfile pp : taskIDs)
		{
			if (pp.GetPlayer().getName() == event.getPlayer().getName())
			{
				
				if (plugin.getServer().getScheduler().isQueued(pp.GetTaskID()))
				{
					plugin.getServer().getScheduler().cancelTask(pp.GetTaskID());
					event.getPlayer().sendMessage(ChatColor.DARK_RED + "Transfer was cancelled!");
				}
				
				taskIDs.remove(index);				
				break;
			}
			index++;
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		for(PlayerProfile pp : taskIDs)
		{
			if (pp.GetPlayer().getName() == event.getPlayer().getName())
			{
				plugin.getServer().getScheduler().cancelTask(pp.GetTaskID());
			}
		}
	}
}
