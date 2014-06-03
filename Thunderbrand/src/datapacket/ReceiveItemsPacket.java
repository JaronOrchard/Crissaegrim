package datapacket;

import items.Item;

import java.util.List;

public class ReceiveItemsPacket extends DataPacket {
	private static final long serialVersionUID = 1L;
	
	private final List<Item> items;
	public List<Item> getItems() { return items; }
	
	public ReceiveItemsPacket(List<Item> items) {
		super(DataPacketTypes.RECEIVE_ITEMS_PACKET);
		this.items = items;
	}
	
}
