package com.polimuevet.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

@SuppressLint("NewApi")
public class Busqueda extends ActivityMenuLateral implements TextWatcher, OnClickListener {
	AutoCompleteTextView destino;
	AutoCompleteTextView origen;
	ArrayList<String> calles = new ArrayList<String>();
	ArrayList<String> facultades = new ArrayList<String>();
	private RadioGroup radiomodoGroup;
	private RadioButton radiomodoButton;
	 ArrayAdapter<String> adapter ;
    List<String> SpinnerArray;
	private boolean cerrar = false;
	Button buscar;
	private int year;
	private int month;
	private int day;
	private Spinner Items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busqueda);
		menu_lateral(R.array.lateral_busqueda, this);
		radiomodoGroup = (RadioGroup) findViewById(R.id.radioGroupmodo);
		radiomodoGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// checkedId is the RadioButton selected
						radiomodoButton = (RadioButton) findViewById(checkedId);
						switch (checkedId) {
						case R.id.radioida:

							break;
						case R.id.radiovuelta:
							
							break;
						default:
							break;
						}
					}
				});
		
		
		
		buscar=(Button)findViewById(R.id.buscar);
		buscar.setOnClickListener(this);
		 Items = (Spinner) findViewById(R.id.spinnerdia);
		configurar_spinner();

		origen = (AutoCompleteTextView) findViewById(R.id.origen);
		origen.addTextChangedListener(this);
		cargarcalles();
		origen.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, calles));
		destino = (AutoCompleteTextView) findViewById(R.id.destino);
		destino.addTextChangedListener(this);
		cargarfacultades();
		destino.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, facultades));

	}

	public void configurar_spinner(){
		 SpinnerArray =  new ArrayList<String>();
		    SpinnerArray.add("Hoy");
		    SpinnerArray.add("Mañana");
		    final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			for(int i=0;i<15;i++){
			c.add(Calendar.DAY_OF_MONTH,+1);
			day = c.get(Calendar.DAY_OF_MONTH);
			month = c.get(Calendar.MONTH);
			year = c.get(Calendar.YEAR);
			int mes=month+1;
			SpinnerArray.add(day+"/"+mes+"/"+year);
			}
		    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SpinnerArray);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    
		    Items.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View arg0) {
	
		if (arg0.getId() == R.id.buscar) {
			cerrar=true;
			//falta recoger los campos y pasar los valores al activity Trayectos para filtrar
			Intent intent = new Intent(Busqueda.this, Trayectos.class);
			startActivity(intent);
		}
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

						facultades
								.add(parser.getAttributeValue(null, "nombre"));

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

	/**
	 * Pone en las variables de los RadioButtons el Radiobutton elegido en cada
	 * RadioGroup
	 */
	public void obtener_radiobuttons() {

		int selectedId = radiomodoGroup.getCheckedRadioButtonId();
		radiomodoButton = (RadioButton) findViewById(selectedId);

	}

	public void intercambiar() {

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

		switch (i) {
		case 0:
			cerrar = true;
			Intent intentadd = new Intent(Busqueda.this, Addtrip.class);
			startActivity(intentadd);
			break;
		case 1:
			cerrar = true;
			Intent intent = new Intent(Busqueda.this, EstadoParking.class);
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

		Intent intent = new Intent(Busqueda.this, Portada.class);
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
		getMenuInflater().inflate(R.menu.busqueda, menu);
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



}
