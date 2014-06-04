package datapacket;

public class DataPacketTypes {
	
	public static final int REQUEST_PLAYER_ID_PACKET =			1;	// Crissaegrim -> Valmanway
	public static final int RECEIVE_PLAYER_ID_PACKET =			2;	// Valmanway -> Crissaegrim
	public static final int RECEIVE_PLAYER_NAME_PACKET =		3;	// Valmanway -> Crissaegrim
	public static final int SEND_PLAYER_STATUS_PACKET =			4;	// Crissaegrim -> Valmanway
	public static final int SEND_ALL_PLAYER_STATUSES_PACKET =	5;	// Valmanway -> Crissaegrim
	public static final int SEND_CHAT_MESSAGE_PACKET =			6;	// (Crissaegrim ->) Valmanway -> Crissaegrim
	public static final int REQUEST_ENTIRE_BOARD_PACKET =		7;	// Crissaegrim -> Valmanway
	public static final int REQUEST_SPECIFIC_CHUNK_PACKET =		8;	// Crissaegrim -> Valmanway
	public static final int CHUNK_PACKET =						9;	// Valmanway -> Crissaegrim
	public static final int	NONEXISTENT_CHUNK_PACKET =			10;	// Valmanway -> Crissaegrim
	public static final int DONE_SENDING_CHUNKS_PACKET =		11;	// Valmanway -> Crissaegrim
	public static final int CLIENT_IS_OUTDATED_PACKET =			12; // Valmanway -> Crissaegrim
	public static final int ATTACK_PACKET =						13; // Crissaegrim -> Valmanway
	public static final int GOT_HIT_BY_ATTACK_PACKET =			14; // Valmanway -> Crissaegrim
	public static final int INCOMING_CHUNK_COUNT_PACKET =		15; // Valmanway -> Crissaegrim
	public static final int RECEIVE_ITEMS_PACKET =				16; // Valmanway -> Crissaegrim
	public static final int PARTICLE_SYSTEM_PACKET =			17; // (Crissaegrim ->) Valmanway -> Crissaegrim
	
}
