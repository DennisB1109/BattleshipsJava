package com.tsystems.SchiffeVersenken;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.sampled.*;
import javax.swing.Timer;

import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameController {
	GameModel model = GameModel.getInstance();
	GameBoard view = GameBoard.getInstance();

	private int f125CountPlayer1;
	private int f124CountPlayer1;
	private int f123CountPlayer1;
	private int k130CountPlayer1;
	private int f125CountPlayer2;
	private int f124CountPlayer2;
	private int f123CountPlayer2;
	private int k130CountPlayer2;
	public int f125Count = 1;
	public int f124Count = 2;
	public int f123Count = 3;
	public int k130Count = 4;
	
	public int triggerBombShot = 0;
	public int triggerAirstrikeShot = 0;
	
	private FieldState[][] currentSpielfeld;
	
	public int currentPlayerIndex;
	
	public int swapPlayerCounter = 1;
	
	private boolean didPlayerShot = false;
	
	public String player1Name = "Player 1";
	public String player2Name = "Player 2";
	public String storeTextAreaOutput = "";
	
	public GameController() {
		// TODO: Change to Singleton
		this.currentPlayerIndex = 0;
	}
	
	public void activateBombShot() {
		if (triggerBombShot == 0) {
			triggerBombShot = 1;			
			view.outputField.setText("Bomb activated. Status: " + triggerBombShot);
			triggerAirstrikeShot = 0;  // To prevent the placer activating both special shots at once
		} else {
			triggerBombShot = 0;
			view.outputField.setText("Bomb deactivated. Status: " + triggerBombShot);
		}
	}
	
	public void activateAirstrikeShot() {
		if (triggerAirstrikeShot == 0) {
			triggerAirstrikeShot = 1;			
			view.outputField.setText("Airstrike activated. Status: " + triggerAirstrikeShot);
			triggerBombShot = 0;  // To prevent the placer activating both special shots at once
		} else {
			triggerAirstrikeShot = 0;
			view.outputField.setText("Airstrike deactivated. Status: " + triggerAirstrikeShot);
		}
	}
	
	private void resetShipCounts() {
		// TODO: Create instance variable for Player and create method to reset the ship count
		f125CountPlayer1 = 0;
        f124CountPlayer1 = 0;
        f123CountPlayer1 = 0;
        k130CountPlayer1 = 0;
        f125CountPlayer2 = 0;
        f124CountPlayer2 = 0;
        f123CountPlayer2 = 0;
        k130CountPlayer2 = 0;
        f125Count = 1;
    	f124Count = 2;
    	f123Count = 3;
    	k130Count = 4;	
	}
	
	public void startGame() {
		if (view.namePlayer1.getText() != "Name Player 1") {
			player1Name = view.namePlayer1.getText();
		}
		if (view.namePlayer2.getText() != "Name Player 2") {
			player2Name = view.namePlayer2.getText();
		}
		
		Player player1 = new Player(player1Name);
		Player player2 = new Player(player2Name);
		
		model.addPlayer(player1);
		model.addPlayer(player2);
		
		view.showGameBoard();
	}
	
	public void addShipToPlayer(Ship ship) {
		String coordinates = view.inputCoordinates.getText();
		if (checkCoordinateString(coordinates, ship.getLength()) == 1) {
			view.outputField.setText("Invalid Coordinates!");
			return;
		}
		
		if (model.getPlayerCount() == 0) {
			return;
		}
		
		Player currentPlayer = model.getPlayers().get(currentPlayerIndex);
		
		if (currentPlayer == model.getPlayers().get(0)) {
	        currentSpielfeld = model.getSpielfeld1();
	    } else if (currentPlayer == model.getPlayers().get(1)) {
	        currentSpielfeld = model.getSpielfeld2();
	    }
		
		if (currentPlayer == model.getPlayers().get(0)) {
            if (ship.getName().equals("F125") && f125CountPlayer1 >= 1) {
                view.outputField.setText("Player " + currentPlayer.getName() + " can have only one F125 ship.");
                return;
            } else if (ship.getName().equals("F124") && f124CountPlayer1 >= 2) {
                view.outputField.setText("Player " + currentPlayer.getName() + " can have only two F124 ships.");
                return;
            } else if (ship.getName().equals("F123") && f123CountPlayer1 >= 3) {
                view.outputField.setText("Player " + currentPlayer.getName() + " can have only three F123 ships.");
                return;
            } else if (ship.getName().equals("K130") && k130CountPlayer1 >= 4) {
                view.outputField.setText("Player " + currentPlayer.getName() + " can have only four K130 ships.");
                return;
            }
        } else if (currentPlayer == model.getPlayers().get(1)) {
            if (ship.getName().equals("F125") && f125CountPlayer2 >= 1) {
                view.outputField.setText("Player " + currentPlayer.getName() + " can have only one F125 ship.");
                return;
            } else if (ship.getName().equals("F124") && f124CountPlayer2 >= 2) {
                view.outputField.setText("Player " + currentPlayer.getName() + " can have only two F124 ships.");
                return;
            } else if (ship.getName().equals("F123") && f123CountPlayer2 >= 3) {
                view.outputField.setText("Player " + currentPlayer.getName() + " can have only three F123 ships.");
                return;
            } else if (ship.getName().equals("K130") && k130CountPlayer2 >= 4) {
                view.outputField.setText("Player " + currentPlayer.getName() + " can have only four K130 ships.");
                return;
            }
        }
		
		// Check if field is already occupied
		if (checkForOverlappingShips(coordinates) == 1) {
			view.outputField.setText("Field(s) already occupied by another Ship!");
			return;
		}
		
		// Check if coordinates are next to each other
		if (checkIfCoordinatesInLine(coordinates) == 1) {
			view.outputField.setText("Ships can only placed horizontally or vertically!");
			return;
		}
		
		currentPlayer.addShip(ship);
		view.inputCoordinates.setText("");
		
		// TODO: increment inside the player instance
		if (currentPlayer == model.getPlayers().get(0)) {
            if (ship.getName().equals("F125")) {
                f125CountPlayer1++;
            } else if (ship.getName().equals("F124")) {
                f124CountPlayer1++;
            } else if (ship.getName().equals("F123")) {
                f123CountPlayer1++;
            } else if (ship.getName().equals("K130")) {
                k130CountPlayer1++;
            }
        } else if (currentPlayer == model.getPlayers().get(1)) {
            if (ship.getName().equals("F125")) {
                f125CountPlayer2++;
            } else if (ship.getName().equals("F124")) {
                f124CountPlayer2++;
            } else if (ship.getName().equals("F123")) {
                f123CountPlayer2++;
            } else if (ship.getName().equals("K130")) {
                k130CountPlayer2++;
            }
        }
		
		List<String> matchedSubstrings = new ArrayList<>();
		Pattern pattern = Pattern.compile("[A-J](10|[1-9])");
		Matcher matcher = pattern.matcher(coordinates);
		while (matcher.find()) {
			matchedSubstrings.add(matcher.group());
		}
		for (int i = 0; i < ship.getLength(); i++) {
			int[] coordinate = translateCoordinates(matchedSubstrings.get(i));
			int rowBoard = coordinate[0];
			int colBoard = coordinate[1];
			currentSpielfeld[rowBoard - 1][colBoard - 1] = FieldState.SHIP;
			
			ship.addField(rowBoard, colBoard);
		}

		List<int[]> shipCoordinates = ship.getCoordinates();
		List<Boolean> fieldStatusList = ship.getFieldStatus();
		int shipsLeft = 0;
		if (ship.getName() == "F125") {
			f125Count--;
			shipsLeft = f125Count;
		} else if (ship.getName() == "F124") {
			f124Count--;			
			shipsLeft = f124Count;
		} else if (ship.getName() == "F123") {
		f123Count--;			
		shipsLeft = f123Count;
		} else if (ship.getName() == "K130") {
		k130Count--;			
		shipsLeft = k130Count;
		}
		
		if (f123Count == 0) {
	    	view.setF123Button.setEnabled(false);
	    }
	    if (f124Count == 0) {
	    	view.setF124Button.setEnabled(false);
	    }
	    if (f125Count == 0) {
	    	view.setF125Button.setEnabled(false);
	    }
	    if (k130Count == 0) {
	    	view.setK130Button.setEnabled(false);
	    }
		
		String shipAddedText = "Ship " + ship.getName() + " added at: " + coordinates + ". " + shipsLeft + " Ships left";
		view.outputField.setText(shipAddedText);
		// Here we check if player 1 has added 10 ships -> True? -> Switch to player 2 and update view
		if (currentPlayer == model.getPlayers().get(0) && currentPlayer.getSchiffe().size() >= 10) {
			currentPlayerIndex = 1;
			view.showGameBoard();
			resetShipCounts();
			view.setF123Button.setEnabled(true);
	    	view.setF124Button.setEnabled(true);
	    	view.setF125Button.setEnabled(true);
	    	view.setK130Button.setEnabled(true);
			return;
		}
		// We also need to check if player 2 has added 10 ships -> True? -> Start actual game
		if (currentPlayer == model.getPlayers().get(1) && currentPlayer.getSchiffe().size() >= 10) {
			currentPlayerIndex = 0;
			view.showGameLayout();
			return;
		}
	}
	
	private int checkForOverlappingShips(String coordinates) {
		String regex = "[A-J](10|[1-9])";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(coordinates);
		int[] coordinateList = new int[2];
		while (matcher.find()) {
			String match = matcher.group();
			coordinateList = translateCoordinates(match);
			int row = coordinateList[0] - 1;
			int col = coordinateList[1] - 1;
			if (currentPlayerIndex == 0) {
				if (model.getSpielfeld1()[row][col] == FieldState.SHIP) {
					return 1;
				} 
			} else {
				if (model.getSpielfeld2()[row][col] == FieldState.SHIP) {
					return 1;
				}
			}
		}
		return 0;
	}
	
	private int checkIfCoordinatesInLine(String coordinates) {
		Pattern pattern = Pattern.compile("[A-J](10|[1-9])");
        Matcher matcher = pattern.matcher(coordinates);

        List<int[]> coordinatesList = new ArrayList<>();
        while (matcher.find()) {
            String matchedCoordinate = matcher.group();
            int[] translatedCoordinate = translateCoordinates(matchedCoordinate);
            coordinatesList.add(translatedCoordinate);
        }

        if (coordinatesList.size() < 2) {
            return 1;
        }

        Collections.sort(coordinatesList, (a, b) -> {
            if (a[0] == b[0]) {
                return Integer.compare(a[1], b[1]);
            } else {
                return Integer.compare(a[0], b[0]);
            }
        });

        int prevRow = coordinatesList.get(0)[0];
        int prevCol = coordinatesList.get(0)[1];

        for (int i = 1; i < coordinatesList.size(); i++) {
            int[] currentCoordinate = coordinatesList.get(i);
            if (currentCoordinate[0] == prevRow) {
                if (Math.abs(currentCoordinate[1] - prevCol) != 1) {
                    return 1; // Coordinates have a gap, not a line
                }
            } else if (currentCoordinate[1] == prevCol) {
                if (Math.abs(currentCoordinate[0] - prevRow) != 1) {
                    return 1; // Coordinates have a gap, not a line
                }
            } else {
                return 1; // Coordinates are not aligned horizontally or vertically
            }

            prevRow = currentCoordinate[0];
            prevCol = currentCoordinate[1];
        }

        return 0; // Coordinates form a horizontal or vertical line -> Structure of ship: ok
	}
	
	public void addAllShipToPlayer() {
		// Method not used! It was used for testing purposes during development
		// to quickly add all ships on both players side
		Player currentPlayer = model.getPlayers().get(currentPlayerIndex);
		
		if (currentPlayer == model.getPlayers().get(0)) {
	        currentSpielfeld = model.getSpielfeld1();
	    } else if (currentPlayer == model.getPlayers().get(1)) {
	        currentSpielfeld = model.getSpielfeld2();
	    }
		
		
		Ship f125_1 = new Ship("F125", 5);
		f125_1.addField(0, 9);
		f125_1.addField(1, 9);
		f125_1.addField(2, 9);
		f125_1.addField(3, 9);
		f125_1.addField(4, 9);
		currentPlayer.addShip(f125_1);
		
		Ship f124_1 = new Ship("F124", 4);
		f124_1.addField(0, 8);
		f124_1.addField(1, 8);
		f124_1.addField(2, 8);
		f124_1.addField(3, 8);
		currentPlayer.addShip(f124_1);
		
		Ship f124_2 = new Ship("F124", 4);
		f124_2.addField(0, 7);
		f124_2.addField(1, 7);
		f124_2.addField(2, 7);
		f124_2.addField(3, 7);
		currentPlayer.addShip(f124_2);
		
		Ship f123_1 = new Ship("F123", 3);
		f123_1.addField(0, 6);
		f123_1.addField(1, 6);
		f123_1.addField(2, 6);
		currentPlayer.addShip(f123_1);
		
		Ship f123_2 = new Ship("F123", 3);
		f123_2.addField(0, 5);
		f123_2.addField(1, 5);
		f123_2.addField(2, 5);
		currentPlayer.addShip(f123_2);
		
		Ship f123_3 = new Ship("F123", 3);
		f123_3.addField(0, 4);
		f123_3.addField(1, 4);
		f123_3.addField(2, 4);
		currentPlayer.addShip(f123_3);
		
		Ship k130_1 = new Ship("K130", 2);
		k130_1.addField(0, 3);
		k130_1.addField(1, 3);
		currentPlayer.addShip(k130_1);
		
		Ship k130_2 = new Ship("K130", 2);
		k130_2.addField(0, 2);
		k130_2.addField(1, 2);
		currentPlayer.addShip(k130_2);
		
		Ship k130_3 = new Ship("K130", 2);
		k130_3.addField(0, 1);
		k130_3.addField(1, 1);
		currentPlayer.addShip(k130_3);
		
		if (currentPlayer == model.getPlayers().get(0)) {
                f125CountPlayer1++;
                f124CountPlayer1 += 2;
                f123CountPlayer1 += 3;
                k130CountPlayer1 += 3;
            } else if (currentPlayer == model.getPlayers().get(1)) {
                f125CountPlayer2++;
                f124CountPlayer2 += 2;
                f123CountPlayer2 += 3;
                k130CountPlayer2 += 3;
            }
		
		// Add F125 to Array
		currentSpielfeld[0][9] = FieldState.SHIP;
		currentSpielfeld[1][9] = FieldState.SHIP;
		currentSpielfeld[2][9] = FieldState.SHIP;
		currentSpielfeld[3][9] = FieldState.SHIP;
		currentSpielfeld[4][9] = FieldState.SHIP;
		// ADD F124 to Array
		currentSpielfeld[0][8] = FieldState.SHIP;
		currentSpielfeld[1][8] = FieldState.SHIP;
		currentSpielfeld[2][8] = FieldState.SHIP;
		currentSpielfeld[3][8] = FieldState.SHIP;
		currentSpielfeld[0][7] = FieldState.SHIP;
		currentSpielfeld[1][7] = FieldState.SHIP;
		currentSpielfeld[2][7] = FieldState.SHIP;
		currentSpielfeld[3][7] = FieldState.SHIP;
		// ADD F123 to Array
		currentSpielfeld[0][6] = FieldState.SHIP;
		currentSpielfeld[1][6] = FieldState.SHIP;
		currentSpielfeld[2][6] = FieldState.SHIP;
		currentSpielfeld[0][5] = FieldState.SHIP;
		currentSpielfeld[1][5] = FieldState.SHIP;
		currentSpielfeld[2][5] = FieldState.SHIP;
		currentSpielfeld[0][4] = FieldState.SHIP;
		currentSpielfeld[1][4] = FieldState.SHIP;
		currentSpielfeld[2][4] = FieldState.SHIP;
		// ADD K130 to Array
		currentSpielfeld[0][3] = FieldState.SHIP;
		currentSpielfeld[1][3] = FieldState.SHIP;
		currentSpielfeld[0][2] = FieldState.SHIP;
		currentSpielfeld[1][2] = FieldState.SHIP;
		currentSpielfeld[0][1] = FieldState.SHIP;
		currentSpielfeld[1][1] = FieldState.SHIP;
		
		// Here we check if player 1 has added 10 ships -> True? -> Switch to player 2 and update view
		if (currentPlayer == model.getPlayers().get(0) && currentPlayer.getSchiffe().size() >= 10) {
			currentPlayerIndex = 1;
			view.showGameBoard();
			resetShipCounts();
			return;
		}
		// We also need to check if player 2 has added 10 ships -> True? -> Start actual game
		if (currentPlayer == model.getPlayers().get(1) && currentPlayer.getSchiffe().size() >= 10) {
			currentPlayerIndex = 0;
			view.showGameLayout();
			return;
		}
		return;
	}
	
	public int checkCoordinateString(String coordinates, int laenge) {
		String regex = "[A-J](10|[1-9])";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(coordinates);
		
		int numMatches = 0;
	    while (matcher.find()) {
	        numMatches++;
	    }

	    if (numMatches == laenge) {
	        return 0;
	    } else {
	        view.outputField.setText("Expected " + laenge + " coordinates, but found " + numMatches);
	        return 1;
	    }
	}
	
	public int[] translateCoordinates(String coordinateString) {
		int[] coordinateList = new int[2];
		
		
		int row = (int) coordinateString.charAt(0);
        if (row >= 65 && row <= 74) {
            row -= 64;
        }
		
		String substring = coordinateString.substring(1);
		int col = Integer.parseInt(substring);

		coordinateList[0] = row;
		coordinateList[1] = col;
		return coordinateList;
	}
	
	public void shoot(String coordinates) {
		if (didPlayerShot) {
			view.outputField.setText("No more shots left, it's the next players turn!");
			return;
		}
		
		// Check if Coordinate is valid
		if (checkCoordinateString(coordinates, 1) == 1) {
			view.outputField.setText("Invalid Coordinates!");
			return;
		}
		
		
		if (coordinates.charAt(0) == ' ') {
			coordinates = coordinates.substring(1);
		}
		int[] coordinate = translateCoordinates(coordinates);
		int row = coordinate[0] - 1;
		int col = coordinate[1] - 1;

		if (triggerBombShot == 1) {
			// ToDo
			triggerBombShot = 0;
			bombShot(coordinates, row, col);
			if (currentPlayerIndex == 0) {
				view.disableBombShotPlayer1 = true;
	        } else {
	        	view.disableBombShotPlayer2 = true;
	        }
			return;
		}
		
		if (triggerAirstrikeShot == 1) {
			// ToDo
			triggerAirstrikeShot = 0;
			airstrikeShot(coordinates, row);
			if (currentPlayerIndex == 0) {
				view.disableAirstrikeShotPlayer1 = true;
	        } else {
	        	view.disableAirstrikeShotPlayer2 = true;
	        }
			return;
		}
		
		regularShot(coordinates, row, col);
		
	}
	
	public void regularShot(String coordinates, int row, int col) {
		// Find out which players turn it is and Check enemy spielfeld
				if(currentPlayerIndex == 0) {
					if (model.getBattleground2Markers()[row][col] != FieldState.EMPTYNOTSHOT) {
						view.outputField.setText("You already shot this target, input other coordinates!");
						return;
					} else if (model.getSpielfeld2()[row][col] == FieldState.SHIP) {
						storeTextAreaOutput = "Ship hit at Field " + coordinates + " Row: " + row + " Col: " + col;
						model.getBattleground2Markers()[row][col] = FieldState.HIT;
						for (Ship ship : model.getPlayers().get(1).getSchiffe()) {
							List<int[]> shipCoordinates = ship.getCoordinates();
							for (int i = 0; i < shipCoordinates.size(); i++) {
								int[] shipCoordinate = shipCoordinates.get(i);
								if ((shipCoordinate[0] - 1) == row && (shipCoordinate[1] - 1) == col) {
									ship.markFieldAsHit(row, col);
									if (ship.isDestroyed()) {
										storeTextAreaOutput = "Ship " + ship.getName() + " was destroyed!";
									}
									List<Boolean> fieldStatusList = ship.getFieldStatus();
									for (int j = 0; j < shipCoordinates.size(); j++) {
										boolean fieldStatus = fieldStatusList.get(j);
									}
									break;
								}
							}
						}
						didPlayerShot = false;
					} else if (model.getSpielfeld2()[row][col] == FieldState.EMPTYNOTSHOT){
						storeTextAreaOutput = "Missed at Field " + coordinates + " Row: " + row + " Col: " + col;
						model.getBattleground2Markers()[row][col] = FieldState.EMPTYSHOT;
						didPlayerShot = true;
					}
				} else {
					if (model.getBattleground1Markers()[row][col] != FieldState.EMPTYNOTSHOT) {
						view.outputField.setText("You already shot this target, input other coordinates!");
						return;
					} else if (model.getSpielfeld1()[row][col] == FieldState.SHIP) {
						storeTextAreaOutput = "Ship hit at Field " + coordinates + " Row: " + row + " Col: " + col;
						model.getBattleground1Markers()[row][col] = FieldState.HIT;
						for (Ship ship : model.getPlayers().get(0).getSchiffe()) {
							List<int[]> shipCoordinates = ship.getCoordinates();
							for (int i = 0; i < shipCoordinates.size(); i++) {
								int[] shipCoordinate = shipCoordinates.get(i);
								if ((shipCoordinate[0] - 1) == row && (shipCoordinate[1] - 1) == col) {
									ship.markFieldAsHit(row, col);
									if (ship.isDestroyed()) {
										storeTextAreaOutput = "Ship " + ship.getName() + " was destroyed!";
									}
									List<Boolean> fieldStatusList = ship.getFieldStatus();
									for (int j = 0; j < shipCoordinates.size(); j++) {
										boolean fieldStatus = fieldStatusList.get(j);
									}
									break;
								}
							}
						}
						didPlayerShot = false;
					} else if (model.getSpielfeld1()[row][col] == FieldState.EMPTYNOTSHOT){
						storeTextAreaOutput = "Missed at Field " + coordinates + " Row: " + row + " Col: " + col;
						model.getBattleground1Markers()[row][col] = FieldState.EMPTYSHOT;
						didPlayerShot = true;
					}
				}
				view.showGameLayout();
				
				checkWinner();
	}
	
	public void bombShot(String coordinates, int row, int col) {
		File file = new File("bomb.wav");
		try {
			AudioInputStream bombAudio = AudioSystem.getAudioInputStream(file);
			Clip clip;
			try {
				clip = AudioSystem.getClip();
				clip.open(bombAudio);
				clip.start();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Get all coordinates surrounding the initial target
		int[][] surroundingCoords = {
		        {-1, -1}, {-1, 0}, {-1, 1},
		        {0, -1},           {0, 1},
		        {1, -1},  {1, 0},  {1, 1}
		    };
		regularShot(coordinates, row, col);
		
		// Check field sourrounding the 
	    for (int[] offset : surroundingCoords) {
	        int newRow = row + offset[0];
	        int newCol = col + offset[1];
	        System.out.println("Test");
	        // Check if the coordinates are within bounds (0 to 9 for a 10x10 array)
	        if (newRow >= 0 && newRow < 10 && newCol >= 0 && newCol < 10) {
	            // Process the coordinate here
	        	if(currentPlayerIndex == 0) {
					if (model.getSpielfeld2()[newRow][newCol] == FieldState.SHIP) {
						storeTextAreaOutput = "Ship hit at Field " + coordinates + " Row: " + newRow + " Col: " + newCol;
						System.out.println("Ship hit at Field " + coordinates + " Row: " + newRow + " Col: " + newCol);
						model.getBattleground2Markers()[newRow][newCol] = FieldState.HIT;
						for (Ship ship : model.getPlayers().get(1).getSchiffe()) {
							List<int[]> shipCoordinates = ship.getCoordinates();
							for (int i = 0; i < shipCoordinates.size(); i++) {
								int[] shipCoordinate = shipCoordinates.get(i);
								if ((shipCoordinate[0] - 1) == newRow && (shipCoordinate[1] - 1) == newCol) {
									ship.markFieldAsHit(newRow, newCol);
									if (ship.isDestroyed()) {
										storeTextAreaOutput = "Ship " + ship.getName() + " was destroyed!";
										System.out.println("Ship " + ship.getName() + " was destroyed!");
									}
									List<Boolean> fieldStatusList = ship.getFieldStatus();
									for (int j = 0; j < shipCoordinates.size(); j++) {
										boolean fieldStatus = fieldStatusList.get(j);
									}
									break;
								}
							}
						}
						didPlayerShot = false;
					} else if (model.getSpielfeld2()[newRow][newCol] == FieldState.EMPTYNOTSHOT){
						storeTextAreaOutput = "Missed at Field " + coordinates + " Row: " + newRow + " Col: " + newCol;
						System.out.println("Missed at Field " + coordinates + " Row: " + newRow + " Col: " + newCol);
						model.getBattleground2Markers()[newRow][newCol] = FieldState.EMPTYSHOT;
						didPlayerShot = true;
					}
				} else {
					if (model.getSpielfeld1()[newRow][newCol] == FieldState.SHIP) {
						storeTextAreaOutput = "Ship hit at Field " + coordinates + " Row: " + newRow + " Col: " + newCol;
						System.out.println("Ship hit at Field " + coordinates + " Row: " + newRow + " Col: " + newCol);
						model.getBattleground1Markers()[newRow][newCol] = FieldState.HIT;
						for (Ship ship : model.getPlayers().get(0).getSchiffe()) {
							List<int[]> shipCoordinates = ship.getCoordinates();
							for (int i = 0; i < shipCoordinates.size(); i++) {
								int[] shipCoordinate = shipCoordinates.get(i);
								if ((shipCoordinate[0] - 1) == newRow && (shipCoordinate[1] - 1) == newCol) {
									ship.markFieldAsHit(newRow, newCol);
									if (ship.isDestroyed()) {
										storeTextAreaOutput = "Ship " + ship.getName() + " was destroyed!";
										System.out.println(storeTextAreaOutput = "Ship " + ship.getName() + " was destroyed!");
									}
									List<Boolean> fieldStatusList = ship.getFieldStatus();
									for (int j = 0; j < shipCoordinates.size(); j++) {
										boolean fieldStatus = fieldStatusList.get(j);
									}
									break;
								}
							}
						}
						didPlayerShot = false;
					} else if (model.getSpielfeld1()[newRow][newCol] == FieldState.EMPTYNOTSHOT){
						storeTextAreaOutput = "Missed at Field " + coordinates + " Row: " + newRow + " Col: " + newCol;
						System.out.println("Missed at Field " + coordinates + " Row: " + newRow + " Col: " + newCol);
						model.getBattleground1Markers()[newRow][newCol] = FieldState.EMPTYSHOT;
						didPlayerShot = true;
					}
				}
				view.showGameLayout();
				
				checkWinner();
	        }
	    }
	}
	
	public void airstrikeShot(String coordinates, int row) {
		File file = new File("plane.wav");
		try {
			AudioInputStream bombAudio = AudioSystem.getAudioInputStream(file);
			Clip clip;
			try {
				clip = AudioSystem.getClip();
				clip.open(bombAudio);
				clip.start();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int delay = 100;  // 100ms = 0.1s
		Timer timer = new Timer(delay, new ActionListener() {
	        private int i = 0;
	        
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if (i < 10) {
	            	if(currentPlayerIndex == 0) {
	    				if (model.getBattleground2Markers()[row][i] != FieldState.EMPTYNOTSHOT) {
	    					view.outputField.setText("You already shot this target, input other coordinates!");
	    					return;
	    				} else if (model.getSpielfeld2()[row][i] == FieldState.SHIP) {
	    					storeTextAreaOutput = "Ship hit at Field " + coordinates + " Row: " + row + " Col: " + i;
	    					model.getBattleground2Markers()[row][i] = FieldState.HIT;
	    					for (Ship ship : model.getPlayers().get(1).getSchiffe()) {
	    						List<int[]> shipCoordinates = ship.getCoordinates();
	    						for (int j = 0; j < shipCoordinates.size(); j++) {
	    							int[] shipCoordinate = shipCoordinates.get(j);
	    							if ((shipCoordinate[0] - 1) == row && (shipCoordinate[1] - 1) == i) {
	    								ship.markFieldAsHit(row, i);
	    								if (ship.isDestroyed()) {
	    									storeTextAreaOutput = "Ship " + ship.getName() + " was destroyed!";
	    								}
	    								List<Boolean> fieldStatusList = ship.getFieldStatus();
	    								for (int k = 0; k < shipCoordinates.size(); k++) {
	    									boolean fieldStatus = fieldStatusList.get(k);
	    								}
	    								break;
	    							}
	    						}
	    					}
	    					didPlayerShot = false;
	    				} else if (model.getSpielfeld2()[row][i] == FieldState.EMPTYNOTSHOT){
	    					storeTextAreaOutput = "Missed at Field " + coordinates + " Row: " + row + " Col: " + i;
	    					model.getBattleground2Markers()[row][i] = FieldState.EMPTYSHOT;
	    					didPlayerShot = true;
	    				}
	    			} else {
	    				if (model.getBattleground1Markers()[row][i] != FieldState.EMPTYNOTSHOT) {
	    					view.outputField.setText("You already shot this target, input other coordinates!");
	    					return;
	    				} else if (model.getSpielfeld1()[row][i] == FieldState.SHIP) {
	    					storeTextAreaOutput = "Ship hit at Field " + coordinates + " Row: " + row + " Col: " + i;
	    					model.getBattleground1Markers()[row][i] = FieldState.HIT;
	    					for (Ship ship : model.getPlayers().get(0).getSchiffe()) {
	    						List<int[]> shipCoordinates = ship.getCoordinates();
	    						for (int j = 0; j < shipCoordinates.size(); j++) {
	    							int[] shipCoordinate = shipCoordinates.get(j);
	    							if ((shipCoordinate[0] - 1) == row && (shipCoordinate[1] - 1) == i) {
	    								ship.markFieldAsHit(row, i);
	    								if (ship.isDestroyed()) {
	    									storeTextAreaOutput = "Ship " + ship.getName() + " was destroyed!";
	    								}
	    								List<Boolean> fieldStatusList = ship.getFieldStatus();
	    								for (int k = 0; k < shipCoordinates.size(); k++) {
	    									boolean fieldStatus = fieldStatusList.get(k);
	    								}
	    								break;
	    							}
	    						}
	    					}
	    					didPlayerShot = false;
	    				} else if (model.getSpielfeld1()[row][i] == FieldState.EMPTYNOTSHOT){
	    					storeTextAreaOutput = "Missed at Field " + coordinates + " Row: " + row + " Col: " + i;
	    					model.getBattleground1Markers()[row][i] = FieldState.EMPTYSHOT;
	    					didPlayerShot = true;
	    				}
	    			}
	    			view.showGameLayout();
	    			try {
	    				TimeUnit.SECONDS.sleep(1);
	    			} catch (InterruptedException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    			}
	    			checkWinner();
	    		
	                i++;

	                view.showGameLayout();
	                checkWinner();
	            } else {
	                // Stop the timer after 10 iterations (0.1 second for each)
	                ((Timer) e.getSource()).stop();
	            }
	        }
	    });

	    timer.start();
	}
	
	public void nextTurn() {
        // Check if Player has shot a target
		if (!didPlayerShot) {
			view.outputField.setText("You have to shot a target before the next player can play!");
			return;
		}
		
		// Increment Swap Player Count to swap Player for next turn
		// (Even Count: increment currentPlayIndex, Odd Count: decrement currentPlayIndex) 
        if (swapPlayerCounter % 2 == 0) {
        	currentPlayerIndex--;        	
        } else {
        	currentPlayerIndex++;        	        	
        }
        swapPlayerCounter++;
//        System.out.println("Switching to Player " + (currentPlayerIndex+1));

        didPlayerShot = false;
        storeTextAreaOutput = "";
        view.showGameLayout();
    }
	
	public void checkWinner() {
		int countHits = 0;
		if (currentPlayerIndex == 0) {
			for (int row = 0; row < 10; row++) {
				for (int col = 0; col < 10; col++) {
					if (model.getBattleground2Markers()[row][col] == FieldState.HIT) {
						countHits++;
					}
				}
			}
			if (countHits == 30) {
//				System.out.println("Player 1 won the game!");
				view.outputField.setText("Player 1 won the game!");
				view.winningScreen(1);
			}
		} else {
			for (int row = 0; row < 10; row++) {
				for (int col = 0; col < 10; col++) {
					if (model.getBattleground1Markers()[row][col] == FieldState.HIT) {
						countHits++;
					}
				}
			}
			if (countHits == 30) {
//				System.out.println("Player 2 won the game!");
				view.outputField.setText("Player 2 won the game!");
				view.winningScreen(2);
			}
		}
	}
	
}
