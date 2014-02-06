package com.yahoo.elantzb.untertetris.main;

import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.sound.TzSoundPlayer;
import com.yahoo.elantzb.tz.texture.TzTextureCollection;

public class UTLoader
{
	public void loadAssets()
	{
		System.out.println("Loading...");
		
		TzTextureCollection.loadTexture("res/texture/blueUnit.png", GL11.GL_NEAREST);
		TzTextureCollection.loadTexture("res/texture/grayUnit.png", GL11.GL_NEAREST);
		TzTextureCollection.loadTexture("res/texture/greenUnit.png", GL11.GL_NEAREST);
		TzTextureCollection.loadTexture("res/texture/limeUnit.png", GL11.GL_NEAREST);
		TzTextureCollection.loadTexture("res/texture/purpleUnit.png", GL11.GL_NEAREST);
		TzTextureCollection.loadTexture("res/texture/redUnit.png", GL11.GL_NEAREST);
		TzTextureCollection.loadTexture("res/texture/yellowUnit.png", GL11.GL_NEAREST);
		
		TzTextureCollection.loadTexture("res/texture/text.png", GL11.GL_NEAREST);
		TzTextureCollection.loadTexture("res/texture/sideBar.png", GL11.GL_NEAREST);

		TzTextureCollection.loadTexture("res/texture/leftLevelSelectArrow.png", GL11.GL_NEAREST);
		TzTextureCollection.loadTexture("res/texture/rightLevelSelectArrow.png", GL11.GL_NEAREST);
		
		TzSoundPlayer.addSound("res/sound/Tetris_Parkerman_1700.wav");
		TzSoundPlayer.addSound("res/sound/Tetris_dj_ghoat.wav");
		
		System.out.println("Done loading.");
	}
}
