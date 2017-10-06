package etf.backgammon.mm140457d.players;

import java.util.ArrayList;

import javax.swing.JFrame;

import etf.backgammon.mm140457d.GUI.GameScreen;
import etf.backgammon.mm140457d.game.Color;
import etf.backgammon.mm140457d.game.GameLogic;

public class CompPlayer extends Player {
	private MinMaxNode expRoot;
	private int treeDepth;
	private int maxNodes;

	public CompPlayer(Color cl, String name, GameLogic gl, int td) {
		super(cl, name, gl);
		treeDepth = td;
		start();
		if (td == 4)
			maxNodes = 2;
		if (td == 3)
			maxNodes = 6;
	}

	public void startTurn(boolean second) {
		gameScreen.TreeButtonEnabled(false);
		synchronized (this) {

			playing = true;
			notifyAll();
		}
	}

	// generise sva moguca stanja za zadate kocke i trenutno stanje i to stavlja
	// kao decu za cvor
	private void formPartOfTree(int[] currState, int dice1, int dice2, MinMaxNode parent, int depth,
			boolean colorWhite) {
		ArrayList<int[]> moves;
		int[] move;
		int[] newMove;
		Type type;
		if (depth == treeDepth)
			type = Type.TERMINAL;
		else
			type = Type.CHANCE;
		// ako nema zetona na baru, generise poteze za prvu kocku i prosledjuje
		// dalje
		if (gameLogic.currentPlayerBar() == 0) {
			moves = gameLogic.generateMoves(currState, dice1, colorWhite, 15 - parent.outMe + parent.outThisMove);
			for (int[] state : moves) {
				// parent.updateStatistic(state, checkerColor == Color.WHITE);
				stageTwoTreeFormation(state, dice1, dice2, type, parent, colorWhite, depth, false);
			}
		} else {
			if (!colorWhite)
				move = gameLogic.redMoves(currState, -1, dice1, 15 - parent.outMe + parent.outThisMove);
			else
				move = gameLogic.whiteMoves(currState, -1, dice1, 15 - parent.outMe + parent.outThisMove);
			if (move != null) {
				parent.barMe--;
				// ako ih ima jos na baru i crveni je igrac
				if (!colorWhite && gameLogic.getRedBar() - 1 > 0) {
					newMove = gameLogic.redMoves(move, -1, dice2, 15 - parent.outMe + parent.outThisMove);
					if (newMove != null) {
						parent.barMe--;
						Node child = new Node(type, newMove, parent.barEnemy + parent.barEnemyThisMove, parent.barMe,
								parent.outEnemy, parent.outMe + parent.outThisMove);
						child.updateStatistic(newMove, colorWhite);
						child.setValue(heuristic(child, depth));
						child.setFirstState(move);
						parent.addChild(child, 0);
					} else {
						Node child = new Node(type, move, parent.barEnemy + parent.barEnemyThisMove, parent.barMe,
								parent.outEnemy, parent.outMe + parent.outThisMove);
						parent.addChild(child, 0);
						child.updateStatistic(move, colorWhite);
						child.setValue(heuristic(child, depth));

					}
				} // ako ih ima jos na baru a beli je igrac
				else if (colorWhite && gameLogic.getWhiteBar() - 1 > 0) {
					newMove = gameLogic.whiteMoves(move, -1, dice2, 15 - parent.outMe + parent.outThisMove);
					if (newMove != null) {
						parent.barMe--;
						Node child = new Node(type, newMove, parent.barEnemy + parent.barEnemyThisMove, parent.barMe,
								parent.outEnemy, parent.outMe + parent.outThisMove);
						child.updateStatistic(newMove, colorWhite);
						child.setValue(heuristic(child, depth));
						child.setFirstState(move);
						parent.addChild(child, 0);
					} else {
						Node child = new Node(type, move, parent.barEnemy + parent.barEnemyThisMove, parent.barMe,
								parent.outEnemy, parent.outMe + parent.outThisMove);
						parent.addChild(child, 0);
						child.updateStatistic(move, colorWhite);
						child.setValue(heuristic(child, depth));
					}
				} // nema vise na baru
				else
					stageTwoTreeFormation(move, dice1, dice2, type, parent, colorWhite, depth, true);

			}
		}
	}

