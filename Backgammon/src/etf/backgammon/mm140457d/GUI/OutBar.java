package etf.backgammon.mm140457d.GUI;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import etf.backgammon.mm140457d.game.Color;
import net.miginfocom.swing.MigLayout;

public class OutBar extends JPanel {
	private ArrayList<GUIChecker> redCheckers = new ArrayList<GUIChecker>();
	private ArrayList<GUIChecker> whiteCheckers = new ArrayList<GUIChecker>();

	public OutBar() {
		setLayout(new MigLayout("insets 0", "[55px,center][55px,center]",
				"[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]"
						+ "0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]"));
	}

	public void addChecker(GUIChecker c) {
		c.setEnabled(false);
		if (c.getColor() == Color.RED) {
			redCheckers.add(c);
				int pos = redCheckers.size();
				if (pos > 7)
					pos = pos - 7;
				this.add(c, "cell " + redCheckers.size() / 8 + " " + (pos - 1));
		} else {
			whiteCheckers.add(c);
				int pos = whiteCheckers.size();
				if (pos > 7)
					pos = pos - 7;
				this.add(c, "cell " + whiteCheckers.size() / 8 + " " + (14 - pos));
		}
		c.setRowNum(-1);
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
