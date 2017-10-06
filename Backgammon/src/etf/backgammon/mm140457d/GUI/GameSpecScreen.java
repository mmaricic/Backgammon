package etf.backgammon.mm140457d.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.JRadioButton;

public abstract class GameSpecScreen extends JFrame {

	protected JPanel contentPane;
	protected JLabel lblUsername1;
	protected JTextField textFieldUsername;
	protected JLabel lblPoints;
	
	protected Controler controler;
	protected JRadioButton radioButton1;
	protected JRadioButton radioButton3;
	protected JRadioButton radioButton5;
	protected JRadioButton radioButton7;
	protected JRadioButton radioButton11;
	protected JRadioButton radioButton15;
	protected ButtonGroup radioGroup;


	/**
	 * Create the frame.
	 */
	public GameSpecScreen(Controler cont) {
		
		controler = cont; 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("Backgammon");
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][grow,right][center][grow]", "[grow][grow][grow][grow][grow][][grow][grow]"));
		contentPane.add(getLblUsername1(), "cell 1 2,alignx trailing");
		contentPane.add(getTextFieldUsername(), "cell 2 2,growx");
		contentPane.add(getLblPoints(), "cell 1 4,alignx trailing");
		contentPane.add(getRadioButton1(), "flowx,cell 2 4");
		contentPane.add(getRadioButton3(), "cell 2 4");
		contentPane.add(getRadioButton5(), "cell 2 4");
		contentPane.add(getRadioButton7(), "cell 2 4");
		contentPane.add(getRadioButton11(), "cell 2 4");
		contentPane.add(getRadioButton15(), "cell 2 4");
		makeRadioGruop();
	}

	private void makeRadioGruop() {
		radioGroup = new ButtonGroup();
		radioGroup.add(radioButton1);
		radioGroup.add(radioButton3);
		radioGroup.add(radioButton5);
		radioGroup.add(radioButton7);
		radioGroup.add(radioButton11);
		radioGroup.add(radioButton15);
	}

	private JLabel getLblUsername1() {
		if (lblUsername1 == null) {
			lblUsername1 = new JLabel("USERNAME:");
			lblUsername1.setFont(new Font("Times New Roman", Font.BOLD, 12));
		}
		return lblUsername1;
	}
	private JTextField getTextFieldUsername() {
		if (textFieldUsername == null) {
			textFieldUsername = new JTextField();
			textFieldUsername.setText("player1");
			textFieldUsername.setFont(new Font("Times New Roman", Font.PLAIN, 12));
			textFieldUsername.setColumns(10);
		}
		return textFieldUsername;
	}
	private JLabel getLblPoints() {
		if (lblPoints == null) {
			lblPoints = new JLabel("POINTS:");
			lblPoints.setFont(new Font("Times New Roman", Font.BOLD, 12));
		}
		return lblPoints;
	}
	private JRadioButton getRadioButton1() {
		if (radioButton1 == null) {
			radioButton1 = new JRadioButton("1");
			radioButton1.setSelected(true);
		}
		return radioButton1;
	}
	private JRadioButton getRadioButton3() {
		if (radioButton3 == null) {
			radioButton3 = new JRadioButton("3");
		}
		return radioButton3;
	}
	private JRadioButton getRadioButton5() {
		if (radioButton5 == null) {
			radioButton5 = new JRadioButton("5");
		}
		return radioButton5;
	}
	private JRadioButton getRadioButton7() {
		if (radioButton7 == null) {
			radioButton7 = new JRadioButton("7");
		}
		return radioButton7;
	}
	private JRadioButton getRadioButton11() {
		if (radioButton11 == null) {
			radioButton11 = new JRadioButton("11");
		}
		return radioButton11;
	}
	private JRadioButton getRadioButton15() {
		if (radioButton15 == null) {
			radioButton15 = new JRadioButton("15");
		}
		return radioButton15;
	}
	
	protected String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }
}
