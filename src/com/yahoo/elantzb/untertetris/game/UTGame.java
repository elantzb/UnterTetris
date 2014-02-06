package com.yahoo.elantzb.untertetris.game;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.yahoo.elantzb.tz.sound.TzSoundPlayer;
import com.yahoo.elantzb.untertetris.input.IUTInputListener;
import com.yahoo.elantzb.untertetris.input.UTInputEvent;
import com.yahoo.elantzb.untertetris.input.UTInputHandler;
import com.yahoo.elantzb.untertetris.main.UnterTetris;
import com.yahoo.elantzb.untertetris.main.UnterTetris.UTState;
import com.yahoo.elantzb.untertetris.shape.UTLBlock;
import com.yahoo.elantzb.untertetris.shape.UTLinePiece;
import com.yahoo.elantzb.untertetris.shape.UTReverseLBlock;
import com.yahoo.elantzb.untertetris.shape.UTReverseSquiggly;
import com.yahoo.elantzb.untertetris.shape.UTShape;
import com.yahoo.elantzb.untertetris.shape.UTShapeUnit;
import com.yahoo.elantzb.untertetris.shape.UTSquareBlock;
import com.yahoo.elantzb.untertetris.shape.UTSquiggly;
import com.yahoo.elantzb.untertetris.shape.UTTBlock;
import com.yahoo.elantzb.untertetris.window.UTWindow;

public class UTGame implements IUTInputListener
{
	public static int MAX_LEVEL = 9;
	
	public enum GameState {
		CREATING, DROPPING, STOPPING, ROTATING, MOVING_LEFT, MOVING_RIGHT, PAUSED, CLEARING, WINNING, LOSING, TO_MENU;
	};
	
	public GameState gameState;
	
	//private ArrayList<UTDebrisUnit> debris;
	private UTShapeUnit[][] gridUnits;
	
	private UTInputHandler inputHandler;
	
	public UTShape nextShape;
	private UTShape currentShape;
	//private int currentShapeX, currentShapeY;
	
	private long millisLastDrop;
	private long hangTimeStartMillis;
	
	private int dropSpeed, defaultDropSpeed;
	private int lineCombo;
	private int linesCleared, totalScore;
	private int linesNeeded;
	private boolean gameOver, gamePaused, fillingGrid;
	private int gridFilledLines;
	private long millisLastGridFilledLine;
	
	private boolean upPressed, leftPressed, rightPressed, downPressed, escapePressed;
	
	private UTSideBar sideBar;
	public UTLineCounter lineCounter;
	
	public UTGame(int lines_needed, int drop_speed)
	{
		//debris = new ArrayList<UTDebrisUnit>();
		gameState = GameState.CREATING;
		gridUnits = new UTShapeUnit[UTWindow.GRID_HEIGHT][UTWindow.GRID_WIDTH];
		for(int y = 0; y < gridUnits.length; y++)
			gridUnits[y] = new UTShapeUnit[UTWindow.GRID_WIDTH];
		
		inputHandler = new UTInputHandler();
		currentShape = getNewShape();
		nextShape = getNewShape();
		currentShape.setCenterGrid(4,1);
		millisLastDrop = System.currentTimeMillis();
		hangTimeStartMillis = 0;
		dropSpeed = defaultDropSpeed = drop_speed;
		lineCombo = 0;
		linesCleared = totalScore = 0;
		linesNeeded = lines_needed;
		gameOver = false;
		gamePaused = false;
		fillingGrid = false;
		gridFilledLines = 0;
		millisLastGridFilledLine = System.currentTimeMillis();
		
		upPressed = leftPressed = rightPressed = downPressed = escapePressed = false;
		
		sideBar = new UTSideBar();
		lineCounter = new UTLineCounter();
		
		
		//TzSoundPlayer.playMusicLooped("res/sound/Tetris_dj_ghoat.wav");
	}
	
	public UTShape getCurrentShape()
	{
		return currentShape;
	}
	
