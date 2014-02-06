package com.yahoo.elantzb.tz.hud;

import com.yahoo.elantzb.tz.clickable.AbTzClickable;
import com.yahoo.elantzb.tz.drawable.TzDrawable;

public abstract class AbTzHUDButton extends AbTzClickable implements TzHUDElement
{
	protected TzDrawable buttonDrawable;
	
	public AbTzHUDButton(int screen_x, int screen_y, int w, int h, String img_path, int img_cols, int img_rows)
	{
		super(screen_x, screen_y, w, h);
		
		buttonDrawable = new TzDrawable(img_path, w, h, img_cols, img_rows);
	}
	
	public void drawThisElement()
	{
		buttonDrawable.draw(screenX, screenY);
	}
	
	public void setTextureFrame(int frame)
	{
		buttonDrawable.setFrame(frame);
	}
}
