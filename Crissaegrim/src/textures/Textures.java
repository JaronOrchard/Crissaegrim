package textures;

public class Textures {
	
	public static final int NONE = 0;
	
	public static final int TILE_FULL = 1;
	public static final int TILE_HALF_VERTICALLY = 2;
	public static final int TILE_1L = 3;
	public static final int TILE_1R = 4;
	public static final int TILE_2L = 5;
	public static final int TILE_2LH = 6;
	public static final int TILE_2R = 7;
	public static final int TILE_2RH = 8;
	public static final int TILE_1LI = 9;
	public static final int TILE_1RI = 10;
	public static final int TILE_2LI = 11;
	public static final int TILE_2LHI = 12;
	public static final int TILE_2RI = 13;
	public static final int TILE_2RHI = 14;
	public static final int TILE_HALF_VERTICALLY_I = 15;
	public static final int TILE_PASS_TOP = 16;
	
	public static final int TOWER_OF_PRELUDES_BLOCK_NONE = 20;
	public static final int TOWER_OF_PRELUDES_BLOCK_T = 21;
	public static final int TOWER_OF_PRELUDES_BLOCK_TR = 22;
	public static final int TOWER_OF_PRELUDES_BLOCK_TRB = 23;
	public static final int TOWER_OF_PRELUDES_BLOCK_TRBL = 24;
	public static final int TOWER_OF_PRELUDES_BLOCK_TRL = 25;
	public static final int TOWER_OF_PRELUDES_BLOCK_TB = 26;
	public static final int TOWER_OF_PRELUDES_BLOCK_TBL = 27;
	public static final int TOWER_OF_PRELUDES_BLOCK_TL = 28;
	public static final int TOWER_OF_PRELUDES_BLOCK_R = 29;
	public static final int TOWER_OF_PRELUDES_BLOCK_RB = 30;
	public static final int TOWER_OF_PRELUDES_BLOCK_RBL = 31;
	public static final int TOWER_OF_PRELUDES_BLOCK_RL = 32;
	public static final int TOWER_OF_PRELUDES_BLOCK_B = 33;
	public static final int TOWER_OF_PRELUDES_BLOCK_BL = 34;
	public static final int TOWER_OF_PRELUDES_BLOCK_L = 35;
	public static final int TOWER_OF_PRELUDES_1L_NONE = 36;
	public static final int TOWER_OF_PRELUDES_1L_B = 37;
	public static final int TOWER_OF_PRELUDES_1R_NONE = 38;
	public static final int TOWER_OF_PRELUDES_1R_B = 39;
	public static final int TOWER_OF_PRELUDES_1R_L = 40;
	public static final int TOWER_OF_PRELUDES_1RI_NONE = 41;
	public static final int TOWER_OF_PRELUDES_1RI_T = 42;
	public static final int TOWER_OF_PRELUDES_1LI_NONE = 43;
	public static final int TOWER_OF_PRELUDES_HALF_VERTICALLY_TB = 44;
	public static final int TOWER_OF_PRELUDES_HALF_VERTICALLY_TBR = 45;
	public static final int TOWER_OF_PRELUDES_HALF_VERTICALLY_TBL = 46;
	public static final int TOWER_OF_PRELUDES_BACKGROUND = 47;
	
	public static final int KIKORI_DG_1L = 60;
	public static final int KIKORI_DG_1R = 61;
	public static final int KIKORI_DG_2L = 62;
	public static final int KIKORI_DG_2LH = 63;
	public static final int KIKORI_DG_2R = 64;
	public static final int KIKORI_DG_2RH = 65;
	public static final int KIKORI_DG_FULL = 66;
	public static final int KIKORI_DG_FULL_L = 67;
	public static final int KIKORI_DG_FULL_OFL = 68;
	public static final int KIKORI_DG_FULL_OFR = 69;
	public static final int KIKORI_DG_FULL_R = 70;
	public static final int KIKORI_DG_HALF_VERTICALLY = 71;
	public static final int KIKORI_DG_TL = 72;
	public static final int KIKORI_DG_TR = 73;
	public static final int KIKORI_D_FULL = 74;
	public static final int KIKORI_D_FULL_L = 75;
	public static final int KIKORI_D_FULL_OFL = 76;
	public static final int KIKORI_D_FULL_OFR = 77;
	public static final int KIKORI_D_FULL_R = 78;
	public static final int KIKORI_GRASS_SPRIG_1 = 79;
	public static final int KIKORI_GRASS_SPRIG_2 = 80;
	public static final int KIKORI_GRASS_SPRIG_3 = 81;
	
