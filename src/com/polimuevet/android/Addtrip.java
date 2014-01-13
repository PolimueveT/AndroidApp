package com.polimuevet.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class Addtrip extends ActivityMenuLateral implements TextWatcher,
		OnClickListener {
	static final int TIME_DIALOG_ID = 999;
	AutoCompleteTextView destino;
	AutoCompleteTextView origen;
	boolean cerrar = false;
	EditText hora;
	EditText plazas;
	EditText precio;

	Button crear;
	private int year;
	private int month;
	private int day;
	private Spinner Items;
	private Spinner equipaje;
	List<String> SpinnerArray;
	List<String> SpinnerArrayeq;
	ArrayAdapter<String> adaptereq;
	ArrayList<String> calles = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	protected int hour;
	protected int minute;

	Respuesta respuesta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_addtrip);
		menu_lateral(R.array.lateral_addtrip, this);
		
		hora = (EditText) findViewById(R.id.hora);
		hora.setFocusable(false);
		hora.setClickable(true);
		// hora.setEnabled(false);
		hora.setOnClickListener(this);
		
		plazas = (EditText) findViewById(R.id.plazas);
		precio = (EditText) findViewById(R.id.precio);

		crear = (Button) findViewById(R.id.crear);
		crear.setOnClickListener(this);
		Items = (Spinner) findViewById(R.id.spinnerdia);
		equipaje = (Spinner) findViewById(R.id.spinnermaleta);
		configurar_spinner();
		configurar_spinner_eq();

		origen = (AutoCompleteTextView) findViewById(R.id.origen);
		origen.addTextChangedListener(this);
		destino = (AutoCompleteTextView) findViewById(R.id.destino);
		destino.addTextChangedListener(this);
		cargarcalles();
		cargarfacultades();
		origen.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, calles));

		destino.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, calles));
	}

	public void configurar_spinner_eq() {
		SpinnerArrayeq = new ArrayList<String>();
		SpinnerArrayeq.add("Mochila");
		SpinnerArrayeq.add("Maleta");
		
		adaptereq = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, SpinnerArrayeq);
		adaptereq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
		today=day + "/" + (month+1) + "/" + year;
		for (int i = 0; i < 15; i++) {
			c.add(Calendar.DAY_OF_MONTH, +1);
			day = c.get(Calendar.DAY_OF_MONTH);
			month = c.get(Calendar.MONTH);
			year = c.get(Calendar.YEAR);
			int mes = month + 1;
			
			if(i==0){
				tomorrow=day + "/" + mes + "/" + year;
			}
			else {
				SpinnerArray.add(day + "/" + mes + "/" + year);
			}
			
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, SpinnerArray);
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

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

		switch (i) {
		case 0:
			cerrar = true;
			Intent intentb = new Intent(Addtrip.this, Busqueda.class);
			startActivity(intentb);
			break;
		case 1:
			cerrar = true;
			Intent intent = new Intent(Addtrip.this, EstadoParking.class);
			startActivity(intent);
			break;
		case 2:
			cerrar_sesion();

			break;

		default:
			break;
		}
		mDrawer.closeDrawers();
	}

	private void cerrar_sesion() {
		cerrar = true;
		SharedPreferences preferences = getSharedPreferences("sesion",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("login", false);
		editor.putString("user", "");
		editor.commit();

		Intent intent = new Intent(Addtrip.this, Portada.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle item selection
		switch (item.getItemId()) {

		case android.R.id.home:
			if (mDrawer.isDrawerOpen(mDrawerOptions)) {
				mDrawer.closeDrawers();
			} else {
				mDrawer.openDrawer(mDrawerOptions);
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerOptions);
		// menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (cerrar) {
			finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.addtrip, menu);
		return true;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					true);

		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			// set current time into textview
			hora.setText(new StringBuilder().append(pad(hour)).append(":")
					.append(pad(minute)));

		}
	};
	private String today;
	private String tomorrow;

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.crear) {
			if (comprobar_datos()) {
				conectar();
			}
		} else {
			showDialog(TIME_DIALOG_ID);
		}

	}
	
	private boolean comprobar_hora() {
		String shora = hora.getText().toString();

		if (shora.compareTo("")!=0) {
			return true;
		}
		return false;
	}

	public boolean comprobar_plazas() {
		String plz = plazas.getText().toString();
		int np=Integer.parseInt(plz);
		if (np > 0 && np<9) {
			return true;
		}
		return false;

	}
	
	
	public boolean comprobar_precio() {
		String price = precio.getText().toString();
		Float pr=Float.parseFloat(price);
		if (pr<=3.0) {
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
				Toast notification = Toast.makeText(this,
						"Error en la hora",
						Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();
				
				ok=false;
			}
							
			

			
			else if (!comprobar_plazas()) {
				
				Toast notification = Toast.makeText(this,
						"Error en el número de plazas",
						Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();
				ok=false;
				
			}
			
			else if (!comprobar_precio()) {
				// toast contraseÃ±as no coinciden
				Toast notification = Toast.makeText(this,
						"Error en el precio",
						Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();
				ok=false;
				
			}
			
		}

		else {
			// toast rellena campos
			Toast notification = Toast.makeText(this,
					"Debes rellenar todos los campos", Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			setProgressBarIndeterminateVisibility(false);
		}

		return ok;
	}

	/**
	 * Intenta regristar un nuevo usuario si no se puede muestra toast con error
	 * 
	 */
	public void conectar() {
		if (isOnline()) {
			crear.setEnabled(false);
			register_trip();

		} else {
			Toast notification = Toast.makeText(this,
					"Activa tu conexión a internet", Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			setProgressBarIndeterminateVisibility(false);
		}
	}

	/**
	 * Conecta con el servidor para intentar registrar un usuario nuevo
	 */
	private void register_trip() {

		HttpNewTrip post = new HttpNewTrip(Addtrip.this,recoger_datos());
		//post.execute("http://polimuevet.eu01.aws.af.cm/api/newtrip");
		 post.execute("http://192.168.1.10:3000/api/newtrip");

	}

	/**
	 * Comprueba si el dispositivo tiene conexión a internet
	 * 
	 * @return true si tiene conexión ,false en caso contrario
	 */

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Recoge la información que ha introducido el usuario en todos los campos y
	 * lo prepara para enviarlo en el POST creando pares de valores
	 * 
	 * @return la lista de pares clave valor
	 */
	public List<NameValuePair> recoger_datos() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("origen", origen.getText()
				.toString()));
		nameValuePairs.add(new BasicNameValuePair("destino", destino.getText()
				.toString()));
		
	
			nameValuePairs.add(new BasicNameValuePair("fecha_time", obtener_fecha()));
		
		
		
		nameValuePairs.add(new BasicNameValuePair("precio", precio.getText()
				.toString()));
		
		String Tequipaje =equipaje.getSelectedItem().toString();
		
		nameValuePairs.add(new BasicNameValuePair("equipaje", Tequipaje));

		nameValuePairs.add(new BasicNameValuePair("num_plazas",plazas.getText().toString()));
		
		SharedPreferences preferences = getSharedPreferences("sesion",
				Context.MODE_PRIVATE);
		String PersonId=preferences.getString("user","");
		
		nameValuePairs.add(new BasicNameValuePair("creador_id", PersonId));

		return nameValuePairs;
	}

	private String obtener_fecha() {
		
		String dia=Items.getSelectedItem().toString();
		if (dia.compareTo("Hoy")==0){
			dia=today;
		}
		else if(dia.compareTo("Mañana")==0){
			dia=tomorrow;
		}
		Date fechatime;
		try {
						
			fechatime = new SimpleDateFormat("dd/MM/yyyy - HH:mm").parse(dia+" - "+pad(hour)+":"+pad(minute));
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss" ).format(fechatime);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dia;
	}
	
	

}
