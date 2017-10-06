package etf.backgammon.mm140457d.game;

import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import etf.backgammon.mm140457d.GUI.Controler;
import etf.backgammon.mm140457d.GUI.GUIChecker;
import etf.backgammon.mm140457d.GUI.GameScreen;
import etf.backgammon.mm140457d.GUI.Row;
import etf.backgammon.mm140457d.players.CompPlayer;
import etf.backgammon.mm140457d.players.Node;
import etf.backgammon.mm140457d.players.Player;

public class GameLogic {
	private int[] gameTable = new int[24];
	private int whiteNumBoard = 15;
	private int whiteNumBar = 0;
	private int redNumBoard = 15;
	private int redNumBar = 0;
	private Player redPlayer;
	private Player whitePlayer;
	private Player currentPlayer;
	public int points;
	public GameScreen gameScreen;
	private Controler controler;
	private int diceTwo;
	private int diceOne = Integer.MIN_VALUE;
	private boolean repeat = false;
	public boolean dice1clicked = false;
	public boolean dice2clicked = false;
	public GUIChecker clicked = null;
	public int whiteWon = 0;
	public int redWon = 0;
	public boolean second = false;

	public GameLogic(Controler cont, int p) {
		points = p;
		controler = cont;
	}

	public void setRedPlayer(Player pl) {
		redPlayer = pl;
	}

	public void setWhitePlayer(Player pl) {
		whitePlayer = pl;
	}

	public int getWhiteBoard() {
		return whiteNumBoard;
	}

	public int getWhiteBar() {
		return whiteNumBar;
	}

	public int getRedBoard() {
		return redNumBoard;
	}

	public int getRedBar() {
		return redNumBar;
	}

	// proverava da li za zadate kocke postoji potez za trenutnog igraca
	public boolean checkForMoves(int a, int b, boolean colorWhite) {
		System.out.println("Usao u proveru za potez");
		// ako ima zetona na baru
		if ((colorWhite && whiteNumBar != 0) || (!colorWhite && redNumBar != 0)) {
			System.out.println("Usao u proveru za pomeraj sa bar-a");
			if (gameScreen.getDiceOne().isEnabled() && gameScreen.getDiceTwo().isEnabled()) {
				if (!checkMoveFromBar(a, colorWhite))
					return checkMoveFromBar(b, colorWhite);
				else
					return true;
			} else {
				if (gameScreen.getDiceOne().isEnabled())
					return checkMoveFromBar(a, colorWhite);
				else
					return checkMoveFromBar(b, colorWhite);
			}
		}
		// ako nema zetona na baru
		if (gameScreen.getDiceOne().isEnabled() && gameScreen.getDiceTwo().isEnabled()) {
			if (generateMoves(null, a, colorWhite, -1).isEmpty())
				return !generateMoves(null, b, colorWhite, -1).isEmpty();
			else
				return true;
		} else {
			if (gameScreen.getDiceOne().isEnabled())
				return !generateMoves(null, a, colorWhite, -1).isEmpty();
			else
				return !generateMoves(null, b, colorWhite, -1).isEmpty();
		}

	}

	// proverava da li za zadatu vrednost kocke i trenutnog igraca moze da se
	// pomeri zeton sa bar-a
	private boolean checkMoveFromBar(int x, boolean colorWhite) {
		if (colorWhite) {
			if (gameTable[24 - x] >= -1)
				return true;
			return false;
		} else {
			if (gameTable[x - 1] <= 1)
				return true;
			return false;
		}
	}

	// generise sve moguce poteze za zadatog igraca za zadato stanje igre i 1 kocku
	public ArrayList<int[]> generateMoves(int[] gameState, int num, boolean colorWhite, int numOnBoard) {
		if (gameState == null)
			gameState = gameTable;
		if (numOnBoard == -1)
			if (colorWhite)
				numOnBoard = whiteNumBoard;
			else
				numOnBoard = redNumBoard;
		ArrayList<int[]> list = new ArrayList<int[]>();
		int[] temp = null;
		for (int i = 0; i < 24; i++) {
			if (colorWhite)
				temp = whiteMoves(gameState, i, num, numOnBoard);
			else
				temp = redMoves(gameState, i, num, numOnBoard);
			if (temp != null)
				list.add(temp);
		}
		return list;
	}

