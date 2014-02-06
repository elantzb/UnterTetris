package com.yahoo.elantzb.untertetris.window;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.sound.TzSoundPlayer;
import com.yahoo.elantzb.untertetris.main.UnterTetris;
import com.yahoo.elantzb.untertetris.shape.UTShapeUnit;

public class UTWindow 
{
	public static final int GRID_WIDTH = 10;
	public static final int GRID_HEIGHT = 25;
	public static final int SIDE_BAR_WIDTH = 10;
	public static final int WINDOW_WIDTH = UTShapeUnit.UNIT_LENGTH * (SIDE_BAR_WIDTH + GRID_WIDTH); // 400 px
	public static final int WINDOW_HEIGHT = UTShapeUnit.UNIT_LENGTH * GRID_HEIGHT; // 500 px
	public static final String WINDOW_TITLE = "UnterTetris";
	
	private boolean timeToQuit;
	
	public UTWindow()
	{
		timeToQuit = false;
		
		initOpenGL();
	}
	
	private void initOpenGL()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT));
			Display.create();
			Display.setTitle(WINDOW_TITLE);	
			
			GL11.glClearColor(0.0f,0.0f,0.0f,1f);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void renderLoop()
	{	
		while(!Display.isCloseRequested() && !timeToQuit)
		{	
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			// do logic and draw elements //	
			UnterTetris.logicCallback();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			
			Display.sync(100);
			Display.update();
		}
		
		// cleanup //
		TzSoundPlayer.closeDevices();
		Display.destroy();
	}
}
