package items;

public abstract class StackableItem extends Item {
	private static final long serialVersionUID = 1L;
	
	private int stack;
	public int getNumberInStack() { return stack; }
	
	public void addOneToStack() { stack++; }
	public void addToStack(int x) { stack += x; }
	/**
	 * Removes one from the stack, returning {@code true} if the stack has reached zero.
	 * @return {@code true} if the stackable item is empty, {@code false} otherwise (if there are stacks left)
	 */
	public boolean removeOneFromStack() {
		stack--;
		return (stack <= 0);
	}
	
	@Override
	public String getDisplayName() { return name + " (" + stack + ")"; }
	
	public StackableItem(String itemName, int itemTexture, int numberInStack) {
		super(itemName, itemTexture);
		stack = numberInStack;
	}
	
}
