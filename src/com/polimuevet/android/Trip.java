package com.polimuevet.android;

import java.util.ArrayList;
import java.util.Date;

public class Trip {

	int Num_plazas;
	String Origen;
	String Destino;
	String Fecha_time;
	float Precio_plaza;
	int Tiempo_max_espera;
	int Max_tamaño_equipaje;
	// ArrayList<Boolean> Tipo_pasajeros;
	tipos Tipo_pasajero;
	String Observaciones;
	String Creador_id;
	ArrayList<Double> Origen_latlng;
	ArrayList<Double> Destino_latlng;
	String Max_tamanyo_equipaje;
	ArrayList<Person> Inscritos;
	String _id;

	public Trip(int num_plazas, String origen, String destino,
			String fecha_time, float precio_plaza, int tiempo_max_espera,
			int max_tamaño_equipaje, tipos tipo_pasajero, String observaciones,
			String creador_id, ArrayList<Double> origen_latlng,
			ArrayList<Double> destino_latlng, String max_tamanyo_equipaje,
			ArrayList<Person> inscritos, String _id) {
		super();
		Num_plazas = num_plazas;
		Origen = origen;
		Destino = destino;
		Fecha_time = fecha_time;
		Precio_plaza = precio_plaza;
		Tiempo_max_espera = tiempo_max_espera;
		Max_tamaño_equipaje = max_tamaño_equipaje;
		Tipo_pasajero = tipo_pasajero;
		Observaciones = observaciones;
		Creador_id = creador_id;
		Origen_latlng = origen_latlng;
		Destino_latlng = destino_latlng;
		Max_tamanyo_equipaje = max_tamanyo_equipaje;
		Inscritos = inscritos;
		this._id = _id;
	}

	public Trip() {

	}

	// class Id {
	// String $oid;
	//
	// public Id(String $oid) {
	// super();
	// this.$oid = $oid;
	// }
	//
	// public String get$oid() {
	// return $oid;
	// }
	//
	// public void set$oid(String $oid) {
	// this.$oid = $oid;
	// }
	//
	// }

	public int getNum_plazas() {
		return Num_plazas;
	}

	public void setNum_plazas(int num_plazas) {
		Num_plazas = num_plazas;
	}

	public String getOrigen() {
		return Origen;
	}

	public void setOrigen(String origen) {
		Origen = origen;
	}

	public String getDestino() {
		return Destino;
	}

	public void setDestino(String destino) {
		Destino = destino;
	}

	public String getFecha_time() {
		return Fecha_time;
	}

	public void setFecha_time(String fecha_time) {
		Fecha_time = fecha_time;
	}

	public float getPrecio_plaza() {
		return Precio_plaza;
	}

	public void setPrecio_plaza(float precio_plaza) {
		Precio_plaza = precio_plaza;
	}

	public int getTiempo_max_espera() {
		return Tiempo_max_espera;
	}

	public void setTiempo_max_espera(int tiempo_max_espera) {
		Tiempo_max_espera = tiempo_max_espera;
	}

	public int getMax_tamaño_equipaje() {
		return Max_tamaño_equipaje;
	}

	public void setMax_tamaño_equipaje(int max_tamaño_equipaje) {
		Max_tamaño_equipaje = max_tamaño_equipaje;
	}

	public tipos getTipo_pasajero() {
		return Tipo_pasajero;
	}

	public void setTipo_pasajero(tipos tipo_pasajero) {
		Tipo_pasajero = tipo_pasajero;
	}

	public String getObservaciones() {
		return Observaciones;
	}

	public void setObservaciones(String observaciones) {
		Observaciones = observaciones;
	}

	public String getCreador_id() {
		return Creador_id;
	}

	public void setCreador_id(String creador_id) {
		Creador_id = creador_id;
	}

	public ArrayList<Double> getOrigen_latlng() {
		return Origen_latlng;
	}

	public void setOrigen_latlng(ArrayList<Double> origen_latlng) {
		Origen_latlng = origen_latlng;
	}

	public ArrayList<Double> getDestino_latlng() {
		return Destino_latlng;
	}

	public void setDestino_latlng(ArrayList<Double> destino_latlng) {
		Destino_latlng = destino_latlng;
	}

	public String getMax_tamanyo_equipaje() {
		return Max_tamanyo_equipaje;
	}

	public void setMax_tamanyo_equipaje(String max_tamanyo_equipaje) {
		Max_tamanyo_equipaje = max_tamanyo_equipaje;
	}

	public ArrayList<Person> getInscritos() {
		return Inscritos;
	}

	public void setInscritos(ArrayList<Person> inscritos) {
		Inscritos = inscritos;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	class corigen {
		ArrayList<Double> Origen_latlng;

		public corigen() {

		}

		public corigen(ArrayList<Double> origen_latlng) {
			super();
			Origen_latlng = origen_latlng;
		}

		public ArrayList<Double> getOrigen_latlng() {
			return Origen_latlng;
		}

		public void setOrigen_latlng(ArrayList<Double> origen_latlng) {
			Origen_latlng = origen_latlng;
		}

	}

	class cdestino {
		ArrayList<Double> Destino_latlng;

		public cdestino() {

		}

		public cdestino(ArrayList<Double> destino_latlng) {
			super();
			Destino_latlng = destino_latlng;
		}

		public ArrayList<Double> getDestino_latlng() {
			return Destino_latlng;
		}

		public void setDestino_latlng(ArrayList<Double> destino_latlng) {
			Destino_latlng = destino_latlng;
		}

	}

	class Person {
		String id;

		public Person() {

		}

		public Person(String id) {
			super();
			this.id = id;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

	class tipos {
		boolean alumnos;
		boolean profesores;
		boolean personal;

		public tipos() {

		}

		public tipos(boolean alumnos, boolean profesores, boolean personal) {
			super();
			this.alumnos = alumnos;
			this.profesores = profesores;
			this.personal = personal;
		}

		public boolean isAlumnos() {
			return alumnos;
		}

		public void setAlumnos(boolean alumnos) {
			this.alumnos = alumnos;
		}

		public boolean isProfesores() {
			return profesores;
		}

		public void setProfesores(boolean profesores) {
			this.profesores = profesores;
		}

		public boolean isPersonal() {
			return personal;
		}

		public void setPersonal(boolean personal) {
			this.personal = personal;
		}

	}
}
