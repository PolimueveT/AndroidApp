package com.polimuevet.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class FragmentnewtripA extends Fragment implements TextWatcher,
		OnClickListener {
	static final int TIME_DIALOG_ID = 999;
	private static EditText hora;
	private EditText plazas;
	private EditText precio;
	private Spinner Items;
	private Spinner equipaje;
	private AutoCompleteTextView origen;
	private AutoCompleteTextView destino;
	private ArrayList<String> SpinnerArrayeq;
	private ArrayList<String> SpinnerArray;
	private int year;
	private int month;
	private int day;
	private String today;
	private String tomorrow;
	ArrayList<String> calles = new ArrayList<String>();
	private ArrayAdapter<String> adaptereq;
	private ArrayAdapter<String> adapter;
	protected int hour;
	protected int minute;
	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		view = inflater.inflate(R.layout.fragment_creartrip_a, null);
	
		hora = (EditText) view.findViewById(R.id.hora);
		hora.setFocusable(false);
		hora.setClickable(true);
		
		hora.setOnClickListener(this);

		plazas = (EditText) view.findViewById(R.id.plazas);
		precio = (EditText) view.findViewById(R.id.precio);

		Items = (Spinner) view.findViewById(R.id.spinnerdia);
		equipaje = (Spinner) view.findViewById(R.id.spinnermaleta);
		configurar_spinner();
		configurar_spinner_eq();

		origen = (AutoCompleteTextView) view.findViewById(R.id.origen);
		origen.addTextChangedListener(this);
		destino = (AutoCompleteTextView) view.findViewById(R.id.destino);
		destino.addTextChangedListener(this);
		cargarcalles();
		cargarfacultades();
		origen.setAdapter(new ArrayAdapter<String>(getActivity()
				.getApplicationContext(),
				android.R.layout.simple_dropdown_item_1line, calles));

		destino.setAdapter(new ArrayAdapter<String>(getActivity()
				.getApplicationContext(),
				android.R.layout.simple_dropdown_item_1line, calles));

		return view;
	}

	public void configurar_spinner_eq() {
		SpinnerArrayeq = new ArrayList<String>();
		SpinnerArrayeq.add("Mochila");
		SpinnerArrayeq.add("Maleta");

		adaptereq = new ArrayAdapter<String>(getActivity()
				, android.R.layout.simple_spinner_item,
				SpinnerArrayeq);
		adaptereq
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		equipaje.setAdapter(adaptereq);
	}

	public void configurar_spinner() {
		SpinnerArray = new ArrayList<String>();
		SpinnerArray.add("Hoy");
		SpinnerArray.add("Mañana");
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		today = day + "/" + (month + 1) + "/" + year;
		for (int i = 0; i < 15; i++) {
			c.add(Calendar.DAY_OF_MONTH, +1);
			day = c.get(Calendar.DAY_OF_MONTH);
			month = c.get(Calendar.MONTH);
			year = c.get(Calendar.YEAR);
			int mes = month + 1;

			if (i == 0) {
				tomorrow = day + "/" + mes + "/" + year;
			} else {
				SpinnerArray.add(day + "/" + mes + "/" + year);
			}

		}
		adapter = new ArrayAdapter<String>(getActivity()
				, android.R.layout.simple_spinner_item,
				SpinnerArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Items.setAdapter(adapter);
	}



	public void cargarcalles() {
		try {
			InputStream inputStream = getResources().openRawResource(
					R.raw.calles);

			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setInput(inputStream, null);
			int eventType = XmlPullParser.START_DOCUMENT;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equalsIgnoreCase("calle")) {

						calles.add(parser.getAttributeValue(null, "nombre"));

					}

				}
				eventType = parser.next();
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void cargarfacultades() {
		try {
			InputStream inputStream = getResources().openRawResource(
					R.raw.facultades);

			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setInput(inputStream, null);
			int eventType = XmlPullParser.START_DOCUMENT;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equalsIgnoreCase("facultad")) {

						calles.add(parser.getAttributeValue(null, "nombre"));

					}

				}
				eventType = parser.next();
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String devuelve() {
		EditText origen=(EditText) view.findViewById(R.id.origen);
		return origen.getText().toString();
	}
	
	public void sethour(int hour){
		this.hour=hour;
	}
	
	public void setminute(int minute){
		this.minute=minute;
	}
	
	public void fijar_tiempo(int h,int m){
		Log.d("Fijando tiempo","tiempo fijado a :"+h+" "+m);
	
		hora.setText(new StringBuilder().append(pad(h)).append(":").append(pad(m)));
	}

	@Override
	public void onClick(View arg0) {

		getActivity().showDialog(TIME_DIALOG_ID);
	}

	private boolean comprobar_hora() {
		String shora = hora.getText().toString();

		if (shora.compareTo("") != 0) {
			return true;
		}
		return false;
	}

	public boolean comprobar_plazas() {
		String plz = plazas.getText().toString();
		int np = Integer.parseInt(plz);
		if (np > 0 && np < 9) {
			return true;
		}
		return false;

	}

	public boolean comprobar_precio() {
		String price = precio.getText().toString();
		Float pr = Float.parseFloat(price);
		if (pr <= 3.0) {
			return true;
		}
		return false;
	}

	/**
	 * Comprueba si todos los campos tienen información "correcta" para enviar
	 * al servidor
	 * 
	 * @return true si la info es correcta , false en caso contrario
	 */
	public boolean comprobar_datos() {
		boolean ok = true;

		// campos vacios
		if (origen.getText().toString().compareTo("") == 0) {

			ok = false;
		} else if (destino.getText().toString().compareTo("") == 0) {

			ok = false;
		} else if (hora.getText().toString().compareTo("") == 0) {

			ok = false;
		} else if (plazas.getText().toString().compareTo("") == 0) {

			ok = false;
		}

		else if (precio.getText().toString().compareTo("") == 0) {

			ok = false;
		}

		// si no hay ningún campo vacio
		if (ok) {
			if (!comprobar_hora()) {
				Toast notification = Toast.makeText(getActivity()
						.getApplicationContext(), "Error en la hora",
						Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();

				ok = false;
			}

			else if (!comprobar_plazas()) {

				Toast notification = Toast.makeText(getActivity()
						.getApplicationContext(),
						"Error en el número de plazas", Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();
				ok = false;

			}

			else if (!comprobar_precio()) {
				// toast contraseÃ±as no coinciden
				Toast notification = Toast.makeText(getActivity()
						.getApplicationContext(), "Error en el precio",
						Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();
				ok = false;

			}

		}

		else {
			// toast rellena campos
			Toast notification = Toast.makeText(getActivity()
					.getApplicationContext(),
					"Debes rellenar todos los campos", Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			// setProgressBarIndeterminateVisibility(false);
		}

		return ok;
	}

	private String obtener_fecha() {

		String dia = Items.getSelectedItem().toString();
		if (dia.compareTo("Hoy") == 0) {
			dia = today;
		} else if (dia.compareTo("Mañana") == 0) {
			dia = tomorrow;
		}
		Date fechatime;
		try {

			fechatime = new SimpleDateFormat("dd/MM/yyyy - HH:mm").parse(dia
					+ " - " + pad(hour) + ":" + pad(minute));
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
					.format(fechatime);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dia;
	}


	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

}