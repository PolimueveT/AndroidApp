package com.polimuevet.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class EstadoParking extends ActionBarActivity implements
		AdapterView.OnItemClickListener {
	TextView iptv;
	EditText ip;
	String server;
	ParkingList lista = new ParkingList();
	private ProgressBar progreso;
	boolean cerrar = false;
	ListView ParkingsView;
	private AdaptadorParking Padapter;
	 private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;
	  private String[] navMenuTitles;
	//private Object mTitle;
	private CharSequence mDrawerTitle;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_estado_parking);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		progreso = (ProgressBar) findViewById(R.id.carga);

		ParkingsView = (ListView) findViewById(R.id.parkings);
		mDrawerTitle = getTitle();
		menu_lateral();

		conectar();

	}

	/**
	 * Crea el adapter para el Listview lista , este adapter se encarga de poner
	 * un layout a cada elemento de la lista, de colocar el color del estado del
	 * parking , de calcular plazas disponibles etc.
	 */
	public void asociarAdapter() {
		Padapter = new AdaptadorParking(this, R.layout.elemento_fila,
				lista.getParkings());
		ParkingsView.setAdapter(Padapter);
	}

	/**
	 * Intenta conectar con el parking si el dispositivo tiene conexión a
	 * internet
	 * 
	 */
	public void conectar() {
		if (isOnline()) {
			cargar_parkings();
			// reintentar.setVisibility(View.GONE);
		} else {
			Toast notification = Toast.makeText(this,
					"Activa tu conexión a internet", Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			progreso.setVisibility(View.GONE);
		}
	}

	/**
	 * Conecta con el servidor para obtener los datos del parking y colocarlos
	 * en el listview lista
	 */
	private void cargar_parkings() {

		// obtener estado parkings

		HttpParkings get = new HttpParkings(EstadoParking.this,progreso);
		get.execute("http://polimuevet.eu01.aws.af.cm/api/parking");

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.estado_parking, menu);
		return true;
	}

	/**
	 * Prepara el menu lateral para su correcta visualización
	 */

	public void menu_lateral() {
		mDrawerOptions = (ListView) findViewById(R.id.left_drawer);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		 // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.lateral_parking);
		mDrawerOptions
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,
						android.R.id.text1, navMenuTitles));
		mDrawerOptions.setOnItemClickListener(this);
	    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
                R.drawable.ic_navigation_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                ActivityCompat.invalidateOptionsMenu(EstadoParking.this);
            }
 
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                ActivityCompat.invalidateOptionsMenu(EstadoParking.this);
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		
		switch (i) {
		case 0:
			//Intent intent = new Intent(Portada.this, Registro.class);
			//startActivity(intent);
			break;
		case 1:
			cerrar=true;
			Intent intent = new Intent(EstadoParking.this, Busqueda.class);
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

	
	/**
	 * Prepara el activity y el shared preference para terminar la sesión , abre la pantalla de login/registro
	 */
	private void cerrar_sesion() {
		cerrar=true;
		SharedPreferences preferences = getSharedPreferences("sesion",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("login", false);
		editor.putString("user", "");
		editor.commit();
		
		Intent intent = new Intent(EstadoParking.this, Portada.class);
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
    public void setTitle(CharSequence title) {
       
        getSupportActionBar().setTitle(mDrawerTitle);
    }
 
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (cerrar) {
			finish();
		}

	}

	

}
