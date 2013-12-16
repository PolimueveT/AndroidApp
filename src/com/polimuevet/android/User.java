package com.polimuevet.android;

public class User {
	String Nombre;
	String Email;
	String Pass;
	String Passconf;
	String UserType;
	String Sexo;
	String FechaNacimiento;
	String Poblacion;
	String Escuela;
	String Observaciones;
	String Telefono;
	String Coche;
	String _id;

	public User(String nombre, String email, String pass, String passconf,
			String userType, String sexo, String fechaNacimiento,
			String poblacion, String escuela, String observaciones,
			String telefono, String coche, String _id) {
		super();
		Nombre = nombre;
		Email = email;
		Pass = pass;
		Passconf = passconf;
		UserType = userType;
		Sexo = sexo;
		FechaNacimiento = fechaNacimiento;
		Poblacion = poblacion;
		Escuela = escuela;
		Observaciones = observaciones;
		Telefono = telefono;
		Coche = coche;
		this._id = _id;
	}

	public User() {

	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPass() {
		return Pass;
	}

	public void setPass(String pass) {
		Pass = pass;
	}

	public String getPassconf() {
		return Passconf;
	}

	public void setPassconf(String passconf) {
		Passconf = passconf;
	}

	public String getUserType() {
		return UserType;
	}

	public void setUserType(String userType) {
		UserType = userType;
	}

	public String getSexo() {
		return Sexo;
	}

	public void setSexo(String sexo) {
		Sexo = sexo;
	}

	public String getFechaNacimiento() {
		return FechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		FechaNacimiento = fechaNacimiento;
	}

	public String getPoblacion() {
		return Poblacion;
	}

	public void setPoblacion(String poblacion) {
		Poblacion = poblacion;
	}

	public String getEscuela() {
		return Escuela;
	}

	public void setEscuela(String escuela) {
		Escuela = escuela;
	}

	public String getObservaciones() {
		return Observaciones;
	}

	public void setObservaciones(String observaciones) {
		Observaciones = observaciones;
	}

	public String getTelefono() {
		return Telefono;
	}

	public void setTelefono(String telefono) {
		Telefono = telefono;
	}

	public String getCoche() {
		return Coche;
	}

	public void setCoche(String coche) {
		Coche = coche;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

}
