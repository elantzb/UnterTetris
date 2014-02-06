package com.yahoo.elantzb.untertetris.main;

import java.util.ArrayList;

import com.yahoo.elantzb.untertetris.file.UTScore;
import com.yahoo.elantzb.untertetris.file.UTScoreFileHandler;
import com.yahoo.elantzb.untertetris.game.UTGame;
import com.yahoo.elantzb.untertetris.input.IUTInputListener;
import com.yahoo.elantzb.untertetris.input.UTInputHandler;
import com.yahoo.elantzb.untertetris.input.UTInputEvent;
import com.yahoo.elantzb.untertetris.menu.UTMenu;
import com.yahoo.elantzb.untertetris.menu.UTSplashPage;
import com.yahoo.elantzb.untertetris.shape.UTLinePiece;
import com.yahoo.elantzb.untertetris.text.UTText;
import com.yahoo.elantzb.untertetris.window.UTWindow;

public class UnterTetris 
{
	public enum UTState {
		LOADING, SPLASH, SPLASH_TO_MENU, MENU, MENU_TO_GAME, GAME, GAME_TO_MENU;
	};
	
	//private static ArrayList<IUTInputListener> inputListeners = new ArrayList<IUTInputListener>();
	
	private static UTState utState;
	public static UTLoader loader;
	public static UTScoreFileHandler scoreFileHandler;
	public static UTWindow window;
	public static UTSplashPage splashPage;
	public static UTMenu menu;
	public static UTGame game;
	
	//public static UTText text;
	
	public static void main(String args[])
	{
		utState = UTState.LOADING;
		loader = new UTLoader();
		scoreFileHandler = new UTScoreFileHandler("res/data/scores.untertetris");
		window = new UTWindow();
		//splashPage = new UTSplashPage();
		//menu = new UTMenu();
		
		//text = new UTText("abcdefghijklmnopqrstuvwxyz", "res/texture/text.png", 16, 16);
		//text.setPadding(-3);
		
		window.renderLoop();
	}
	
	public static void logicCallback()
	{
		// send input
		UTInputEvent event;
		
		while((event = UTInputHandler.getNextInput()) != null)
		{
			switch(utState)
			{
			case LOADING:
				break;
			case SPLASH:
				splashPage.onKeyEvent(event);
				break;
			case MENU:
				menu.onKeyEvent(event);
				break;
			case GAME:
				game.onKeyEvent(event);
				break;
			}
		}
		
		//text.drawText(0,0);
		
		// do logic and drawing
		switch(utState)
		{
		case LOADING:
			// make this another thread later
			loader.loadAssets();
			utState = UTState.SPLASH;
			break;
		case SPLASH:
			if(splashPage == null)
				splashPage = new UTSplashPage();
			splashPage.drawSplashPage();				
			break;
		case SPLASH_TO_MENU:
			utState = UTState.MENU;
			break;
		case MENU:
			if(menu == null)
				menu = new UTMenu();
			menu.drawMenu();
			break;
		case MENU_TO_GAME:
			game = new UTGame(menu.levelSelection * 20, 1000 - menu.levelSelection * 100);
			utState = UTState.GAME;
			break;
		case GAME:
			game.doLogic();
			game.drawGame();
			break;
		case GAME_TO_MENU:
			utState = UTState.MENU;
			break;
		}
		
	}
	
	public static void requestStateChange(UTState new_state)
	{
		utState = new_state;
	}
}
