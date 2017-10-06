package etf.backgammon.mm140457d.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Forest;
import etf.backgammon.mm140457d.game.GameLogic;
import etf.backgammon.mm140457d.players.MinMaxNode;
import etf.backgammon.mm140457d.players.Node;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.MultipleGradientPaint.ColorSpaceType;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.border.LineBorder;

public class GameScreen extends JFrame {

	private JPanel contentPane;
	private Controler controler;
	private JPanel gamePanel;
	private JPanel helpPanel;
	private JPanel LabelPanel;
	private JLabel playingLabel;
	private JLabel helpLabel;
	private JTextPane textPaneRed;
	private JTextPane textPaneWhite;
	private JButton DiceButton;
	private JLabel diceOne;
	private JLabel diceTwo;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private Bar bar;
	private OutBar outBar;
	private Row[] rowPanel = new Row[24];
	private GameLogic gameLogic;

	private boolean turnDet = false;
	private JButton treeButton;

	public GameScreen(Controler cont, GameLogic gl) {
		setTitle("Backgammon");
		gameLogic = gl;
		controler = cont;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int quit = JOptionPane.showConfirmDialog(contentPane, "Are you sure you want to exit the game?", "EXIT",
						JOptionPane.YES_NO_OPTION);
				if (quit == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});

