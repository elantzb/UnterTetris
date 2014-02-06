package com.yahoo.elantzb.untertetris.input;

public class UTInputEvent 
{
	public String type;
	public int data;
	
	public UTInputEvent(String event_type, int event_data)
	{
		type = event_type;
		data = event_data;
	}
}