	// generise novo stanje igre za zadati zeton,, vrednost kocke i trenutno
	// stanje igre za crvenog igraca
	public int[] redMoves(int[] gameState, int i, int num, int numOnBoard) {
		int[] temp = null;
		if (gameState == null)
			gameState = gameTable;
		if (numOnBoard == -1)
			numOnBoard = redNumBoard;
		// ako je zeton za koji proveravamo na baru
		if (i == -1) {
			if (gameState[num - 1] <= 1) {
				temp = new int[24];
				for (int j = 0; j < 24; j++)
					temp[j] = gameState[j];
				temp[num - 1]--;
				if (gameState[num - 1] == 1)
					temp[num - 1]--;

			}
			return temp;
		}
		// ako zeton pomeramo negde na tabli
		if (gameState[i] < 0 && (i + num < 24) && (gameState[i + num] <= 1)) {
			temp = setTemp(gameState, i, false);
			if (gameState[i + num] == 1)
				temp[i + num]--;
			temp[i + num]--;
		}
		// ako zeton izbacujemo sa table
		else if (gameState[i] < 0) {
			int count = 0;
			for (int j = 18; j < 24; j++)
				if (gameState[j] < 0)
					count += gameState[j];
			if ((-1) * count == numOnBoard) {
				if (i + num == 24)
					temp = setTemp(gameState, i, false);
				else if (i + num > 24) {
					int k = 24 - num;
					while (k < i && gameState[k] >= 0)
						k++;
					if (k == i)
						temp = setTemp(gameState, i, false);
				}
			}
		}
		return temp;
	}

	// generise novo stanje igre za zadati zeton,, vrednost kocke i trenutno
	// stanje igre za belog igraca
	public int[] whiteMoves(int[] gameState, int i, int num, int numOnBoard) {
		if (gameState == null)
			gameState = gameTable;
		if (numOnBoard == -1)
			numOnBoard = whiteNumBoard;
		int[] temp = null;
		// ako pomeramo zeton sa bara
		if (i == -1) {
			if (gameState[24 - num] >= -1) {
				temp = new int[24];
				for (int j = 0; j < 24; j++)
					temp[j] = gameState[j];
				temp[24 - num]++;
				if (gameState[24 - num] == -1)
					temp[24 - num]++;
			}
			return temp;
		}
		// ako pomeramo zeton po tabli
		if (gameState[i] > 0 && (i - num >= 0) && (gameState[i - num] >= -1)) {
			temp = setTemp(gameState, i, true);
			if (gameState[i - num] == -1)
				temp[i - num]++;
			temp[i - num]++;
		}
		// ako zeton izbacujemo sa table
		else if (gameState[i] > 0) {
			int count = 0;
			for (int j = 0; j < 6; j++)
				if (gameState[j] > 0)
					count += gameState[j];
			if (count == numOnBoard) {
				if (i - num == -1)
					temp = setTemp(gameState, i, true);
				else if (i - num < -1) {
					int k = num - 1;
					while (k > i && gameState[k] <= 0)
						k--;
					if (k == i)
						temp = setTemp(gameState, i, true);
				}

			}
		}
		return temp;
	}

	// formira novo stanje
	private int[] setTemp(int[] gameState, int i, boolean colorWhite) {
		int[] temp = new int[24];
		for (int j = 0; j < 24; j++)
			temp[j] = gameState[j];
		if (colorWhite)
			temp[i]--;
		else
			temp[i]++;
		return temp;
	}

