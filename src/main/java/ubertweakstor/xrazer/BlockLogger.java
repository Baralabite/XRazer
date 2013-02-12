package ubertweakstor.xrazer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

public class BlockLogger {
	XRazer parent;
	HashMap<Player, Score> scores = new HashMap<Player, Score>();

	/*
	 * Name: BlockLogger Description: Constructor Returns: None Parameters:
	 * XRazer Requirements: None
	 */
	BlockLogger(XRazer instance) {
		parent = instance;
	}

	/*
	 * Name: incrementBlockScore Description: Increments the numbers of blocks
	 * mined in the subsiquent catagories Returns: None Parameters: int id:
	 * Block ID, int data: Block data, Player player: Which player's profile to
	 * update Requirements: None
	 */
	public void incrementBlockScore(int id, int data, Player player) {
		Score score;
		if (scores.containsKey(player)) {
			score = scores.get(player);
		} else {
			score = new Score(this);
		}
		score.incrementBlockIDScore(id, data);
		scores.put(player, score);
	}

	public Score getPlayerScore(Player player) {
		return scores.get(player);
	}

	/*
	 * Name: getPossibleXrayers Description: Returns List of all the players
	 * suspected of xraying since last check. Returns: List<Player> Parameters:
	 * None Requirements: None
	 */
	@SuppressWarnings("rawtypes")
	public List<Player> getPossibleXrayers() {
		List<Player> possibleXrayers = new ArrayList<Player>();
		Iterator it = scores.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Score score = (Score) pairs.getValue();
			if (score.getTotalScore() >= parent.getConfig().getInt(
					"XRayThreshold")
					&& !(parent.isPlayerWhitelisted(((Player) pairs.getKey())
							.getName()))) {
				possibleXrayers.add((Player) pairs.getKey());
			}
		}
		return possibleXrayers;
	}

        /*
	 * Name: clearScores()
         * Description: Clears everyone's mining scores
	 * Returns: None
	 * Parameters: None
	 * Requirements: None
	 */
	@SuppressWarnings("rawtypes")
	public void clearScores() {
		Iterator it = scores.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			scores.remove(pairs.getKey());
		}
	}

	/*
	 * Name: getBlockID 
         * Description: Get's the blockid, from the id, and data
	 * Returns: String, similar to: "35:0", which would be white wool
	 * Parameters: int id: The block's id, int data: The data of the block
	 * Requirements: None
	 */
	public String getBlockID(int id, int data) {
		return String.valueOf(id) + "," + String.valueOf(data);
	}
}