package me.Mytl.ItemTransfer;

import org.bukkit.entity.Player;

public class PlayerProfile {

	public PlayerProfile(Player player, int taskID)
	{
		this.player = player;
		this.taskID = taskID;
	}
	
	public int GetTaskID()
	{
		return taskID;
	}
	
	public void SetPlayer(Player player)
	{
		this.player = player;
	}
	
	public Player GetPlayer()
	{
		return player;
	}
	
	private Player player;
	private int taskID;
	
}
