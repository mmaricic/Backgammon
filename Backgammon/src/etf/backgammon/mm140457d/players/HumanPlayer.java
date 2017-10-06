package etf.backgammon.mm140457d.players;

import javax.swing.JFrame;

import etf.backgammon.mm140457d.GUI.GUIChecker;
import etf.backgammon.mm140457d.GUI.GameScreen;
import etf.backgammon.mm140457d.game.Color;
import etf.backgammon.mm140457d.game.GameLogic;

public class HumanPlayer extends Player {

	private boolean firstEntry;
	private GUIChecker movingChecker;
	private int diceNum;

	public HumanPlayer(Color cl, String name, GameLogic gl) {
		super(cl, name, gl);
		start();
	}

	@Override
	public void throwDices() {
		gameScreen.enableButton();
		gameScreen.getHelpLabel().setText("Click on a button to throw dices");
		gameScreen.getHelpLabel().setForeground(java.awt.Color.BLACK);

	}

	@Override
	public void startTurn(boolean second) {
		gameScreen.enableCheckers();
		gameScreen.enableDices();
		gameScreen.disableButton();
		gameScreen.getHelpLabel().setForeground(java.awt.Color.BLACK);
		if(second)
			gameScreen.getHelpLabel()
			.setText("You get to move again! Click on a dice and then click on a checker to move it for a selected number");
		else
		gameScreen.getHelpLabel()
				.setText("Click on a dice and then click on a checker to move it for a selected number");
	}

	@Override
	public void makeAMove(GUIChecker checker, int diceNum, boolean firstEntry) {
		synchronized (this) {

			playing = true;
			notifyAll();
		}
		movingChecker = checker;
		this.diceNum = diceNum;
		this.firstEntry = firstEntry;
	}

	@Override
	public void throwDice() {
		gameScreen.getHelpLabel().setText("Throw a dice to determine who's playing first");
		gameScreen.getHelpLabel().setForeground(java.awt.Color.BLACK);
		gameScreen.getHelpLabel().setOpaque(true);
		gameScreen.turnDetermined(false);
	}

	@Override
	public void run() {
		while (!interrupted()) {
			try {
				synchronized (this) {

					while (!playing)
						wait();
				}
				play();
			} catch (InterruptedException e) {
			}
		}

	}

	private void play() {
		playing = false;
		super.makeAMove(movingChecker, diceNum, firstEntry);
		//provera pomera protivnika
		if (movingChecker.getColor() != checkerColor) {
			gameScreen.getHelpLabel().setText("You can't move enemies checkers! Try again.");
			gameScreen.getHelpLabel().setForeground(java.awt.Color.YELLOW);
			gameLogic.removeClicked();
			gameScreen.enableCheckers();
			return;
		}
		//provera pomera zeton koji nije na vrhu
		if (movingChecker.getOnTop() == false) {
			gameScreen.getHelpLabel().setText("You can't move a checker that's not on top! Try again.");
			gameScreen.getHelpLabel().setForeground(java.awt.Color.YELLOW);
			gameLogic.removeClicked();
			gameScreen.enableCheckers();
			return;
		}
		//provera pomera zeton sa table a ima ih na baru
		if ((checkerColor == Color.WHITE && (gameLogic.getWhiteBar() > 0) && (movingChecker.getRowNum() != -1))
				|| (checkerColor == Color.RED && (gameLogic.getRedBar() > 0) && (movingChecker.getRowNum() != -1))) {
			gameScreen.getHelpLabel().setText("You must move checker from bar first!");
			gameScreen.getHelpLabel().setForeground(java.awt.Color.YELLOW);
			gameLogic.removeClicked();
			gameScreen.enableCheckers();
			return;
		}
		int[] temp;
		//potez
		if (checkerColor == Color.WHITE) {
			temp = gameLogic.whiteMoves(null, movingChecker.getRowNum(), diceNum, -1);
		} else {
			temp = gameLogic.redMoves(null, movingChecker.getRowNum(), diceNum, -1);
		}
		if (temp != null) {
			gameLogic.updateTable(temp);
			if (firstEntry) {
				gameScreen.getHelpLabel().setText("Move made. Choose other dice and a new checker to be moved.");
				gameScreen.getHelpLabel().setForeground(java.awt.Color.BLACK);
				gameLogic.secondMove();
			} else {
				gameScreen.getHelpLabel().setText("End of move.");
				gameScreen.getHelpLabel().setForeground(java.awt.Color.BLACK);
				gameScreen.disableDices();
				gameLogic.removeClicked();
				gameLogic.endOfMove();
			}
		} else {
			gameScreen.enableCheckers();
			gameLogic.removeClicked();
			gameScreen.getHelpLabel().setText("You can't move selected checker on a chosen position. Try again.");
			gameScreen.getHelpLabel().setForeground(java.awt.Color.YELLOW);
		}

	}

}
