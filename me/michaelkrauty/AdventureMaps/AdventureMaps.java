package me.michaelkrauty.AdventureMaps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AdventureMaps extends JavaPlugin{
	
	public ArrayList<String> requestedhm1 = new ArrayList<String>();
	public ArrayList<String> invitedtohm1 = new ArrayList<String>();
	public ArrayList<String> inhm1 = new ArrayList<String>();
	public ArrayList<String> joinedhm1 = new ArrayList<String>();
	public ArrayList<String> exitinghm1 = new ArrayList<String>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]){
		
		if(commandLabel.equalsIgnoreCase("adventuremaps") || commandLabel.equalsIgnoreCase("adventuremap") || commandLabel.equalsIgnoreCase("adventure") || commandLabel.equalsIgnoreCase("am")){
			
			if(args.length == 0){
				sender.sendMessage(ChatColor.GOLD + "Unknown command! Use " + ChatColor.RED + "/am help" + ChatColor.GOLD + " for help!");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("help")){
				sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "AdventureMaps Commands and Help");
				sender.sendMessage(ChatColor.RED + "/am list" + ChatColor.GOLD + ": List available adventure maps");
				sender.sendMessage(ChatColor.RED + "/am listplayers <map>" + ChatColor.GOLD + ": List players currently playing the specified map");
				sender.sendMessage(ChatColor.RED + "/am join <adventuremap name>" + ChatColor.GOLD + ": Join an adventure map");
				sender.sendMessage(ChatColor.RED + "/am stop" + ChatColor.GOLD + ": Stop your adventure map world. This will regenerate the world");
				sender.sendMessage(ChatColor.RED + "/am admin" + ChatColor.GOLD + " help: Admin help");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("list")){
				sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Available adventure maps:");
				sender.sendMessage(ChatColor.GOLD + "Herobrine's Mansion (hm1)");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("listplayers")){
				sender.sendMessage(ChatColor.GOLD + "Players playing hm1:");
				if(inhm1.toString().equals("[]")){
					sender.sendMessage(ChatColor.GOLD + "" + inhm1);
					return true;
				}else{
					sender.sendMessage(ChatColor.GOLD + "There are no players in that map!");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("invite")){
				if(getServer().getPlayer(args[1]) instanceof Player){
					Player target = getServer().getPlayer(args[1]);
					String targetName = target.getName();
					if(!invitedtohm1.contains(targetName)){
						if(!inhm1.contains(targetName)){
							invitedtohm1.add(targetName);
							sender.sendMessage(ChatColor.GOLD + "Invited " + targetName + " to join your adventure map.");
							target.sendMessage(ChatColor.GOLD + "" + sender.getName() + " invited you to join their adventure map!");
							target.sendMessage(ChatColor.GOLD + "Use " + ChatColor.RED + "/am join" + ChatColor.GOLD + " to join!");
							return true;
						}else{
							sender.sendMessage(ChatColor.GOLD + targetName + " is already in hm1!");
							return true;
						}
					}else{
						sender.sendMessage(ChatColor.GOLD + "You've already invited " + targetName + " to join your adventure map!");
						return true;
					}
				}else{
					sender.sendMessage(ChatColor.GOLD + "" + args[1] + " doesn't exist! Is their name spelled correctly?");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("join")){
				if(args.length != 2){
					sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RED + "/am join <adventuremap>");
					return true;
				}
				if(args[1].equalsIgnoreCase("herobrinesmansion1") || args[1].equalsIgnoreCase("hm1")){
					if(inhm1.toString().equals("[]")){
						Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), "mv tp " + sender.getName() + " hm1");
						inhm1.add(sender.getName());
						joinedhm1.add(sender.getName());
						sender.sendMessage(ChatColor.GOLD + "Joined hm1.");
						return true;
					}else{
						if(joinedhm1.contains(sender.getName())){
							getServer().dispatchCommand(getServer().getConsoleSender(), "mv tp " + sender.getName() + " hm1");
							sender.sendMessage(ChatColor.GOLD + "Joined hm1.");
							inhm1.add(sender.getName());
							return true;
						}
						if(!joinedhm1.contains(sender.getName())){
							if(!invitedtohm1.contains(sender.getName())){
								sender.sendMessage(ChatColor.GOLD + "Asked players in the adventure map if you can join them.");
								sender.sendMessage(ChatColor.GOLD + "Players in map: " + inhm1);
								for(Player players : getServer().getWorld("hm1").getPlayers()){
									players.sendMessage(ChatColor.RED + sender.getName() + ChatColor.GOLD + " would like to join your adventure map. Do " + ChatColor.RED + "/am invite <player>" + ChatColor.GOLD + " to let them join!");
								}
								return true;
							}
							if(invitedtohm1.contains(sender.getName())){
								getServer().dispatchCommand(getServer().getConsoleSender(), "mv tp " + sender.getName() + " hm1");
								sender.sendMessage(ChatColor.GOLD + "Joined hm1.");
								inhm1.add(sender.getName());
								joinedhm1.add(sender.getName());
								return true;
							}
							else{
								return true;
							}
						}
					}
				}
				
				sender.sendMessage(ChatColor.GOLD + "Couldn't find that adventure map! Use " + ChatColor.RED + "/am list" + ChatColor.GOLD + " to list available adventure maps.");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("leave")){
				if(args.length == 1){
					if(inhm1.contains(sender.getName())){
						inhm1.remove(sender.getName());
						sender.sendMessage(ChatColor.GOLD + "Exited hm1. To get back, " + ChatColor.RED + "/am join hm1");
						return true;
					}else{
						sender.sendMessage(ChatColor.GOLD + "You're not in an adventure map!");
						return true;
					}
				}else{
					sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RED + "/am leave");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("stop")){
				if(inhm1.contains(sender.getName())){
					if(exitinghm1.contains(sender.getName())){
						
						if(args[1].equalsIgnoreCase("cancel")){
							exitinghm1.remove(sender.getName());
							sender.sendMessage(ChatColor.GOLD + "Canceled stop.");
							return true;
						}
						
						if(args[1].equalsIgnoreCase("confirm")){
							exitinghm1.remove(sender.getName());
							sender.sendMessage(ChatColor.RED + "Stopping map...");
							getServer().dispatchCommand(getServer().getConsoleSender(), "am admin stop hm 1");
							inhm1.clear();
							joinedhm1.clear();
							return true;
						}
						return true;
					}
					if(!exitinghm1.contains(sender.getName())){
						exitinghm1.add(sender.getName());
						sender.sendMessage(ChatColor.RED + "Are you sure you want to stop the adventure map? This will reset the map.");
						sender.sendMessage(ChatColor.GOLD + "Type " + ChatColor.RED + "/am stop confirm" + ChatColor.GOLD + " to confirm.");
						sender.sendMessage(ChatColor.GOLD + "If you do not want to stop the map, use " + ChatColor.RED + "/am stop cancel");
						return true;
					}
				}
				if(!inhm1.contains(sender.getName())){
					sender.sendMessage(ChatColor.GOLD + "You're not in an adventure map!");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("admin")){
				
				if(args[1].equalsIgnoreCase("help")){
					sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "AdventureMaps Admin Help");
					sender.sendMessage(ChatColor.GOLD + "/am admin stop <adventuremap world>: regenerate an adventuremap world");
					return true;
				}
				
				if(args[1].equalsIgnoreCase("stop")){
					if(args.length != 4){
						sender.sendMessage(ChatColor.GOLD + "Usage: " + ChatColor.RED + "/am admin stop <map> <number>");
						return true;
					}
					if(getServer().getWorld(args[2] + args[3]) instanceof World){
						sender.sendMessage(ChatColor.GREEN + "Starting regeneration of " + args[2] + args[3]);
						sender.sendMessage(ChatColor.GOLD + "Unloading " + args[2] + args[3]);
						getServer().dispatchCommand(getServer().getConsoleSender(), "mv unload " + args[2] + args[3]);
						sender.sendMessage(ChatColor.GOLD + "Removing + Copying " + args[2] + args[3]);
						ProcessBuilder pb = new ProcessBuilder(getServer().getWorldContainer().getAbsolutePath() + args[2] + "reload.sh", args[2], args[3]);
						try{
							Process p = pb.start();
							BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line = null;
							while ((line = reader.readLine()) != null){
									Logger.getLogger("minecraft").info(line);
							}
						}catch (IOException e){
							e.printStackTrace();
						}
						sender.sendMessage(ChatColor.GOLD + "Importing " + args[2] + args[3]);
						getServer().dispatchCommand(getServer().getConsoleSender(), "mv import " + args[2] + args[3] + " NORMAL");
						sender.sendMessage(ChatColor.GREEN + "Done.");
						return true;
					}else{
						sender.sendMessage(ChatColor.GOLD + "Couldn't find that world! Make sure it's spelled properly!");
						return true;
					}
				}
				sender.sendMessage(ChatColor.GOLD + "Unknown command! Use " + ChatColor.RED + "/am help" + ChatColor.GOLD + " for help!");
				return true;
			}
			sender.sendMessage(ChatColor.GOLD + "Unknown command! Use " + ChatColor.RED + "/am help" + ChatColor.GOLD + " for help!");
			return true;
		}
		return true;
	}
}
