package ubertweakstor.xrazer;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class BlockListener implements Listener{
	
	XRazer parent;
	
	/*
	 * Name: BlockListener 
	 * Description: Constructor
	 * Returns: None
	 * Parameters: XRazer
	 * Requirements: None
	 */
	BlockListener(XRazer instance){
		parent = instance;
	}
	
	/*
	 * Name: onBlockBreak 
	 * Description: BlockBreakEvent listener
	 * Returns: None
	 * Parameters: BlockBreakEvent
	 * Requirements: None
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		List<String> loggedblocks = parent.getConfig().getStringList("LoggedBlocks");
		String blockid = parent.blocklogger.getBlockID(event.getBlock().getTypeId(), event.getBlock().getData());
		if((event.getBlock().getData()==0 && loggedblocks.contains(String.valueOf(event.getBlock().getTypeId()))) ||
				loggedblocks.contains(blockid)){	
			parent.blocklogger.incrementBlockScore(event.getBlock().getTypeId(), event.getBlock().getData(), event.getPlayer());
		}
	}
}
