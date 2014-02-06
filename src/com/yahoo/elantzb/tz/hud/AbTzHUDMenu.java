package com.yahoo.elantzb.tz.hud;

import java.util.ArrayList;

import com.yahoo.elantzb.tz.drawable.TzDrawable;

public abstract class AbTzHUDMenu implements TzHUDElement
{
	public int screenX, screenY;
	public int width, height;
	
	TzDrawable menuBackdropTopLeft, menuBackdropTop, menuBackdropTopRight, 
	menuBackdropLeft, menuBackdropCenter, menuBackdropRight, 
	menuBackdropBottomLeft, menuBackdropBottom, menuBackdropBottomRight;
	
	public ArrayList<TzHUDElement> hudElements;
	
	public AbTzHUDMenu(int screen_x, int screen_y, int w, int h, String backdrop_path)
	{
		screenX = screen_x;
		screenY = screen_y;
		width = w >= 20 ? w : 20;
		height = h >= 20 ? h : 20;
		
		menuBackdropTopLeft = new TzDrawable(backdrop_path, 10, 10, 3, 3);
		menuBackdropTopLeft.setFrame(1);
		menuBackdropTop = new TzDrawable(backdrop_path, width - 20, 10, 3, 3);
		menuBackdropTop.setFrame(2);
		menuBackdropTopRight = new TzDrawable(backdrop_path, 10, 10, 3, 3);
		menuBackdropTopRight.setFrame(3);
		menuBackdropLeft = new TzDrawable(backdrop_path, 10, height - 20, 3, 3);
		menuBackdropLeft.setFrame(4);
		menuBackdropCenter = new TzDrawable(backdrop_path, width - 20, height - 20, 3, 3);
		menuBackdropCenter.setFrame(5);
		menuBackdropRight = new TzDrawable(backdrop_path, 10, height - 20, 3, 3);
		menuBackdropRight.setFrame(6);
		menuBackdropBottomLeft = new TzDrawable(backdrop_path, 10, 10, 3, 3);
		menuBackdropBottomLeft.setFrame(7);
		menuBackdropBottom = new TzDrawable(backdrop_path, width - 20, 10, 3, 3);
		menuBackdropBottom.setFrame(8);
		menuBackdropBottomRight = new TzDrawable(backdrop_path, 10, 10, 3, 3);
		menuBackdropBottomRight.setFrame(9);
		
		hudElements = new ArrayList<TzHUDElement>();
	}
	
	public void addHUDElement(TzHUDElement element)
	{
		hudElements.add(element);
	}
	
	public void drawThisElement()
	{
		// empty. Override drawMenu()
	}
	
	public void drawMenu()
	{
		menuBackdropTopLeft.draw(screenX,  screenY);
		menuBackdropTop.draw(screenX + 10,  screenY);
		menuBackdropTopRight.draw(screenX + (width - 10),  screenY);
		menuBackdropLeft.draw(screenX,  screenY + 10);
		menuBackdropCenter.draw(screenX + 10,  screenY + 10);
		menuBackdropRight.draw(screenX + (width - 10),  screenY + 10);
		menuBackdropBottomLeft.draw(screenX,  screenY + (height - 10));
		menuBackdropBottom.draw(screenX + 10,  screenY + (height - 10));
		menuBackdropBottomRight.draw(screenX + (width - 10),  screenY + (height - 10));
		
		for(TzHUDElement element : hudElements)
			element.drawThisElement();
	}
}
