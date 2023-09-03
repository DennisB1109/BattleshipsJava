package com.tsystems.SchiffeVersenken;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;

public class GameBoard extends JPanel {
	private Image backgroundImage;
	
	public JTextField namePlayer1;
	public JTextField namePlayer2;
	private JButton startButton;
	public JTextField inputCoordinates;
	public JButton setF125Button;
	public JButton setF124Button;
	public JButton setF123Button;
	public JButton setK130Button;
	private JButton bombButton;
	private JButton airstrikeButton;
	private JButton setAllShips;
	private JButton shootButton;
	private JButton nextButton;
	private GameController controller;
	
	boolean disableBombShotPlayer1 = false;
	boolean disableAirstrikeShotPlayer1 = false;
	boolean disableBombShotPlayer2 = false;
	boolean disableAirstrikeShotPlayer2 = false;
	
	GameModel myModel = GameModel.getInstance();
//	GameController controller = GameController.getInstance();
	
	JTextArea outputField;
	
	private static GameBoard instance;
	
	public void setBackgroundImage(Image image) {
        this.backgroundImage = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
	
	private GameBoard() {
		// TODO: Change to Singleton
		
		startButton = new JButton("Spiel Starten");
		startButton.setPreferredSize(new Dimension(150, 50));
		startButton.addActionListener(e -> controller.startGame());
		add(startButton);
		add(new JLabel("                                                               Enter Player Names (optional)                                                               "));
		namePlayer1 = new JTextField("Name Player 1", 15);
		namePlayer2 = new JTextField("Name Player 2", 15);
		add(namePlayer1);
		add(namePlayer2);
		
		ImageIcon k125Icon = new ImageIcon("F125.png");
		setF125Button = new JButton("F125");
		setF125Button.setIcon(k125Icon);
		setF125Button.setPreferredSize(new Dimension(70, 50));
		setF125Button.addActionListener(e -> controller.addShipToPlayer(new Ship("F125", 5)));
		
		ImageIcon k124Icon = new ImageIcon("F124.png");
		setF124Button = new JButton("F124");
		setF124Button.setIcon(k124Icon);
		setF124Button.setPreferredSize(new Dimension(70, 50));
		setF124Button.addActionListener(e -> controller.addShipToPlayer(new Ship("F124", 4)));
		
		ImageIcon k123Icon = new ImageIcon("F123.png");
		setF123Button = new JButton("F123");
		setF123Button.setIcon(k123Icon);
		setF123Button.setPreferredSize(new Dimension(70, 50));
		setF123Button.addActionListener(e -> controller.addShipToPlayer(new Ship("F123", 3)));
		
		ImageIcon k130Icon = new ImageIcon("K130.png");
		setK130Button = new JButton("K130");
		setK130Button.setIcon(k130Icon);
		setK130Button.setPreferredSize(new Dimension(70, 50));
		setK130Button.addActionListener(e -> controller.addShipToPlayer(new Ship("K130", 2)));
		
		setAllShips = new JButton("setALL");
		setAllShips.setPreferredSize(new Dimension(70, 50));
		setAllShips.addActionListener(e -> controller.addAllShipToPlayer());
		
		shootButton = new JButton("Shoot!");
		shootButton.setPreferredSize(new Dimension(90, 30));
		shootButton.addActionListener(e -> controller.shoot(inputCoordinates.getText()));
		
		ImageIcon bombIcon = new ImageIcon("bomb.png");
		bombButton = new JButton();
		bombButton.setIcon(bombIcon);
		bombButton.setPreferredSize(new Dimension(30, 30));
		bombButton.addActionListener(e -> controller.activateBombShot());
		
		ImageIcon airstrikeIcon = new ImageIcon("Bomber.png");
		airstrikeButton = new JButton();
		airstrikeButton.setIcon(airstrikeIcon);
		airstrikeButton.setPreferredSize(new Dimension(30, 30));
		airstrikeButton.addActionListener(e -> controller.activateAirstrikeShot());
		
		nextButton = new JButton("Next Turn");
		nextButton.setPreferredSize(new Dimension(100, 30));
		nextButton.addActionListener(e -> controller.nextTurn());
		
		revalidate();
		repaint();
	}
	
	public static GameBoard getInstance() {
		if (instance == null) {
			instance = new GameBoard();
		}
		return instance;
	}

	public void setController(GameController controller) {
		// TODO: Replace with Singleton
		this.controller = controller;
	}
	
	public void showGameBoard() {
		String battlegroundName;
	    if (controller.currentPlayerIndex == 0) {
	        battlegroundName = "Battleground 1";
	    } else {
	        battlegroundName = "Battleground 2";
	    }

	    removeAll();

	    setLayout(new BorderLayout());
	    
	    JPanel firstLevelPanel = new JPanel();
	    firstLevelPanel.add(new JLabel(battlegroundName));
	    
	    JPanel secondLevelPanel = new JPanel(new GridLayout(1, 11));
	    secondLevelPanel.add(new JLabel());
	    for (int i = 1; i <= 10; i++) {
	    	secondLevelPanel.add(new JLabel(Integer.toString(i)));
	    }
	    
	    JPanel thirdLevelLeftPanel = new JPanel(new GridLayout(10, 1));
	    for (char c = 'A'; c <= 'J'; c++) {
	        thirdLevelLeftPanel.add(new JLabel(Character.toString(c)));
	    }
	    
	    JPanel thirdLevelRightPanel = new JPanel(new GridLayout(10, 10));
	    JButton[][] buttons = new JButton[10][10];
	    FieldState[][] currentBattleground = null;
	    int markerBattleground = controller.currentPlayerIndex == 0 ? 3 : 4;
	    if (markerBattleground == 3) {
	    	currentBattleground = myModel.getBattleground2Markers();
	    } else if (markerBattleground == 4) {
	    	currentBattleground = myModel.getBattleground1Markers();
	    }
	    for (int row = 0; row < 10; row++) {
	    	for (int col = 0; col < 10; col++) {
	    		if (controller.currentPlayerIndex == 0) {
	    			buttons[row][col] = new CustomButton(row, col, 1);
	    		} else {
	    			buttons[row][col] = new CustomButton(row, col, 2);
	    		}
	    		thirdLevelRightPanel.add(buttons[row][col]);
	    	}
	    }

	    JPanel bottomPanel = new JPanel();
	    inputCoordinates = new JTextField();
	    inputCoordinates.setPreferredSize(new Dimension(120, 30));
	    JLabel chooseShipLabel = new JLabel("Pick a Ship");
	    GroupLayout layout = new GroupLayout(bottomPanel);
	    bottomPanel.setLayout(layout);
	    layout.setAutoCreateGaps(true);
	    layout.setAutoCreateContainerGaps(true);
	    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
	            .addComponent(inputCoordinates)
	            .addComponent(chooseShipLabel)
	            .addGroup(layout.createSequentialGroup()
	                    .addComponent(setF125Button)
	                    .addComponent(setF124Button)
	                    .addComponent(setF123Button)
	                    .addComponent(setK130Button)
	                    .addComponent(setAllShips)
	                    )
	    );
	    layout.setVerticalGroup(layout.createSequentialGroup()
	            .addComponent(inputCoordinates)
	            .addComponent(chooseShipLabel)
	            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
	                    .addComponent(setF125Button)
	                    .addComponent(setF124Button)
	                    .addComponent(setF123Button)
	                    .addComponent(setK130Button)
	                    .addComponent(setAllShips)
	                    )
	    );
	    