	// radi update GUI table
	public void updateTable(int[] temp) {
		gameTable = temp;
		Row[] rows = gameScreen.getRows();
		GUIChecker removedPlayer = null;
		GUIChecker removedEnemy = null;
		int newPos = -1;
		// proverava gde su razlike izmedju GUI-a i modela stanja
		for (int i = 0; i < 24; i++) {
			if (Math.abs(gameTable[i]) < rows[i].getCheckerNum())
				if (rows[i].getCheckerColor() == currentPlayer.getColor())
					if (removedPlayer == null)
						removedPlayer = rows[i].removeChecker();
					else
						removedEnemy = rows[i].removeChecker();
			if (Math.abs(gameTable[i]) > rows[i].getCheckerNum()) {
				newPos = i;
			}
			if (removedEnemy == null && Math.abs(gameTable[i]) == rows[i].getCheckerNum()
					&& rows[i].getCheckerNum() != 0) {
				int num;
				if (rows[i].getCheckerColor() == etf.backgammon.mm140457d.game.Color.RED)
					num = -1 * rows[i].getCheckerNum();
				else
					num = rows[i].getCheckerNum();
				if (gameTable[i] != num) {
					removedEnemy = rows[i].removeChecker();
					newPos = i;
				}
			}
		}
		// zeton uklonjen sa table ili je pojeden protivnik
		if (newPos == -1 && removedPlayer != null) {
			if (removedEnemy == null) {
				// zeton uklonjen sa table
				if (removedPlayer.getColor() == etf.backgammon.mm140457d.game.Color.RED)
					redNumBoard--;
				else
					whiteNumBoard--;
				gameScreen.getOutBar().addChecker(removedPlayer);
			} else {
				// pojeden zeton
				newPos = removedEnemy.getRowNum();
			}
		}
		// update gde se sada nalazi pomereni zeton
		if (removedPlayer != null && newPos != -1)
			rows[newPos].addChecker(removedPlayer, 0);

		// ubacivanej zetona sa bara
		if (removedPlayer == null) {
			removedPlayer = gameScreen.getBar().removeChecker(currentPlayer.getColor());
			if (newPos == -1)
				newPos = removedEnemy.getRowNum();

			rows[newPos].addChecker(removedPlayer, 0);
			if (currentPlayer.getColor() == etf.backgammon.mm140457d.game.Color.RED)
				redNumBar--;
			else
				whiteNumBar--;
		}
		// pojedeni zeton protivnika se ubacuje na bar
		if (removedEnemy != null) {
			if (currentPlayer.getColor() == etf.backgammon.mm140457d.game.Color.RED)
				whiteNumBar++;
			else
				redNumBar++;
			gameScreen.getBar().addChecker(removedEnemy);
		}
		gameScreen.repaint();
	}

	public void startMatch() {
		gameTable = new int [24];
		redPlayer.stopPlaying();
		whitePlayer.stopPlaying();
		gameTable[0] = -2;
		gameTable[5] = 5;
		gameTable[7] = 3;
		gameTable[11] = -5;
		gameTable[12] = 5;
		gameTable[16] = -3;
		gameTable[18] = -5;
		gameTable[23] = 2;

		/*
		 * gameTable[5] = 1; gameTable[8] = 2; gameTable[12] = gameTable[15] =
		 * gameTable[23] = -1;
		 */

		gameScreen.getPlayingLabel().setForeground(java.awt.Color.RED);
		gameScreen.getPlayingLabel().setText("RED PLAYER'S TURN");
		gameScreen.disableCheckers();
		gameScreen.disableDices();
		gameScreen.enableButton();

		whiteNumBoard = 15;
		whiteNumBar = 0;
		redNumBoard = 15;
		redNumBar = 0;

		repeat = false;
		dice1clicked = false;
		dice2clicked = false;
		clicked = null;
		/* whiteNumBoard = 3; redNumBoard = 3; */

		redPlayer.throwDice();
	}

