package datapacket;

import textblock.TextBlock;

public class SendSystemMessagePacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final TextBlock textBlock;
	public TextBlock getTextBlock() { return textBlock; }
	
	public SendSystemMessagePacket(TextBlock tb) {
		super(DataPacketTypes.SEND_SYSTEM_MESSAGE_PACKET);
		textBlock = tb;
	}
	
}
