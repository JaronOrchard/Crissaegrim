package valmanway;

import java.io.PrintWriter;
import java.util.Set;
import java.util.Map.Entry;

class ValmanwayOutputWriter {
	
	private static String TRANSMIT_PLAYER_ID = "01"; // 01[int playerId]
	private static String TRANSMIT_PLAYER_STATUSES = "03";
	
	/**
	 * == Valmanway templates for output to client ==
	 * 
	 * 1[int playerId]
	 * 		(The playerId the connected player should use)
	 * 3{player_id|board_name|x_pos|y_pos|texture|facing_right|color_scheme}|{REPEAT...}
	 * 
	 */
	
	public static void sendPlayerId(PrintWriter out, int playerId) {
		out.println(TRANSMIT_PLAYER_ID + playerId);
	}
	
	public static void sendAllPlayerStatuses(PrintWriter out) {
		Set<Entry<Integer, PlayerStatus>> playerStatuses = Valmanway.getSharedData().getPlayerStatuses();
		if (playerStatuses.size() <= 1) return; // Only one player is here!
		StringBuilder outputLine = new StringBuilder();
		outputLine.append(TRANSMIT_PLAYER_STATUSES);
		for (Entry<Integer, PlayerStatus> playerStatus : playerStatuses) {
			outputLine.append(playerStatus.getKey());
			outputLine.append("|");
			outputLine.append(playerStatus.getValue().getBoardName());
			outputLine.append("|");
			outputLine.append(playerStatus.getValue().getXPos());
			outputLine.append("|");
			outputLine.append(playerStatus.getValue().getYPos());
			outputLine.append("|");
			outputLine.append(playerStatus.getValue().getCurrentTexture());
			outputLine.append("|");
			outputLine.append(playerStatus.getValue().getFacingRight());
			outputLine.append("|");
		}
		outputLine.deleteCharAt(outputLine.length() - 1); // Remove last pipe
		out.println(outputLine.toString());
		System.out.println(outputLine.toString());
	}
	
}
