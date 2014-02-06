package com.yahoo.elantzb.untertetris.input;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.yahoo.elantzb.untertetris.main.UnterTetris;

public class UTInputHandler 
{
	public UTInputHandler()
	{
		
	}
	
	public static UTInputEvent getNextInput()
	{		
		if(Keyboard.next())
		{
			if(Keyboard.getEventKeyState())
				return new UTInputEvent("KEY_PRESSED", Keyboard.getEventKey());
			else
				return new UTInputEvent("KEY_RELEASED", Keyboard.getEventKey());
		}
		
		return null;
	}
}
