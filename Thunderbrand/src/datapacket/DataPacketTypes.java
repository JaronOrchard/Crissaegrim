package datapacket;

public class DataPacketTypes {
	
	public static final int REQUEST_PLAYER_ID_PACKET =			1;	// Crissaegrim -> Valmanway
	public static final int RECEIVE_PLAYER_ID_PACKET =			2;	// Valmanway -> Crissaegrim
	public static final int RECEIVE_PLAYER_NAME_PACKET =		3;	// Valmanway -> Crissaegrim
	public static final int SEND_PLAYER_STATUS_PACKET =			4;	// Crissaegrim -> Valmanway
	public static final int SEND_ALL_PLAYER_STATUSES_PACKET =	5;	// Valmanway -> Crissaegrim
	public static final int SEND_CHAT_MESSAGE_PACKET =			6;	// (Crissaegrim ->) Valmanway -> Crissaegrim
	
}
