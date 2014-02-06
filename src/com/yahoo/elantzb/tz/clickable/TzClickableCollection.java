package com.yahoo.elantzb.tz.clickable;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

/** Manages instances of AbTzClickable, notifying them of click events. 
 * This class must be registered with register(), 
 * and clickables must be added to the registry with addClickable().
 */
public class TzClickableCollection implements Runnable
{
	private static TzClickableCollection registeredInstance;
	
	private ArrayList<ArrayList<AbTzClickable>> clickableLayers;
	
	private int waitTime;
	private int windowWidth, windowHeight;
	
	private TzClickableCollection()
	{
		clickableLayers = new ArrayList<ArrayList<AbTzClickable>>();
	}
	
	public static int getNumClickables()
	{
		int ret_numClickables = 0;
		
		for(ArrayList<AbTzClickable> layer : registeredInstance.clickableLayers)
			ret_numClickables += layer.size();
		
		return ret_numClickables;
	}
	
	public static void register(int window_width, int window_height)
	{
		register(window_width, window_height, 100);
	}
	
	public static void register(int window_width, int window_height, int wait_time)
	{
		registeredInstance = new TzClickableCollection();
		
		updateWindowSize(window_width, window_height);
		registeredInstance.waitTime = wait_time;
		
		Thread t = new Thread(registeredInstance);
		t.start();
	}
	
	public static void removeClickable(int layer_attempt, AbTzClickable clickable)
	{
		for( ;layer_attempt < registeredInstance.clickableLayers.size(); layer_attempt++)
			registeredInstance.clickableLayers.get(layer_attempt).remove(clickable);
	}
	public static void updateWindowSize(int window_width, int window_height)
	{
		registeredInstance.windowWidth = window_width;
		registeredInstance.windowHeight = window_height;
	}
	
	public static void unregister()
	{
		registeredInstance = null;
	}
	
	public static void addClickable(Integer layer, AbTzClickable clickable)
	{
		while(registeredInstance.clickableLayers.size() < layer + 1)
			registeredInstance.clickableLayers.add(new ArrayList<AbTzClickable>());
		
		registeredInstance.clickableLayers.get(layer).add(clickable);
	}
	
	public static void setClickabilityByTag(String tag, boolean is_clickable)
	{
		for(ArrayList<AbTzClickable> layer : registeredInstance.clickableLayers)
		{
			for(AbTzClickable clickable : layer)
			{
				if(clickable.getTags().contains(tag))
				{
					clickable.setIsClickable(is_clickable);
				}
			}
		}
	}
	
	/*public static void clearDeadClickables()
	{
		for(ArrayList<AbTzClickable> layer : registeredInstance.clickableLayers)
			for(int i = 0; i < layer.size(); i++)
			{
				if( layer.get(i).getIsDead())
				{
					layer.remove(i);
					i--;
				}
			}
	}*/
	
	public void clearClickable(AbTzClickable clickable)
	{
		for(int layer = 0; layer < registeredInstance.clickableLayers.size(); layer++)
		{
			for(int c = 0; c < registeredInstance.clickableLayers.get(layer).size(); c++)
			{
				if(registeredInstance.clickableLayers.get(layer).get(c).equals(clickable))
					registeredInstance.clickableLayers.get(layer).remove(c);
			}
		}
	}
	
