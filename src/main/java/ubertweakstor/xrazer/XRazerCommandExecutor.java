package ubertweakstor.xrazer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class XRazerCommandExecutor {

	XRazer parent;

	/*
	 * Name: XRazerCommandExecutor Description: Constructor Returns: None
	 * Parameters: XRazer Requirements: None
	 */
	XRazerCommandExecutor(XRazer instance) {
		parent = instance;
	}

	/*
	 * Name: onCommand Description: Gets called when a command is executed
	 * Returns: boolean Parameters: CommandSender, Command, String, String[]
	 * Requirements: None
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (parent.getServer().getPlayer(sender.getName()) == null) {
			parent.log.info("This command can only be sent by a player");
			return true;
		}
		Player player = (Player) sender;
		String subname;

		if (label.equalsIgnoreCase("xrazer")) {
			if (args.length == 0) {
				errorUnknownCommand(player);
				return true;
			} else {
				subname = args[0];
			}

			if (subname.equalsIgnoreCase("version")) {
				if (player.hasPermission("xrazer.version")) {
					if (args.length == 1) {
						commandVersion(player, args);
						return true;
					} else {
						errorInvalidSyntax(player);
						return true;
					}
				} else {
					errorPermissions(player);
					return true;
				}
			}

			else if (subname.equalsIgnoreCase("reload")) {
				if (player.hasPermission("xrazer.reload")) {
					if (args.length == 1) {
						commandReload(player, args);
						return true;
					} else {
						errorInvalidSyntax(player);
						return true;
					}
				} else {
					errorPermissions(player);
					return true;
				}
			}

			else if (subname.equalsIgnoreCase("check")) {
				if (player.hasPermission("xrazer.check")) {
					if (args.length == 2) {

						if (!(parent.getServer().getPlayer(args[1]) == null)) {
							commandCheck(player, args);
							return true;
						} else {
							errorPlayerNotOnline(player);
							return true;
						}
					} else {
						errorInvalidSyntax(player);
						return true;
					}
				} else {
					errorPermissions(player);
					return true;
				}
			}

			else if (subname.equalsIgnoreCase("reload")) {
				if (args.length == 1) {
					commandReload(player, args);
					return true;
				} else {
					errorInvalidSyntax(player);
					return true;
				}
			}

			else if (subname.equalsIgnoreCase("whitelist")) {
				if (player.hasPermission("xrazer.whitelist")) {
					if (args.length >= 2) {
						String subsubname = args[1];

						if (subsubname.equalsIgnoreCase("add")) {
							if (args.length == 3) {
								commandWhitelistAdd(player, args);
								return true;
							} else {
								errorInvalidSyntax(player);
								return true;
							}
						} else if (subsubname.equalsIgnoreCase("remove")) {
							if (args.length == 3) {
								commandWhitelistRemove(player, args);
								return true;
							} else {
								errorInvalidSyntax(player);
								return true;
							}
						} else if (subsubname.equalsIgnoreCase("list")) {
							commandWhitelistList(player, args);
							return true;
						} else {
							errorUnknownCommand(player);
							return true;
						}
					} else {
						errorInvalidSyntax(player);
						return true;
					}
				} else {
					errorPermissions(player);
					return true;
				}
			}

			else {
				errorUnknownCommand(player);
				return true;
			}

		} else {
			errorUnknownCommand(player);
			return true;
		}
	}

	// =====[Command Actions]=====//

	public void commandWhitelistList(Player player, String[] args) {
		List<String> whitelistedplayers = parent.getConfig().getStringList(
				"WhitelistedPlayers");
		player.sendMessage(ChatColor.BLUE + "=====[" + ChatColor.GOLD
				+ "Whitelisted Players" + ChatColor.BLUE + "]=====");
		for (String ply : whitelistedplayers) {
			player.sendMessage(ChatColor.GREEN + ply);
		}
		player.sendMessage(ChatColor.BLUE + "===========================");
	}

	public void commandWhitelistAdd(Player player, String[] args) {
		boolean isWhitelisted = parent.isPlayerWhitelisted(args[2]);
		if (!isWhitelisted) {
			parent.addPlayerToWhitelist(args[2]);
			parent.saveConfig();
			player.sendMessage(ChatColor.RED + args[2] + ChatColor.AQUA
					+ " added to the whitelist.");
		} else {
			player.sendMessage(ChatColor.RED
					+ "ERROR: Player already whitelisted.");
		}
	}

	public void commandWhitelistRemove(Player player, String[] args) {
		boolean isWhitelisted = parent.isPlayerWhitelisted(args[2]);
		if (isWhitelisted) {
			parent.removePlayerFromWhitelist(args[2]);
			parent.saveConfig();
			player.sendMessage(ChatColor.RED + args[2] + ChatColor.AQUA
					+ " removed from the whitelist.");
		} else {
			player.sendMessage(ChatColor.RED + "ERROR: Player not whitelisted");
		}
	}

	@SuppressWarnings("rawtypes")
	public void commandCheck(Player player, String[] args) {
		Player target = parent.getServer().getPlayer(args[1]);
		int totalScore;
		HashMap<String, Integer> score;

		try {
			score = parent.blocklogger.getPlayerScore(target).getScore();
			totalScore = parent.blocklogger.getPlayerScore(target)
					.getTotalScore();
		} catch (NullPointerException ex) {
			player.sendMessage(ChatColor.RED + player.getName()
					+ ChatColor.AQUA + " XRaying Statistics");
			player.sendMessage(ChatColor.DARK_AQUA
					+ "=======================================");
			player.sendMessage(ChatColor.BLUE + "Total: " + ChatColor.GREEN
					+ "0");
			return;
		}

		player.sendMessage(ChatColor.RED + player.getName() + ChatColor.AQUA
				+ " XRaying Statistics");
		player.sendMessage(ChatColor.DARK_AQUA
				+ "=======================================");
		Iterator it = score.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			player.sendMessage(ChatColor.GRAY + (String) pairs.getKey() + ": "
					+ String.valueOf(pairs.getValue()));
		}
		String total;
		if (totalScore >= parent.getConfig().getInt("XRayThreshold")) {
			total = ChatColor.RED + String.valueOf(totalScore);
		} else {
			total = ChatColor.GREEN + String.valueOf(totalScore);
		}
		player.sendMessage(ChatColor.BLUE + "Total: " + total);
	}

	public void commandVersion(Player player, String[] args) {
		player.sendMessage(ChatColor.GREEN + "XRazer v" + parent.log.VERSION);
	}

	public void commandReload(Player player, String[] args) {
		parent.reloadConfig();
		player.sendMessage(ChatColor.GREEN + "Configuration Reloaded.");
	}

	// =====[Error Messages]=====//

	public void errorInvalidSyntax(Player player) {
		player.sendMessage(ChatColor.RED + "ERROR: Invalid Syntax");
	}

	public void errorPermissions(Player player) {
		player.sendMessage(ChatColor.RED + "ERROR: You do not have permission");
	}

	public void errorUnknownCommand(Player player) {
		player.sendMessage(ChatColor.RED + "ERROR: Unknown Command");
	}

	public void errorPlayerNotOnline(Player player) {
		player.sendMessage(ChatColor.RED + "ERROR: Player not online");
	}

	/*
	 * if (label.equalsIgnoreCase("xrazer")){ if
	 * (args[0].equalsIgnoreCase("check")){ if
	 * (player.hasPermission("xrazer.check")){ if (!(args.length < 2)){
	 * 
	 * if (parent.getServer().getPlayer(args[1])==null){
	 * player.sendMessage(ChatColor.RED+"ERROR: Player Not Online."); return
	 * true; }
	 * 
	 * HashMap<String, Integer> ratios; int stonescore; float ratio;
	 * 
	 * try{ stonescore =
	 * parent.blocklogger.ratios.get(parent.getServer().getPlayer
	 * (args[1])).getStoneScore(); } catch (Exception ex){ stonescore = 0; }
	 * try{ ratios =
	 * parent.blocklogger.ratios.get(parent.getServer().getPlayer(args
	 * [1])).ratio; ratios.remove("Stone"); } catch (Exception ex){ ratios = new
	 * HashMap<String, Integer>(); System.out.println("B"); } try{ ratio =
	 * parent
	 * .blocklogger.ratios.get(parent.getServer().getPlayer(args[1])).getRatio
	 * (); } catch (Exception ex){ ratio = 0.0F; System.out.println("C"); }
	 * player
	 * .sendMessage(ChatColor.RED+args[1]+ChatColor.AQUA+" XRaying Statistics."
	 * ); player.sendMessage(ChatColor.DARK_AQUA+
	 * "========================================");
	 * player.sendMessage(ChatColor.
	 * GRAY+"Stone Mined: "+ChatColor.GREEN+stonescore); Iterator it =
	 * ratios.entrySet().iterator(); while (it.hasNext()) { Map.Entry pairs =
	 * (Map.Entry)it.next(); player.sendMessage(ChatColor.BLUE+(String)
	 * pairs.getKey()+": "+ChatColor.GREEN+pairs.getValue()); it.remove(); }
	 * player.sendMessage(ChatColor.DARK_RED+"Ratio: "+ChatColor.GOLD+ratio);
	 * return true; } else{
	 * player.sendMessage(ChatColor.RED+"ERROR: Invalid Syntax."); return true;
	 * } } else{ player.sendMessage(ChatColor.RED+
	 * "ERROR: You do not have permission to do this."); return true; } } else
	 * if (args[0].equalsIgnoreCase("whitelist")){ if (!(args.length < 2)){ if
	 * (args[1].equalsIgnoreCase("add")){ if
	 * (player.hasPermission("xrazer.whitelist")){ List<String>
	 * whitelistedplayers =
	 * parent.getConfig().getStringList("WhitelistedPlayers"); if
	 * (whitelistedplayers.contains(args[2])==true){
	 * player.sendMessage(ChatColor
	 * .RED+"ERROR: That player is already on the whitelist configuration.");
	 * return true; } else{ whitelistedplayers.add(args[2]);
	 * parent.getConfig().set("WhitelistedPlayers", whitelistedplayers);
	 * parent.saveConfig();
	 * player.sendMessage(ChatColor.AQUA+"Player added to the whitelist!");
	 * return true; } } else{ player.sendMessage(ChatColor.RED+
	 * "ERROR: You do not have permission to do this."); return true; }
	 * 
	 * } else if (args[1].equalsIgnoreCase("remove")){ if
	 * (player.hasPermission("xrazer.whitelist")){ List<String>
	 * whitelistedplayers =
	 * parent.getConfig().getStringList("WhitelistedPlayers"); if
	 * (whitelistedplayers.contains(args[2])==false){
	 * player.sendMessage(ChatColor
	 * .RED+"ERROR: That player is not whitelisted in the configuration.");
	 * return true; } else{ whitelistedplayers.remove(args[2]);
	 * parent.getConfig().set("WhitelistedPlayers", whitelistedplayers);
	 * parent.saveConfig();
	 * player.sendMessage(ChatColor.AQUA+"Player removed from whitelist!");
	 * return true; } } else{ player.sendMessage(ChatColor.RED+
	 * "ERROR: You do not have permission to do this."); return true; } } }
	 * else{ player.sendMessage(ChatColor.RED+"ERROR: Invalid Syntax."); return
	 * true; } if (args[1].equalsIgnoreCase("list")){ if
	 * (player.hasPermission("xrazer.whitelist")){ List<String>
	 * whitelistedplayers =
	 * parent.getConfig().getStringList("WhitelistedPlayers");
	 * player.sendMessage
	 * (ChatColor.BLUE+"=====["+ChatColor.GOLD+"Whitelisted Players"+"]=====");
	 * for (String ply: whitelistedplayers){
	 * player.sendMessage(ChatColor.GREEN+ply); }
	 * player.sendMessage(ChatColor.BLUE+"===============================");
	 * return true; } else{ player.sendMessage(ChatColor.RED+
	 * "ERROR: You do not have permission to do this."); } } } else if
	 * (args[0].equalsIgnoreCase("reload")){ if
	 * (player.hasPermission("xrazer.reload")){ parent.reloadConfig(); return
	 * true; } else{ player.sendMessage(ChatColor.RED+
	 * "ERROR: You do not have permission to do this."); return true; } } else
	 * if (args[0].equalsIgnoreCase("version")){
	 * player.sendMessage(ChatColor.GREEN+"XRazer v"+parent.log.VERSION); return
	 * true; }
	 * 
	 * } else{ player.sendMessage(ChatColor.RED+"ERROR: Unknown Command.");
	 * return true; } return true; }
	 */

}
