package com.polimuevet.android;

import java.util.ArrayList;
import java.util.Date;

public class Trip {

	int Num_plazas;
	String Origen;
	String Destino;
	Date Fecha_time;
	float Precio_plaza;
	int Tiempo_max_espera;
	int Max_tamaño_equipaje;
	ArrayList<Boolean> Tipo_pasajeros;
	String Observaciones;
	String Creador_id;
	String Max_tamanyo_equipaje;
	ArrayList<Person> Inscritos;
	Id _id;

	class Id {
		String $oid;

		public Id(String $oid) {
			super();
			this.$oid = $oid;
		}

		public String get$oid() {
			return $oid;
		}

		public void set$oid(String $oid) {
			this.$oid = $oid;
		}

	}

	class Person {
		String id;

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