	public void doLogic()
	{	
		// logic
		// CREATING, DROPPING, ROTATING, MOVING, CLEARING, WINNING, LOSING;
		switch(gameState)
		{
		case CREATING:
			currentShape = nextShape;
			currentShape.setCenterGrid(4, 1);
			
			tryForceDrop(false);
			
			if(checkPositionGood(currentShape))
			{
				for(UTShapeUnit unit : currentShape.getUnits())
				{
					gridUnits[unit.getGridY(currentShape.getCenterGridY())][unit.getGridX(currentShape.getCenterGridX())] = unit;
				}
			}
			else
			{
				requestStateChange(GameState.LOSING);
				return;
			}
			
			nextShape = getNewShape();
			
			gameState = GameState.DROPPING;
			break;
			
		case DROPPING:
			tryForceDrop(downPressed);
			
			if(currentShape != null && System.currentTimeMillis() - millisLastDrop >= dropSpeed)
			{
				millisLastDrop = System.currentTimeMillis();
				
				tryDrop();
			}
			break;
			
		case STOPPING:
			for(UTShapeUnit debris_unit : currentShape.getUnits())
			{
				debris_unit.parentShape = null;
				gridUnits[debris_unit.getGridY(currentShape.getCenterGridY())][debris_unit.getGridX(currentShape.getCenterGridX())] = debris_unit;
			}
			
			requestStateChange(GameState.CLEARING);
			break;
			
		case ROTATING:
			break;
			
		case MOVING_LEFT:
			tryMove(-1,0);
			requestStateChange(GameState.DROPPING);
			break;
			
		case MOVING_RIGHT:
			tryMove(1,0);
			requestStateChange(GameState.DROPPING);
			break;
			
		case PAUSED:
			break;
			
		case CLEARING:
			while(clearRow())
				lineCombo++;
			
			if(lineCombo > 0)
			{
				//System.out.println("Lines cleared: " + lineCombo);
				linesCleared += lineCombo;
				totalScore += (95 + Math.pow(5, lineCombo));
				System.out.println("Points awarded for " + lineCombo + " lines: " + (90 + Math.pow(5, lineCombo)));
				System.out.println("score: " + totalScore);
				lineCombo = 0;
				
				if(linesNeeded - linesCleared <= 0)
				{
					requestStateChange(GameState.WINNING);
					return;
				}
			}
			
			requestStateChange(GameState.CREATING);
			break;
			
		case WINNING:
			onWin();
			break;
			
		case LOSING:
			onLose();
			break;
			
		case TO_MENU:
			UnterTetris.requestStateChange(UTState.GAME_TO_MENU);
			break;
		}
	}
	
	public void requestStateChange(GameState new_state)
	{
		gameState = new_state;
	}
	
	public void drawGame()
	{
		sideBar.draw(UTWindow.GRID_WIDTH * UTShapeUnit.UNIT_LENGTH, 0, UTWindow.SIDE_BAR_WIDTH * UTShapeUnit.UNIT_LENGTH, UTWindow.WINDOW_HEIGHT);
		lineCounter.setValue(linesNeeded - linesCleared);
		lineCounter.draw((UTWindow.GRID_WIDTH + UTWindow.SIDE_BAR_WIDTH/3) * UTShapeUnit.UNIT_LENGTH, (int)(UTWindow.WINDOW_HEIGHT * 2/3));
		
		if(nextShape != null)
		{
			//System.out.println(".");
			for(UTShapeUnit unit : UnterTetris.game.nextShape.getUnits())
			{
				unit.drawAtScreenCoordRelative((UTWindow.GRID_WIDTH + UTWindow.SIDE_BAR_WIDTH * 2/5) * UTShapeUnit.UNIT_LENGTH, UTWindow.WINDOW_HEIGHT * 1/5);
			}
		}
		
		for(int y = 0; y < gridUnits.length; y++)
		{
			for(int x = 0; x < gridUnits[y].length; x++)
			{
				if(gridUnits[y][x] != null)
					gridUnits[y][x].drawAtGridCoord(x,y);
			}
		}
		
		//if(currentShape != null)
			//currentShape.draw();
	}
	
