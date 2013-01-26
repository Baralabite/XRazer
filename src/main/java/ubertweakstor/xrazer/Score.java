package ubertweakstor.xrazer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Score {

	public HashMap<String, Integer> score = new HashMap<String, Integer>();
	BlockLogger parent;

	/*
	 * Name: Score Description: Constructor Returns: None Parameters:
	 * BlockLogger Requirements: None
	 */
	Score(BlockLogger instance) {
		parent = instance;
	}

	// =====[Change Score]=====//

	/*
	 * Name: incrementBlockIDScore Description: Increment a certain BlockID's
	 * score Returns: None Parameters: int id: Block's ID, int data: Block's
	 * Data Requirements: None
	 */
	public void incrementBlockIDScore(int id, int data) {
		String blockID = parent.getBlockID(id, data);
		if (score.containsKey(blockID) == false) {
			score.put(blockID, 1);
		} else {
			score.put(blockID, score.get(blockID) + 1);
		}
	}

	/*
	 * Name: setBlockIDScore Description: Set a certain BlockID's score Returns:
	 * None Parameters: int id: Block's ID, int data: Block's Data, int score:
	 * Score to set Requirements: None
	 */
	public void setBlockIDScore(int id, int data, int value) {
		String blockid = parent.getBlockID(id, data);
		score.put(blockid, value);
	}

	// =====[Gets]=====//

	/*
	 * Name: getBlockIDScore Description: Gets a certain BlockID's score
	 * Returns: Integer Parameters: int id: Block's ID, int data: Block's Data
	 * Requirements: None
	 */
	public Integer getBlockIDScore(int id, int data) {
		return score.get(parent.getBlockID(id, data));
	}

	/*
	 * Name: getTotalScore Description: Gets the score of everything Returns:
	 * Integer Parameters: None Requirements: None
	 */
	@SuppressWarnings("rawtypes")
	public Integer getTotalScore() {
		int total = 0;
		Iterator it = score.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			total = total + (Integer) pairs.getValue();
		}
		return total;
	}

	public HashMap<String, Integer> getScore() {
		return score;
	}

	// =====[Misc]=====//

	/*
	 * Name: clearData Description: Clears the hashmap Returns: None Parameters:
	 * None Requirements: None
	 */
	public void clearData() {
		score = new HashMap<String, Integer>();
	}
}