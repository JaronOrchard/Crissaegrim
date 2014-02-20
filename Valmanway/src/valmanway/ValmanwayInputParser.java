package valmanway;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ValmanwayInputParser {
	
	private static final String RECEIVE_PLAYER_STATUS = "02";
	
	/**
	 * == Valmanway templates for input from client ==
	 * 
	 * 02|player_id|board_name|x_pos|y_pos|texture|facing_right
	 * 		(The current status/position/likeliness of a player)
	 *  
	 */
	
	/**
	 * 
	 * @param inputFromClient
	 * @throws IOException
	 */
	public static void parseInputFromClient(BufferedReader inputFromClient) throws IOException {
		List<String> tokens = tokenizeInputLine(inputFromClient.readLine());
		
		if (tokens.isEmpty()) { return; }
		if (tokens.get(0).equals(RECEIVE_PLAYER_STATUS)) {
			Valmanway.getSharedData().updatePlayerStatusMap(
					Integer.parseInt(tokens.get(1)),
					new PlayerStatus(
							tokens.get(2),
							Double.parseDouble(tokens.get(3)),
							Double.parseDouble(tokens.get(4)),
							Integer.parseInt(tokens.get(5)),
							Boolean.parseBoolean(tokens.get(6))));
		} else {
			System.out.println("** Invalid input type parsed from client: " + tokens.get(0));
		}
	}
	
	private static List<String> tokenizeInputLine(String inputLine) {
		List<String> tokens = new ArrayList<String>();
		while (inputLine.contains("|")) {
			tokens.add(inputLine.substring(0, inputLine.indexOf("|")));
			inputLine = inputLine.substring(inputLine.indexOf("|") + 1);
		}
		tokens.add(inputLine);
		return tokens;
	}
	
}