	private void tryForceDrop(boolean is_forced)
	{
		if(is_forced)
		{
			//System.out.println("starting force drop...");
			if(hangTimeStartMillis == 0)
			{
				dropSpeed = defaultDropSpeed/2;
				hangTimeStartMillis = System.currentTimeMillis();
			}
			
			if(System.currentTimeMillis() - hangTimeStartMillis > 300)
			{
				//System.out.println("!!!");
				dropSpeed = 30;
			}
			
		}
		else
		{
			//System.out.println("stopping force drop...");
			dropSpeed = defaultDropSpeed;
			hangTimeStartMillis = 0;
		}
	}
	
	public UTShape getNewShape()
	{
		UTShape newShape;
		
		int rand = (int)(Math.random() * 7.0);
		
		switch(rand)
		{
		case 0:
			newShape = new UTLBlock();
			break;
			
		case 1:
			newShape = new UTReverseLBlock();
			break;
			
		case 2:
			newShape = new UTSquareBlock();
			break;
			
		case 3:
			newShape = new UTSquiggly();
			break;
			
		case 4:
			newShape = new UTReverseSquiggly();
			break;
			
		case 5:
			newShape = new UTLinePiece();
			break;
			
		default:
			newShape = new UTTBlock();
		}
		
		return newShape;
	}
	
	public boolean checkPositionGood(UTShape shape)
	{
		for(UTShapeUnit unit : shape.getUnits())
		{
			if(unit.getGridX(shape.getCenterGridX()) >= UTWindow.GRID_WIDTH ||
					unit.getGridX(shape.getCenterGridX()) < 0 ||
					unit.getGridY(shape.getCenterGridY()) >= UTWindow.GRID_HEIGHT ||
					unit.getGridY(shape.getCenterGridY()) < 0)
			{
				return false;
			}
			
			if(gridUnits[unit.getGridY(shape.getCenterGridY())][unit.getGridX(shape.getCenterGridX())] != null &&
					gridUnits[unit.getGridY(shape.getCenterGridY())][unit.getGridX(shape.getCenterGridX())].parentShape == null)
				return false;
		}
		
		return true;
	}
	
	// returns true when the drop fails
	public void tryDrop()
	{
		UTShape droppedShape = currentShape.getShapeCopy(currentShape.getCenterGridX(), currentShape.getCenterGridY() + 1, false);
		
		if(checkPositionGood(droppedShape))
		{
			
			for(UTShapeUnit unit : currentShape.getUnits())
			{
				gridUnits[unit.getGridY(currentShape.getCenterGridY())][unit.getGridX(currentShape.getCenterGridX())] = null;
			}
			
			for(UTShapeUnit unit : currentShape.getUnits())
			{
				gridUnits[unit.getGridY(currentShape.getCenterGridY())+1][unit.getGridX(currentShape.getCenterGridX())] = unit;
			}
			
			currentShape.setCenterGrid(currentShape.getCenterGridX(), currentShape.getCenterGridY() + 1);
			
			return;
		}
		System.out.println("Stopping() returned true.");
		
		requestStateChange(GameState.STOPPING);
	}
	
	public void tryRotate()
	{
		UTShape rotatedShape = currentShape.getShapeCopy(currentShape.getCenterGridX(), currentShape.getCenterGridY(), true);
		
		if(checkPositionGood(rotatedShape))
		{	
			for(UTShapeUnit unit : currentShape.getUnits())
			{
				gridUnits[unit.getGridY(currentShape.getCenterGridY())][unit.getGridX(currentShape.getCenterGridX())] = null;
			}
			
			currentShape.rotate();
			
			for(UTShapeUnit unit : currentShape.getUnits())
			{
				gridUnits[unit.getGridY(currentShape.getCenterGridY())][unit.getGridX(currentShape.getCenterGridX())] = unit;
			}
		}
	}
	
