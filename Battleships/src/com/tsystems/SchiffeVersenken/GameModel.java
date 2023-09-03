package com.tsystems.SchiffeVersenken;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
	private FieldState[][] spielfeld1;
	private FieldState[][] spielfeld2;
	private FieldState[][] battleground1Markers;
	private FieldState[][] battleground2Markers;
	private List<Player> players;
	public int currentPlayerIndex;
	
	private static GameModel instance;
	
	private GameModel() {
		spielfeld1 = new FieldState[10][10];
		spielfeld2 = new FieldState[10][10];
		battleground1Markers = new FieldState[10][10];
		battleground2Markers = new FieldState[10][10];
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 10; col++) {
				spielfeld1[col][row] = FieldState.EMPTYNOTSHOT;
				spielfeld2[col][row] = FieldState.EMPTYNOTSHOT;
				battleground1Markers[col][row] = FieldState.EMPTYNOTSHOT;
				battleground2Markers[col][row] = FieldState.EMPTYNOTSHOT;
			}
		}
		players = new ArrayList<>();
		currentPlayerIndex = 0;
	}
	
	public static GameModel getInstance() {
		if (instance == null) {
			instance = new GameModel();
		}
		return instance;
	}
	
	public FieldState[][] getSpielfeld1() {
		return spielfeld1;
	}
	
	public FieldState[][] getSpielfeld2() {
		return spielfeld2;
	}
	
	public FieldState[][] getBattleground1Markers() {
		return battleground1Markers;
	}
	
	public FieldState[][] getBattleground2Markers() {
		return battleground2Markers;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public int getPlayerCount() {
		return players.size();
	}
	
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	
	public String getCurrentPlayerName() {
		return players.get(currentPlayerIndex).getName();
	}
	
	public List<Player> getPlayers() {
        return players;
    }
}
