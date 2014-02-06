package com.yahoo.elantzb.tz.drawable;
import java.awt.Dimension;

import org.lwjgl.opengl.GL11;

import com.yahoo.elantzb.tz.texture.TzTextureBounds;
import com.yahoo.elantzb.tz.texture.TzTextureCollection;


public class TzDrawable
{
	protected Dimension size;
	
	// bounds of texture. similar to the Rectangle class, but with floats.
	protected TzTextureBounds textureBounds;
	
	public int frameCols, frameRows;
	
	// needed by OpenGL to draw the texture, i.e. returned by TextureCollection.loadTexture()
	protected int textureID;
	
	public TzDrawable(String texture_ref, int width, int height, int cols, int rows)
	{
		this(texture_ref, width, height, cols, rows, GL11.GL_NEAREST);
	}
	
	public TzDrawable(String texture_ref, int width, int height, int cols, int rows, int texture_filter)
	{
		setSize(width, height);	
		setTexture(texture_ref, texture_filter);
		setTextureBounds(0f, 0f, 1f, 1f);
		frameCols = cols;
		frameRows = rows;
	}
	
	public Dimension getSize()
	{
		return size;
	}

	public int getTextureID()
	{
		return this.textureID;
	}
	
	public void setSize(int w, int h)
	{
		size = new Dimension(w,h);
	}
	
	public TzTextureBounds getTextureBounds()
	{
		return textureBounds;
	}
	
	public void setTextureBounds(float left, float top, float width, float height)
	{
		textureBounds = new TzTextureBounds(left, top, width, height);
	}
	
	public void setTextureBounds(TzTextureBounds new_bounds)
	{
		textureBounds = new_bounds;
	}
	
	public void setFrame(int frameID)
	{
		float frameRow = (frameID - 1) / frameCols;
		float frameCol = (frameID % frameCols) - 1;
		float frameLeft = frameCol/frameCols;
		float frameTop = frameRow/frameRows;
		
		setTextureBounds(frameLeft, frameTop, 1f/frameCols, 1f/frameRows);
	}
	
	public void setTextureID(int id)
	{
		textureID = id;
	}
	
	public void setTexture(String texture_ref, int texture_filter)
	{
		setTextureID(TzTextureCollection.loadTexture(texture_ref, texture_filter));
	}
	
