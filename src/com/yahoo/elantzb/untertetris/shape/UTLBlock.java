package com.yahoo.elantzb.untertetris.shape;

import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.texture.TzTextureCollection;
import com.yahoo.elantzb.untertetris.main.UnterTetris;

public class UTLBlock extends UTShape
{
	public UTLBlock()
	{
		super("res/texture/redUnit.png");
		
		addUnit(0,0,-1);
		addUnit(0,0,0);
		addUnit(0,0,1); addUnit(0,1,1);
		
		addUnit(1,-1,0); addUnit(1,0,0); addUnit(1,1,0);
		addUnit(1,-1,1);
		
		addUnit(2,-1,-1); addUnit(2,0,-1);
		                  addUnit(2,0,0);
		                  addUnit(2,0,1);
		                            
		                                 addUnit(3,1,-1);
		addUnit(3,-1,0); addUnit(3,0,0); addUnit(3,1,0);
	}
}
