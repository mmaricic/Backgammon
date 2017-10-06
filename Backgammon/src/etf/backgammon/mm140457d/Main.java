package etf.backgammon.mm140457d;

import java.awt.EventQueue;

import etf.backgammon.mm140457d.GUI.Controler;
import etf.backgammon.mm140457d.GUI.WelcomeScreen;

public class Main {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeScreen welcome = new WelcomeScreen();
					welcome.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
