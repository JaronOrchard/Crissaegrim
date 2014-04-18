package board;

import thunderbrand.Thunderbrand;
import tiles.Tile1L;
import tiles.Tile1LI;
import tiles.Tile1R;
import tiles.Tile1RI;
import tiles.Tile2L;
import tiles.Tile2LH;
import tiles.Tile2LHI;
import tiles.Tile2LI;
import tiles.Tile2R;
import tiles.Tile2RH;
import tiles.Tile2RHI;
import tiles.Tile2RI;
import tiles.TileBlank;
import tiles.TileFull;
import tiles.TileHalfVertically;
import tiles.TileHalfVerticallyI;
import tiles.TilePassTop;

public class DebugChunk extends Chunk {
	
	public DebugChunk(int xOrig, int yOrig) {
		super(xOrig, yOrig);
		createDebugBoard();
	}
	
	private void createDebugBoard() {
		int chunkSideSize = Thunderbrand.getChunkSideSize();
		for (int i = 0; i < chunkSideSize; i++) {
			for (int j = 0; j < chunkSideSize; j++) {
				tiles[i][j] = new TileBlank();
			}
		}
		
		tiles[11][39] = new Tile1L();
		tiles[14][39] = new Tile1LI();
		tiles[17][39] = new Tile1R();
		tiles[20][39] = new Tile1RI();
		tiles[23][39] = new Tile2L();
		tiles[26][39] = new Tile2LH();
		tiles[29][39] = new Tile2LHI();
		tiles[32][39] = new Tile2LI();
		tiles[35][39] = new Tile2R();
		tiles[38][39] = new Tile2RH();
		tiles[41][39] = new Tile2RHI();
		tiles[44][39] = new Tile2RI();
		tiles[47][39] = new TileFull();
		tiles[50][39] = new TileHalfVertically();
		tiles[53][39] = new TileHalfVerticallyI();
		tiles[56][39] = new TilePassTop();
		
		tiles[2][99] = new TilePassTop();
		
		tiles[0][95] = new TilePassTop();
		tiles[1][95] = new TilePassTop();
		tiles[2][95] = new TilePassTop();
		
		tiles[1][91] = new TilePassTop();
		tiles[2][91] = new TilePassTop();
		
		tiles[0][87] = new TilePassTop();
		tiles[1][87] = new TilePassTop();
		
		tiles[0][83] = new TilePassTop();
		
		tiles[0][79] = new TilePassTop();
		tiles[1][79] = new TilePassTop();
		
		tiles[1][75] = new TilePassTop();
		tiles[2][75] = new TilePassTop();
		
		tiles[0][71] = new TilePassTop();
		tiles[1][71] = new TilePassTop();
		
		tiles[0][67] = new TilePassTop();
		tiles[2][67] = new TilePassTop();
		
		tiles[0][63] = new TilePassTop();
		tiles[2][63] = new TilePassTop();
		
		tiles[0][59] = new TilePassTop();
		tiles[1][59] = new TilePassTop();
		tiles[2][59] = new TilePassTop();
		
		tiles[0][55] = new TilePassTop();
		tiles[1][55] = new TilePassTop();
		tiles[2][55] = new TilePassTop();
		
		tiles[1][51] = new TilePassTop();
		
		tiles[0][47] = new TilePassTop();
		tiles[1][47] = new TilePassTop();
		
		tiles[0][43] = new TilePassTop();
		tiles[1][43] = new TilePassTop();
		
		tiles[1][39] = new TilePassTop();
		tiles[2][39] = new TilePassTop();
				
		tiles[0][35] = new TilePassTop();
		tiles[1][35] = new TilePassTop();
		tiles[2][35] = new TilePassTop();
		
		tiles[1][31] = new TilePassTop();
		tiles[2][31] = new TilePassTop();
		
		tiles[0][27] = new TilePassTop();
		tiles[1][27] = new TilePassTop();
		
		tiles[15][25] = new Tile1R();
		
		tiles[15][24] = new TileFull();
		tiles[16][24] = new Tile1R();
		tiles[84][24] = new Tile1L();
		tiles[85][24] = new Tile1R();
		
		tiles[1][23] = new TilePassTop();
		tiles[2][23] = new TilePassTop();
		tiles[15][23] = new TileFull();
		tiles[16][23] = new TileFull();
		tiles[17][23] = new Tile1R();
		tiles[34][23] = new TileFull();
		tiles[35][23] = new Tile2RH();
		tiles[36][23] = new Tile2R();
		tiles[83][23] = new Tile1L();
		tiles[84][23] = new TileFull();
		tiles[85][23] = new Tile1RI();
		
		tiles[15][22] = new TileFull();
		tiles[16][22] = new TileFull();
		tiles[17][22] = new TileFull();
		tiles[34][22] = new TileFull();
		tiles[35][22] = new TileFull();
		tiles[36][22] = new TileFull();
		tiles[43][22] = new TileFull();
		tiles[44][22] = new Tile1R();
		tiles[83][22] = new Tile1LI();
		tiles[84][22] = new Tile1RI();
		
		tiles[14][21] = new TileFull();
		tiles[15][21] = new TileFull();
		tiles[16][21] = new TileFull();
		tiles[17][21] = new TileFull();
		tiles[34][21] = new TileFull();
		tiles[44][21] = new Tile1LI();
		tiles[45][21] = new TileFull();
		tiles[51][21] = new TileFull();
		tiles[58][21] = new TileFull();
		tiles[66][21] = new TileFull();
		tiles[75][21] = new TileFull();
		
		tiles[14][20] = new TileFull();
		tiles[15][20] = new TileFull();
		tiles[16][20] = new TileFull();
		tiles[17][20] = new TileFull();
		tiles[34][20] = new TileFull();
		
		tiles[0][19] = new TilePassTop();
		tiles[1][19] = new TilePassTop();
		tiles[2][19] = new TilePassTop();
		tiles[14][19] = new TileFull();
		tiles[15][19] = new TileFull();
		tiles[16][19] = new TileFull();
		tiles[17][19] = new Tile1RI();
		tiles[23][19] = new TilePassTop();
		tiles[24][19] = new TilePassTop();
		tiles[25][19] = new TilePassTop();
		tiles[26][19] = new TilePassTop();
		tiles[33][19] = new TileFull();
		tiles[34][19] = new TileFull();
		tiles[82][19] = new TileHalfVertically();
		tiles[83][19] = new TileHalfVertically();
		
		tiles[14][18] = new TileFull();
		tiles[15][18] = new TileFull();
		tiles[82][18] = new Tile1LI();
		tiles[83][18] = new Tile1RI();
		
		tiles[13][17] = new TileFull();
		tiles[14][17] = new TileFull();
		tiles[15][17] = new TileFull();
		tiles[51][17] = new Tile1LI();
		tiles[52][17] = new TileFull();
		
		tiles[13][16] = new TileFull();
		tiles[14][16] = new TileFull();
		tiles[15][16] = new TileFull();
		
		tiles[0][15] = new TilePassTop();
		tiles[1][15] = new TilePassTop();
		tiles[2][15] = new TilePassTop();
		tiles[13][15] = new TileFull();
		tiles[14][15] = new TileFull();
		tiles[17][15] = new TileFull();
		tiles[77][15] = new Tile1L();
		tiles[78][15] = new TileHalfVertically();
		tiles[79][15] = new TileHalfVertically();
		
		tiles[13][14] = new TileFull();
		tiles[14][14] = new TileFull();
		tiles[17][14] = new TileFull();
		tiles[18][14] = new TileFull();
		tiles[77][14] = new TileFull();
		tiles[78][14] = new TileHalfVerticallyI();
		
		tiles[12][13] = new TileFull();
		tiles[13][13] = new TileFull();
		tiles[18][13] = new TileFull();
		tiles[19][13] = new Tile2RH();
		tiles[20][13] = new Tile2R();
		tiles[21][13] = new Tile1L();
		tiles[22][13] = new TileFull();
		tiles[23][13] = new TileFull();
		tiles[24][13] = new TileFull();
		tiles[25][13] = new Tile1R();
		tiles[51][13] = new Tile2LI();
		tiles[52][13] = new Tile2LHI();
		tiles[53][13] = new TileFull();
		tiles[60][13] = new TileFull();
		tiles[61][13] = new Tile2RH();
		tiles[62][13] = new Tile2R();
		tiles[77][13] = new TileFull();
		
		tiles[12][12] = new TileFull();
		tiles[13][12] = new TileFull();
		tiles[16][12] = new TilePassTop();
		tiles[19][12] = new TileFull();
		tiles[20][12] = new TileFull();
		tiles[21][12] = new TileFull();
		tiles[22][12] = new TileFull();
		tiles[23][12] = new TileFull();
		tiles[24][12] = new TileFull();
		tiles[25][12] = new TileFull();
		tiles[26][12] = new Tile1R();
		tiles[61][12] = new Tile2LI();
		tiles[62][12] = new Tile2LHI();
		tiles[63][12] = new Tile2RH();
		tiles[64][12] = new Tile2R();
		tiles[72][12] = new TileFull();
		tiles[77][12] = new TileFull();
		
		tiles[0][11] = new TilePassTop();
		tiles[1][11] = new TilePassTop();
		tiles[2][11] = new TilePassTop();
		tiles[12][11] = new TileFull();
		tiles[13][11] = new TileFull();
		tiles[19][11] = new TileFull();
		tiles[20][11] = new TileFull();
		tiles[21][11] = new TileFull();
		tiles[22][11] = new TileFull();
		tiles[23][11] = new TileFull();
		tiles[24][11] = new TileFull();
		tiles[25][11] = new TileFull();
		tiles[26][11] = new TileFull();
		tiles[63][11] = new Tile2LI();
		tiles[64][11] = new Tile2LHI();
		tiles[65][11] = new Tile2RH();
		tiles[66][11] = new Tile2R();
		tiles[72][11] = new TileFull();
		tiles[76][11] = new Tile1L();
		tiles[77][11] = new TileFull();
		
		tiles[12][10] = new TileFull();
		tiles[13][10] = new TileFull();
		tiles[15][10] = new TileFull();
		tiles[16][10] = new TileFull();
		tiles[17][10] = new TileFull();
		tiles[18][10] = new TileFull();
		tiles[19][10] = new Tile1RI();
		tiles[20][10] = new Tile1LI();
		tiles[21][10] = new TileFull();
		tiles[22][10] = new TileFull();
		tiles[23][10] = new TileFull();
		tiles[24][10] = new TileFull();
		tiles[25][10] = new TileFull();
		tiles[26][10] = new TileFull();
		tiles[65][10] = new Tile2LI();
		tiles[66][10] = new Tile2LHI();
		tiles[72][10] = new TileFull();
		tiles[77][10] = new TileFull();
		
		tiles[9][9] = new TileHalfVertically();
		tiles[10][9] = new TileHalfVertically();
		tiles[11][9] = new Tile2LH();
		tiles[12][9] = new TileFull();
		tiles[13][9] = new TileFull();
		tiles[15][9] = new TileFull();
		tiles[16][9] = new Tile1RI();
		tiles[17][9] = new Tile1LI();
		tiles[18][9] = new Tile1RI();
		tiles[21][9] = new Tile1LI();
		tiles[22][9] = new TileFull();
		tiles[23][9] = new TileFull();
		tiles[24][9] = new TileFull();
		tiles[25][9] = new TileFull();
		tiles[26][9] = new TileFull();
		tiles[72][9] = new TileFull();
		tiles[77][9] = new TileFull();
		
		tiles[10][8] = new Tile2RHI();
		tiles[11][8] = new Tile2RI();
		tiles[13][8] = new Tile1RI();
		tiles[15][8] = new Tile1RI();
		tiles[22][8] = new Tile1LI();
		tiles[23][8] = new TileFull();
		tiles[24][8] = new TileFull();
		tiles[25][8] = new Tile1RI();
		tiles[71][8] = new TileFull();
		tiles[72][8] = new TileFull();
		tiles[77][8] = new TileFull();
		
		tiles[0][7] = new TileFull();
		tiles[23][7] = new Tile1LI();
		tiles[24][7] = new Tile1RI();
		tiles[29][7] = new TilePassTop();
		tiles[30][7] = new TilePassTop();
		tiles[31][7] = new TilePassTop();
		tiles[71][7] = new TileFull();
		tiles[72][7] = new TileFull();
		tiles[73][7] = new TileFull();
		tiles[77][7] = new TileFull();
		
		tiles[0][6] = new TileFull();
		tiles[2][6] = new TileFull();
		tiles[50][6] = new Tile1L();
		tiles[51][6] = new Tile1R();
		tiles[52][6] = new Tile1L();
		tiles[53][6] = new Tile1R();
		tiles[61][6] = new TileFull();
		tiles[71][6] = new TileFull();
		tiles[72][6] = new TileFull();
		tiles[77][6] = new TileFull();
		
		tiles[0][5] = new TileFull();
		tiles[5][5] = new Tile2L();
		tiles[6][5] = new TileHalfVertically();
		tiles[7][5] = new Tile2R();
		tiles[49][5] = new Tile1L();
		tiles[50][5] = new TileFull();
		tiles[51][5] = new TileFull();
		tiles[52][5] = new TileFull();
		tiles[53][5] = new TileFull();
		tiles[54][5] = new Tile1R();
		tiles[70][5] = new TileFull();
		tiles[71][5] = new TileFull();
		tiles[72][5] = new TileFull();
		tiles[77][5] = new TileFull();
		
		tiles[0][4] = new TileFull();
		tiles[1][4] = new Tile1R();
		tiles[4][4] = new Tile1L();
		tiles[5][4] = new TileFull();
		tiles[6][4] = new TileFull();
		tiles[7][4] = new TileFull();
		tiles[8][4] = new Tile2RH();
		tiles[18][4] = new Tile1R();
		tiles[26][4] = new TileFull();
		tiles[48][4] = new Tile1L();
		tiles[49][4] = new TileFull();
		tiles[50][4] = new TileFull();
		tiles[51][4] = new TileFull();
		tiles[52][4] = new TileFull();
		tiles[53][4] = new TileFull();
		tiles[54][4] = new TileFull();
		tiles[55][4] = new Tile2RH();
		tiles[56][4] = new Tile2R();
		tiles[70][4] = new TileFull();
		tiles[71][4] = new TileFull();
		tiles[72][4] = new TileFull();
		tiles[77][4] = new TileFull();
		
		tiles[0][3] = new TileFull();
		tiles[1][3] = new TileFull();
		tiles[3][3] = new Tile1L();
		tiles[4][3] = new TileFull();
		tiles[5][3] = new TileFull();
		tiles[6][3] = new TileFull();
		tiles[7][3] = new TileFull();
		tiles[8][3] = new TileFull();
		tiles[12][3] = new Tile1L();
		tiles[13][3] = new Tile1R();
		tiles[16][3] = new Tile2L();
		tiles[18][3] = new TileFull();
		tiles[19][3] = new Tile1R();
		tiles[21][3] = new Tile1L();
		tiles[26][3] = new TileFull();
		tiles[27][3] = new TileFull();
		tiles[31][3] = new Tile2RH();
		tiles[34][3] = new Tile2R();
		tiles[36][3] = new Tile2RH();
		tiles[37][3] = new Tile2R();
		tiles[39][3] = new Tile1R();
		tiles[47][3] = new Tile1L();
		tiles[48][3] = new TileFull();
		tiles[49][3] = new TileFull();
		tiles[50][3] = new TileFull();
		tiles[51][3] = new TileFull();
		tiles[52][3] = new TileFull();
		tiles[53][3] = new TileFull();
		tiles[54][3] = new TileFull();
		tiles[55][3] = new TileFull();
		tiles[56][3] = new TileFull();
		tiles[57][3] = new Tile2RH();
		tiles[58][3] = new Tile2R();
		tiles[59][3] = new Tile2L();
		tiles[60][3] = new TileHalfVertically();
		tiles[61][3] = new TileHalfVertically();
		tiles[62][3] = new TileHalfVertically();
		tiles[63][3] = new Tile2R();
		tiles[64][3] = new Tile1L();
		tiles[65][3] = new TileFull();
		tiles[66][3] = new Tile2RH();
		tiles[67][3] = new Tile2LH();
		tiles[68][3] = new TileFull();
		tiles[70][3] = new TileFull();
		tiles[71][3] = new TileFull();
		tiles[72][3] = new TileFull();
		tiles[75][3] = new Tile1L();
		tiles[76][3] = new TileFull();
		tiles[77][3] = new TileFull();
		
		for (int i = 0; i <= 77; i++) {
			tiles[i][2] = new TileFull();
		}
		
	}
	
}
