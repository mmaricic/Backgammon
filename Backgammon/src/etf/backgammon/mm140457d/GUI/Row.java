package etf.backgammon.mm140457d.GUI;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.GridLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;

public class Row extends JPanel {
	int dir;
	int rowNum;
	private ArrayList<GUIChecker> checkers = new ArrayList<GUIChecker>();

	public Row(int dir, int rn) {
		this.dir = dir;
		rowNum = rn;
		setOpaque(false);
		setPreferredSize(new Dimension(60, 350));
		setLayout(new MigLayout("insets 0", "[60px,center]",
				"[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]0[50px,center]"));
	}

	public void addChecker(GUIChecker c, int pos) {
		c.setRowNum(rowNum);
		c.setOnTop(true);
		c.setEnabled(true);
		if (!checkers.isEmpty())
			checkers.get(0).setOnTop(false);
		checkers.add(0, c);
		int d;
		if (pos == 0)
			pos = checkers.size();
		if (dir == 0)
			d = pos - 1;
		else if (checkers.size() > 7)
			d = pos - 1;
		else
			d = 7 - pos;
		this.add(c, "cell 0 " + d);
	}

	public GUIChecker removeChecker() {
		GUIChecker c;
		c = checkers.remove(0);
		if (!checkers.isEmpty())
			checkers.get(0).setOnTop(true);

		return c;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(153, 51, 0));
		g.drawPolygon(new int[] { 0, 60, 30 }, new int[] { 350 * dir, 350 * dir, 75 * (3 - 2 * dir) + 50 * (1 - dir) },
				3);
		g.fillPolygon(new int[] { 0, 60, 30 }, new int[] { 350 * dir, 350 * dir, 75 * (3 - 2 * dir) + 50 * (1 - dir) },
				3);
	}

	public GUIChecker getTopChecker() {
		return checkers.get(0);
	}

	public int getCheckerNum() {
		return checkers.size();
	}

	public etf.backgammon.mm140457d.game.Color getCheckerColor() {
		return checkers.get(0).getColor();
	}

	public void setEnableCheckers(boolean b) {
		for (GUIChecker c : checkers)
			c.setEnabled(b);

	}

	public void empty() {
		for(int i = 0; i < checkers.size(); i++)
			this.remove(checkers.get(i));
		checkers = new ArrayList<GUIChecker>();
		
	}
}
