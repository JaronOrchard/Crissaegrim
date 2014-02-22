package datapacket;

public class SendPlayerStatusPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final String boardName;
	private final double xPos;
	private final double yPos;
	private final int currentTexture;
	private boolean facingRight;
	
	public String getBoardName() { return boardName; }
	public double getXPos() { return xPos; }
	public double getYPos() { return yPos; }
	public int getCurrentTexture() { return currentTexture; }
	public boolean getFacingRight() { return facingRight; }
	
	public SendPlayerStatusPacket(String boardName, double xPos, double yPos, int currentTexture, boolean facingRight) {
		super(DataPacketTypes.SEND_PLAYER_STATUS_PACKET);
		this.boardName = boardName;
		this.xPos = xPos;
		this.yPos = yPos;
		this.currentTexture = currentTexture;
		this.facingRight = facingRight;
	}
	
}
