package com.polimuevet.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.polimuevet.android.R.color;

public class Addtrip extends ActivityMenuLateral implements OnClickListener {

	static final int TIME_DIALOG_ID = 999;
	boolean cerrar = false;
	Button crear;
	ArrayAdapter<String> adapter;
	protected int hour;
	protected int minute;
	Respuesta respuesta;
	ViewPager pager;
	MyPageAdapter padapter;

	List<Fragment> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_addtrip);
		menu_lateral(R.array.lateral_addtrip, this);
		padapter = new MyPageAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.viewPager);
		getSupportActionBar().setStackedBackgroundDrawable(
				new ColorDrawable(color.Orange));

		// Asigno memoria a la lista de fragments
		fragments = new ArrayList<Fragment>();
		// Agrego los fragments
		fragments.add(new FragmentNewTripForm());
		fragments.add(new FragmentNewTripRestricciones());

		pager.setAdapter(padapter);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				getSupportActionBar().setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		TabListener tabListener = new TabListener() {

			@Override
			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub
				pager.setCurrentItem(arg0.getPosition());
			}

			@Override
			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
				// TODO Auto-generated method stub

			}
		};

		Tab tab = getSupportActionBar().newTab();
		tab.setText(R.string.trip_informacion);
		tab.setTabListener(tabListener);

		getSupportActionBar().addTab(tab);

		tab = getSupportActionBar().newTab();
		tab.setText(R.string.trip_restricciones);
		tab.setTabListener(tabListener);
		getSupportActionBar().addTab(tab);

		crear = (Button) findViewById(R.id.crear);
		crear.setOnClickListener(this);

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

			FragmentNewTripForm f = (FragmentNewTripForm) padapter.getItem(0);
			Log.d("TEST visible", "" + f.isVisible());
			// f.get
			f.sethour(selectedHour);
			f.setminute(selectedMinute);
			f.fijar_tiempo(selectedHour, selectedMinute);

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.crear) {
			FragmentNewTripForm f = (FragmentNewTripForm) padapter.getItem(0);
			if (f.comprobar_datos()) {
				conectar();
			}

		} else {
			showDialog(TIME_DIALOG_ID);
		}

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
					R.string.conexion_internet, Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			setProgressBarIndeterminateVisibility(false);
		}
	}

	/**
	 * Conecta con el servidor para intentar registrar un usuario nuevo
	 */
	private void register_trip() {

		HttpNewTrip post = new HttpNewTrip(Addtrip.this, recoger_datos());
		post.execute(Config.URL + "/api/newtrip");

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
	 * Recoge la información que ha introducido el usuario en todos los campos
	 * y lo prepara para enviarlo en el POST creando pares de valores
	 * 
	 * @return la lista de pares clave valor
	 */
	public List<NameValuePair> recoger_datos() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		// Recoger los datos del primer Fragment
		FragmentNewTripForm f = (FragmentNewTripForm) padapter.getItem(0);

		nameValuePairs.add(new BasicNameValuePair("origen", f.origen.getText()
				.toString()));
		nameValuePairs.add(new BasicNameValuePair("destino", f.destino
				.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("num_plazas", f.plazas
				.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("precio", f.precio
				.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("equipaje",
				(String) f.equipaje.getSelectedItem()));
		nameValuePairs.add(new BasicNameValuePair("fecha_time", f
				.obtener_fecha()));
		
		Log.d("FECHAAAA", f.obtener_fecha());

		// Recoger datos del segundo fragment
		FragmentNewTripRestricciones frag = (FragmentNewTripRestricciones) padapter
				.getItem(1);

		if (frag.cbNoFumadores.isChecked()) {
			nameValuePairs.add(new BasicNameValuePair(
					"restricciones[no_fumadores]", "true"));
		}

		if (frag.cbNoComida.isChecked()) {
			nameValuePairs.add(new BasicNameValuePair(
					"restricciones[no_comida]", "true"));
		}

		if (frag.cbNoAnimales.isChecked()) {
			nameValuePairs.add(new BasicNameValuePair(
					"restricciones[no_animales]", "true"));
		}

		if (frag.cbAlumnos.isChecked()) {
			nameValuePairs.add(new BasicNameValuePair("tipo_pasajero[alumnos]",
					"true"));
		}

		if (frag.cbProfesores.isChecked()) {
			nameValuePairs.add(new BasicNameValuePair(
					"tipo_pasajero[profesores]", "true"));
		}

		if (frag.cbPersonal.isChecked()) {
			nameValuePairs.add(new BasicNameValuePair(
					"tipo_pasajero[personal]", "true"));
		}

		nameValuePairs.add(new BasicNameValuePair("observaciones",
				frag.etObservaciones.getText().toString()));

		// Creador ID
		SharedPreferences preferences = getSharedPreferences("sesion",
				Context.MODE_PRIVATE);
		String PersonId = preferences.getString("user", "");

		nameValuePairs.add(new BasicNameValuePair("creador_id", PersonId));


		return nameValuePairs;
	}

	private class MyPageAdapter extends FragmentPagerAdapter {

		public MyPageAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return fragments.get(position);
		}

		@Override
		public int getCount() {

			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {

			switch (position) {
			case 0:
				return "Fragment";
			case 1:
				return "Fragment";

			}
			return null;
		}

	}

}