		setBounds(100, 100, 1020, 740);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getGamePanel(), BorderLayout.CENTER);
		contentPane.add(getHelpPanel(), BorderLayout.WEST);
		contentPane.add(getLabelPanel(), BorderLayout.NORTH);

		setGame();
		if (gameLogic.compPlayerColor() != null)
			addTreeButton(gameLogic.compPlayerColor());
	}

	private void addTreeButton(etf.backgammon.mm140457d.game.Color compColor) {
		if (compColor == etf.backgammon.mm140457d.game.Color.RED)
			helpPanel.add(getTreeButton(), "cell 0 1");
		else
			helpPanel.add(getTreeButton(), "cell 0 5");
	}

	private JButton getTreeButton() {
		if (treeButton == null) {
			treeButton = new JButton();
			treeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (treeButton.isEnabled()) {
						if(gameLogic.getTreeRoot() != null)
							treeButtonAction();
					}
				}
			});
			treeButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
			treeButton.setPreferredSize(new Dimension(40, 30));
			treeButton.setText("Expectimax tree");
			treeButton.setEnabled(false);
		}
		return treeButton;
	}

	private void treeButtonAction() {
		JFrame frame = new JFrame();
		Container content = frame.getContentPane();
		frame.setTitle("Backgammon - Tree visualisation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content.add(new GraphicTree(gameLogic.getTreeRoot(), this));
		//content.add(new TreeCollapseDemo(this, gameLogic.getTreeRoot()));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private Row getRow(int i, int dir) {
		if (rowPanel[i] == null) {
			rowPanel[i] = new Row(dir, i);

		}
		return rowPanel[i];
	}

	private JPanel getGamePanel() {
		if (gamePanel == null) {
			gamePanel = new JPanel();
			gamePanel.setBackground(Color.LIGHT_GRAY);
			gamePanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
			gamePanel.setLayout(new MigLayout("insets 0", "[]0[]0[]0[]", "[grow]"));
			gamePanel.add(getLeftPanel(), "cell 0 0,grow");
			gamePanel.add(getRightPanel(), "cell 2 0,grow");
			gamePanel.add(getBar(), "cell 1 0");
			gamePanel.add(getOutBar(), "cell 3 0");
			gamePanel.setPreferredSize(new Dimension(840, 700));
			// gamePanel.setMaximumSize(new Dimension(770, 700));
		}
		return gamePanel;
	}

	public OutBar getOutBar() {
		if (outBar == null) {
			outBar = new OutBar();
			outBar.setBackground(Color.DARK_GRAY);
			outBar.setPreferredSize(new Dimension(100, 700));
		}
		return outBar;
	}

	public Bar getBar() {
		if (bar == null) {
			bar = new Bar();
			bar.setBackground(Color.black);
			bar.setPreferredSize(new Dimension(70, 700));
		}
		return bar;
	}

	private JPanel getHelpPanel() {
		if (helpPanel == null) {
			helpPanel = new JPanel();
			helpPanel.setPreferredSize(new Dimension(200, 700));
			helpPanel.setLayout(new MigLayout("insets 0", "[150.00,grow,center]", "[grow][][53.00][56.00][grow][]"));
			helpPanel.add(getTextPaneWhite(), "cell 0 4");
			helpPanel.add(getDiceButton(), "cell 0 3");
			helpPanel.add(getTextPaneRed(), "cell 0 0");
			helpPanel.add(getDiceOne(), "flowx,cell 0 2");
			helpPanel.add(getDiceTwo(), "cell 0 2");
			helpPanel.setBackground(Color.LIGHT_GRAY);
		}
		return helpPanel;
	}

	private JPanel getLabelPanel() {
		if (LabelPanel == null) {
			LabelPanel = new JPanel();
			LabelPanel.setLayout(new MigLayout("insets 0", "[919.00,grow,fill]", "[][]"));
			LabelPanel.add(getPlayingLabel(), "cell 0 0");
			LabelPanel.add(getHelpLabel(), "cell 0 1");
			LabelPanel.setBackground(Color.LIGHT_GRAY);
		}
		return LabelPanel;
	}

	public JLabel getPlayingLabel() {
		if (playingLabel == null) {
			playingLabel = new JLabel("Ko je na potezu");
			// lblNewLabel.setForeground(Color.RED);
			playingLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
			playingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return playingLabel;
	}

	public JLabel getHelpLabel() {
		if (helpLabel == null) {
			helpLabel = new JLabel("pomoc, sta treba da uradi");
			helpLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 22));
			helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
			helpLabel.setForeground(Color.BLACK);
			helpLabel.setBackground(Color.GREEN);
			helpLabel.setOpaque(true);
		}
		return helpLabel;
	}

	private JTextPane getTextPaneRed() {
		if (textPaneRed == null) {
			textPaneRed = new JTextPane();
			textPaneRed.setEditable(false);
			textPaneRed.setBorder(new LineBorder(new Color(255, 0, 0), 4, true));
			textPaneRed.setFont(new Font("Times New Roman", Font.PLAIN, 17));
			textPaneRed.setBackground(Color.LIGHT_GRAY);

			updateRedPane();
		}
		return textPaneRed;
	}

	private JTextPane getTextPaneWhite() {
		if (textPaneWhite == null) {
			textPaneWhite = new JTextPane();
			textPaneWhite.setEditable(false);
			textPaneWhite.setBorder(new LineBorder(new Color(255, 255, 255), 4, true));
			textPaneWhite.setFont(new Font("Times New Roman", Font.PLAIN, 17));
			textPaneWhite.setBackground(Color.LIGHT_GRAY);

			updateWhitePane();
		}
		return textPaneWhite;
	}

	private JButton getDiceButton() {
		if (DiceButton == null) {
			DiceButton = new JButton("Roll the dices!");
			DiceButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (DiceButton.isEnabled()) {
						if (!turnDet){
							gameLogic.diceDet();
							helpLabel.setOpaque(false);
						}
						else {
							diceOne.setEnabled(false);
							diceTwo.setEnabled(false);
							gameLogic.throwDices();
						}

					}
				}
			});
			DiceButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
			DiceButton.setPreferredSize(new Dimension(50, 35));
		}
		return DiceButton;
	}

	public JLabel getDiceOne() {
		if (diceOne == null) {
			diceOne = new JLabel("1");
			diceOne.setEnabled(false);
			diceOne.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (diceOne.isEnabled())
						if (gameLogic.clicked != null) {
							boolean entry;
							if (diceTwo.isEnabled())
								entry = true;
							else
								entry = false;
							diceOne.setBorder(new LineBorder(java.awt.Color.GREEN, 4));
							gameLogic.dice1clicked = true;
							gameLogic.getCurrentPlayer().makeAMove(gameLogic.clicked,
									Integer.parseInt(diceOne.getText()), entry);
						} else {
							diceTwo.setBorder(new EmptyBorder(5, 5, 5, 5));
							gameLogic.dice2clicked = false;
							diceOne.setBorder(new LineBorder(java.awt.Color.GREEN, 4));
							gameLogic.dice1clicked = true;
						}
				}
			});
			diceOne.setBackground(new Color(245, 222, 179));
			diceOne.setPreferredSize(new Dimension(45, 45));
			diceOne.setHorizontalAlignment(SwingConstants.CENTER);
			diceOne.setFont(new Font("Times New Roman", Font.BOLD, 28));
			diceOne.setOpaque(true);
		}
		return diceOne;
	}

	public JLabel getDiceTwo() {
		if (diceTwo == null) {
			diceTwo = new JLabel("6");
			diceTwo.setEnabled(false);
			diceTwo.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (diceTwo.isEnabled())
						if (gameLogic.clicked != null) {
							boolean entry;
							if (diceOne.isEnabled())
								entry = true;
							else
								entry = false;
							diceTwo.setBorder(new LineBorder(java.awt.Color.GREEN, 4));
							gameLogic.dice2clicked = true;
							gameLogic.getCurrentPlayer().makeAMove(gameLogic.clicked,
									Integer.parseInt(diceTwo.getText()), entry);
						} else {
							diceOne.setBorder(new EmptyBorder(5, 5, 5, 5));
							gameLogic.dice1clicked = false;
							diceTwo.setBorder(new LineBorder(java.awt.Color.GREEN, 4));
							gameLogic.dice2clicked = true;
						}
				}
			});
			diceTwo.setHorizontalAlignment(SwingConstants.CENTER);
			diceTwo.setBackground(new Color(245, 222, 179));
			diceTwo.setFont(new Font("Times New Roman", Font.BOLD, 28));
			diceTwo.setOpaque(true);
			diceTwo.setPreferredSize(new Dimension(45, 45));
		}
		return diceTwo;
	}

	private JPanel getLeftPanel() {
		if (leftPanel == null) {
			leftPanel = new JPanel();
			leftPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
			leftPanel.setBackground(new Color(245, 222, 179));
			leftPanel.setPreferredSize(new Dimension(360, 700));
			leftPanel.setLayout(
					new MigLayout("insets 0", "[grow]0[grow]0[grow]0[grow]0[grow]0[grow]", "[grow,top]0[grow,bottom]"));
			for (int i = 0; i < 6; i++) {
				leftPanel.add(getRow(i + 12, 0), "cell " + i + " 0");
				leftPanel.add(getRow(11 - i, 1), "cell " + i + " 1");
			}

		}
		return leftPanel;
	}

	private JPanel getRightPanel() {
		if (rightPanel == null) {
			rightPanel = new JPanel();
			rightPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
			rightPanel.setBackground(new Color(245, 222, 179));
			rightPanel.setPreferredSize(new Dimension(360, 700));
			rightPanel.setLayout(
					new MigLayout("insets 0", "[grow]0[grow]0[grow]0[grow]0[grow]0[grow]", "[grow,top]0[grow,bottom]"));
			for (int i = 0; i < 6; i++) {
				rightPanel.add(getRow(i + 18, 0), "cell " + i + " 0");
				rightPanel.add(getRow(5 - i, 1), "cell " + i + " 1");
			}

		}
		return rightPanel;
	}

	private void setGame() { // ORIGINALNI
		rowPanel[0].addChecker(new GUIChecker(false, 0, etf.backgammon.mm140457d.game.Color.RED, 1, gameLogic), 1);
		rowPanel[0].addChecker(new GUIChecker(true, 0, etf.backgammon.mm140457d.game.Color.RED, 2, gameLogic), 2);
		rowPanel[23].addChecker(new GUIChecker(false, 23, etf.backgammon.mm140457d.game.Color.WHITE, 1, gameLogic), 1);
		rowPanel[23].addChecker(new GUIChecker(true, 23, etf.backgammon.mm140457d.game.Color.WHITE, 2, gameLogic), 2);

		for (int i = 1; i < 4; i++) {
			rowPanel[16].addChecker(new GUIChecker(false, 16, etf.backgammon.mm140457d.game.Color.RED, i, gameLogic), i);
			rowPanel[7].addChecker(new GUIChecker(false, 7, etf.backgammon.mm140457d.game.Color.WHITE, i, gameLogic), i);
			if (i == 3) {
				rowPanel[16].getTopChecker().setOnTop(true);
				rowPanel[7].getTopChecker().setOnTop(true);
			}
		}
		for (int i = 1; i < 6; i++) {
			rowPanel[11].addChecker(new GUIChecker(false, 11, etf.backgammon.mm140457d.game.Color.RED, i, gameLogic), i);
			rowPanel[5].addChecker(new GUIChecker(false, 5, etf.backgammon.mm140457d.game.Color.WHITE, i, gameLogic), i);
			rowPanel[18].addChecker(new GUIChecker(false, 18, etf.backgammon.mm140457d.game.Color.RED, i, gameLogic), i);
			rowPanel[12].addChecker(new GUIChecker(false, 12, etf.backgammon.mm140457d.game.Color.WHITE, i, gameLogic), i);
			System.out.println("i = " + i);
			if (i == 5) {
				rowPanel[11].getTopChecker().setOnTop(true);
				rowPanel[5].getTopChecker().setOnTop(true);
				rowPanel[18].getTopChecker().setOnTop(true);
				rowPanel[12].getTopChecker().setOnTop(true);
			}
		}
	}

	public Row[] getRows() {
		return rowPanel;
	}

	public void setGameLogic(GameLogic gl) {
		gameLogic = gl;
	}

	public void disableButton() {
		DiceButton.setEnabled(false);
	}

	public void disableDices() {
		if (gameLogic.dice1clicked)
			diceOne.setEnabled(false);
		if (gameLogic.dice2clicked)
			diceTwo.setEnabled(false);
	}

	public void disableCheckers() {
		for (int i = 0; i < 24; i++)
			rowPanel[i].setEnableCheckers(false);
	}

	public void enableButton() {
		DiceButton.setEnabled(true);
	}

	public void enableDices() {
		diceOne.setEnabled(true);
		diceTwo.setEnabled(true);
	}

	public void enableCheckers() {
		for (int i = 0; i < 24; i++)
			rowPanel[i].setEnableCheckers(true);
	}

	public void turnDetermined(boolean b) {
		turnDet = b;

	}

	public void restart() {
		for (int i = 0; i < 24; i++)
			rowPanel[i].empty();
		turnDet = false;
		bar.empty();
		outBar.empty();
		updateRedPane();
		updateWhitePane();
		setGame();
		diceOne.setBorder(new EmptyBorder(5, 5, 5, 5));
		diceTwo.setBorder(new EmptyBorder(5, 5, 5, 5));
		diceOne.setEnabled(false);
		diceTwo.setEnabled(false);

	}

	private void updateWhitePane() {
		try {
			textPaneWhite.setText("Player: \n");
			StyledDocument doc = textPaneWhite.getStyledDocument();
			Style style = textPaneWhite.addStyle("I'm a Style", null);

			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			StyleConstants.setForeground(style, Color.white);
			doc.insertString(doc.getLength(), gameLogic.whitePlayerName(), style);
			StyleConstants.setForeground(style, Color.black);
			doc.insertString(doc.getLength(), "\nColor: \n", style);
			StyleConstants.setForeground(style, Color.white);
			doc.insertString(doc.getLength(), "White\n", style);
			StyleConstants.setForeground(style, Color.black);
			doc.insertString(doc.getLength(), "Match points: \n", style);
			StyleConstants.setForeground(style, Color.white);
			doc.insertString(doc.getLength(), gameLogic.whiteWon + "/" + gameLogic.points, style);
			StyleConstants.setForeground(style, Color.black);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
			int curr = doc.getLength();
			doc.insertString(doc.getLength(), "\nMoving direction:\n -----------\n |\n |\n ---------->", style);
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_LEFT);
			doc.setParagraphAttributes(curr + 3, doc.getLength(), center, false);
		} catch (BadLocationException e) {
		}

	}

	private void updateRedPane() {
		try {
			textPaneRed.setText("Player: \n");
			StyledDocument doc = textPaneRed.getStyledDocument();
			Style style = textPaneRed.addStyle("I'm a Style", null);
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			StyleConstants.setForeground(style, Color.red);
			doc.insertString(doc.getLength(), gameLogic.redPlayerName(), style);
			StyleConstants.setForeground(style, Color.black);
			doc.insertString(doc.getLength(), "\nColor: \n", style);
			StyleConstants.setForeground(style, Color.red);
			doc.insertString(doc.getLength(), "Red\n", style);
			StyleConstants.setForeground(style, Color.black);
			doc.insertString(doc.getLength(), "Match points: \n", style);
			StyleConstants.setForeground(style, Color.red);
			doc.insertString(doc.getLength(), gameLogic.redWon + "/" + gameLogic.points, style);
			StyleConstants.setForeground(style, Color.black);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);

			int curr = doc.getLength();
			doc.insertString(doc.getLength(), "\nMoving direction:\n ---------->\n |\n |\n -----------", style);
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_LEFT);
			doc.setParagraphAttributes(curr + 3, doc.getLength(), center, false);
		} catch (BadLocationException e) {
		}
	}

	public void showGraphicTree(Forest<Node, Branch> tree, Node root) {
		if (root == null)
			return;
		if(root == gameLogic.getTreeRoot())
			tree.addVertex(root);
			for (Node child : root.getChildren()) {
				if (child != null) {
					tree.addEdge(new Branch(child.getDiceOne(), child.getDiceTwo()), root, child);
					tree.addVertex(child);
				}
			}
	}
	
	public void TreeButtonEnabled(boolean b){
		treeButton.setEnabled(b);
	}
}
