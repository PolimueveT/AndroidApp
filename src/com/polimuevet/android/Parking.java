package com.polimuevet.android;

public class Parking implements Comparable<Parking> {

	int id;
	String codigo;
	String lugar;
	int plazas;
	int ocupadas;
	String estado;

	public Parking() {
	}

	public Parking(int id, String codigo, String lugar, int plazas,
			int ocupadas, String estado) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.lugar = lugar;
		this.plazas = plazas;
		this.ocupadas = ocupadas;
		this.estado = estado;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public int getPlazas() {
		return plazas;
	}

	public void setPlazas(int plazas) {
		this.plazas = plazas;
	}

	public int getOcupadas() {
		return ocupadas;
	}

	public void setOcupadas(int ocupadas) {
		this.ocupadas = ocupadas;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public int compareTo(Parking p) {
		// TODO Auto-generated method stub
		if (this.getId() > p.getId()) {
			return 1;
		} else if (this.getId() < p.getId()) {
			return -1;
		} else {
			return 0;
		}
	}

}
