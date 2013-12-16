package com.polimuevet.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TripDetail extends ActionBarActivity {

	boolean cerrar = false;
	private TextView destino;
	private TextView origen;
	private TextView fecha;
	private TextView plazas;
	private TextView precio;
	private TextView equipaje;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip_detail);
		// menu_lateral(R.array.lateral_trayectos, this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getSupportActionBar().setHomeButtonEnabled(true);
		
		Bundle b = getIntent().getExtras();

		origen = (TextView) findViewById(R.id.origen);
		destino = (TextView) findViewById(R.id.destino);
		fecha = (TextView) findViewById(R.id.fecha);
		plazas = (TextView) findViewById(R.id.plazas);
		precio = (TextView) findViewById(R.id.precio);
		equipaje = (TextView) findViewById(R.id.equipaje);

		origen.setText(b.getString("Origen"));
		destino.setText(b.getString("Destino"));
		plazas.setText("Plazas disponibles:" + b.getInt("Num_plazas"));
		precio.setText("" + b.getFloat("Precio_plaza") + "€");
		color_precio(precio, b.getFloat("Precio_plaza"));
		equipaje.setText(b.getString("Max_tamanyo_equipaje"));
		try {
			fecha.setText(formato_fecha(b.getString("Fecha_time")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String formato_fecha(String fecha_servidor) throws ParseException {
		Object parsedDateInstance = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss").parse(fecha_servidor);
		String formattedDate = new SimpleDateFormat("MM/dd/yyyy - HH:mm")
				.format(parsedDateInstance);
		return formattedDate;

	}

	private void color_precio(TextView precio, Float Precio_plaza) {
		// TODO Auto-generated method stub
		if (Precio_plaza <= 0.75) {
			precio.setTextColor(getResources().getColor(R.color.Icverde));

		} else if (Precio_plaza > 0.75 && Precio_plaza < 2.0) {
			precio.setTextColor(getResources().getColor(R.color.Orange));
		} else if (Precio_plaza >= 2.0) {
			precio.setTextColor(getResources().getColor(R.color.Icrojo));
		}

	}

	// @Override
	// public void onItemClick(AdapterView<?> adapterView, View view, int i,
	// long l) {
	//
	// switch (i) {
	// case 0:
	// // Intent intent = new Intent(Portada.this, Registro.class);
	// // startActivity(intent);
	// break;
	// case 1:
	// cerrar = true;
	// Intent intentb = new Intent(TripDetail.this, Busqueda.class);
	// startActivity(intentb);
	// break;
	// case 2:
	// cerrar = true;
	// Intent intente = new Intent(TripDetail.this, EstadoParking.class);
	// startActivity(intente);
	//
	// break;
	// case 3:
	// cerrar_sesion();
	//
	// break;
	//
	// default:
	// break;
	// }
	// mDrawer.closeDrawers();
	//
	//
	// }
	private void cerrar_sesion() {
		cerrar = true;
		SharedPreferences preferences = getSharedPreferences("sesion",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("login", false);
		editor.putString("user", "");
		editor.commit();
		Intent intent = new Intent(TripDetail.this, Portada.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		// if (mDrawerToggle.onOptionsItemSelected(item)) {
		// return true;
		// }

		// Handle item selection
		switch (item.getItemId()) {

		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
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
		// boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerOptions);
		// menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPause() {

		super.onPause();
		/*
		 * if (cerrar) { finish(); }
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trip_detail, menu);
		return true;
	}

}
