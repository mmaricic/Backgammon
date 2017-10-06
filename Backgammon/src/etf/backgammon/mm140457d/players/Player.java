package etf.backgammon.mm140457d.players;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import etf.backgammon.mm140457d.GUI.GUIChecker;
import etf.backgammon.mm140457d.GUI.GameScreen;
import etf.backgammon.mm140457d.game.GameLogic;



public abstract class Player extends Thread{
	protected etf.backgammon.mm140457d.game.Color checkerColor;
	protected GameScreen gameScreen;
	protected String name;
	protected GameLogic gameLogic;
	protected boolean playing = false;
	
	public Player(etf.backgammon.mm140457d.game.Color cl, String n, GameLogic gl){
		checkerColor = cl;
		name = n;
		gameLogic = gl;
	}
	
	public abstract void throwDice();
	
	public abstract void startTurn(boolean second);
	
	public void makeAMove(GUIChecker checker, int diceNum, boolean firstEntry){
		gameLogic.clicked.setClicked(false);
		gameLogic.clicked = null;
		gameScreen.disableCheckers();
	}
	
	public etf.backgammon.mm140457d.game.Color getColor(){
		return checkerColor;
	}

	public abstract void throwDices(); 
	
	@Override
	public abstract void run();

	public void setGameScreen(GameScreen game) {
		gameScreen = game;
		
	}
	
	public String name(){
		return name;
	}
	
	public void stopPlaying(){
		playing = false;
	}
}
