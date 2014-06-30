package dialogboxes;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_MODULATE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glVertex2d;
import geometry.Coordinate;
import geometry.Rect;

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
		glDisable(GL_TEXTURE_2D);
		glColor3d(0.313, 0.313, 0.313);
		glBegin(GL_QUADS); // Draw dialog background
			glVertex2d(dialogRect.getLeft(), dialogRect.getTop());
			glVertex2d(dialogRect.getRight(), dialogRect.getTop());
			glVertex2d(dialogRect.getRight(), dialogRect.getBottom());
			glVertex2d(dialogRect.getLeft(), dialogRect.getBottom());
		glEnd();
		glColor3d(0.75, 0.75, 0.75);
		glLineWidth(3);
		glBegin(GL_LINE_LOOP); // Draw dialog outline
			glVertex2d(dialogRect.getLeft(), dialogRect.getTop());
			glVertex2d(dialogRect.getRight(), dialogRect.getTop());
			glVertex2d(dialogRect.getRight(), dialogRect.getBottom());
			glVertex2d(dialogRect.getLeft(), dialogRect.getBottom());
		glEnd();
		glLineWidth(1);
		
		glEnable(GL_TEXTURE_2D);
		glColor3d(1.0, 1.0, 1.0);
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
		
		int dialogMiddleX = (int)(dialogRect.getLeft() + dialogRect.getRight()) / 2;
		int messageLineY = (int)(dialogRect.getTop()) - 15;
		
		for (int i = 0; i < messageLines.size(); i++) {
			TextTexture textTexture = Crissaegrim.getCommonTextures().getTextTexture(messageLines.get(i));
			glPushMatrix();
				glBindTexture(GL_TEXTURE_2D, textTexture.getTextureId());
				glBegin(GL_QUADS);
					glTexCoord2d(0, 1);
					glVertex2d(dialogMiddleX - (textTexture.getWidth() / 2), messageLineY - 20);
					glTexCoord2d(1, 1);
					glVertex2d(dialogMiddleX - (textTexture.getWidth() / 2) + textTexture.getWidth(), messageLineY - 20);
					glTexCoord2d(1, 0);
					glVertex2d(dialogMiddleX - (textTexture.getWidth() / 2) + textTexture.getWidth(), messageLineY);
					glTexCoord2d(0, 0);
					glVertex2d(dialogMiddleX - (textTexture.getWidth() / 2), messageLineY);
				glEnd();
			glPopMatrix();
			messageLineY -= 22;
		}
		
		int buttonY = messageLineY - 15;
		for (int i = 0; i < buttonLabels.size(); i++) {
			glDisable(GL_TEXTURE_2D);
			if (i == hoveredButton) { glColor3d(0.224, 0.314, 0.220); } else {glColor3d(0.208, 0.255, 0.325); }
			glBegin(GL_QUADS); // Draw button background
				glVertex2d(dialogRect.getLeft() + 10, buttonY - 36);
				glVertex2d(dialogRect.getRight() - 10, buttonY - 36);
				glVertex2d(dialogRect.getRight() - 10, buttonY);
				glVertex2d(dialogRect.getLeft() + 10, buttonY);
			glEnd();
			if (i == hoveredButton) { glColor3d(0.675, 0.749, 0.667); } else { glColor3d(0.667, 0.702, 0.749); }
			glLineWidth(2);
			glBegin(GL_LINE_LOOP); // Draw button outline
				glVertex2d(dialogRect.getLeft() + 10, buttonY - 36);
				glVertex2d(dialogRect.getRight() - 10, buttonY - 36);
				glVertex2d(dialogRect.getRight() - 10, buttonY);
				glVertex2d(dialogRect.getLeft() + 10, buttonY);
			glEnd();
			glLineWidth(1);
			glColor3d(1.0, 1.0, 1.0);
			
			glEnable(GL_TEXTURE_2D);
			TextTexture textTexture = Crissaegrim.getCommonTextures().getTextTexture(buttonLabels.get(i));
			glPushMatrix();
				glBindTexture(GL_TEXTURE_2D, textTexture.getTextureId());
				glBegin(GL_QUADS);
					glTexCoord2d(0, 1);
					glVertex2d(dialogMiddleX - (textTexture.getWidth() / 2), buttonY - 28);
					glTexCoord2d(1, 1);
					glVertex2d(dialogMiddleX - (textTexture.getWidth() / 2) + textTexture.getWidth(), buttonY - 28);
					glTexCoord2d(1, 0);
					glVertex2d(dialogMiddleX - (textTexture.getWidth() / 2) + textTexture.getWidth(), buttonY - 8);
					glTexCoord2d(0, 0);
					glVertex2d(dialogMiddleX - (textTexture.getWidth() / 2), buttonY - 8);
				glEnd();
			glPopMatrix();
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