	public void draw(int x, int y)
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
				
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2d(textureBounds.left + textureBounds.width, textureBounds.top);
			GL11.glVertex2i(x + size.width, y);
			GL11.glTexCoord2d(textureBounds.left, textureBounds.top);
			GL11.glVertex2i(x, y);
			GL11.glTexCoord2d(textureBounds.left, textureBounds.top + textureBounds.height);
			GL11.glVertex2i(x, y + size.height);
			GL11.glTexCoord2d(textureBounds.left + textureBounds.width, textureBounds.top + textureBounds.height);
			GL11.glVertex2i(x + size.width, y + size.height);
		GL11.glEnd();
	}
	
	public void draw(int x, int y, int alpha_value)
	{
		double alphaValue = 1.0;
		if(alpha_value >= 0 && alpha_value <= 255)
			alphaValue = alpha_value / 255.0;
		
		System.out.println(alphaValue);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
				
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4d(1.0,  1.0, 1.0, alphaValue);
			GL11.glTexCoord2d(textureBounds.left + textureBounds.width, textureBounds.top);
			GL11.glVertex2i(x + size.width, y);
			GL11.glTexCoord2d(textureBounds.left, textureBounds.top);
			GL11.glVertex2i(x, y);
			GL11.glTexCoord2d(textureBounds.left, textureBounds.top + textureBounds.height);
			GL11.glVertex2i(x, y + size.height);
			GL11.glTexCoord2d(textureBounds.left + textureBounds.width, textureBounds.top + textureBounds.height);
			GL11.glVertex2i(x + size.width, y + size.height);
			GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
		GL11.glEnd();
	}
	
	public void draw(int screen_x, int screen_y, double rotation_degree)
	{
		//System.out.println("Setting rotation of drawable...");
		
		//System.out.println("Top-left corner of quad: " + screen_x + ", " + screen_y);
		
		int centerX = screen_x + size.width/2;
		int centerY = screen_y + size.height/2;
		
		//System.out.println("Center of rotation: " + centerX + ", " + centerY);
		
		double radius = Math.sqrt(size.width * size.width + size.height * size.height)/2;
	
		//System.out.println("Radius: " + radius);
		
		double topRightAngle = 45, 
				topLeftAngle = topRightAngle + 90, 
				bottomLeftAngle = topLeftAngle + 90,
				bottomRightAngle = bottomLeftAngle + 90;
		
		/*System.out.println("Angles for each corner, CC from top-right:" + 
		topRightAngle + ", " + topLeftAngle + ", " + bottomLeftAngle + ", " + bottomRightAngle);*/
		
		topRightAngle = (topRightAngle + rotation_degree < 360) ? topRightAngle + rotation_degree : topRightAngle + rotation_degree - 360;
		topLeftAngle = (topLeftAngle + rotation_degree < 360) ? topLeftAngle + rotation_degree : topLeftAngle + rotation_degree - 360;
		bottomLeftAngle = (bottomLeftAngle + rotation_degree < 360) ? bottomLeftAngle + rotation_degree : bottomLeftAngle + rotation_degree - 360;
		bottomRightAngle = (bottomRightAngle + rotation_degree < 360) ? bottomRightAngle + rotation_degree : bottomRightAngle + rotation_degree - 360;
		
		/*System.out.println("Angles after rotation by " + rotation_degree + " degrees: " + 
				topRightAngle + ", " + topLeftAngle + ", " + bottomLeftAngle + ", " + bottomRightAngle);*/
		
		int topRightX = centerX + (int)Math.floor((Math.cos(Math.toRadians(topRightAngle)) * radius));
		int topRightY = centerY - (int)Math.floor((Math.sin(Math.toRadians(topRightAngle)) * radius));
		
		int topLeftX = centerX + (int)Math.floor((Math.cos(Math.toRadians(topLeftAngle)) * radius));
		int topLeftY = centerY - (int)Math.floor((Math.sin(Math.toRadians(topLeftAngle)) * radius));
		
		int bottomLeftX = centerX + (int)Math.floor((Math.cos(Math.toRadians(bottomLeftAngle)) * radius));
		int bottomLeftY = centerY - (int)Math.floor((Math.sin(Math.toRadians(bottomLeftAngle)) * radius));
		
		int bottomRightX = centerX + (int)Math.floor((Math.cos(Math.toRadians(bottomRightAngle)) * radius));
		int bottomRightY = centerY - (int)Math.floor((Math.sin(Math.toRadians(bottomRightAngle)) * radius));
		
		/*System.out.println("Coordinates of corners before rotation: " +
		(screen_x + size.width) + "," + screen_y + " " +
				screen_x + "," + screen_y + " " +
		screen_x + "," + (screen_y + size.height) + " " +
				(screen_x + size.width) + "," + (screen_y + size.height));
		
		System.out.println("After rotation: " +
		topRightX + "," + topRightY + " " +
				topLeftX + "," + topLeftY + " " +
		bottomLeftX + "," + bottomLeftY + " " +
				bottomRightX + "," + bottomRightY);
		
		System.out.println("Operation complete.");*/
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2d(textureBounds.left + textureBounds.width, textureBounds.top);
			GL11.glVertex2i(topRightX, topRightY);
			GL11.glTexCoord2d(textureBounds.left, textureBounds.top);
			GL11.glVertex2i(topLeftX, topLeftY);
			GL11.glTexCoord2d(textureBounds.left, textureBounds.top + textureBounds.height);
			GL11.glVertex2i(bottomLeftX, bottomLeftY);
			GL11.glTexCoord2d(textureBounds.left + textureBounds.width, textureBounds.top + textureBounds.height);
			GL11.glVertex2i(bottomRightX, bottomRightY);
		GL11.glEnd();
	}
	
	/*public static void drawQuad(int texture_id, int screen_x, int screen_y, Dimension quad_size, TextureBounds texture_bounds)
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_id);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2d(texture_bounds.left + texture_bounds.width, texture_bounds.top);
			GL11.glVertex2i(screen_x + quad_size.width, screen_y);
			GL11.glTexCoord2d(texture_bounds.left, texture_bounds.top);
			GL11.glVertex2i(screen_x, screen_y);
			GL11.glTexCoord2d(texture_bounds.left, texture_bounds.top + texture_bounds.height);
			GL11.glVertex2i(screen_x, screen_y + quad_size.height);
			GL11.glTexCoord2d(texture_bounds.left + texture_bounds.width, texture_bounds.top + texture_bounds.height);
			GL11.glVertex2i(screen_x + quad_size.width, screen_y + quad_size.height);
		GL11.glEnd();
	}*/
}