	// zapocinje novi potez
	public void game() {
		if (whiteNumBoard != 0 && redNumBoard != 0 && !pat()) {
			gameScreen.disableCheckers();
			//gameScreen.getDiceOne().setEnabled(false);
			//gameScreen.getDiceTwo().setEnabled(false);
			currentPlayer.throwDices();
		} else {
			String winner;
			if (whiteNumBoard == 0) {
				winner = "Player " + whitePlayerName() + " won this round!";
				whiteWon++;
				if (redNumBoard == 15)
					whiteWon++;
				int i = 0;
				while (i < 6 && gameTable[i] == 0)
					i++;
				if (i < 6 || redNumBar > 0)
					whiteWon++;
			} else if (redNumBoard == 0) {
				winner = "Player " + redPlayerName() + " won this round!";
				redWon++;
				if (whiteNumBoard == 15)
					redWon++;
				int i = 18;
				while (i < 24 && gameTable[i] == 0)
					i++;
				if (i < 24 || whiteNumBar > 0)
					redWon++;
			} else
				winner = "Pat position. No winners.";
			JOptionPane.showMessageDialog(gameScreen, "Game over!\n" + winner, "Backgammon", JOptionPane.PLAIN_MESSAGE);
			if (redWon < points && whiteWon < points) {
				int n = JOptionPane.showConfirmDialog(gameScreen,
						"Game is not over yet! \nNo one reached chosen number \nof points. Do you want to continue?",
						"Backgammon", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					controler.resetMatch();
				} else
					controler.finishGame();
			} else {
				Object[] options = { "Exit", "Go back" };
				int n = JOptionPane.showOptionDialog(gameScreen,
						"Game finnished! " + winner
								+ "\n Do you want to exit or go back to welcome screen?",
						"Backgammon", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (n == JOptionPane.YES_OPTION) {
					controler.finishGame();
				} else
					controler.startAllOver();
			}
		}
	}

	// zavrsava trenutni potez
	public void endOfMove() {

		dice1clicked = false;
		gameScreen.getDiceOne().setBorder(new EmptyBorder(5, 5, 5, 5));
		dice2clicked = false;
		gameScreen.getDiceTwo().setBorder(new EmptyBorder(5, 5, 5, 5));
		gameScreen.repaint();
		if(diceTwo == diceOne && !second){
			second = true;
			if (checkForMoves(diceOne, diceTwo, currentPlayer.getColor() == etf.backgammon.mm140457d.game.Color.WHITE)) {
				System.out.println("Ima poteza za kocke: " + diceOne + " " + diceTwo);
				currentPlayer.startTurn(false);
			} else {
				if (!(currentPlayer instanceof CompPlayer)) {
					System.out.print("Ispalo da nema poteza za kocke");
					JOptionPane.showMessageDialog(gameScreen, "No moves available.", "Backgammon",
							JOptionPane.PLAIN_MESSAGE);
				}
				endOfMove();
			}
			currentPlayer.startTurn(true);
			return;
		}
		if (currentPlayer.getColor() == etf.backgammon.mm140457d.game.Color.RED) {
			currentPlayer = whitePlayer;
			gameScreen.getPlayingLabel().setForeground(java.awt.Color.WHITE);
			gameScreen.getPlayingLabel().setText("WHITE PLAYER'S TURN");
		} else {
			currentPlayer = redPlayer;
			gameScreen.getPlayingLabel().setForeground(java.awt.Color.RED);
			gameScreen.getPlayingLabel().setText("RED PLAYER'S TURN");
		}
		second = false;
		game();
	}

	// proverava da li je doslo do pat pozicije
	private boolean pat() {
		if (gameTable[0] > 1 && gameTable[1] > 1 && gameTable[2] > 1 && gameTable[3] > 1 && gameTable[4] > 1
				&& gameTable[5] > 1)
			if (gameTable[23] < -1 && gameTable[22] < -1 && gameTable[21] < -1 && gameTable[20] < -1
					&& gameTable[19] < -1 && gameTable[18] < -1)
				if (redNumBar > 0 && whiteNumBar > 0)
					return true;
		return false;
	}

	// simulira bacanje kockica
	public void throwDices() {
		System.out.println("Usao u metodu");
		gameScreen.getDiceOne().setForeground(java.awt.Color.BLACK);
		gameScreen.getDiceTwo().setForeground(java.awt.Color.BLACK);
		int one = (int) (Math.random() * 6 + 1);
		int two = (int) (Math.random() * 6 + 1);
		if ((one == diceOne && two == diceTwo) || (one == diceTwo && two == diceOne)) {
			if (repeat) {
				diceOne = one / 2 + (int) (Math.random() * 6 + 1);
				if (diceOne > 6)
					diceOne -= 6;
				diceOne = (int) (Math.random() * 6 + 1) - two / 2;
				if (diceTwo < 1)
					diceOne += 6;
				repeat = false;
			}
			repeat = true;
		}
		diceOne = one;
		diceTwo = two;
		gameScreen.getDiceOne().setText("" + diceOne);
		gameScreen.getDiceTwo().setText("" + diceTwo);
		gameScreen.enableDices();
		if (checkForMoves(diceOne, diceTwo, currentPlayer.getColor() == etf.backgammon.mm140457d.game.Color.WHITE)) {
			System.out.println("Ima poteza za kocke: " + diceOne + " " + diceTwo);
			currentPlayer.startTurn(false);
		} else {
			if (!(currentPlayer instanceof CompPlayer)) {
				System.out.print("Ispalo da nema poteza za kocke");
				JOptionPane.showMessageDialog(gameScreen, "No moves available.", "Backgammon",
						JOptionPane.PLAIN_MESSAGE);
			}
			endOfMove();
		}
	}

	// simulira bacanje 1 kocke za odredjivanje koji igrac igra prvi
	public void diceDet() {
		if (diceOne == Integer.MIN_VALUE) {
			diceOne = (int) (Math.random() * 6 + 1);
			gameScreen.getPlayingLabel().setForeground(java.awt.Color.WHITE);
			gameScreen.getPlayingLabel().setText("WHITE PLAYER'S TURN");
			gameScreen.getDiceOne().setEnabled(true);
			gameScreen.getDiceOne().setForeground(java.awt.Color.RED);
			gameScreen.getDiceOne().setText("" + diceOne);
			whitePlayer.throwDice();
		} else {
			diceTwo = (int) (Math.random() * 6 + 1);
			gameScreen.getDiceTwo().setEnabled(true);
			gameScreen.getDiceTwo().setForeground(java.awt.Color.WHITE);
			gameScreen.getDiceTwo().setText("" + diceTwo);
			if (diceOne >= diceTwo) {
				currentPlayer = redPlayer;
				gameScreen.getPlayingLabel().setForeground(java.awt.Color.RED);
				gameScreen.getPlayingLabel().setText("RED PLAYER'S TURN");
			} else {
				currentPlayer = whitePlayer;
				gameScreen.getPlayingLabel().setForeground(java.awt.Color.WHITE);
				gameScreen.getPlayingLabel().setText("WHITE PLAYER'S TURN");
			}
			gameScreen.turnDetermined(true);
			game();
		}
	}

	public String whitePlayerName() {
		return whitePlayer.name();
	}

	public String redPlayerName() {
		return redPlayer.name();
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public int getdiceOne() {
		return diceOne;
	}

	public int getdiceTwo() {
		return diceTwo;
	}

	// podesava parametre za drugi potez Human igraca
	public void secondMove() {
		if(redNumBoard == 0 || whiteNumBoard == 0)
			game();
		if (dice1clicked) {
			gameScreen.getDiceOne().setBorder(new EmptyBorder(5, 5, 5, 5));
			dice1clicked = false;
			gameScreen.getDiceTwo().setBorder(new LineBorder(java.awt.Color.GREEN, 4));
			dice2clicked = true;
			gameScreen.getDiceOne().setEnabled(false);
			gameScreen.getDiceTwo().setEnabled(true);
		} else {
			gameScreen.getDiceTwo().setBorder(new EmptyBorder(5, 5, 5, 5));
			dice2clicked = false;
			gameScreen.getDiceTwo().setEnabled(false);
			gameScreen.getDiceOne().setEnabled(true);
			gameScreen.getDiceOne().setBorder(new LineBorder(java.awt.Color.GREEN, 4));
			dice1clicked = true;
		}
		gameScreen.enableCheckers();
		gameScreen.getHelpLabel().setText("Select a checker that you want to move for a value of other dice");
		if (checkForMoves(diceOne, diceTwo, currentPlayer.getColor() == etf.backgammon.mm140457d.game.Color.WHITE)) {
			System.out.print("Ima poteza za kocke");
		} else {
			System.out.print("Ispalo da nema poteza za kocke");
			JOptionPane.showMessageDialog(gameScreen, "No moves available.", "Backgammon", JOptionPane.PLAIN_MESSAGE);
			endOfMove();
		}

	}

	public void setGameScreen(GameScreen game) {
		gameScreen = game;
		redPlayer.setGameScreen(game);
		whitePlayer.setGameScreen(game);
	}

	public void removeClicked() {
		if (gameScreen.getDiceOne().isEnabled() && gameScreen.getDiceTwo().isEnabled()) {
			gameScreen.getDiceOne().setBorder(new EmptyBorder(5, 5, 5, 5));
			dice1clicked = false;
			dice2clicked = false;
			gameScreen.getDiceTwo().setBorder(new EmptyBorder(5, 5, 5, 5));
		}
	}

	public int currentPlayerBar() {
		if (currentPlayer.getColor() == Color.RED)
			return redNumBar;
		return whiteNumBar;
	}

	public int[] getCurrentState() {
		return gameTable;
	}

	public int getCurrentBar() {
		if (currentPlayer.getColor() == Color.WHITE)
			return whiteNumBar;
		return redNumBar;
	}

	public int getCurrentOut() {
		if (currentPlayer.getColor() == Color.WHITE)
			return 15 - whiteNumBoard;
		return 15 - redNumBoard;
	}
	
	public Node getTreeRoot(){
		if (whitePlayer instanceof CompPlayer)
			return ((CompPlayer)whitePlayer).getRoot();
		if(redPlayer instanceof CompPlayer)
			return ((CompPlayer)redPlayer).getRoot();
		return null;
	}

	public int getNextPlayerBar() {
		if (currentPlayer.getColor() != Color.WHITE)
			return whiteNumBar;
		return redNumBar;
	}

	public int getNextPlayerOut() {
		if (currentPlayer.getColor() != Color.WHITE)
			return 15 - whiteNumBoard;
		return 15 - redNumBoard;
	}

	public void disablePlayers() {
		whitePlayer.interrupt();
		redPlayer.interrupt();
		
	}

	public Color compPlayerColor() {
		if (whitePlayer instanceof CompPlayer)
			return whitePlayer.getColor();
		if(redPlayer instanceof CompPlayer)
			return redPlayer.getColor();
		return null;
	}
}
