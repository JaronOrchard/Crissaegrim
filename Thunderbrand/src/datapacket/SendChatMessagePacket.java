package datapacket;

import thunderbrand.TextBlock;

public class SendChatMessagePacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final TextBlock textBlock;
	public TextBlock getTextBlock() { return textBlock; }
	
	public SendChatMessagePacket(TextBlock tb) {
		super(DataPacketTypes.SEND_CHAT_MESSAGE_PACKET);
		textBlock = tb;
	}
	
}
