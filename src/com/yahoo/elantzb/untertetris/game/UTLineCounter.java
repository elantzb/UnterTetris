package com.yahoo.elantzb.untertetris.game;

import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.drawable.TzDrawable;
import com.yahoo.elantzb.tz.texture.TzTextureCollection;
import com.yahoo.elantzb.untertetris.main.UnterTetris;

public class UTLineCounter 
{
	public static final int DIGIT_WIDTH = 24;
	public static final int DIGIT_HEIGHT = 32;
	
	private TzDrawable[] lineCounterDrawables;
	private int counterValue;
	
	public UTLineCounter()
	{
		lineCounterDrawables = new TzDrawable[3];
		lineCounterDrawables[0] = new TzDrawable("res/texture/text.png", DIGIT_WIDTH, DIGIT_HEIGHT, 16, 16);
		lineCounterDrawables[1] = new TzDrawable("res/texture/text.png", DIGIT_WIDTH, DIGIT_HEIGHT, 16, 16);
		lineCounterDrawables[2] = new TzDrawable("res/texture/text.png", DIGIT_WIDTH, DIGIT_HEIGHT, 16, 16);
		setValue(0);
	}
	
	public int getValue()
	{
		return counterValue;
	}
	
	public void setValue(int counter_value)
	{
		if(counter_value > 999)
			counterValue = 999;
		else if(counter_value < 0)
			counterValue = 0;
		else
			counterValue = counter_value;
		
		int digits[] = new int[3];
		
		if(counterValue > 99)
		{
			digits[0] = counterValue / 100;
			digits[1] = (counterValue - digits[0] * 100) % 9;
			digits[2] = (counterValue - (digits[0] * 100 + digits[1] * 10));
		}
		else if(counterValue > 9)
		{
			digits[0] = 0;
			digits[1] = counterValue / 10;
			digits[2] = (counterValue - digits[1] * 10);
		}
		else
		{
			digits[0] = 0;
			digits[1] = 0;
			digits[2] = counterValue;
		}
		
		lineCounterDrawables[0].setFrame(digits[0] + 17);
		lineCounterDrawables[1].setFrame(digits[1] + 17);
		lineCounterDrawables[2].setFrame(digits[2] + 17);
		
		//System.out.println(digits[0] + "" + digits[1] + "" + digits[2]);
	}
	
	public void decrementValue()
	{
		setValue(counterValue - 1);
	}
	
	public void draw(int screen_x, int screen_y)
	{
		lineCounterDrawables[0].draw(screen_x, screen_y);
		lineCounterDrawables[1].draw(screen_x + DIGIT_WIDTH, screen_y);
		lineCounterDrawables[2].draw(screen_x + DIGIT_WIDTH * 2, screen_y);
	}
}
