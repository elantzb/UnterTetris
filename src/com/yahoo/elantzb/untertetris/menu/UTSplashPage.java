package com.yahoo.elantzb.untertetris.menu;

import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.drawable.TzDrawable;
import com.yahoo.elantzb.tz.texture.TzTextureCollection;
import com.yahoo.elantzb.untertetris.input.IUTInputListener;
import com.yahoo.elantzb.untertetris.input.UTInputEvent;
import com.yahoo.elantzb.untertetris.main.UnterTetris;
import com.yahoo.elantzb.untertetris.main.UnterTetris.UTState;
import com.yahoo.elantzb.untertetris.window.UTWindow;

public class UTSplashPage implements IUTInputListener
{
	public TzDrawable title, flashyText;
	public int flashyTextFrame;
	public long millisLastFlashyTextFrame;
	
	public UTSplashPage()
	{
		title = new TzDrawable("res/texture/title.png", 512, 128, 1, 1);
		flashyText = new TzDrawable("res/texture/flashyText.png", 100, 50, 1, 4);
		flashyTextFrame = 0;
		millisLastFlashyTextFrame = System.currentTimeMillis();
	}
	public void drawSplashPage()
	{
		title.draw(-64, UTWindow.WINDOW_HEIGHT/3);
		
		if(System.currentTimeMillis() - millisLastFlashyTextFrame > 100)
		{
			millisLastFlashyTextFrame = System.currentTimeMillis();
			flashyTextFrame++;
			if(flashyTextFrame > 4)
				flashyTextFrame = 1;
			
			flashyText.setFrame(flashyTextFrame);
		}
		
		flashyText.draw(UTWindow.WINDOW_WIDTH/2 - 50, UTWindow.WINDOW_HEIGHT - 50);
	}

	@Override
	public void onKeyEvent(UTInputEvent event) 
	{
		if(event.type.equals("KEY_PRESSED"))
			UnterTetris.requestStateChange(UTState.SPLASH_TO_MENU);
	}
}
