package com.yahoo.elantzb.tz.typeable;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.yahoo.elantzb.tz.clickable.TzClickableCollection;

public class TzTypeableCollection implements Runnable
{
	private static TzTypeableCollection registeredInstance;
	
	private ArrayList<ITzTypeable> typeables;
	
	private int waitTime;
	
	private TzTypeableCollection()
	{
		typeables = new ArrayList<ITzTypeable>();
	}
	
	public static void register()
	{
		register(100);
	}
	
	public static void register(int wait_time)
	{
		registeredInstance = new TzTypeableCollection();
		
		registeredInstance.waitTime = wait_time;
		
		Thread t = new Thread(registeredInstance);
		t.start();
	}
	
	public static void unregister()
	{
		registeredInstance = null;
	}
	
	public static void addTypeable(ITzTypeable typeable)
	{
		registeredInstance.typeables.add(typeable);
	}
	
	public void run()
	{
		while(registeredInstance != null)
		{
			while(Keyboard.isCreated() && Keyboard.next())
			{
				if(Keyboard.getEventKeyState())
				{
					fireKeyPress(Keyboard.getEventCharacter(), Keyboard.getEventKey());
				}
				else
				{
					fireKeyRelease(Keyboard.getEventCharacter(), Keyboard.getEventKey());
				}
			}
			
			try 
			{
				Thread.sleep(waitTime);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private static void fireKeyPress(char c, int key_code)
	{
		for(ITzTypeable typeable : registeredInstance.typeables)
		{
			typeable.onKeyPress(c, key_code);
		}
	}
	
	private static void fireKeyRelease(char c, int key_code)
	{
		for(ITzTypeable typeable : registeredInstance.typeables)
		{
			typeable.onKeyRelease(c, key_code);
		}
	}
}
