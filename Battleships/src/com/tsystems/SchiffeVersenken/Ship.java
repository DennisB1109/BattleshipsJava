package com.tsystems.SchiffeVersenken;

import java.util.ArrayList;
import java.util.List;

public class Ship {
	private String name;
	private int length;
	private List<int[]> coordinates;
	private List<Boolean> fieldStatus;
	private boolean destroyed;
	
	public Ship(String name, int length) {
		this.name = name;
		this.length = length;
		this.coordinates = new ArrayList<>();
		this.fieldStatus = new ArrayList<>();
		this.destroyed = false;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLength() {
		return length;
	}
	
	public List<Boolean> getFieldStatus() {
		return fieldStatus;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void addField(int row, int col) {
		coordinates.add(new int[] {row, col});
		fieldStatus.add(false);
	}
	
	public void markFieldAsHit(int row, int col) {
		for (int i = 0; i < coordinates.size(); i++) {
			int[] coord = coordinates.get(i);
//			System.out.println("markFieldAsHit method: coord[0]: " + coord[0] + " coord[1]: " + coord[1] + " row: " + row + " col: " + col);
			if ((coord[0] - 1) == row && (coord[1] - 1) == col) {
				fieldStatus.set(i, true);
				checkDestroyed();
				break;
			}
		}
	}
	
	public List<int[]> getCoordinates() {
        return coordinates;
    }
	
	private void checkDestroyed() {
		destroyed = !fieldStatus.contains(false);
	}
}