	    JPanel testPanel = new JPanel();
	    outputField = new JTextArea(2, 30);
		outputField.setEditable(false);
		testPanel.add(outputField);
	    
	    JPanel secondLevelContainer = new JPanel(new BorderLayout());
	    secondLevelContainer.add(secondLevelPanel, BorderLayout.NORTH);
	    secondLevelContainer.add(thirdLevelLeftPanel, BorderLayout.WEST);
	    secondLevelContainer.add(thirdLevelRightPanel, BorderLayout.CENTER);
	    secondLevelContainer.add(bottomPanel, BorderLayout.SOUTH);
	    add(firstLevelPanel, BorderLayout.NORTH);
	    add(secondLevelContainer, BorderLayout.CENTER);
	    add(testPanel, BorderLayout.SOUTH);
	    
	    revalidate();
	    repaint();
	}

	private class CustomButton extends JButton {
		private final int row;
		private final int col;
		private final int battleground;
		
		public CustomButton(int row, int col, int battleground) {
			this.row = row;
			this.col = col;
			this.battleground = battleground;
			setPreferredSize(new Dimension(50, 50));
			addActionListener(e -> handleClick());
		}
		
		private void handleClick() {
	        char coordinateX = (char) ('A' + row);
            int coordinateY = (col + 1);
            String existingText = inputCoordinates.getText();
            inputCoordinates.setText(existingText + " " + coordinateX + Integer.toString(coordinateY));
		}
		
		@Override
	    protected void paintComponent(Graphics g) {
	        FieldState[][] currentBattleground = null;
	        if (battleground == 1) {
	            currentBattleground = myModel.getSpielfeld1();
	        } else if (battleground == 2) {
	            currentBattleground = myModel.getSpielfeld2();
	        } else if (battleground == 3) {
	            currentBattleground = myModel.getBattleground2Markers();
	        } else if (battleground == 4) {
	            currentBattleground = myModel.getBattleground1Markers();
	        }

	        FieldState state = currentBattleground[row][col];

	        // Set button color based on the FieldState
	        if (state == FieldState.EMPTYNOTSHOT) {
	            setBackground(Color.BLUE);
	        } else if (state == FieldState.SHIP) {
	            setBackground(Color.GRAY);
	        } else if (state == FieldState.EMPTYSHOT) {
	            setBackground(Color.CYAN);
	        } else if (state == FieldState.HIT) {
	            setBackground(Color.RED);
	        } else {
	            setBackground(Color.WHITE);
	        }

	        super.paintComponent(g);
	        revalidate();
	        repaint();
	    }
	}
	
	public void showGameLayout() {
        String palyerTurn;
        String switchingToPlayer;
        
//        GameController controller = new Controller(model, view); 
        int markerBattleground;
        if (controller.currentPlayerIndex == 0) {
        	palyerTurn = "Currently playing: " + controller.player1Name;
        	switchingToPlayer = "Switching to Spieler 1";
        	markerBattleground = 3;
        } else {
        	palyerTurn = "Currently playing: " + controller.player2Name;
        	switchingToPlayer = "Switching to Spieler 2";
        	markerBattleground = 4;
        }
        
        removeAll();
        
        setLayout(new BorderLayout());
        
		JPanel firstLevelPanel = new JPanel();
		firstLevelPanel.add(new JLabel(palyerTurn));
		
		JPanel secondLevelPanel = new JPanel(new GridLayout(1, 11));
		secondLevelPanel.add(new JLabel());
		for (int i = 1; i <= 10; i++) {
			secondLevelPanel.add(new JLabel(Integer.toString(i)));
		}
		
		JPanel thirdLevelLeftPanel = new JPanel(new GridLayout(10, 1));
		for (char c = 'A'; c <= 'J'; c++) {
	        thirdLevelLeftPanel.add(new JLabel(Character.toString(c)));
	    }
		
		JPanel thirdLevelRightPanel = new JPanel(new GridLayout(10, 10));
		for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
            	CustomButton customButton = new CustomButton(i, j, markerBattleground);
            	thirdLevelRightPanel.add(customButton);
            }
        }
		
		JPanel fourthLevelPanel = new JPanel();
		
		inputCoordinates = new JTextField();
		inputCoordinates.setPreferredSize(new Dimension(120, 30));
		
		fourthLevelPanel.setLayout(new FlowLayout());		
		fourthLevelPanel.add(new JLabel("Enter coordinates: "));
		fourthLevelPanel.add(inputCoordinates);
		fourthLevelPanel.add(shootButton);
		if (controller.currentPlayerIndex == 0) {
			if (disableBombShotPlayer1 == false) {			
				fourthLevelPanel.add(bombButton);
			}
			if (disableAirstrikeShotPlayer1 == false) {			
				fourthLevelPanel.add(airstrikeButton);
			}
        } else {
        	if (disableBombShotPlayer2 == false) {			
				fourthLevelPanel.add(bombButton);
			}
			if (disableAirstrikeShotPlayer2 == false) {			
				fourthLevelPanel.add(airstrikeButton);
			}
        }
        
		fourthLevelPanel.add(nextButton);
		
		JPanel bottomPanel = new JPanel();
		outputField = new JTextArea(2, 30);
		outputField.setEditable(false);
		outputField.setText(controller.storeTextAreaOutput);
		bottomPanel.add(outputField);
		
		JPanel secondLevelContainer = new JPanel(new BorderLayout());
		secondLevelContainer.add(secondLevelPanel, BorderLayout.NORTH);
		secondLevelContainer.add(thirdLevelLeftPanel, BorderLayout.WEST);
		secondLevelContainer.add(thirdLevelRightPanel, BorderLayout.CENTER);
		
		JPanel southPanelContainer = new JPanel(new BorderLayout());
		southPanelContainer.add(fourthLevelPanel, BorderLayout.NORTH);
		southPanelContainer.add(bottomPanel, BorderLayout.SOUTH);
		
		add(firstLevelPanel, BorderLayout.NORTH);
		add(secondLevelContainer, BorderLayout.CENTER);
		add(southPanelContainer, BorderLayout.SOUTH);
		
		revalidate();
		repaint();
	}
	
	public void winningScreen(int playerNumber) {
		removeAll();
		
		setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel();
		
		ImageIcon backgroundImage = new ImageIcon("win.jpg");
		String congratsLabel = "Congratulations! Player " + playerNumber + " won the game!";
		String credits = "<html><div style='text-align: center;'>"
				+ "<h2>Credits:</h2><br>"
				+ "<h3>Development Team:</h3>"
				+ "- Supervisor/Expert: Mueller Tobias<br>"
				+ "- UI/UX Designer: Bakalarz Dennis<br>"
				+ "- Backend Developer: Bakalarz Dennis<br><br>"
				+ "<h3>Special Thanks:</h3>"
				+ "- Mueller Tobias: Providing information and guiding the project<br>"
				+ "- Starrost Frank: Sponsoring"
				+ "<h3>External Resources:</h3>"
				+ "- Icons: https://www.freepik.com/icon/boat_3754238<br>"
				+ "- Images: https://stock.adobe.com/de/images/you-win-message/314566645<br>"
				+ "<h3>Software Libraries:</h3>"
				+ "- Swing: For creating the guided user interface<br>"
				+ "<h3>Attributions:</h3>"
				+ "- Background Image: https://onefortheplanet.de/weltozeantag-2020/<br>"
				+ "This software was developed with contributions from various individuals and resources.<br>"
				+ "We appreciate the support of everyone who helped make this project a reality."
				+ "</div></html>";

		mainPanel.add(new JLabel(congratsLabel));
		mainPanel.add(new JLabel(backgroundImage));
		mainPanel.add(new JLabel(credits));
		
		add(mainPanel, BorderLayout.CENTER);
		
		revalidate();
		repaint();
	}
}
