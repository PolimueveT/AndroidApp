package com.polimuevet.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

public class Busqueda extends ActionBarActivity implements TextWatcher,
		AdapterView.OnItemClickListener {
	AutoCompleteTextView destino;
	AutoCompleteTextView origen;
	ArrayList<String> calles = new ArrayList<String>();
	ArrayList<String> facultades = new ArrayList<String>();
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;
	private ListView mDrawerOptions;
	private String[] navMenuTitles;
	// private Object mTitle;
	private CharSequence mDrawerTitle;
	private boolean cerrar = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busqueda);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mDrawerTitle = getTitle();

		getSupportActionBar().setHomeButtonEnabled(true);
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
		menu_lateral();
	}

	/**
	 * Prepara el menu lateral para su correcta visualización
	 */

	public void menu_lateral() {
		mDrawerOptions = (ListView) findViewById(R.id.left_drawer);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.lateral_busqueda);
		mDrawerOptions.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				navMenuTitles));
		mDrawerOptions.setOnItemClickListener(this);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
				R.drawable.ic_navigation_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				ActivityCompat.invalidateOptionsMenu(Busqueda.this);
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				ActivityCompat.invalidateOptionsMenu(Busqueda.this);
			}
		};
		mDrawer.setDrawerListener(mDrawerToggle);
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

	public void intercambiar() {

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

		switch (i) {
		case 0:
			// Intent intent = new Intent(Portada.this, Registro.class);
			// startActivity(intent);
			break;
		case 1:
			cerrar = true;
			Intent intent = new Intent(Busqueda.this, Busqueda.class);
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
