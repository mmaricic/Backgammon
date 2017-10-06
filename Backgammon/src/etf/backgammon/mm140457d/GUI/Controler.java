package etf.backgammon.mm140457d.GUI;

import javax.swing.JOptionPane;

import etf.backgammon.mm140457d.game.Color;
import etf.backgammon.mm140457d.game.GameLogic;
import etf.backgammon.mm140457d.players.*;

public class Controler {

	private WelcomeScreen welcome;
	private GameSpecScreen gameSpec;
	private GameScreen game;
	private GameLogic gameLogic;

	public Controler(WelcomeScreen ws){
		welcome = ws;
	}

	public void twoPlayersGameSpec() {
		if(gameSpec == null)
			gameSpec = new PPGameSpecScreen(this);
		welcome.setVisible(false);
		gameSpec.setVisible(true);
	}

	public void CompGameSpec() {
		if(gameSpec == null)
			gameSpec = new PCGameSpecScreen(this);
		welcome.setVisible(false);
		gameSpec.setVisible(true);

	}

	public void start2playerGame(String p1, String p2, String points) {
		
		gameLogic = new GameLogic(this, Integer.parseInt(points));
		gameLogic.setRedPlayer(new HumanPlayer(Color.RED, p1, gameLogic));
		gameLogic.setWhitePlayer(new HumanPlayer(Color.WHITE, p2, gameLogic));
		game = new GameScreen(this, gameLogic);
		gameLogic.setGameScreen(game);
		game.setVisible(true);
		gameSpec.setVisible(false);
		gameLogic.startMatch();
	}

	public void startCompGame(String username, String color, String points, String depth) {
		
		gameLogic = new GameLogic(this, Integer.parseInt(points));
		if (color.equals("RED")) {
			gameLogic.setRedPlayer(new HumanPlayer(Color.RED, username, gameLogic));
			gameLogic.setWhitePlayer(new CompPlayer(Color.WHITE, "Computer", gameLogic, Integer.parseInt(depth)));
		} else {
			gameLogic.setWhitePlayer(new HumanPlayer(Color.WHITE, username, gameLogic));
			gameLogic.setRedPlayer(new CompPlayer(Color.RED, "Computer", gameLogic,Integer.parseInt(depth)));
		}
		game = new GameScreen(this, gameLogic);
		gameLogic.setGameScreen(game);
		game.setVisible(true);
		gameSpec.dispose();
		gameLogic.startMatch();
	}

	public void resetMatch() {
		game.restart();
		game.repaint();
		gameLogic.startMatch();
		
	}

	public void finishGame() {
		game.dispose();
		welcome.dispose();
		gameSpec.dispose();
		gameLogic.disablePlayers();
		gameLogic = null;
		System.exit(0);
	}

	public void startAllOver() {
		welcome.setVisible(true);
		game.dispose();
		gameLogic = null;
	}
}
