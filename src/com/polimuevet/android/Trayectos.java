package com.polimuevet.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Trayectos extends ActivityMenuLateral {

	public boolean cerrar=false;
	private ProgressBar progreso;
	ListView TripsView;
	TripList lista = new TripList();
	private AdaptadorTrips Tadapter;
	private HttpSearch post;
	Bundle datosBusq;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datosBusq = getIntent().getExtras();
		setContentView(R.layout.activity_trayectos);
		menu_lateral(R.array.lateral_btrayectos, this);
		progreso = (ProgressBar) findViewById(R.id.carga);
		
		//TripsView = (ListView) findViewById(R.id.trayectos);
		conectar();
	}
	/**
	 * Crea el adapter para el Listview lista , este adapter se encarga de poner
	 * un layout a cada elemento de la lista
	 */
//	public void asociarAdapter() {
//		Tadapter = new AdaptadorTrips(this, R.layout.parking_row,
//				lista.getTrips());
//		TripsView.setAdapter(Tadapter);
//	}
	/**
	 * Intenta conectar con el servidor si el dispositivo tiene conexión a
	 * internet
	 * 
	 */
	public void conectar() {
		if (isOnline()) {
			cargar_trayectos();
			// reintentar.setVisibility(View.GONE);
		} else {
			Toast notification = Toast.makeText(this,
					R.string.conexion_internet, Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			progreso.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Conecta con el servidor para obtener los datos del parking y colocarlos
	 * en el listview lista
	 */
	private void cargar_trayectos() {
		
		// obtener trayectos 
		
		post = new HttpSearch(Trayectos.this, recoger_datos(datosBusq));
		post.execute(Config.URL + "/api/getfilteredtrips");

	}
	
	/**
	 * Comprueba si el dispositivo tiene conexión a internet
	 * 
	 * @return true si tiene conexi�n ,false en caso contrario
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
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
	
		switch (i) {
		case 0:
			cerrar=true;
			 Intent intent = new Intent(Trayectos.this, Addtrip.class);
			 startActivity(intent);
			break;
		case 1:
			cerrar = true;
			Intent intentb = new Intent(Trayectos.this, Busqueda.class);
			startActivity(intentb);
			break;
		case 2:
			cerrar = true;
			Intent intentc = new Intent(Trayectos.this, MisTrayectos.class);
			startActivity(intentc);
			
			break;
		case 3:
			cerrar = true;
			Intent intente = new Intent(Trayectos.this, EstadoParking.class);
			startActivity(intente);

			break;
		case 4:
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
		Intent intent = new Intent(Trayectos.this, Portada.class);
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
			
		case R.id.action_refresh:
			conectar();
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
		 menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (cerrar) {
			Log.d("pause", "trayectos se cierra");
			finish();
		}
		
		post.cancel(true);

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trayectos, menu);
		return true;
	}
	
	
	private List<NameValuePair> recoger_datos(Bundle b) {
		SharedPreferences prefs =
			     getSharedPreferences("ultimabusqueda",Context.MODE_PRIVATE);
			 
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		if(b!=null){
		nameValuePairs.add(new BasicNameValuePair("origen", b.getString("origen")));
		nameValuePairs.add(new BasicNameValuePair("destino", b.getString("destino")));
		nameValuePairs.add(new BasicNameValuePair("Fecha", b.getString("Fecha")));
		nameValuePairs.add(new BasicNameValuePair("Hora", b.getString("Hora")));
		
		
			SharedPreferences.Editor editor = prefs.edit();
		 editor.putString("origen",b.getString("origen"));
		 editor.putString("destino", b.getString("destino"));
		 editor.putString("Fecha", b.getString("Fecha"));
		 editor.putString("Hora", b.getString("Hora"));
		 editor.commit();
		
		}
		else{
			nameValuePairs.add(new BasicNameValuePair("origen", prefs.getString("origen", "")));
			nameValuePairs.add(new BasicNameValuePair("destino",prefs.getString("destino", "")));
			nameValuePairs.add(new BasicNameValuePair("Fecha", prefs.getString("Fecha", "")));
			nameValuePairs.add(new BasicNameValuePair("Hora", prefs.getString("Hora", "")));
		}
		return nameValuePairs;
	}

}
