package com.yahoo.elantzb.untertetris.text;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.drawable.TzDrawable;
import com.yahoo.elantzb.tz.texture.TzTextureCollection;

public class UTText 
{
	int textTextureID;
	String textString;
	int charWidth, charHeight, padding;
	ArrayList<TzDrawable> textDrawables;
	
	public UTText(String text_string, String text_file_path, int char_width, int char_height)
	{
		textTextureID = TzTextureCollection.loadTexture(text_file_path, GL11.GL_NEAREST);
		charWidth = char_width;
		charHeight = char_height;
		padding = 0;
		textDrawables = new ArrayList<TzDrawable>();
		setText(text_string);
	}
	
	public void setText(String text_string)
	{
		char[] charArray = text_string.toCharArray();
		
		for(char c : charArray)
		{
			TzDrawable charDrawable = new TzDrawable(TzTextureCollection.getTextureName(textTextureID), charWidth, charHeight, 16, 16);
			if((int)c >= 32 && (int)c <= 122)
				charDrawable.setFrame((int)c - 31);
			else
				charDrawable.setFrame(32);
			textDrawables.add(charDrawable);
		}
	}
	
	public void setPadding(int pad_ding)
	{
		padding = pad_ding;
	}
	
	public void drawText(int screen_x, int screen_y)
	{
		for(int x = 0; x < textDrawables.size(); x++)
			textDrawables.get(x).draw(screen_x + x * (charWidth + padding), screen_y);
	}
}
