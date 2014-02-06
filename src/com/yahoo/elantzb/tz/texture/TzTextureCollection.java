package com.yahoo.elantzb.tz.texture;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TzTextureCollection 
{
	private static Hashtable<String,Texture> textures = new Hashtable<String,Texture>();
	
	public static int loadTexture(String path, int filter)
	{
		int textureID = -1;
		
		String path1 = path.replace('\\', '/');
		
		if(!textures.containsKey(path1))
		{
			Texture tex = loadTextureFromSlick(path1, filter);
			textures.put(path1, tex);
			textureID = tex.getTextureID();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			
			System.out.println("Loaded texture " + path1 + " : " + textures.size());
		}
		else
			textureID = textures.get(path1).getTextureID();
		
		return textureID;
	}
	
	public static void preloadTextureDirectory(String directory_path, int filter)
	{
		File texture_dir = new File(directory_path);
		
		//if(texture_dir.isDirectory())
		{
			File[] contents = texture_dir.listFiles();
			
			for(int i = 0; i < contents.length; i++)
			{
				loadTexture(contents[i].getPath(), filter);
			}
		}
		//else
		{
			//System.out.println("Could not bulk load textures from " + directory_path + " -- Path is not a directory.");
		}
	}
	
	public static Texture getTextureByID(int id)
	{
		Texture ret = null;
		
		for(Enumeration<String> keys = textures.keys(); keys.hasMoreElements(); )
		{
			Texture currentTexture = textures.get(keys.nextElement());
			if(currentTexture.getTextureID() == id)
				ret = currentTexture;
		}
		
		return ret;
	}
	
	public static String getTextureName(int id)
	{
		String textureName = null;
		
		for(Enumeration<String> keys = textures.keys(); keys.hasMoreElements(); )
		{
			String key = keys.nextElement();
			Texture currentTexture = textures.get(key);
			if(currentTexture.getTextureID() == id)
			{
				textureName = key;
				break;
			}
		}
		
		return textureName;
	}
	
	private static Texture loadTextureFromSlick(String p_textureRef, int filter)
	{
		Texture r_texture = null;
		
		String ext = "";
		StringTokenizer st = new StringTokenizer(p_textureRef,".");
		
		while(st.hasMoreTokens()){
			ext = st.nextToken();
		}
		
		try
		{
			r_texture = TextureLoader.getTexture(ext, ResourceLoader.getResourceAsStream(p_textureRef), false, filter);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
		return r_texture;
	}
}
