package com.tsystems.SchiffeVersenken;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private String name;
	private List<Ship> schiffe;
	
	public Player(String name) {
		this.name = name;
		this.schiffe = new ArrayList<>();
	}
	
	public void addShip(Ship schiff) {
		schiffe.add(schiff);
	}
	
	public List<Ship> getSchiffe(){
		return schiffe;
	}
	
	public String getName() {
		return name;
	}
}
