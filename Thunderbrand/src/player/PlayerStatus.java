package player;

import java.io.Serializable;

public class PlayerStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String boardName;
	private double xPos;
	private double yPos;
	private int currentTexture;
	private boolean facingRight;
	
	public String getBoardName() { return boardName; }
	public double getXPos() { return xPos; }
	public double getYPos() { return yPos; }
	public int getCurrentTexture() { return currentTexture; }
	public boolean getFacingRight() { return facingRight; }
	
	public PlayerStatus(String boardName, double xPos, double yPos, int currentTexture, boolean facingRight) {
		setAll(boardName, xPos, yPos, currentTexture, facingRight);
	}
	
	public void setAll(String boardName, double xPos, double yPos, int currentTexture, boolean facingRight) {
		this.boardName = boardName;
		this.xPos = xPos;
		this.yPos = yPos;
		this.currentTexture = currentTexture;
		this.facingRight = facingRight;
	}
	
}
