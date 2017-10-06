package etf.backgammon.mm140457d.players;

import java.text.DecimalFormat;
import java.util.ArrayList;

import etf.backgammon.mm140457d.game.Color;

public class Node {
	protected ArrayList<Node> children;
	protected Type type;
	protected double value;
	private int[] finalState;
	protected int[] firstState = null;
	protected int barEnemy;
	protected int barMe; // menja se tokom poteza
	protected int outEnemy;
	protected int outMe;
	protected int outThisMove = 0; // menja se tokom poteza
	protected int barEnemyThisMove = 0; // menja se tokom poteza
	private String text = "";
	
	public Node(Type type, int[] state, int barEnemy, int barMe, int outEnemy, int outMe) {
		this.type = type;
		finalState = state;
		children = new ArrayList<Node>();
		this.barEnemy = barEnemy;
		this.barMe = barMe;
		this.outEnemy = outEnemy;
		this.outMe = outMe;
	}

	public Type getType() {
		return type;
	}

	public void setText(){
		if(type == Type.CHANCE)
			text = "CHANCE";
		else{
		text=" [";
		for(int i = 0; i < 24; i++)
			text+=finalState[i]+",";
		text+="]";
		}
	}
	
	public void hideText(){
		text = "";
	}
	public double getValue() {
		return value;
	}

	public void setValue(double d) {
		value = d;
	}

	public void addChild(Node child, int i) {
		children.add(i, child);
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public int[] getFinalState() {
		return finalState;
	}

	public int[] getFirstState() {
		return firstState;
	}

	public void setFirstState(int[] state) {
		firstState = state;
	}

	public void updateStatistic(int[] state, boolean colorWhite) {
		int whiteCount = 0;
		int redCount = 0;
		for (int i = 0; i < 24; i++) {
			if (state[i] > 0)
				whiteCount += state[i];
			else
				redCount += state[i];
		}
		if (colorWhite) {
			if (type == Type.MIN) {
				barEnemyThisMove = (15 - outEnemy - barEnemy) - whiteCount;
				outThisMove = (15 - outMe - barMe) + redCount;
			} else {
				barEnemyThisMove = (15 - outEnemy - barEnemy) + redCount;
				outThisMove = (15 - outMe - barMe) - whiteCount;
			}
		} else {
			if (type == Type.MIN) {
				barEnemyThisMove = (15 - outEnemy - barEnemy) + redCount;
				outThisMove = (15 - outMe - barMe) - whiteCount;
			} else {
				barEnemyThisMove = (15 - outEnemy - barEnemy) - whiteCount;
				outThisMove = (15 - outMe - barMe) + redCount;
			}
		}
	}

	public int getHomeNum(boolean colorWhite) {
		int count = 0;
		if (colorWhite) {
			for (int i = 0; i < 6; i++)
				if (finalState[i] > 0)
					count += finalState[i];
		} else {
			for (int i = 18; i < 24; i++)
				if (finalState[i] < 0)
					count += finalState[i];
		}
		return Math.abs(count);
	}

	public double section2num(boolean colorWhite) {
		int count = 0;
		if (colorWhite) {
			for (int i = 6; i < 12; i++)
				if (finalState[i] > 0)
					count += finalState[i];
		} else {
			for (int i = 12; i < 18; i++)
				if (finalState[i] < 0)
					count += finalState[i];
		}
		return Math.abs(count);
	}

	public boolean sameAs(Node node) {
		if (barEnemy == node.barEnemy && barMe == node.barMe && outEnemy == node.outEnemy && outMe == node.outMe
				&& outThisMove == node.outThisMove && barEnemyThisMove == node.barEnemyThisMove) {
			for (int i = 0; i < 24; i++)
				if (node.finalState[i] != finalState[i])
					return false;
			return true;
		}
		return false;
	}

	public double getEnemyHomeNum(boolean colorWhite) {
		int count = 0;
		if (!colorWhite) {
			for (int i = 0; i < 6; i++)
				if (finalState[i] < 0)
					count += (-1*finalState[i])*((i-6)/1.5);
		} else {
			for (int i = 23; i > 17; i--)
				if (finalState[i] > 0)
					count += finalState[i]*((17-i)/1.5);
		}
		return count;
	}

	public int getDiceOne() {
		return 0;
	}

	public int getDiceTwo() {
		return 0;
	}
	
	public String toString(){
		DecimalFormat df = new DecimalFormat("##.00");
		
		return String.format("%.2f", value)+text;
	}
}
