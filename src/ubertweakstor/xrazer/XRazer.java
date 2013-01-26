/*
 * Author: John "Ubertweakstor" Board
 * Date: 21/12/12 14:10
 * Description: XRay Detector Plugin
 */

package ubertweakstor.xrazer;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ubertweakstor.xrazer.Log;

public class XRazer extends JavaPlugin{
	
	Log log = new Log();
	BlockListener blocklistener = new BlockListener(this);
	BlockLogger blocklogger = new BlockLogger(this);
	XRazerCommandExecutor commandexecutor = new XRazerCommandExecutor(this);
	
	//=====[Starting/Stopping]=====//
	
	/*
	 * Name: onEnable 
	 * Description: Called when plugin is enabled.
	 * Returns: None
	 * Parameters: None
	 * Requirements: None
	 */
	public void onEnable(){		
		initalizeEvents();
		getConfig().options().copyDefaults(true);
		saveConfig();
		//initalizeConfig();
		initalizeMainLoop();
		log.info("Enabled.");
	}
	
	/*
	 * Name: initalizeEvents 
	 * Description: Intalizes all events
	 * Returns: None
	 * Parameters: None
	 * Requirements: None
	 */
	public void initalizeEvents(){
		getServer().getPluginManager().registerEvents(blocklistener, this);
	}
	
	/*
	 * Name: initalizeConfig 
	 * Description: Initalizes configuration
	 * Returns: None
	 * Parameters: None
	 * Requirements: None
	 */
	/*public void initalizeConfig(){
		getConfig().options().copyDefaults(true);
		saveConfig();
	}*/
	
	/*
	 * Name: initalizeMainLoop 
	 * Description: Initalizes the main loop (scheduled event)
	 * Returns: None
	 * Parameters: None
	 * Requirements: None
	 */
	public void initalizeMainLoop(){
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
		    @Override  
		    public void run() {
		    	log.info("Scanning for XRayers...");
		        List<Player> possibleXrayers = blocklogger.getPossibleXrayers();
		        for (Player ply: possibleXrayers){				        	
		        	log.info(ply.getName()+" is suspected of XRaying.");
		        	broadcastToRank(ChatColor.RED+ply.getName()+ChatColor.AQUA+" is suspected of XRaying. "+
		        			ChatColor.RED+"Total: "+ChatColor.GOLD+
		        			String.valueOf(blocklogger.getPlayerScore(ply).getTotalScore())
		        			, "xrazer.alert");
		        }		        
		    }
		}, minutesToTicks(getConfig().getInt("AlertPeriod")), minutesToTicks(getConfig().getInt("AlertPeriod")));
		
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
		    @Override  
		    public void run() {
		    	log.info("Resetting XRaying Statistics.");
		        blocklogger.clearScores();
		    }
		}, minutesToTicks(getConfig().getInt("ResetPeriod")), minutesToTicks(getConfig().getInt("ResetPeriod")));
		
	}
	
	
	/*
	 * Name: onDisable 
	 * Description: Called when plugin is disabled.
	 * Returns: None
	 * Parameters: None
	 * Requirements: None
	 */
	public void onDisable(){
		log.info("Disabled.");
	}
	
	
	//=====[Util]=====//
	
	/*
	 * Name: onCommand 
	 * Description: Called when a command has been executed
	 * Returns: boolean
	 * Parameters: CommandSender sender, Command cmd, String label, String[] args
	 * Requirements: None
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		commandexecutor.onCommand(sender, cmd, label, args);
		return true;
	}
	
	/*
	 * Name: isPlayerWhitelisted 
	 * Description: Returns if a player is whitelisted in the configuration
	 * Returns: boolean
	 * Parameters: String player, player to check
	 * Requirements: None
	 */
	public boolean isPlayerWhitelisted(String player){
		if (getConfig().getStringList("WhitelistedPlayers").contains(player)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/*
	 * Name: addPlayerToWhitelist 
	 * Description: Adds a player to the whitelist in the configuration
	 * Returns: None
	 * Parameters: String player
	 * Requirements: None
	 */
	public void addPlayerToWhitelist(String player){
		List<String> whitelistedplayers = getConfig().getStringList("WhitelistedPlayers");		
		whitelistedplayers.add(player);
		getConfig().set("WhitelistedPlayers", whitelistedplayers);
	}
	
	/*
	 * Name: removePlayerFromWhitelist 
	 * Description: removes a player from the whitelist in the configuration
	 * Returns: None
	 * Parameters: String player
	 * Requirements: None
	 */
	public void removePlayerFromWhitelist(String player){
		List<String> whitelistedplayers = getConfig().getStringList("WhitelistedPlayers");
		whitelistedplayers.remove(player);
		getConfig().set("WhitelistedPlayers", whitelistedplayers);
	}
	
	/*
	 * Name: minutesToTicks 
	 * Description: Converts minutes to the equivalent ticks
	 * Returns: Integer (ticks)
	 * Parameters: Integer minutes: Number of minutes to convert
	 * Requirements: None
	 */
	public Integer minutesToTicks(int minutes){
		return minutes*1200;
	}
	
	/*
	 * Name: secondsToTicks 
	 * Description: Converts x seconds to x ticks.
	 * Returns: Integer (ticks)
	 * Parameters: Integer (seconds)
	 * Requirements: None
	 */
	public Integer secondsToTicks(int seconds){
		return seconds*20;
	}
	
	
	/*
	 * Name: broadcastToRank 
	 * Description: Broadcasts a certain message to a certain rank
	 * Returns: None
	 * Parameters: String msg: The message to broadcast, String perms: The node someone needs 
	 *             to hear this
	 * Requirements: None
	 */
	public void broadcastToRank(String msg, String perms){
		for(Player p: getServer().getOnlinePlayers()){
			if (p.hasPermission(perms)){
				p.sendMessage(msg);
			}
		}
	}

}
