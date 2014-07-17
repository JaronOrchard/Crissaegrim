package dialogboxes;

import geometry.Coordinate;
import geometry.Rect;
import gldrawer.GLDrawer;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Mouse;

import textblock.TextTexture;
import crissaegrim.Crissaegrim;
import crissaegrim.GameInitializer;

public class DialogBox {
	
	private transient int dialogWidth;
	private transient int dialogHeight;
	
	public enum Result { BUTTON_1, BUTTON_2, BUTTON_3, BUTTON_4, BUTTON_5, BUTTON_6, BUTTON_7, BUTTON_8, BUTTON_9, BUTTON_10 }
	
	private List<String> messageLines;
	private List<String> buttonLabels;
	private Rect dialogRect;
	
	public DialogBox(String messageLine, String buttonLabel) { this(Arrays.asList(messageLine), Arrays.asList(buttonLabel)); }
	public DialogBox(List<String> messageLines, String buttonLabel) { this(messageLines, Arrays.asList(buttonLabel)); }
	public DialogBox(String messageLine, List<String> buttonLabels) { this(Arrays.asList(messageLine), buttonLabels); }
	public DialogBox(List<String> messageLines, List<String> buttonLabels) {
		this.messageLines = messageLines;
		this.buttonLabels = buttonLabels;
		recalculateDialogDimensions();
	}
	
	public void recalculateDialogDimensions() {
		int maxWidth = 0;
		for (int i = 0; i < messageLines.size(); i++) {
			maxWidth = Math.max(maxWidth, Crissaegrim.getCommonTextures().getTextTexture(messageLines.get(i)).getWidth());
		}
		for (int i = 0; i < buttonLabels.size(); i++) {
			maxWidth = Math.max(maxWidth, Crissaegrim.getCommonTextures().getTextTexture(buttonLabels.get(i)).getWidth() + 20);
		}
		dialogWidth = maxWidth + 40;
		dialogHeight = 31 + (22 * messageLines.size()) + (46 * buttonLabels.size());
		
		int left = (Crissaegrim.getWindowWidth() - dialogWidth) / 2;
		int right = left + dialogWidth;
		int top = Crissaegrim.getWindowHeight() - 30;
		int bottom = top - dialogHeight;
		dialogRect = new Rect(new Coordinate(left, bottom), new Coordinate(right, top));
	}
	
	/**
	 * Draws the dialog box.  The layout is set up as follows:
	 * 
	 * 15 pixels padding between the top of the dialog and the top of first message line
	 * 22 pixels per message line
	 * 15 pixels padding between the bottom of last message line and the top of first button
	 * 46 pixels per button
	 * 1 pixel padding between the bottom of last button and the bottom of the dialog
	 */
	public void draw() {
		int hoveredButton = getHoveredButtonIndex();
		
		GameInitializer.initializeNewFrameForWindow();
		GLDrawer.disableTextures();
		GLDrawer.setColor(0.313, 0.313, 0.313);
		GLDrawer.drawQuad(dialogRect); // Draw dialog background
		GLDrawer.setColor(0.75, 0.75, 0.75);
		GLDrawer.setLineWidth(3);
		GLDrawer.drawOutline(dialogRect); // Draw dialog outline
		GLDrawer.setLineWidth(1);
		
		int dialogMiddleX = (int)(dialogRect.getLeft() + dialogRect.getRight()) / 2;
		int messageLineY = (int)(dialogRect.getTop()) - 15;
		
		for (int i = 0; i < messageLines.size(); i++) {
			TextTexture textTexture = Crissaegrim.getCommonTextures().getTextTexture(messageLines.get(i));
			GLDrawer.useTexture(textTexture.getTextureId());
			GLDrawer.drawQuad(dialogMiddleX - (textTexture.getWidth() / 2),
					dialogMiddleX - (textTexture.getWidth() / 2) + textTexture.getWidth(), messageLineY - 20, messageLineY);
			messageLineY -= 22;
		}
		
		int buttonY = messageLineY - 15;
		for (int i = 0; i < buttonLabels.size(); i++) {
			GLDrawer.disableTextures();
			if (i == hoveredButton) { GLDrawer.setColor(0.224, 0.314, 0.220); } else { GLDrawer.setColor(0.208, 0.255, 0.325); }
			GLDrawer.drawQuad(dialogRect.getLeft() + 10, dialogRect.getRight() - 10, buttonY - 36, buttonY); // Draw button background
			if (i == hoveredButton) { GLDrawer.setColor(0.675, 0.749, 0.667); } else { GLDrawer.setColor(0.667, 0.702, 0.749); }
			GLDrawer.setLineWidth(2);
			GLDrawer.drawOutline(dialogRect.getLeft() + 10, dialogRect.getRight() - 10, buttonY - 36, buttonY); // Draw button outline
			GLDrawer.setLineWidth(1);
			
			TextTexture textTexture = Crissaegrim.getCommonTextures().getTextTexture(buttonLabels.get(i));
			GLDrawer.useTexture(textTexture.getTextureId());
			GLDrawer.drawQuad(dialogMiddleX - (textTexture.getWidth() / 2),
					dialogMiddleX - (textTexture.getWidth() / 2) + textTexture.getWidth(), buttonY - 28, buttonY - 8);
			buttonY -= 46;
		}
		
	}
	
	public int getHoveredButtonIndex() {
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		int buttonY = Crissaegrim.getWindowHeight() - 60 - (22 * messageLines.size());
		if (mouseX >= dialogRect.getLeft() + 10 && mouseX <= dialogRect.getRight() - 10 &&
				mouseY <= buttonY && mouseY >= dialogRect.getBottom()) {
			for (int i = 0; i < buttonLabels.size(); i++) {
				if (mouseY <= buttonY && mouseY >= buttonY - 36) { return i; }
				buttonY -= 46;
			}
		}
		return -1;
	}
	
}
