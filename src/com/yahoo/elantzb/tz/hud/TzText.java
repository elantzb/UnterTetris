package com.yahoo.elantzb.tz.hud;

import java.util.ArrayList;

import com.yahoo.elantzb.tz.drawable.TzDrawable;

public class TzText 
{
	private String textString;
	private String fontImage;
	ArrayList<TzDrawable> fontDrawables;
	int charWidth, charHeight;
	
	public TzText(String text_string, String font_img_path, int char_width, int char_height)
	{
		fontImage = font_img_path;
		charWidth = char_width;
		charHeight = char_height;
		
		setText(text_string);
	}
	
	public void draw(int screen_x, int screen_y)
	{
		for(int i = 0; i < fontDrawables.size(); i++)
		{
			fontDrawables.get(i).draw((screen_x + charWidth * i), screen_y);
		}
	}
	
	public int getWidth()
	{
		return charWidth * textString.length();
	}
	
	public int getHeight()
	{
		return charHeight;
	}
	
	public void setText(String text)
	{
		textString = text.toUpperCase();
		fontDrawables = new ArrayList<TzDrawable>();
		
		char[] textArray = textString.toCharArray();
		
		for(char c : textArray)
		{
			int frameID = 1;
			
			if((int)c >= 32 && (int)c <= 90)
			{
				frameID = (int)c - 31;
			}
			
			TzDrawable charDrawable = new TzDrawable(fontImage, charWidth, charHeight, 8, 8);
			charDrawable.setFrame(frameID);
			fontDrawables.add(charDrawable);
		}
	}
	
	/*public static void writeText(String text, int screen_x, int screen_y, int char_width, int char_height)
	{
		TzText textObject = new TzText(text, screen_x, screen_y, char_width, char_height);
		textObject.draw();
	}*/
}