	public void run()
	{
		while(registeredInstance != null)
		{
			int mouseX, mouseY;
			
			for(int i = registeredInstance.clickableLayers.size() - 1; i >= 0; i--)
			{
				for(AbTzClickable clickable : registeredInstance.clickableLayers.get(i))
				{
					if(clickable == null)
					{
						registeredInstance.clickableLayers.get(i).remove(clickable);
					}
				}
			}
			
			while(Mouse.isCreated() && Mouse.next())
			{
				mouseX = Mouse.getEventX();
				mouseY = Mouse.getEventY();
				
				if(Mouse.getEventButton() == 0)
				{
					if(Mouse.getEventButtonState())
						fireLeftMousePress(mouseX,mouseY);
					else
						fireLeftMouseRelease(mouseX,mouseY);
				}
				else if(Mouse.getEventButton() == 1)
				{
					if(Mouse.getEventButtonState())
						fireRightMousePress(mouseX,mouseY);
					else
						fireRightMouseRelease(mouseX,mouseY);
				}
				else
				{
					fireMouseOver(mouseX,mouseY);
				}
			}
			
			try
			{
				Thread.sleep(waitTime);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private static void fireLeftMousePress(int mouse_x, int mouse_y)
	{
		boolean targetFound = false;
		
		for(int i = registeredInstance.clickableLayers.size() - 1; i >= 0; i--)
		{
			for(AbTzClickable clickable : registeredInstance.clickableLayers.get(i))
			{
				if(clickable.getIsClickable())
				{
					if(!targetFound && mouse_x >= clickable.getX() && mouse_x < clickable.getX() + clickable.getWidth() &&
							registeredInstance.windowHeight - mouse_y >= clickable.getY() && registeredInstance.windowHeight - mouse_y < clickable.getY() + clickable.getHeight())
					{
						targetFound = true;
						clickable.onLeftMousePress(false);
						//return;// removed this because every clickable after this one was not receiving global notifications
					}
					else
						clickable.onLeftMousePress(true);
				}
			}
		}
	}
	
	private static void fireLeftMouseRelease(int mouse_x, int mouse_y)
	{
		boolean targetFound = false;
		
		for(int i = registeredInstance.clickableLayers.size() - 1; i >= 0; i--)
		{
			for(AbTzClickable clickable : registeredInstance.clickableLayers.get(i))
			{
				if(clickable.getIsClickable())
				{
					if(!targetFound && mouse_x >= clickable.getX() && mouse_x < clickable.getX() + clickable.getWidth() &&
							registeredInstance.windowHeight - mouse_y >= clickable.getY() && registeredInstance.windowHeight - mouse_y < clickable.getY() + clickable.getHeight())
					{
						targetFound = true;
						clickable.onLeftMouseRelease(false);
						//return; // removed this because every clickable after this one was not receiving global notifications
					}
					else
						clickable.onLeftMouseRelease(true); // true = global
				}
			}
		}
	}
	
	private static void fireRightMousePress(int mouse_x, int mouse_y)
	{
		boolean targetFound = false;
		
		for(int i = registeredInstance.clickableLayers.size() - 1; i >= 0; i--)
		{
			for(AbTzClickable clickable : registeredInstance.clickableLayers.get(i))
			{
				if(clickable.getIsClickable())
				{
					if(!targetFound && mouse_x >= clickable.getX() && mouse_x < clickable.getX() + clickable.getWidth() &&
							registeredInstance.windowHeight - mouse_y >= clickable.getY() && registeredInstance.windowHeight - mouse_y < clickable.getY() + clickable.getHeight())
					{
						targetFound = true;
						clickable.onRightMousePress(false);
						//return;// removed this because every clickable after this one was not receiving global notifications
					}
					else
						clickable.onRightMousePress(true);
				}
			}
		}
	}
	
	private static void fireRightMouseRelease(int mouse_x, int mouse_y)
	{
		boolean targetFound = false;
		
		for(int i = registeredInstance.clickableLayers.size() - 1; i >= 0; i--)
		{
			for(AbTzClickable clickable : registeredInstance.clickableLayers.get(i))
			{
				if(clickable.getIsClickable())
				{
					if(!targetFound && mouse_x >= clickable.getX() && mouse_x < clickable.getX() + clickable.getWidth() &&
							registeredInstance.windowHeight - mouse_y >= clickable.getY() && registeredInstance.windowHeight - mouse_y < clickable.getY() + clickable.getHeight())
					{
						targetFound = true;
						clickable.onRightMouseRelease(false);
						//return;// removed this because every clickable after this one was not receiving global notifications
					}
					else
						clickable.onRightMouseRelease(true);
				}
			}
		}
	}
	
	private static void fireMouseOver(int mouse_x, int mouse_y)
	{
		boolean targetFound = false;
		
		for(int i = registeredInstance.clickableLayers.size() - 1; i >= 0; i--)
		{
			for(AbTzClickable clickable : registeredInstance.clickableLayers.get(i))
			{
				if(clickable.getIsClickable())
				{
					if(!targetFound && mouse_x >= clickable.getX() && mouse_x < clickable.getX() + clickable.getWidth() &&
							registeredInstance.windowHeight - mouse_y >= clickable.getY() && registeredInstance.windowHeight - mouse_y < clickable.getY() + clickable.getHeight())
					{
						targetFound = true;
						clickable.onMouseOver();
						//return;// removed this because every clickable after this one was not receiving global notifications
					}
					else
						clickable.onMouseMove(mouse_x, mouse_y);
				}
			}
		}
	}
}
