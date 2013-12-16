package com.polimuevet.android;

import java.util.ArrayList;

/**
 * Lista de trayectos
 * 
 * @author César
 * @see ArrayList
 */

public class UserList {
	private ArrayList<User> data;
	boolean success;
	String info;

	public ArrayList<User> getUsers() {
		return data;
	}

	public void setTrips(ArrayList<User> trips) {
		this.data = trips;
	}

	public UserList(ArrayList<User> data, boolean success, String info) {
		super();
		this.data = data;
		this.success = success;
		this.info = info;
	}
	public UserList() {
	}
}
