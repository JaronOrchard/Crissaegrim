package entities;

import java.io.Serializable;

public class EntityStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name; // TODO: Is it possible to remove this?  Crissaegrim needs it, but perhaps it could keep track of it in a client-side map.
	private String boardName;
	private double xPos;
	private double yPos;
	private int currentTexture;
	private boolean facingRight;
	private double amtHealth;
	private final double textureHalfWidth;
	private final double textureHeight;
	
	public String getName() { return name; }
	public String getBoardName() { return boardName; }
	public double getXPos() { return xPos; }
	public double getYPos() { return yPos; }
	public int getCurrentTexture() { return currentTexture; }
	public boolean getFacingRight() { return facingRight; }
	public double getAmtHealth() { return amtHealth; }
	public double getTextureHalfWidth() { return textureHalfWidth; }
	public double getTextureHeight() { return textureHeight; }
	
	public EntityStatus(String name, String boardName, double xPos, double yPos, int currentTexture, boolean facingRight, double amtHealth,
			double textureHalfWidth, double textureHeight) {
		setAll(name, boardName, xPos, yPos, currentTexture, facingRight, amtHealth);
		this.textureHalfWidth = textureHalfWidth;
		this.textureHeight = textureHeight;
	}
	
	public void setAll(String name, String boardName, double xPos, double yPos, int currentTexture, boolean facingRight, double amtHealth) {
		this.name = name;
		this.boardName = boardName;
		this.xPos = xPos;
		this.yPos = yPos;
		this.currentTexture = currentTexture;
		this.facingRight = facingRight;
		this.amtHealth = amtHealth;
	}
	
}
