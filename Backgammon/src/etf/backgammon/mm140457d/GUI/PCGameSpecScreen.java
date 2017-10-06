package etf.backgammon.mm140457d.GUI;

import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.TextField;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;

public class PCGameSpecScreen extends GameSpecScreen {
	private JLabel lblCheckerColor;
	private JLabel lblTreeDepth;
	private JButton btnStart;
	private JRadioButton rdbtnRed;
	private JRadioButton rdbtnWhite;
	private ButtonGroup colorRadioGroup; 
	private JRadioButton depth2;
	private JRadioButton depth3;
	private JRadioButton depth4;
	private ButtonGroup depthRadioGroup;
	/**
	 * Create the frame.
	 */
	public PCGameSpecScreen(Controler cont) {
		super(cont);
		getContentPane().add(getLblCheckerColor(), "cell 1 3");
		getContentPane().add(getRdbtnRed(), "flowx,cell 2 3,alignx center");
		getContentPane().add(getLblTreeDepth(), "cell 1 5");
		getContentPane().add(getDepth2(), "flowx,cell 2 5");
		getContentPane().add(getBtnStart(), "cell 2 6,alignx center");
		getContentPane().add(getRdbtnWhite(), "cell 2 3,alignx center");
		getContentPane().add(getDepth3(), "cell 2 5,aligny top");
		getContentPane().add(getDepth4(), "cell 2 5");
		setColorRadioGroup();
		setDepthRadioGroup();

	}

	private void setDepthRadioGroup() {
		depthRadioGroup = new ButtonGroup();
		depthRadioGroup.add(depth2);
		depthRadioGroup.add(depth3);
		depthRadioGroup.add(depth4);
		
	}

	private void setColorRadioGroup() {
		colorRadioGroup = new ButtonGroup();
		colorRadioGroup.add(rdbtnRed);
		colorRadioGroup.add(rdbtnWhite);
		
	}

	private JLabel getLblCheckerColor() {
		if (lblCheckerColor == null) {
			lblCheckerColor = new JLabel("CHECKER COLOR:");
			lblCheckerColor.setFont(new Font("Times New Roman", Font.BOLD, 12));
		}
		return lblCheckerColor;
	}
	private JLabel getLblTreeDepth() {
		if (lblTreeDepth == null) {
			lblTreeDepth = new JLabel("TREE DEPTH:");
			lblTreeDepth.setFont(new Font("Times New Roman", Font.BOLD, 12));
		}
		return lblTreeDepth;
	}
	private JButton getBtnStart() {
		if (btnStart == null) {
			btnStart = new JButton("START");
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controler.startCompGame(textFieldUsername.getText(), getSelectedButtonText(colorRadioGroup), getSelectedButtonText(radioGroup), getSelectedButtonText(depthRadioGroup));
				}
			});
			btnStart.setFont(new Font("Times New Roman", Font.BOLD, 14));
		}
		return btnStart;
	}
	private JRadioButton getRdbtnRed() {
		if (rdbtnRed == null) {
			rdbtnRed = new JRadioButton("RED");
			rdbtnRed.setSelected(true);
			rdbtnRed.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		}
		return rdbtnRed;
	}
	private JRadioButton getRdbtnWhite() {
		if (rdbtnWhite == null) {
			rdbtnWhite = new JRadioButton("WHITE");
			rdbtnWhite.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		}
		return rdbtnWhite;
	}
	private JRadioButton getDepth2() {
		if (depth2 == null) {
			depth2 = new JRadioButton("2");
			depth2.setSelected(true);
		}
		return depth2;
	}
	private JRadioButton getDepth3() {
		if (depth3 == null) {
			depth3 = new JRadioButton("3");
		}
		return depth3;
	}
	private JRadioButton getDepth4() {
		if (depth4 == null) {
			depth4 = new JRadioButton("4");
		}
		return depth4;
	}
}
