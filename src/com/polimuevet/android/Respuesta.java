package com.polimuevet.android;

public class Respuesta {
	
	boolean success;
	String info;
	String data;
	
	
	public Respuesta() {
	}
	
	public Respuesta(boolean success, String info, String data) {
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
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String testrespuesta(){
		return new String("success: "+success+" info: "+info+" data: "+data);
	}

}
