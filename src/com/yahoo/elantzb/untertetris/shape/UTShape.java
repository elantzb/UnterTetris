package com.yahoo.elantzb.untertetris.shape;

import java.util.ArrayList;

public class UTShape 
{
	private String textureRef;
	protected int centerGridX, centerGridY;
	
	private int currentOrientation = 0;
	private ArrayList<ArrayList<UTShapeUnit>> orientations = new ArrayList<ArrayList<UTShapeUnit>>();
	
	//private boolean isFalling = true;
	
	public UTShape(String texture_ref)
	{
		textureRef = texture_ref;
	}
	
	public int getCenterGridX()
	{
		return centerGridX;
	}
	
	public int getCenterGridY()
	{
		return centerGridY;
	}
	
	public void setCenterGrid(int center_grid_x, int center_grid_y)
	{
		centerGridX = center_grid_x;
		centerGridY = center_grid_y;
	}
	
	protected void addUnit(int orientation, UTShapeUnit unit)
	{
		while(orientations.size() < orientation + 1)
			orientations.add(new ArrayList<UTShapeUnit>());
			
		orientations.get(orientation).add(unit);
	}
	
	protected void addUnit(int orientation, int rel_x, int rel_y)
	{
		while(orientations.size() < orientation + 1)
			orientations.add(new ArrayList<UTShapeUnit>());
			
		orientations.get(orientation).add(new UTShapeUnit(rel_x, rel_y, this, textureRef));
	}
	
	public ArrayList<UTShapeUnit> getUnits()
	{
		return orientations.get(currentOrientation);
	}
	
	/*public ArrayList<UTDebrisUnit> getDebris()
	{
		ArrayList<UTDebrisUnit> debris = new ArrayList<UTDebrisUnit>();
		
		for(UTShapeUnit unit : orientations.get(currentOrientation))
		{
			debris.add(new UTDebrisUnit(unit, unit.getGridX(centerGridX), unit.getGridY(centerGridY)));
		}
		
		return debris;
	}*/
	
	/*public void draw(int center_grid_x, int center_grid_y)
	{
		for(UTShapeUnit unit : orientations.get(currentOrientation))
			unit.drawRelative(center_grid_x, center_grid_y);
	}*/
	
	public UTShape rotate()
	{
		currentOrientation++;
		
		if(currentOrientation >= orientations.size())
			currentOrientation = 0;	
		
		return this;
	}
	
	public UTShape getShapeCopy(int center_grid_x, int center_grid_y, boolean rotated)
	{
		ArrayList<UTShapeUnit> orientation = null;
		
		if(rotated)
		{
			if(currentOrientation + 1 < orientations.size())
				orientation = orientations.get(currentOrientation + 1);
			else
				orientation = orientations.get(0);
		}
		else
			orientation = orientations.get(currentOrientation);
		
		UTShape transShape = new UTShape(textureRef);
		for(UTShapeUnit unit : orientation)
			transShape.addUnit(0, unit.cloneOrphan(transShape));
		transShape.setCenterGrid(center_grid_x, center_grid_y);
		
		return transShape;
	}
}
