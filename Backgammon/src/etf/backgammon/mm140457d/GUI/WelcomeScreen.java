package etf.backgammon.mm140457d.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WelcomeScreen extends JFrame {

	private Controler controler;
	private JPanel contentPane;
	private JButton pVSpButton;
	private JButton pVScButton;

	
	public WelcomeScreen() {
		controler = new Controler(this);
		setTitle("Backgammon");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int quit  = JOptionPane.showConfirmDialog(contentPane, "Are you sure you want to exit the game?", "EXIT", JOptionPane.YES_NO_OPTION);
				if (quit == JOptionPane.YES_OPTION)	
					System.exit(0);
			}
		});
		
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow][grow][grow]"));
		contentPane.add(getPVSpButton(), "cell 1 1,alignx center,aligny center");
		contentPane.add(getPVScButton(), "cell 1 2,alignx center,aligny top");
	}
	
	
	private JButton getPVSpButton() {
		if (pVSpButton == null) {
			pVSpButton = new JButton("Player VS Player");
			pVSpButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					controler.twoPlayersGameSpec();
				}
			});
			pVSpButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
			pVSpButton.setPreferredSize(new Dimension(200, 50));
			
		}
		return pVSpButton;
	}
	private JButton getPVScButton() {
		if (pVScButton == null) {
			pVScButton = new JButton("Player VS Computer");
			pVScButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controler.CompGameSpec();
				}
			});
			pVScButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
			pVScButton.setPreferredSize(new Dimension(200, 50));
		}
		return pVScButton;
	}
}
