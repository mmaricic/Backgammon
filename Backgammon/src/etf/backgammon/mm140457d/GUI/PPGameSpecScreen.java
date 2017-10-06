package etf.backgammon.mm140457d.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.awt.event.ActionEvent;

public class PPGameSpecScreen extends GameSpecScreen {
	private JLabel lblUsername2;
	private JTextField textFieldUsername2;
	private JButton btnStart;


	public PPGameSpecScreen(Controler cont) {
		super(cont);
		getContentPane().add(getLblUsername2(), "cell 1 3");
		getContentPane().add(getTextFieldUsername2(), "cell 2 3, growx");
		getContentPane().add(getBtnStart(), "cell 2 6");

	}

	private JLabel getLblUsername2() {
		if (lblUsername2 == null) {
			lblUsername2 = new JLabel("USERNAME:");
			lblUsername2.setFont(new Font("Times New Roman", Font.BOLD, 12));
		}
		return lblUsername2;
	}
	
	private JTextField getTextFieldUsername2() {
		if (textFieldUsername2 == null) {
			textFieldUsername2 = new JTextField();
			textFieldUsername.setText("redPlayer");
			textFieldUsername2.setText("whitePlayer");
			textFieldUsername2.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			textFieldUsername2.setColumns(10);
		}
		return textFieldUsername2;
	}
	private JButton getBtnStart() {
		if (btnStart == null) {
			btnStart = new JButton("START");
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					controler.start2playerGame(textFieldUsername.getText(), textFieldUsername2.getText(), getSelectedButtonText(radioGroup));
				}
			});
			btnStart.setFont(new Font("Times New Roman", Font.BOLD, 14));
		}
		return btnStart;
	}
	
	
}
