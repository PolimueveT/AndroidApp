package com.polimuevet.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

public class FragmentNewTripRestricciones extends Fragment{

	private View view;

	public EditText etObservaciones;
	public CheckBox cbNoFumadores;
	public CheckBox cbNoComida;
	public CheckBox cbNoAnimales;
	public CheckBox cbAlumnos;
	public CheckBox cbProfesores;
	public CheckBox cbPersonal;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		view = inflater.inflate(R.layout.fragment_creartrip_restricciones, null);
		
		etObservaciones = (EditText) view.findViewById(R.id.etObservaciones);
		cbNoFumadores = (CheckBox) view.findViewById(R.id.cbNoFumadores);
		cbNoAnimales = (CheckBox) view.findViewById(R.id.cbNoAnimales);
		cbNoComida = (CheckBox) view.findViewById(R.id.cbNoComida);
		cbAlumnos = (CheckBox) view.findViewById(R.id.cbAlumnos);
		cbPersonal = (CheckBox) view.findViewById(R.id.cbPersonal);
		cbProfesores = (CheckBox) view.findViewById(R.id.cbProfesores);
		
		return view;
	}
	
}
