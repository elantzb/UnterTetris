package com.yahoo.elantzb.untertetris.shape;

import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.drawable.TzDrawable;
import com.yahoo.elantzb.tz.texture.TzTextureCollection;

public class UTShapeUnit
{
	public static final int UNIT_LENGTH = 20;
	
	private int relGridX, relGridY;
	public UTShape parentShape;
	private TzDrawable unitDrawable;
	public int alphaValue;
	
	public UTShapeUnit(int rel_grid_x, int rel_grid_y, UTShape parent_shape, String texture_ref)
	{
		relGridX = rel_grid_x;
		relGridY = rel_grid_y;
		parentShape = parent_shape;
		unitDrawable = new TzDrawable(texture_ref, UNIT_LENGTH, UNIT_LENGTH, 1, 1);
		alphaValue = 255;
	}
	
	public UTShapeUnit cloneOrphan(UTShape parent_shape)
	{
		return new UTShapeUnit(relGridX, relGridY, parent_shape, TzTextureCollection.getTextureName(unitDrawable.getTextureID()));
	}
	
	public int getGridX(int parent_grid_x)
	{
		return parent_grid_x + relGridX;
	}
	
	public int getGridY(int parent_grid_y)
	{
		return parent_grid_y + relGridY;
	}
	
	public int getScreenX(int parent_screen_x)
	{
		return parent_screen_x + (relGridX * UNIT_LENGTH);
	}
	
	public int getScreenY(int parent_screen_y)
	{
		return parent_screen_y + (relGridY * UNIT_LENGTH);
	}
	
	public void drawAtScreenCoordRelative(int parent_screen_x, int parent_screen_y)
	{
		unitDrawable.draw(parent_screen_x + ( relGridX * UNIT_LENGTH), parent_screen_y + (relGridY * UNIT_LENGTH), alphaValue);
	}
	
	public void drawAtGridCoord(int gridX, int gridY)
	{
		unitDrawable.draw(gridX * UNIT_LENGTH, gridY * UNIT_LENGTH, alphaValue);
	}
}