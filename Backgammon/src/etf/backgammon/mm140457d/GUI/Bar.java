package etf.backgammon.mm140457d.GUI;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import etf.backgammon.mm140457d.game.Color;
import net.miginfocom.swing.MigLayout;

public class Bar extends JPanel {
	private ArrayList<GUIChecker> redCheckers = new ArrayList<GUIChecker>();
	private ArrayList<GUIChecker> whiteCheckers= new ArrayList<GUIChecker>();
	
	public Bar(){
		setLayout(new MigLayout("insets 0", "[50px,center]",
				"[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]"
				+ "0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]"));
	}
	
public void addChecker(GUIChecker c){
		c.setEnabled(true);
		if(c.getColor() == Color.RED){
			redCheckers.add(0, c);
			int pos = redCheckers.size();
			if(pos > 6)
				pos = 6;
			this.add(c, "cell 0 " + (pos-1));
		}
		else{
			whiteCheckers.add(0, c);
			int pos = whiteCheckers.size();
			this.add(c, "cell 0 " + (14 - pos));
		}
		c.setRowNum(-1);
	}

	public GUIChecker removeChecker(Color color){
		if(color == Color.RED)
			return redCheckers.remove(0);
		else
			return whiteCheckers.remove(0);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	public void empty() {
		for(int i = 0; i < redCheckers.size(); i++)
			this.remove(redCheckers.get(i));
		for(int i = 0; i < whiteCheckers.size(); i++)
			this.remove(whiteCheckers.get(i));
		redCheckers = new ArrayList<GUIChecker>();
		whiteCheckers = new ArrayList<GUIChecker>();
	}
}
