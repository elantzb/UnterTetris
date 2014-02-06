package com.yahoo.elantzb.untertetris.shape;

import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.texture.TzTextureCollection;
import com.yahoo.elantzb.untertetris.main.UnterTetris;

public class UTSquareBlock extends UTShape
{
	public UTSquareBlock()
	{
		super("res/texture/grayUnit.png");
		
		addUnit(0,0,0); addUnit(0,1,0);
		addUnit(0,0,1); addUnit(0,1,1);
	}
}