	public void tryMove(int x_offset, int y_offset)
	{
		UTShape translatedShape = currentShape.getShapeCopy(currentShape.getCenterGridX() + x_offset, currentShape.getCenterGridY() + y_offset, false);
		
		if(checkPositionGood(translatedShape))
		{
			for(UTShapeUnit unit : currentShape.getUnits())
			{
				gridUnits[unit.getGridY(currentShape.getCenterGridY())][unit.getGridX(currentShape.getCenterGridX())] = null;
			}
			
			currentShape.setCenterGrid(currentShape.getCenterGridX() + x_offset, currentShape.getCenterGridY() + y_offset);
			
			for(UTShapeUnit unit : currentShape.getUnits())
			{
				gridUnits[unit.getGridY(currentShape.getCenterGridY())][unit.getGridX(currentShape.getCenterGridX())] = unit;
			}
		}
	}
	
	public boolean clearRow()
	{
		for(int y = 0; y < UTWindow.GRID_HEIGHT; y++)
		{				
			for(int x = 0; x < UTWindow.GRID_WIDTH; x++)
			{
				if(gridUnits[y][x] == null)
					break;
				
				if(x + 1 == UTWindow.GRID_WIDTH)
				{	
					for(int i = y; i > 0; i--)
						gridUnits[i] = gridUnits[i-1].clone();
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void onLose()
	{
		gameOver = true;

		if(gridFilledLines < gridUnits.length && System.currentTimeMillis() - millisLastGridFilledLine > 10)
		{
			millisLastGridFilledLine = System.currentTimeMillis();
			
			for(int x = 0; x < gridUnits[gridFilledLines].length; x++)
			{
				//gridUnits[gridFilledLines][x] = new UTShapeUnit(0,0,null,(int)(Math.random() * 7 + 1));
			}
			
			gridFilledLines++;
		}
		
		if(gridFilledLines >= gridUnits.length && System.currentTimeMillis() - millisLastGridFilledLine > 1000)
		{
			fillingGrid = false;
			requestStateChange(GameState.TO_MENU);
		}
	}
	
	private void onWin()
	{
		gameOver = true;
		
		if(System.currentTimeMillis() - millisLastDrop > 1000)
			requestStateChange(GameState.TO_MENU);
	}

	@Override
	public void onKeyEvent(UTInputEvent event) 
	{
		switch(event.data)
		{
		case Keyboard.KEY_UP:
			if(!upPressed && event.type.equals("KEY_PRESSED"))
			{
				upPressed = true;
				tryRotate();
			}
			else
				upPressed = false;
			break;
			
		case Keyboard.KEY_LEFT:
			if(!leftPressed && event.type.equals("KEY_PRESSED"))
			{
				leftPressed = true;
				requestStateChange(GameState.MOVING_LEFT);
			}
			else
				leftPressed = false;
			break;
		
		case Keyboard.KEY_RIGHT:
			if(!rightPressed && event.type.equals("KEY_PRESSED"))
			{
				rightPressed = true;
				requestStateChange(GameState.MOVING_RIGHT);
			}
			else
				rightPressed = false;
			break;
			
		case Keyboard.KEY_DOWN:
			if(!downPressed && event.type.equals("KEY_PRESSED"))
			{
				downPressed = true;
				millisLastDrop -= dropSpeed;
			}
			else
			{
				downPressed = false;
			}
			break;
			
		case Keyboard.KEY_ESCAPE:
			if(event.type.equals("KEY_PRESSED"))
				if(gameState.equals(GameState.DROPPING))
				{
					escapePressed = true;
					requestStateChange(GameState.PAUSED);
				}
				else
				{
					escapePressed = false;
					requestStateChange(GameState.DROPPING);
					millisLastDrop = System.currentTimeMillis();
				}
		}
	}
}
