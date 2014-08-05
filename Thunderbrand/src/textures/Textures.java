package textures;

import java.util.Arrays;
import java.util.List;

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
	public static final int TILE_PASS_LEFT = 17;
	public static final int TILE_PASS_RIGHT = 18;
	public static final int TILE_PASS_BOTTOM = 19;
	public static final int TILE_PASS_1L = 20;
	public static final int TILE_PASS_1R = 21;
	public static final int TILE_PASS_2L = 22;
	public static final int TILE_PASS_2LH = 23;
	public static final int TILE_PASS_2R = 24;
	public static final int TILE_PASS_2RH = 25;
	public static final int TILE_PASS_HALF_VERTICALLY = 26;
	
	public static final int TOWER_OF_PRELUDES_BLOCK_NONE = 120;
	public static final int TOWER_OF_PRELUDES_BLOCK_T = 121;
	public static final int TOWER_OF_PRELUDES_BLOCK_TR = 122;
	public static final int TOWER_OF_PRELUDES_BLOCK_TRB = 123;
	public static final int TOWER_OF_PRELUDES_BLOCK_TRBL = 124;
	public static final int TOWER_OF_PRELUDES_BLOCK_TRL = 125;
	public static final int TOWER_OF_PRELUDES_BLOCK_TB = 126;
	public static final int TOWER_OF_PRELUDES_BLOCK_TBL = 127;
	public static final int TOWER_OF_PRELUDES_BLOCK_TL = 128;
	public static final int TOWER_OF_PRELUDES_BLOCK_R = 129;
	public static final int TOWER_OF_PRELUDES_BLOCK_RB = 130;
	public static final int TOWER_OF_PRELUDES_BLOCK_RBL = 131;
	public static final int TOWER_OF_PRELUDES_BLOCK_RL = 132;
	public static final int TOWER_OF_PRELUDES_BLOCK_B = 133;
	public static final int TOWER_OF_PRELUDES_BLOCK_BL = 134;
	public static final int TOWER_OF_PRELUDES_BLOCK_L = 135;
	public static final int TOWER_OF_PRELUDES_1L_NONE = 136;
	public static final int TOWER_OF_PRELUDES_1L_B = 137;
	public static final int TOWER_OF_PRELUDES_1R_NONE = 138;
	public static final int TOWER_OF_PRELUDES_1R_B = 139;
	public static final int TOWER_OF_PRELUDES_1R_L = 140;
	public static final int TOWER_OF_PRELUDES_1RI_NONE = 141;
	public static final int TOWER_OF_PRELUDES_1RI_T = 142;
	public static final int TOWER_OF_PRELUDES_1LI_NONE = 143;
	public static final int TOWER_OF_PRELUDES_HALF_VERTICALLY_TB = 144;
	public static final int TOWER_OF_PRELUDES_HALF_VERTICALLY_TBR = 145;
	public static final int TOWER_OF_PRELUDES_HALF_VERTICALLY_TBL = 146;
	public static final int TOWER_OF_PRELUDES_BACKGROUND = 147;
	
	public static final int TOPLEVEL_GRASS_1L = 160;
	public static final int TOPLEVEL_GRASS_1R = 161;
	public static final int TOPLEVEL_GRASS_2L = 162;
	public static final int TOPLEVEL_GRASS_2LH = 163;
	public static final int TOPLEVEL_GRASS_2R = 164;
	public static final int TOPLEVEL_GRASS_2RH = 165;
	public static final int TOPLEVEL_GRASS_FULL_TOP = 166;
	public static final int TOPLEVEL_GRASS_HALF_VERTICALLY = 167;
	public static final int TOPLEVEL_GRASS_FULL_TL = 168;
	public static final int TOPLEVEL_GRASS_FULL_TR = 169;
	public static final int TOPLEVEL_GRASS_SPRIG_1 = 170;
	public static final int TOPLEVEL_GRASS_SPRIG_2 = 171;
	public static final int TOPLEVEL_GRASS_SPRIG_3 = 172;
	
	public static final int TOPLEVEL_DIRT_FULL = 174;
	public static final int TOPLEVEL_DIRT_FULL_TOP = 175;
	public static final int TOPLEVEL_DIRT_FULL_TL = 176;
	public static final int TOPLEVEL_DIRT_FULL_TR = 177;
	public static final int TOPLEVEL_DIRT_1L = 178;
	public static final int TOPLEVEL_DIRT_1R = 179;
	public static final int TOPLEVEL_DIRT_1LI = 180;
	public static final int TOPLEVEL_DIRT_1RI = 181;
	public static final int TOPLEVEL_DIRT_2L = 182;
	public static final int TOPLEVEL_DIRT_2LH = 183;
	public static final int TOPLEVEL_DIRT_2RH = 184;
	public static final int TOPLEVEL_DIRT_2R = 185;
	public static final int TOPLEVEL_DIRT_2LI = 186;
	public static final int TOPLEVEL_DIRT_2LHI = 187;
	public static final int TOPLEVEL_DIRT_2RHI = 188;
	public static final int TOPLEVEL_DIRT_2RI = 189;
	public static final int TOPLEVEL_DIRT_HALF_VERTICALLY = 190;
	public static final int TOPLEVEL_DIRT_HALF_VERTICALLY_I = 191;
	public static final int TOPLEVEL_DIRT_FULL_BOTTOM = 192;
	public static final int TOPLEVEL_DIRT_TEXDAD_1 = 193;
	public static final int TOPLEVEL_DIRT_TEXDAD_2 = 194;
	public static final int TOPLEVEL_DIRT_TEXDAD_3 = 195;
	public static final int TOPLEVEL_DIRT_TEXDAD_4 = 196;
	
	public static final int DAWNING_BRIDGE_2RH = 200;
	public static final int DAWNING_BRIDGE_2R = 201;
	public static final int DAWNING_BRIDGE_2RU = 202;
	public static final int DAWNING_BRIDGE_POST_L = 203;
	public static final int DAWNING_BRIDGE_POST_R = 204;
	
	public static final int WINDOW_TL = 210;
	public static final int WINDOW_TR = 211;
	public static final int WINDOW_BL = 212;
	public static final int WINDOW_BR = 213;
	
	public static final int MIDLEVEL_DIRT_1L = 220;
	public static final int MIDLEVEL_DIRT_1LI = 221;
	public static final int MIDLEVEL_DIRT_1R = 222;
	public static final int MIDLEVEL_DIRT_1RI = 223;
	public static final int MIDLEVEL_DIRT_2L = 224;
	public static final int MIDLEVEL_DIRT_2LH = 225;
	public static final int MIDLEVEL_DIRT_2LHI = 226;
	public static final int MIDLEVEL_DIRT_2LI = 227;
	public static final int MIDLEVEL_DIRT_2R = 228;
	public static final int MIDLEVEL_DIRT_2RH = 229;
	public static final int MIDLEVEL_DIRT_2RHI = 230;
	public static final int MIDLEVEL_DIRT_2RI = 231;
	public static final int MIDLEVEL_DIRT_FULL = 232;
	public static final int MIDLEVEL_DIRT_FULL_TOP = 233;
	public static final int MIDLEVEL_DIRT_FULL_TL = 234;
	public static final int MIDLEVEL_DIRT_FULL_TR = 235;
	public static final int MIDLEVEL_DIRT_HALF_VERTICALLY = 236;
	public static final int MIDLEVEL_DIRT_HALF_VERTICALLY_I = 237;
	public static final int MIDLEVEL_DIRT_FULL_BOTTOM = 238;
	public static final int MIDLEVEL_DIRT_TEXDAD_1 = 239;
	public static final int MIDLEVEL_DIRT_TEXDAD_2 = 240;
	public static final int MIDLEVEL_DIRT_TEXDAD_3 = 241;
	public static final int MIDLEVEL_DIRT_TEXDAD_4 = 242;
	
	public static final int TRANSITION_TOP_MID_DIRT_VERTICALLY = 250;
	public static final int TRANSITION_TOP_MID_DIRT_DIAGONALLY_LEFT_1 = 251;
	public static final int TRANSITION_TOP_MID_DIRT_DIAGONALLY_LEFT_2= 252;
	public static final int TRANSITION_TOP_MID_DIRT_DIAGONALLY_RIGHT_1 = 253;
	public static final int TRANSITION_TOP_MID_DIRT_DIAGONALLY_RIGHT_2 = 254;
	
	public static final int MINE_BACKGROUND = 265;
	public static final int MINE_BACKGROUND_LEFT = 266;
	public static final int MINE_BACKGROUND_RIGHT = 267;
	
	public static final int WOODEN_SIDE_LEDGE_LEFT = 270;
	public static final int WOODEN_SIDE_LEDGE_RIGHT = 271;
	public static final int WOODEN_SCAFFOLD_TOP_LEFT = 272;
	public static final int WOODEN_SCAFFOLD_TOP_RIGHT = 273;
	public static final int WOODEN_SCAFFOLD_TOP = 274;
	public static final int WOODEN_SCAFFOLD_LEFT = 275;
	public static final int WOODEN_SCAFFOLD_RIGHT = 276;
	public static final int WOODEN_SCAFFOLD_MIDDLE = 277;
	public static final int WOODEN_SCAFFOLD_SINGLE_TOP = 278;
	public static final int WOODEN_SCAFFOLD_SINGLE_BOTTOM = 279;
	
	public static final int STONE_LEDGE_LEFT = 300;
	public static final int STONE_LEDGE_MIDDLE = 301;
	public static final int STONE_LEDGE_RIGHT = 302;
	public static final int ROCK_SMALL = 303;
	public static final int ROCK_LARGE_TL_FLAT = 304;
	public static final int ROCK_LARGE_TL_SLOPED = 305;
	public static final int ROCK_LARGE_TR_FLAT = 306;
	public static final int ROCK_LARGE_TR_SLOPED = 307;
	public static final int ROCK_LARGE_BL = 308;
	public static final int ROCK_LARGE_BR = 309;
	
	public static final int CHUNK_NOT_FOUND = 999;
	
	public static final int STICK_PLAYER = 1000;
	public static final int STICK_PLAYER_TYPING = 1001;
	public static final int STICK_PLAYER_RHICHITE_SWORD_SWING_1 = 1002;
	public static final int STICK_PLAYER_RHICHITE_SWORD_SWING_2 = 1003;
	public static final int STICK_PLAYER_RHICHITE_SWORD_SWING_3 = 1004;
	public static final int STICK_PLAYER_RHICHITE_MINING_1 = 1005;
	public static final int STICK_PLAYER_RHICHITE_MINING_2 = 1006;
	public static final int STICK_PLAYER_STUNNED = 1007;
	public static final int STICK_PLAYER_DEAD = 1008;
	public static final int STICK_PLAYER_SMELTING_ORE = 1009;
	
	public static final int NPC_STICK_NINJA = 1100;
	public static final int NPC_STICK_NINJA_STUNNED = 1101;
	public static final int NPC_PHANTO = 1102;
	public static final int NPC_CHARGING_SPIKE = 1103;
	public static final int NPC_SPIKE_PIT_15 = 1104;
	
	public static final int ICON_F = 1800;
	public static final int ICON_LEFT_CLICK = 1801;
	
	public static final int TARGET = 2000;
	public static final int DOOR_CLOSED = 2001;
	public static final int DEPLETED_ROCK = 2020;
	public static final int RHICHITE_ROCK = 2021;
	public static final int VALENITE_ROCK = 2022;
	public static final int SANDELUGE_ROCK = 2023;
	public static final int FURNACE = 2040;
	
	public static final int LOADING_MESSAGE = 17900;
	public static final int NO_CONNECTION_MESSAGE = 17901;
	public static final int LOST_CONNECTION_MESSAGE = 17902;
	public static final int CLIENT_OUTDATED_MESSAGE = 17903;
	
	public static final int BACKGROUND_SOTN = 17960;
	
	public static final int ITEM_RHICHITE_SWORD = 20000;
	public static final int ITEM_PARTY_POPPER_BLUE = 20001;
	public static final int ITEM_PARTY_POPPER_GREEN = 20002;
	public static final int ITEM_PARTY_POPPER_RED = 20003;
	public static final int ITEM_PARTY_POPPER_WHITE = 20004;
	public static final int ITEM_PARTY_POPPER_YELLOW = 20005;
	public static final int ITEM_RHICHITE_PICKAXE = 20006;
	public static final int ITEM_RHICHITE_ORE = 20020;
	public static final int ITEM_VALENITE_ORE = 20021;
	public static final int ITEM_SANDELUGE_ORE = 20022;
	public static final int ITEM_RHICHITE_BAR = 20023;
	public static final int ITEM_VAL_SAN_BAR = 20024;
	
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
		TextureLoader.loadTexture(TILE_PASS_LEFT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePassLeft.png")));
		TextureLoader.loadTexture(TILE_PASS_RIGHT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePassRight.png")));
		TextureLoader.loadTexture(TILE_PASS_BOTTOM, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePassBottom.png")));
		TextureLoader.loadTexture(TILE_PASS_1L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePass1L.png")));
		TextureLoader.loadTexture(TILE_PASS_1R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePass1R.png")));
		TextureLoader.loadTexture(TILE_PASS_2L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePass2L.png")));
		TextureLoader.loadTexture(TILE_PASS_2LH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePass2LH.png")));
		TextureLoader.loadTexture(TILE_PASS_2R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePass2R.png")));
		TextureLoader.loadTexture(TILE_PASS_2RH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePass2RH.png")));
		TextureLoader.loadTexture(TILE_PASS_HALF_VERTICALLY, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/TilePassHalfVertically.png")));
		
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
		
		TextureLoader.loadTexture(TOPLEVEL_GRASS_1L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/1L.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_1R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/1R.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_2L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/2L.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_2LH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/2LH.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_2R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/2R.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_2RH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/2RH.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_FULL_TOP, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/FullTop.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_HALF_VERTICALLY, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/HalfVertically.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_FULL_TL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/FullTL.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_FULL_TR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/FullTR.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_SPRIG_1, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/GrassSprig1.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_SPRIG_2, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/GrassSprig2.png")));
		TextureLoader.loadTexture(TOPLEVEL_GRASS_SPRIG_3, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_grass/GrassSprig3.png")));
		
		TextureLoader.loadTexture(TOPLEVEL_DIRT_FULL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/Full.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_FULL_TOP, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/FullTop.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_FULL_TL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/FullTL.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_FULL_TR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/FullTR.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_1L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/1L.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_1R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/1R.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_1LI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/1LI.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_1RI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/1RI.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_2L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/2L.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_2LH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/2LH.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_2RH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/2RH.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_2R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/2R.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_2LI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/2LI.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_2LHI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/2LHI.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_2RHI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/2RHI.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_2RI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/2RI.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_HALF_VERTICALLY, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/HalfVertically.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_HALF_VERTICALLY_I, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/HalfVerticallyI.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_FULL_BOTTOM, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/FullBottom.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_TEXDAD_1, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/Texdad1.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_TEXDAD_2, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/Texdad2.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_TEXDAD_3, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/Texdad3.png")));
		TextureLoader.loadTexture(TOPLEVEL_DIRT_TEXDAD_4, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/toplevel_dirt/Texdad4.png")));
		
		TextureLoader.loadTexture(DAWNING_BRIDGE_2RH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/bridge/Bridge2RH.png")));
		TextureLoader.loadTexture(DAWNING_BRIDGE_2R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/bridge/Bridge2R.png")));
		TextureLoader.loadTexture(DAWNING_BRIDGE_2RU, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/bridge/Bridge2RUnder.png")));
		TextureLoader.loadTexture(DAWNING_BRIDGE_POST_L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/bridge/BridgePostL.png")));
		TextureLoader.loadTexture(DAWNING_BRIDGE_POST_R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/bridge/BridgePostR.png")));
		
		TextureLoader.loadTexture(WINDOW_TL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/window/windowTL.png")));
		TextureLoader.loadTexture(WINDOW_TR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/window/windowTR.png")));
		TextureLoader.loadTexture(WINDOW_BL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/window/windowBL.png")));
		TextureLoader.loadTexture(WINDOW_BR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/window/windowBR.png")));
		
		TextureLoader.loadTexture(MIDLEVEL_DIRT_1L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/1L.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_1LI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/1LI.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_1R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/1R.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_1RI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/1RI.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_2L, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/2L.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_2LH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/2LH.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_2LHI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/2LHI.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_2LI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/2LI.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_2R, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/2R.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_2RH, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/2RH.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_2RHI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/2RHI.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_2RI, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/2RI.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_FULL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/Full.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_FULL_TOP, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/FullTop.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_FULL_TL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/FullTL.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_FULL_TR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/FullTR.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_HALF_VERTICALLY, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/HalfVertically.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_HALF_VERTICALLY_I, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/HalfVerticallyI.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_FULL_BOTTOM, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/FullBottom.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_TEXDAD_1, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/Texdad1.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_TEXDAD_2, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/Texdad2.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_TEXDAD_3, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/Texdad3.png")));
		TextureLoader.loadTexture(MIDLEVEL_DIRT_TEXDAD_4, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/midlevel_dirt/Texdad4.png")));
		
		TextureLoader.loadTexture(TRANSITION_TOP_MID_DIRT_VERTICALLY, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/transitions/TopMidDirtVertically.png")));
		TextureLoader.loadTexture(TRANSITION_TOP_MID_DIRT_DIAGONALLY_LEFT_1, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/transitions/TopMidDirtDiagonallyLeft1.png")));
		TextureLoader.loadTexture(TRANSITION_TOP_MID_DIRT_DIAGONALLY_LEFT_2, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/transitions/TopMidDirtDiagonallyLeft2.png")));
		TextureLoader.loadTexture(TRANSITION_TOP_MID_DIRT_DIAGONALLY_RIGHT_1, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/transitions/TopMidDirtDiagonallyRight1.png")));
		TextureLoader.loadTexture(TRANSITION_TOP_MID_DIRT_DIAGONALLY_RIGHT_2, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ground/transitions/TopMidDirtDiagonallyRight2.png")));
		
		TextureLoader.loadTexture(MINE_BACKGROUND, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/BackgroundFull.png")));
		TextureLoader.loadTexture(MINE_BACKGROUND_LEFT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/BackgroundLeft.png")));
		TextureLoader.loadTexture(MINE_BACKGROUND_RIGHT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/BackgroundRight.png")));
		
		TextureLoader.loadTexture(WOODEN_SIDE_LEDGE_LEFT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/WoodenSideLedgeLeft.png")));
		TextureLoader.loadTexture(WOODEN_SIDE_LEDGE_RIGHT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/WoodenSideLedgeRight.png")));
		TextureLoader.loadTexture(WOODEN_SCAFFOLD_TOP_LEFT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/WoodenScaffoldTopLeft.png")));
		TextureLoader.loadTexture(WOODEN_SCAFFOLD_TOP_RIGHT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/WoodenScaffoldTopRight.png")));
		TextureLoader.loadTexture(WOODEN_SCAFFOLD_TOP, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/WoodenScaffoldTop.png")));
		TextureLoader.loadTexture(WOODEN_SCAFFOLD_LEFT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/WoodenScaffoldLeft.png")));
		TextureLoader.loadTexture(WOODEN_SCAFFOLD_RIGHT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/WoodenScaffoldRight.png")));
		TextureLoader.loadTexture(WOODEN_SCAFFOLD_MIDDLE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/WoodenScaffoldMiddle.png")));
		TextureLoader.loadTexture(WOODEN_SCAFFOLD_SINGLE_TOP, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/WoodenScaffoldSingleTop.png")));
		TextureLoader.loadTexture(WOODEN_SCAFFOLD_SINGLE_BOTTOM, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/WoodenScaffoldSingleBottom.png")));
		
		TextureLoader.loadTexture(STONE_LEDGE_LEFT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/StoneLedgeLeft.png")));
		TextureLoader.loadTexture(STONE_LEDGE_MIDDLE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/StoneLedgeMiddle.png")));
		TextureLoader.loadTexture(STONE_LEDGE_RIGHT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/StoneLedgeRight.png")));
		TextureLoader.loadTexture(ROCK_SMALL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/RockSmall.png")));
		TextureLoader.loadTexture(ROCK_LARGE_TL_FLAT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/RockLargeTLFlat.png")));
		TextureLoader.loadTexture(ROCK_LARGE_TL_SLOPED, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/RockLargeTLSloped.png")));
		TextureLoader.loadTexture(ROCK_LARGE_TR_FLAT, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/RockLargeTRFlat.png")));
		TextureLoader.loadTexture(ROCK_LARGE_TR_SLOPED, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/RockLargeTRSloped.png")));
		TextureLoader.loadTexture(ROCK_LARGE_BL, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/RockLargeBL.png")));
		TextureLoader.loadTexture(ROCK_LARGE_BR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/mine/RockLargeBR.png")));
		
		TextureLoader.loadTexture(CHUNK_NOT_FOUND, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ChunkNotFound.png")));
				
		TextureLoader.loadTexture(STICK_PLAYER, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayer.png")));
		TextureLoader.loadTexture(STICK_PLAYER_TYPING, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerTyping.png")));
		TextureLoader.loadTexture(STICK_PLAYER_RHICHITE_SWORD_SWING_1, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerRhichiteSwordSwing1.png")));
		TextureLoader.loadTexture(STICK_PLAYER_RHICHITE_SWORD_SWING_2, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerRhichiteSwordSwing2.png")));
		TextureLoader.loadTexture(STICK_PLAYER_RHICHITE_SWORD_SWING_3, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerRhichiteSwordSwing3.png")));
		TextureLoader.loadTexture(STICK_PLAYER_RHICHITE_MINING_1, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerRhichiteMining1.png")));
		TextureLoader.loadTexture(STICK_PLAYER_RHICHITE_MINING_2, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerRhichiteMining2.png")));
		TextureLoader.loadTexture(STICK_PLAYER_STUNNED, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerStunned.png")));
		TextureLoader.loadTexture(STICK_PLAYER_DEAD, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerDead.png")));
		TextureLoader.loadTexture(STICK_PLAYER_SMELTING_ORE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/StickPlayerSmeltingOre.png")));
		
		TextureLoader.loadTexture(NPC_STICK_NINJA, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/NPC.png")));
		TextureLoader.loadTexture(NPC_STICK_NINJA_STUNNED, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/NPCStunned.png")));
		TextureLoader.loadTexture(NPC_PHANTO, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/npc/Phanto.png")));
		TextureLoader.loadTexture(NPC_CHARGING_SPIKE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/npc/ChargingSpike.png")));
		TextureLoader.loadTexture(NPC_SPIKE_PIT_15, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/npc/SpikePit15.png")));
		
		TextureLoader.loadTexture(ICON_F, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/IconF.png")));
		TextureLoader.loadTexture(ICON_LEFT_CLICK, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/IconLeftClick.png")));
		
		TextureLoader.loadTexture(TARGET, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/Target.png")));
		TextureLoader.loadTexture(DOOR_CLOSED, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/DoorClosed.png")));
		TextureLoader.loadTexture(DEPLETED_ROCK, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/doodads/DepletedRock.png")));
		TextureLoader.loadTexture(RHICHITE_ROCK, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/doodads/RhichiteRock.png")));
		TextureLoader.loadTexture(VALENITE_ROCK, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/doodads/ValeniteRock.png")));
		TextureLoader.loadTexture(SANDELUGE_ROCK, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/doodads/SandelugeRock.png")));
		TextureLoader.loadTexture(FURNACE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/doodads/Furnace.png")));
		
		TextureLoader.loadTexture(LOADING_MESSAGE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/LoadingMessage.png")));
		TextureLoader.loadTexture(NO_CONNECTION_MESSAGE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/NoConnectionMessage.png")));
		TextureLoader.loadTexture(LOST_CONNECTION_MESSAGE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/LostConnectionMessage.png")));
		TextureLoader.loadTexture(CLIENT_OUTDATED_MESSAGE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/ClientOutdatedMessage.png")));
		
		TextureLoader.loadTexture(BACKGROUND_SOTN, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/SotNCastle.png")));
		
		TextureLoader.loadTexture(ITEM_RHICHITE_SWORD, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/RhichiteSword.png")));
		TextureLoader.loadTexture(ITEM_PARTY_POPPER_BLUE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/PartyPopperBlue.png")));
		TextureLoader.loadTexture(ITEM_PARTY_POPPER_GREEN, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/PartyPopperGreen.png")));
		TextureLoader.loadTexture(ITEM_PARTY_POPPER_RED, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/PartyPopperRed.png")));
		TextureLoader.loadTexture(ITEM_PARTY_POPPER_WHITE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/PartyPopperWhite.png")));
		TextureLoader.loadTexture(ITEM_PARTY_POPPER_YELLOW, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/PartyPopperYellow.png")));
		TextureLoader.loadTexture(ITEM_RHICHITE_PICKAXE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/RhichitePickaxe.png")));
		TextureLoader.loadTexture(ITEM_RHICHITE_ORE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/RhichiteOre.png")));
		TextureLoader.loadTexture(ITEM_VALENITE_ORE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/ValeniteOre.png")));
		TextureLoader.loadTexture(ITEM_SANDELUGE_ORE, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/SandelugeOre.png")));
		TextureLoader.loadTexture(ITEM_RHICHITE_BAR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/RhichiteBar.png")));
		TextureLoader.loadTexture(ITEM_VAL_SAN_BAR, TextureLoader.loadImage(ClassLoader.getSystemResourceAsStream("resources/textures/items/ValSanBar.png")));
		
	}
	
	private static List<Integer> selectableTextures = Arrays.asList(
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, // default tiles
			120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, // tower of preludes
			160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, // toplevel_grass
			174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, // toplevel_dirt
			200, 201, 202, 203, 204, // dawning_bridge
			210, 211, 212, 213, // window
			220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, // midlevel_dirt
			250, 251, 252, 253, 254, // transitions
			265, 266, 267, // mine backgrounds
			270, 271, 272, 273, 274, 275, 276, 277, 278, 279, // wooden ledges/scaffolding
			300, 301, 302, 303, 304, 305, 306, 307, 308, 309, // mine stones/rocks
			999, // chunk not found
			2000 // target
			);
	
	public static List<Integer> getSelectableTextures() { return selectableTextures; }
	
}
