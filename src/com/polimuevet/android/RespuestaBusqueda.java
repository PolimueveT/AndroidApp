package com.polimuevet.android;

public class RespuestaBusqueda {
	
	boolean success;
	String info;
	TripList data;
	
	
	public RespuestaBusqueda() {
	}
	
	public RespuestaBusqueda(boolean success, String info, TripList data) {
		super();
		this.success = success;
		this.info = info;
		this.data = data;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public TripList getData() {
		return data;
	}

	public void setData(TripList data) {
		this.data = data;
	}

	public String testrespuesta(){
		return new String("success: "+success+" info: "+info+" data: "+data);
	}

}