	// generise stanja za 2. kocku, formira cvorove i ubacuje ih u stablo
	private void stageTwoTreeFormation(int[] state, int dice1, int dice2, Type type, Node parent, boolean colorWhite,
			int depth, boolean barMoved) {
		ArrayList<int[]> newMoves;
		int count = 0;
		for (int i = 0; i < 24; i++)
			if (colorWhite) {
				if (state[i] > 0)
					count += state[i];
			} else 
				if (state[i] < 0)
				count += state[i];
		if(!colorWhite)
			count = (-1)*count;
		if(barMoved)
			--count;
		
		newMoves = gameLogic.generateMoves(state, dice2, colorWhite, count + parent.barMe);
		if (newMoves.isEmpty()) {
			Node child = new Node(type, state, parent.barEnemy + parent.barEnemyThisMove, parent.barMe, parent.outEnemy,
					parent.outMe + parent.outThisMove);
			child.updateStatistic(state, colorWhite);
			child.setValue(heuristic(child, depth));
			// ovde ne moze da se desi da se duplira stanje(odigrano samo za 1
			// kocku)
			ArrayList<Node> temp = parent.getChildren();
			int i = 0;
			while (i < temp.size() && temp.get(i).getValue() < child.getValue())
				i++;
			parent.addChild(child, i);
		} else
			for (int[] newState : newMoves) {
				Node child = new Node(type, newState, parent.barEnemy + parent.barEnemyThisMove, parent.barMe,
						parent.outEnemy, parent.outMe + parent.outThisMove);
				child.updateStatistic(newState, colorWhite);
				child.setValue(heuristic(child, depth));
				child.setFirstState(state);
				int i = 0;
				ArrayList<Node> temp = parent.getChildren();
				while (i < temp.size() && temp.get(i).getValue() < child.getValue())
					i++;
				while (i < temp.size() && temp.get(i).getValue() == child.getValue()
						&& !child.sameAs(temp.get(i)))
					i++;

				if ((i < temp.size() && !child.sameAs(temp.get(i))) || i == temp.size())
					parent.addChild(child, i);
			}
	}

	// generise stablo rekurzivno
	private void generateTree(Node root, int depth) {
		if (depth > treeDepth)
			return;
		Type type;
		boolean colorWhite;
		if (depth % 2 == 0) {
			type = Type.MIN;
			colorWhite = !(checkerColor == Color.WHITE);
		} else {
			type = Type.MAX;
			colorWhite = checkerColor == Color.WHITE;
		}

		// za svaku mogucu kombinaciju kocaka pravi chance cvor kome dodeljuje
		// decu koja su svi moguci potezi za te kocke
		int i = 0;
		for (int dice1 = 1; dice1 < 7; dice1++)
			for (int dice2 = dice1; dice2 < 7; dice2++) {
				MinMaxNode child = new MinMaxNode(type, root.getFinalState(), dice1, dice2, root.barMe,
						root.barEnemy + root.barEnemyThisMove, root.outMe + root.outThisMove, root.outEnemy);
				root.addChild(child, i);
				i++;
				formPartOfTree(root.getFinalState(), dice1, dice2, child, depth, colorWhite);
				formPartOfTree(root.getFinalState(), dice2, dice1, child, depth, colorWhite);
				if (treeDepth > 2 && child.getChildren().size() > maxNodes)
					while (child.getChildren().size() > maxNodes)
						if (type == Type.MIN)
							child.getChildren().remove(child.getChildren().size() - 1);
						else
							child.getChildren().remove(0);
				if (depth < treeDepth) {
					for (Node node : child.getChildren())
						generateTree(node, depth + 1);
				}
			}
	}

	// rekurzivno odredjivanje tezine cvorova
	private void expectimax(int currentDepth, Node root) {
		double min = Double.MAX_VALUE;
		double max = Integer.MIN_VALUE;
		//System.out.println("Dubina: " + currentDepth + ", tip: " + root.getType() + "ID: " + root.hashCode());
		if (currentDepth == treeDepth && root.getType() == Type.TERMINAL) {
			root.setValue(heuristic(root, treeDepth));
			return;
		}
		double val = 0;

		if (root.getType() == Type.CHANCE) {
			root.setValue(0);
			for (Node node : root.getChildren()) {
				expectimax(currentDepth + 1, (MinMaxNode) node);
				val = node.getValue() / 18.0;
				if (((MinMaxNode) node).dicesEqual())
					val = val / 2;
				root.setValue(root.getValue() + val);
			}
			return;
		} else {
			for (Node node : root.getChildren()) {
				expectimax(currentDepth, node);
				if ((root.getType() == Type.MIN) && (node.getValue() <= min)) {
					min = node.getValue();
					root.setValue(min);
					((MinMaxNode) root).setChosen(node);
				} else if ((root.getType() == Type.MAX) && (node.getValue() >= max)) {
					max = node.getValue();
					root.setValue(max);
					((MinMaxNode) root).setChosen(node);
				}
			}
			//if(root != expRoot)
				//root.setFirstState(((MinMaxNode)root).getChosen().getFinalState());
			return;
		}
	}

