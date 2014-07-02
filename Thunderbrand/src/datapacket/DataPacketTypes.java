package datapacket;

public class DataPacketTypes {
	
	public static final int REQUEST_PLAYER_ID_PACKET =			1;	// Crissaegrim -> Valmanway
	public static final int RECEIVE_PLAYER_ID_PACKET =			2;	// Valmanway -> Crissaegrim
	public static final int RECEIVE_PLAYER_NAME_PACKET =		3;	// Valmanway -> Crissaegrim
	public static final int SEND_PLAYER_STATUS_PACKET =			4;	// Crissaegrim -> Valmanway
	public static final int SEND_ENTITY_STATUSES_PACKET =		5;	// Valmanway -> Crissaegrim
	public static final int SEND_CHAT_MESSAGE_PACKET =			6;	// (Crissaegrim ->) Valmanway -> Crissaegrim
	public static final int SEND_SYSTEM_MESSAGE_PACKET =		7;	// (Crissaegrim ->) Valmanway -> Crissaegrim
	public static final int PLAYER_IS_CHANGING_BOARDS_PACKET =	8;	// Crissaegrim -> Valmanway
	public static final int REQUEST_SPECIFIC_CHUNK_PACKET =		9;	// Crissaegrim -> Valmanway
	public static final int INCOMING_CHUNK_COUNT_PACKET =		10; // Valmanway -> Crissaegrim
	public static final int CHUNK_PACKET =						11;	// Valmanway -> Crissaegrim
	public static final int	NONEXISTENT_CHUNK_PACKET =			12;	// Valmanway -> Crissaegrim
	public static final int DONE_SENDING_CHUNKS_PACKET =		13;	// Valmanway -> Crissaegrim
	public static final int CLIENT_IS_OUTDATED_PACKET =			14; // Valmanway -> Crissaegrim
	public static final int ATTACK_PACKET =						15; // Crissaegrim -> Valmanway
	public static final int GOT_HIT_BY_ATTACK_PACKET =			16; // Valmanway -> Crissaegrim
	public static final int RECEIVE_ITEMS_PACKET =				17; // Valmanway -> Crissaegrim
	public static final int PARTICLE_SYSTEM_PACKET =			18; // (Crissaegrim ->) Valmanway -> Crissaegrim
	public static final int BOARD_DOODADS_PACKET =				19; // Valmanway -> Crissaegrim
	public static final int UPDATED_DOODAD_PACKET =				20; // (Crissaegrim ->) Valmanway -> Crissaegrim
	public static final int MINE_ROCK_REQUEST_PACKET =			21; // Crissaegrim -> Valmanway
	public static final int MINE_ROCK_RESULT_PACKET =			22; // Valmanway -> Crissaegrim
	
}
