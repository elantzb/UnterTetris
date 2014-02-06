package com.yahoo.elantzb.untertetris.file;

import java.util.StringTokenizer;

public class UTScore 
{
	public String name;
	public int score;
	public int level;
	
	public UTScore(String p_name, int p_score, int p_level)
	{
		name = p_name;
		score = p_score;
		level = p_level;
	}
	
	public static UTScore unserialize(String data_string)
	{
		StringTokenizer tokens = new StringTokenizer(data_string, "|");
		String name;
		int score, level;
		
		try
		{
			name = tokens.nextToken();
			score = Integer.parseInt(tokens.nextToken());
			level = Integer.parseInt(tokens.nextToken());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
			
		return new UTScore(name,score,level);
	}
	
	public static String serialize(UTScore score)
	{
		return score.name + '|' + score.score + '|' + score.level;
	}
}
