package com.yahoo.elantzb.tz.sound;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.sound.sampled.AudioSystem;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import org.lwjgl.openal.AL;

/** For playing sounds.
 *
 */
public class TzSoundPlayer 
{
	private static Hashtable<String, Audio> sounds = new Hashtable<String, Audio>();
	
	public static void addSound(String sound_path)
	{
		sound_path = sound_path.replace('\\', '/');
		
		sounds.put(sound_path, getWavAudio(sound_path));
		
		System.out.println("Loaded sound " + sound_path + " : " + sounds.size());
	}
	
	public static void preloadSoundDirectory(String directory_path)
	{
		File sound_dir = new File(directory_path);
		
		//if(sound_dir.isDirectory())
		{
			File[] contents = sound_dir.listFiles();
			
			for(int i = 0; i < contents.length; i++)
			{
				addSound(contents[i].getPath());
			}
		}
		//else
		{
			//System.out.println("Could not bulk load soundss from " + directory_path + " -- Path is not a directory.");
		}
	}
	
	public static void playMusic(String musicRef)
	{
		sounds.get(musicRef).playAsMusic(1f, 1f, false);
	}
	
	public static void playMusicLooped(String musicRef)
	{
		sounds.get(musicRef).playAsMusic(1f, 1f, true);
	}
	
	public static void playSound(String musicRef)
	{
		sounds.get(musicRef).playAsSoundEffect(1f, 1f, false);
	}
	
	public static void playSoundLooped(String musicRef)
	{
		sounds.get(musicRef).playAsSoundEffect(1f, 1f, true);
	}
	
	public static void stopSound(String musicRef)
	{
		sounds.get(musicRef).stop();
	}
	
	public static boolean isSoundPlaying(String musicRef)
	{
		return sounds.get(musicRef).isPlaying();
	}
	
	private static Audio getWavAudio(String path)
	{
		Audio wav = null;
		
		try 
		{
			wav = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream(path));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return wav;
	}
	
	public static void closeDevices()
	{
		AL.destroy();
	}
}
