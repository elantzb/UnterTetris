package com.yahoo.elantzb.untertetris.game;

import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.texture.TzTextureCollection;
import com.yahoo.elantzb.untertetris.main.UnterTetris;

public class UTSideBar 
{
	private int textureID;
	
	public UTSideBar()
	{
		textureID = TzTextureCollection.loadTexture("res/texture/sideBar.png", GL11.GL_NEAREST);
	}
	
	public void draw(int screen_x, int screen_y, int side_bar_width, int side_bar_height)
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2d(1, 0);
			GL11.glVertex2i(screen_x + side_bar_width, screen_y);
			GL11.glTexCoord2d(0, 0);
			GL11.glVertex2i(screen_x, screen_y);
			GL11.glTexCoord2d(0, 1);
			GL11.glVertex2i(screen_x, screen_y + side_bar_height);
			GL11.glTexCoord2d(1, 1);
			GL11.glVertex2i(screen_x + side_bar_width, screen_y + side_bar_height);
		GL11.glEnd();
	}
}