	public static final int DAWNING_BRIDGE_2RH = 100;
	public static final int DAWNING_BRIDGE_2R = 101;
	public static final int DAWNING_BRIDGE_2RU = 102;
	public static final int DAWNING_BRIDGE_POST_L = 103;
	public static final int DAWNING_BRIDGE_POST_R = 104;
	
	public static final int CHUNK_NOT_FOUND = 999;
	
	public static final int STICK_PLAYER = 1000;
	public static final int STICK_PLAYER_ATTACK_1 = 1001;
	public static final int STICK_PLAYER_ATTACK_2 = 1002;
	public static final int STICK_PLAYER_TYPING = 1003;
	
	public static final int ICON_X = 1800;
	
	public static final int TARGET = 2000;
	public static final int DOOR_CLOSED = 2001;
	
	public static final int LOADING_MESSAGE = 17900;
	public static final int NO_CONNECTION_MESSAGE = 17901;
	public static final int LOST_CONNECTION_MESSAGE = 17902;
	
	public static final int BACKGROUND_SOTN = 17960;
	
	public static final int ITEM_STARTER_SWORD = 20000;
	
	// (Texture IDs 100000 and up are reserved for text)
	
	public static void initializeTextures() {
		TextureLoader.loadTexture(NONE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Blank.png")));
		
		TextureLoader.loadTexture(TILE_FULL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TileFull.png")));
		TextureLoader.loadTexture(TILE_HALF_VERTICALLY, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TileHalfVertically.png")));
		TextureLoader.loadTexture(TILE_1L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile1L.png")));
		TextureLoader.loadTexture(TILE_1R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile1R.png")));
		TextureLoader.loadTexture(TILE_2L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile2L.png")));
		TextureLoader.loadTexture(TILE_2LH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile2LH.png")));
		TextureLoader.loadTexture(TILE_2R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile2R.png")));
		TextureLoader.loadTexture(TILE_2RH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile2RH.png")));
		TextureLoader.loadTexture(TILE_1LI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile1LI.png")));
		TextureLoader.loadTexture(TILE_1RI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile1RI.png")));
		TextureLoader.loadTexture(TILE_2LI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile2LI.png")));
		TextureLoader.loadTexture(TILE_2LHI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile2LHI.png")));
		TextureLoader.loadTexture(TILE_2RI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile2RI.png")));
		TextureLoader.loadTexture(TILE_2RHI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Tile2RHI.png")));
		TextureLoader.loadTexture(TILE_HALF_VERTICALLY_I, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TileHalfVerticallyI.png")));
		TextureLoader.loadTexture(TILE_PASS_TOP, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePassTop.png")));
		
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_NONE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockNone.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_T, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockT.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_TR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockTR.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_TRB, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockTRB.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_TRBL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockTRBL.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_TRL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockTRL.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_TB, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockTB.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_TBL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockTBL.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_TL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockTL.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockR.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_RB, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockRB.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_RBL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockRBL.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_RL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockRL.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_B, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockB.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_BL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockBL.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BLOCK_L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/blockL.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_1L_NONE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/1LNone.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_1L_B, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/1LB.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_1R_NONE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/1RNone.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_1R_B, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/1RB.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_1R_L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/1RL.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_1RI_NONE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/1RINone.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_1RI_T, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/1RIT.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_1LI_NONE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/1LINone.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_HALF_VERTICALLY_TB, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/HalfVerticallyTB.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_HALF_VERTICALLY_TBR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/HalfVerticallyTBR.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_HALF_VERTICALLY_TBL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/HalfVerticallyTBL.png")));
		TextureLoader.loadTexture(TOWER_OF_PRELUDES_BACKGROUND, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/tower_of_preludes/towerBG.png")));
		
		TextureLoader.loadTexture(KIKORI_DG_1L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&G1L.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_1R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&G1R.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_2L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&G2L.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_2LH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&G2LH.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_2R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&G2R.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_2RH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&G2RH.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_FULL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&GFull.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_FULL_L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&GFullL.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_FULL_OFL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&GFullOFL.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_FULL_OFR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&GFullOFR.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_FULL_R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&GFullR.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_HALF_VERTICALLY, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&GHalfVertically.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_TL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&GTL.PNG")));
		TextureLoader.loadTexture(KIKORI_DG_TR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileD&GTR.PNG")));
		TextureLoader.loadTexture(KIKORI_D_FULL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileDFull.PNG")));
		TextureLoader.loadTexture(KIKORI_D_FULL_L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileDFullL.PNG")));
		TextureLoader.loadTexture(KIKORI_D_FULL_OFL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileDFullOFL.PNG")));
		TextureLoader.loadTexture(KIKORI_D_FULL_OFR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileDFullOFR.PNG")));
		TextureLoader.loadTexture(KIKORI_D_FULL_R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/TileDFullR.PNG")));
		TextureLoader.loadTexture(KIKORI_GRASS_SPRIG_1, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/GrassSprig1.png")));
		TextureLoader.loadTexture(KIKORI_GRASS_SPRIG_2, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/GrassSprig2.png")));
		TextureLoader.loadTexture(KIKORI_GRASS_SPRIG_3, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/kikori/GrassSprig3.png")));
		
		TextureLoader.loadTexture(DAWNING_BRIDGE_2RH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/bridge/Bridge2RH.png")));
		TextureLoader.loadTexture(DAWNING_BRIDGE_2R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/bridge/Bridge2R.png")));
		TextureLoader.loadTexture(DAWNING_BRIDGE_2RU, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/bridge/Bridge2RUnder.png")));
		TextureLoader.loadTexture(DAWNING_BRIDGE_POST_L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/bridge/BridgePostL.png")));
		TextureLoader.loadTexture(DAWNING_BRIDGE_POST_R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/bridge/BridgePostR.png")));
		
		TextureLoader.loadTexture(CHUNK_NOT_FOUND, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ChunkNotFound.png")));
				
		TextureLoader.loadTexture(STICK_PLAYER, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayer.png")));
		TextureLoader.loadTexture(STICK_PLAYER_ATTACK_1, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerAttack1.png")));
		TextureLoader.loadTexture(STICK_PLAYER_ATTACK_2, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerAttack2.png")));
		TextureLoader.loadTexture(STICK_PLAYER_TYPING, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerTyping.png")));
		
		TextureLoader.loadTexture(ICON_X, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/IconX.png")));
		
		TextureLoader.loadTexture(TARGET, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Target.png")));
		TextureLoader.loadTexture(DOOR_CLOSED, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/DoorClosed.png")));
		
		TextureLoader.loadTexture(LOADING_MESSAGE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/LoadingMessage.png")));
		TextureLoader.loadTexture(NO_CONNECTION_MESSAGE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/NoConnectionMessage.png")));
		TextureLoader.loadTexture(LOST_CONNECTION_MESSAGE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/LostConnectionMessage.png")));
		
		TextureLoader.loadTexture(BACKGROUND_SOTN, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/SotNCastle.png")));
		
		TextureLoader.loadTexture(ITEM_STARTER_SWORD, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ItemStarterSword.png")));
		
	}
	
}