	private double heuristic(Node root, int treeDepth) {
		boolean colorWhite;
		if (treeDepth % 2 == 0) {
			colorWhite = !(checkerColor == Color.WHITE);
		} else
			colorWhite = checkerColor == Color.WHITE;
		if(root.getHomeNum(colorWhite) + root.outMe+root.outThisMove == 15)
			return 5*root.outThisMove;
		double value = 1.5*root.getHomeNum(colorWhite) - root.getHomeNum(!colorWhite) + 0.75 * root.section2num(colorWhite)
				+ 2 * (root.barEnemy - root.barMe) + root.getEnemyHomeNum(colorWhite) + 5* (root.barEnemyThisMove + root.outThisMove);
		if (treeDepth % 2 == 0)
			value = -1 * value;
		return value;
	}

	@Override
	public void throwDices() {
		gameScreen.disableButton();
		gameScreen.disableCheckers();
		gameScreen.disableButton();
		if (gameLogic.clicked != null)
			gameLogic.clicked.setClicked(false);
		gameLogic.clicked = null;
		System.out.println("Komp baca kocke");
		gameLogic.throwDices();
	}

	@Override
	public void throwDice() {
		gameScreen.getHelpLabel().setText("");
		gameScreen.turnDetermined(false);
		gameLogic.diceDet();

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

	public void play() {
		gameScreen.disableCheckers();
		gameScreen.getDiceOne().setEnabled(false);
		gameScreen.getDiceTwo().setEnabled(false);
		playing = false;
		gameScreen.getHelpLabel().setText("Computer is determining best move.");
		int depth = 1;
		// napravi koren i prvi nivo
		expRoot = new MinMaxNode(Type.MAX, gameLogic.getCurrentState(), gameLogic.getdiceOne(), gameLogic.getdiceTwo(),
				gameLogic.getNextPlayerBar(), gameLogic.getCurrentBar(), gameLogic.getNextPlayerOut(),
				gameLogic.getCurrentOut());
		System.out.println("Stvorio root");
		formPartOfTree(null, gameLogic.getdiceOne(), gameLogic.getdiceTwo(), expRoot, depth,
				checkerColor == Color.WHITE);
		if (gameLogic.getdiceOne() != gameLogic.getdiceTwo())
			formPartOfTree(null, gameLogic.getdiceTwo(), gameLogic.getdiceOne(), expRoot, depth,
					checkerColor == Color.WHITE);
		System.out.println("Stvorio prvi nivo");
		// ako ima vise od 1 nivoa, poziva generisanje stabla
		if (depth != treeDepth) {
			if (treeDepth > 2 && expRoot.getChildren().size() > maxNodes)
				while (expRoot.getChildren().size() > maxNodes)
					expRoot.getChildren().remove(0);
			ArrayList<Node> children = expRoot.getChildren();
			for (Node node : children)
				generateTree(node, depth + 1);
		}
		if(expRoot.getHomeNum(checkerColor == Color.WHITE) + expRoot.outMe == 15)
			expRoot.setChosen(expRoot.getChildren().get(expRoot.getChildren().size()-1));
		else
			expectimax(1, expRoot);
		// update-uje tablu za izabrano stanje
		
		if (expRoot.getChosen() == null)
			System.out.println("GOVNO NE RADI");
		if (expRoot.getChosen().getFirstState() != null){
			gameLogic.updateTable(expRoot.getChosen().getFirstState());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		gameLogic.updateTable(expRoot.getChosen().getFinalState());
		//expRoot = null;
		gameScreen.TreeButtonEnabled(true);
		gameLogic.endOfMove();
	}

	public Node getRoot() {
		return expRoot;
	}
}
