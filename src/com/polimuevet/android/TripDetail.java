package com.polimuevet.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TripDetail extends ActionBarActivity implements OnClickListener {

	boolean cerrar = false;
	private TextView destino;
	private TextView origen;
	private TextView fecha;
	private TextView plazas;
	private TextView precio;
	private TextView equipaje;
	private Button unirse;
	private ProgressBar progreso;
	private HttpConductor get;
	String id_conductor;
	private HttpUnirse put;
	private String tripId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trip_detail);
		// menu_lateral(R.array.lateral_trayectos, this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//getSupportActionBar().setHomeButtonEnabled(true);
		progreso = (ProgressBar) findViewById(R.id.carga);
		Bundle b = getIntent().getExtras();

		origen = (TextView) findViewById(R.id.origen);
		destino = (TextView) findViewById(R.id.destino);
		fecha = (TextView) findViewById(R.id.fecha);
		plazas = (TextView) findViewById(R.id.plazas);
		precio = (TextView) findViewById(R.id.precio);
		equipaje = (TextView) findViewById(R.id.equipaje);
		
		tripId=b.getString("_id");

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
		id_conductor=b.getString("Creador_id");
		Log.d("ID Conductor", id_conductor);
		unirse=(Button)findViewById(R.id.unirse);
		unirse.setOnClickListener(this);
		
		conectar();

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

	/**
	 * Intenta conectar con el servidor si el dispositivo tiene conexión a
	 * internet
	 * 
	 */
	public void conectar() {
		if (isOnline()) {
			cargar_conductor();
			// reintentar.setVisibility(View.GONE);
		} else {
			Toast notification = Toast.makeText(this,
					"Activa tu conexión a internet", Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			progreso.setVisibility(View.GONE);
		}
	}
	

	private void cargar_conductor() {

		// obtener conductor

		 get = new HttpConductor(TripDetail.this, progreso);
		//get.execute("http://polimuevet.eu01.aws.af.cm/api/gettrips");
		get.execute("http://192.168.1.12:3000/api/getuser/"+id_conductor);

	}
	
	
	public void unir_trayecto() {
		if (isOnline()) {
			unir();
			// reintentar.setVisibility(View.GONE);
		} else {
			Toast notification = Toast.makeText(this,
					"Activa tu conexión a internet", Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			progreso.setVisibility(View.GONE);
		}
	}
	

	private void unir() {

		// obtener conductor

		 put = new HttpUnirse(TripDetail.this, progreso,"",tripId);
		//get.execute("http://polimuevet.eu01.aws.af.cm/api/gettrips");
		put.execute("http://192.168.1.12:3000/api/applytrip");

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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		unir_trayecto();
		
	}

}
