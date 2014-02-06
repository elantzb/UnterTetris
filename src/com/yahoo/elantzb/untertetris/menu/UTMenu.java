package com.yahoo.elantzb.untertetris.menu;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.yahoo.elantzb.tz.drawable.TzDrawable;
import com.yahoo.elantzb.untertetris.file.UTScore;
import com.yahoo.elantzb.untertetris.file.UTScoreFileHandler;
import com.yahoo.elantzb.untertetris.input.IUTInputListener;
import com.yahoo.elantzb.untertetris.input.UTInputEvent;
import com.yahoo.elantzb.untertetris.main.UnterTetris;
import com.yahoo.elantzb.untertetris.main.UnterTetris.UTState;
import com.yahoo.elantzb.untertetris.window.UTWindow;

public class UTMenu implements IUTInputListener
{
	private TzDrawable levelSelectTitle, leftLevelSelectArrow, rightLevelSelectArrow, levelIndicator;
	private int levelSelectArrowFrame;
	private long millisLevelSelectArrowLastFrame;
	
	public int levelSelection;
	private ArrayList<ArrayList<UTScore>> loadedScores;
	
	public UTMenu()
	{
		levelSelectTitle = new TzDrawable("res/texture/levelSelectTitle.png", 256, 64, 1, 1);
		leftLevelSelectArrow = new TzDrawable("res/texture/leftLevelSelectArrow.png", 32, 32, 2, 2);
		rightLevelSelectArrow = new TzDrawable("res/texture/rightLevelSelectArrow.png", 32, 32, 2, 2);
		levelIndicator = new TzDrawable("res/texture/text.png", 24, 32, 16, 16);
		levelSelectArrowFrame = 0;
		millisLevelSelectArrowLastFrame = System.currentTimeMillis();
		
		levelSelection = 1;
		levelIndicator.setFrame(levelSelection + 17);
		loadedScores = UnterTetris.scoreFileHandler.getScores();
	}
	
	public void drawMenu()
	{
		levelSelectTitle.draw(UTWindow.WINDOW_WIDTH/2 - levelSelectTitle.getSize().width/2, 0);
		
		if(System.currentTimeMillis() - millisLevelSelectArrowLastFrame > 100)
		{
			millisLevelSelectArrowLastFrame = System.currentTimeMillis();
			levelSelectArrowFrame++;
			if(levelSelectArrowFrame > 4)
				levelSelectArrowFrame = 1;
			
			leftLevelSelectArrow.setFrame(levelSelectArrowFrame);
			rightLevelSelectArrow.setFrame(levelSelectArrowFrame);
		}
		
		leftLevelSelectArrow.draw(UTWindow.WINDOW_WIDTH/2 - 64, levelSelectTitle.getSize().height);
		levelIndicator.draw(UTWindow.WINDOW_WIDTH/2 - 12, levelSelectTitle.getSize().height);
		rightLevelSelectArrow.draw(UTWindow.WINDOW_WIDTH/2 + 32, levelSelectTitle.getSize().height);
		
		//draw scores here
	}
	
	public void offsetLevelSelection(int level_offset)
	{
		levelSelection += level_offset;
		if(levelSelection < 17)
			levelSelection = 17;
		else if(levelSelection > 25)
			levelSelection = 25;
		
		levelIndicator.setFrame(levelSelection + 1);
	}

	@Override
	public void onKeyEvent(UTInputEvent event) 
	{
		if(event.type.equals("KEY_PRESSED"))
		{
			switch(event.data)
			{
			case Keyboard.KEY_RETURN:
				UnterTetris.requestStateChange(UTState.MENU_TO_GAME);
				break;
			
			case Keyboard.KEY_LEFT:
				offsetLevelSelection(-1);
				break;
				
			case Keyboard.KEY_RIGHT:
				offsetLevelSelection(1);
				break;
			}
		}
	}
}
