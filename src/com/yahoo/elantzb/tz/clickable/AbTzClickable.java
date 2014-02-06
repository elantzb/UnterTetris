package com.yahoo.elantzb.tz.clickable;

import java.util.ArrayList;

/**
 * Extend this class to receive click notifications from TzClickableCollection.
 * Must be set as "clickable" with setIsClickable() before use.
 *
 */
public abstract class AbTzClickable 
{
	protected int screenX, screenY, width, height;
	private boolean isClickable;
	private ArrayList<String> tags;
	//private boolean isDead = false;
	
	public AbTzClickable(int screen_x, int screen_y, int w, int h)
	{
		screenX = screen_x;
		screenY = screen_y;
		width = w;
		height = h;
		
		setIsClickable(false);
		
		tags = new ArrayList<String>();
	}
	
	public AbTzClickable() {
		// TODO Auto-generated constructor stub
	}

	public int getX()
	{
		return screenX;
	}
	
	public int getY()
	{
		return screenY;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public boolean getIsClickable()
	{
		return isClickable;
	}
	
	public void setIsClickable(boolean is_clickable)
	{
		isClickable = is_clickable;
	}
	
	public ArrayList<String> getTags()
	{
		return tags;
	}
	
	public void setTags(String...new_tags)
	{
		tags.clear();
		
		for(String tag : new_tags)
			tags.add(tag);
	}
	
	/*public void kill()
	{
		isDead = true;
	}
	
	public boolean getIsDead()
	{
		return isDead;
	}*/
	
	public abstract void onLeftMousePress(boolean is_global);
	public abstract void onLeftMouseRelease(boolean is_global);
	public abstract void onRightMousePress(boolean is_global);
	public abstract void onRightMouseRelease(boolean is_global);
	public abstract void onMouseOver();
	public abstract void onMouseMove(int mouse_x, int mouse_y);
}
