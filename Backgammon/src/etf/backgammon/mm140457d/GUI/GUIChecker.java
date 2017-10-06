package etf.backgammon.mm140457d.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import etf.backgammon.mm140457d.game.GameLogic;

public class GUIChecker extends JPanel {
	private int size = 46;
	private int pos;
	private GameLogic gameLogic;
	private boolean onTop;
	private int rowNum;
	private etf.backgammon.mm140457d.game.Color color;
	private boolean clicked = false;
	private GUIChecker gui;
	private Color borderColor = Color.BLACK;

	public boolean getOnTop() {
		return onTop;
	}

	public void setOnTop(boolean ot) {
		onTop = ot;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int v) {
		rowNum = v;
	}

	public etf.backgammon.mm140457d.game.Color getColor() {
		return color;
	}

	public void setClicked(boolean b) {
		clicked = b;
		if (b)
			borderColor = Color.GREEN;
		else
			borderColor = Color.BLACK;
		repaint();
	}

	public GUIChecker(boolean ot, int rn, etf.backgammon.mm140457d.game.Color color, int p, GameLogic gs) {
		onTop = ot;
		rowNum = rn;
		this.color = color;
		pos = p;
		gameLogic = gs;

		setPreferredSize(new Dimension(50, 50));
		setOpaque(false);
		setBackground(Color.GREEN);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (isEnabled()) {
					clickedChecker();
				}
				//System.out.println("Zeton: " + (rowNum + 1) + " " + pos + " " + onTop);
			}
		});
	}

	private void clickedChecker() {
		//System.out.print("ENABLED ");
		if (gameLogic.dice1clicked)
			callMakeAMove(gameLogic.gameScreen.getDiceTwo(), gameLogic.getdiceOne());

		else if (gameLogic.dice2clicked)
			callMakeAMove(gameLogic.gameScreen.getDiceOne(), gameLogic.getdiceTwo());

		else {
			if (gameLogic.clicked != null)
				gameLogic.clicked.setClicked(false);
			setClicked(true);
			gameLogic.clicked = this;
		}
	}

	private void callMakeAMove(JLabel checkingDice, int callingDice) {
		boolean entry;
		if (checkingDice.isEnabled())
			entry = true;
		else
			entry = false;
		gameLogic.clicked = this;
		gameLogic.getCurrentPlayer().makeAMove(this, callingDice, entry);
	}

	public void setPosition(int pos) {
		this.pos = pos;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		
		if (color == etf.backgammon.mm140457d.game.Color.RED)
			g2.setColor(Color.RED);
		else
			g2.setColor(Color.WHITE);

		g2.fillOval(1, 0, size, size);
		g2.setColor(borderColor);
		if (borderColor == Color.GREEN) {
			g2.setStroke(new BasicStroke(5));
			g2.drawOval(3, 2, size-4, size-4);
		}
		else{
			g2.setStroke(new BasicStroke(2));
			g2.drawOval(1, 0, size, size);
		}
		
	}
}
