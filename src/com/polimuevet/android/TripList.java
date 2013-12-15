package com.polimuevet.android;

import java.util.ArrayList;

/**
 * Lista de trayectos
 * 
 * @author César
 * @see ArrayList
 */

public class TripList {
	private ArrayList<Trip> data;
	boolean success;
	String info;

	public ArrayList<Trip> getTrips() {
		return data;
	}

	public void setTrips(ArrayList<Trip> trips) {
		this.data = trips;
	}

	public TripList(ArrayList<Trip> data, boolean success, String info) {
		super();
		this.data = data;
		this.success = success;
		this.info = info;
	}
	public TripList() {
	}
}
