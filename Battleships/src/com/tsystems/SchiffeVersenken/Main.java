package com.tsystems.SchiffeVersenken;

import java.awt.Image;

import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		// TODO: Create JFrame in the view
		SwingUtilities.invokeLater(() -> {
//			JFrame.setDefaultLookAndFeelDecorated(true);
			JFrame frame = new JFrame("Battleships");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			// Model erstellen
			GameModel model = GameModel.getInstance();
			
			// View ertsellen
			GameBoard gameboard = GameBoard.getInstance();
			frame.add(gameboard);
			Image backgroundImage = new ImageIcon("gamePanel.jpg").getImage();
			gameboard.setBackgroundImage(backgroundImage);
			Image iconImage = new ImageIcon("icon.png").getImage();
            frame.setIconImage(iconImage);
			
			// Controller erstellen
			GameController controller = new GameController();
			gameboard.setController(controller);
			
			frame.pack();
			frame.setSize(600, 700);
			frame.setVisible(true);
		});
	}
}
