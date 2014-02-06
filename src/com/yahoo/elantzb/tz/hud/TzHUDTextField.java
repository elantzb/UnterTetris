package com.yahoo.elantzb.tz.hud;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.yahoo.elantzb.tz.clickable.AbTzClickable;
import com.yahoo.elantzb.tz.clickable.TzClickableCollection;
import com.yahoo.elantzb.tz.drawable.TzDrawable;
import com.yahoo.elantzb.tz.typeable.ITzTypeable;
import com.yahoo.elantzb.tz.typeable.TzTypeableCollection;

public class TzHUDTextField extends AbTzClickable implements TzHUDElement, ITzTypeable
{
	int charWidth, charHeight;
	public int length;
	private TzDrawable leftCap, midSection, rightCap, caret;
	public String text;
	public TzText textObject;
	public boolean hasFocus;
	public int caretPos;
	
	public TzHUDTextField(String initial_text, String font_img_path, String text_field_img_path, int screen_x, int screen_y, int char_width, int char_height, int len)
	{
		super(screen_x, screen_y, (len + 2) * char_width, char_height * 2);
		
		charWidth = char_width;
		charHeight = char_height;
		length = len;
		
		leftCap = new TzDrawable(text_field_img_path, charWidth, charHeight * 2, 4, 1);
		leftCap.setFrame(1);
		midSection = new TzDrawable(text_field_img_path, length * charWidth, charHeight * 2, 4, 1);
		midSection.setFrame(2);
		rightCap = new TzDrawable(text_field_img_path, charWidth, charHeight * 2, 4, 1);
		rightCap.setFrame(3);
		caret = new TzDrawable(text_field_img_path, 8, charHeight, 4, 1);
		caret.setFrame(4);
		
		text = initial_text;
		textObject = new TzText(text, font_img_path, 8, 16);
		hasFocus = false;
		setIsClickable(false);
		
		caretPos = initial_text.length();
		
		TzClickableCollection.addClickable(0, this);
		TzTypeableCollection.addTypeable(this);
	}
	
	public void drawThisElement()
	{
		leftCap.draw(screenX, screenY);
		midSection.draw(screenX + charWidth, screenY);
		rightCap.draw(screenX + charWidth + length * charWidth, screenY);
		
		textObject.setText(text);
		textObject.draw(screenX + charWidth, screenY + charHeight/2);
		
		if(hasFocus)
		{
			caret.draw(screenX + charWidth + charWidth * caretPos, screenY + charHeight/2);
		}
	}
	
	public void clearText()
	{
		text = ""; 
		caretPos = 0;
	}

	@Override
	public void onLeftMousePress(boolean is_global) 
	{
		if(is_global)
			hasFocus = false;
		else
		{
			hasFocus = true;
			int x = Mouse.getX();
			
			x -= screenX + charWidth;
			caretPos = (x / charWidth <= text.length()) ? x / charWidth : text.length();
		}
	}

	@Override
	public void onLeftMouseRelease(boolean is_global) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightMousePress(boolean is_global) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightMouseRelease(boolean is_global) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyPress(char c, int key_code) 
	{
		c = Character.toUpperCase(c);
		
		if(hasFocus)
		{
			if(text.length() < length && (int)c >= 32 && (int)c <= 90)
			{
				text = text.substring(0, caretPos) + c + text.substring(caretPos, text.length());
				caretPos++;
			}
			else if(key_code == Keyboard.KEY_BACK && text.length() > 0 && caretPos > 0)
			{
				text = text.substring(0, caretPos - 1) + text.substring(caretPos, text.length());
				caretPos--;
			}
			else if(key_code == Keyboard.KEY_LEFT && caretPos > 0)
			{
				caretPos--;
			}
			else if(key_code == Keyboard.KEY_RIGHT && caretPos < text.length())
			{
				caretPos++;
			}
		}
	}

	@Override
	public void onKeyRelease(char c, int key_code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseMove(int mouse_x, int mouse_y) {
		// TODO Auto-generated method stub
		
	}
}
