package com.yahoo.elantzb.untertetris.shape;

import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.texture.TzTextureCollection;
import com.yahoo.elantzb.untertetris.main.UnterTetris;

public class UTLinePiece extends UTShape
{
	public UTLinePiece()
	{
		super("res/texture/blueUnit.png");
		
		addUnit(0,0,-1); 
		addUnit(0,0,0);
		addUnit(0,0,1); 
		addUnit(0,0,2);
		
		addUnit(1,-1,0); addUnit(1,0,0); addUnit(1,1,0); addUnit(1,2,0);
	}
}
