package com.yahoo.elantzb.untertetris.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;

import com.yahoo.elantzb.untertetris.game.UTGame;

public class UTScoreFileHandler 
{
	public String scoreFilePath;
	public ArrayList<UTScore> loadedScores;
	
	public UTScoreFileHandler(String score_file_path)
	{
		scoreFilePath = score_file_path;
		loadedScores = new ArrayList<UTScore>();
		
		try
		{
			File scoreFile = new File(scoreFilePath);
			if(!scoreFile.exists())
			{
				scoreFile.getParentFile().mkdirs();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		loadScoresFromFile();
	}
	
	public void loadScoresFromFile()
	{
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		loadedScores.clear();
		
		try
		{
			fileReader = new FileReader(scoreFilePath);
			bufferedReader = new BufferedReader(fileReader);
			
			String line;
			UTScore nextScore;
			
			while(bufferedReader.ready())
			{
				line = bufferedReader.readLine();
				
				nextScore = UTScore.unserialize(line);
				loadedScores.add(nextScore);
			}
			
			sortScores();
			
			bufferedReader.close();
			fileReader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void writeScoresToFile()
	{
		PrintStream printStream = null;
		
		try
		{
			printStream = new PrintStream(scoreFilePath, "UTF-8");
			
			for(UTScore score : loadedScores)
				printStream.println(UTScore.serialize(score));
			
			printStream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void addScore(String name, int score, int level)
	{
		loadedScores.add(new UTScore(name,score,level));
		
		sortScores();
	}
	
	public void clearScores()
	{
		loadedScores.clear();
		writeScoresToFile();
	}
	
	private void sortScores()
	{
		ArrayList<UTScore> unsortedScores = new ArrayList<UTScore>();
		unsortedScores.addAll(loadedScores);
		loadedScores.clear();
		
		for(int level = 1; level <= UTGame.MAX_LEVEL; level++)
		{
			for(int s = 0; s < unsortedScores.size(); s++)
			{
				if(unsortedScores.get(s).level == level)
					loadedScores.add(unsortedScores.get(s));
			}
		}
	}
	
	public ArrayList<ArrayList<UTScore>> getScores()
	{
		ArrayList<ArrayList<UTScore>> scoreArray = new ArrayList<ArrayList<UTScore>>();
		
		for(int level = 1; level < UTGame.MAX_LEVEL; level++)
		{
			scoreArray.add(new ArrayList<UTScore>());
		}
		
		for(UTScore score : loadedScores)
		{
			scoreArray.get(score.level - 1).add(score);
		}
		
		return scoreArray;
	}
	
	public ArrayList<ArrayList<String>> getScoresAsStringArray()
	{
		ArrayList<ArrayList<String>> stringArray = new ArrayList<ArrayList<String>>();
		
		for(int level = 1; level < UTGame.MAX_LEVEL; level++)
		{
			stringArray.add(new ArrayList<String>());
		}
		
		for(UTScore score : loadedScores)
		{
			stringArray.get(score.level - 1).add(UTScore.serialize(score));
		}
		
		return stringArray;
	}
}
