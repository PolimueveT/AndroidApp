package com.polimuevet.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.polimuevet.android.R.color;

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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MisTrayectos extends ActivityMenuLateral {

	private boolean cerrar = false;
	ListView TripsView;
	private HttpMisTrayectos get;

	ViewPager pager;
	MyPageAdapter padapter;

	List<Fragment> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mis_trayectos);
		menu_lateral(R.array.lateral_mistrayectos, this);

		padapter = new MyPageAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.viewPager);
		getSupportActionBar().setStackedBackgroundDrawable(
				new ColorDrawable(color.Orange));

		// Creador ID
		SharedPreferences preferences = getSharedPreferences("sesion",
				Context.MODE_PRIVATE);
		String PersonId = preferences.getString("user", "");

		Bundle bundleInscritos = new Bundle();
		bundleInscritos.putString("url", Config.URL + "/api/getinscritotrips/"
				+ PersonId);

		Bundle bundleOfreciendo = new Bundle();
		bundleOfreciendo.putString("url", Config.URL + "/api/getpersontrips/"
				+ PersonId);

		// Creo los fragments
		FragmentMisTrips fragInscritos = new FragmentMisTrips();
		fragInscritos.setArguments(bundleInscritos);
		FragmentMisTrips fragOfreciendo = new FragmentMisTrips();
		fragOfreciendo.setArguments(bundleOfreciendo);

		// Asigno memoria a la lista de fragments
		fragments = new ArrayList<Fragment>();
		// Agrego los fragments
		fragments.add(fragInscritos);
		fragments.add(fragOfreciendo);

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
		tab.setText("Inscrito");
		tab.setTabListener(tabListener);
		getSupportActionBar().addTab(tab);

		tab = getSupportActionBar().newTab();
		tab.setText("Ofreciendo");
		tab.setTabListener(tabListener);
		getSupportActionBar().addTab(tab);

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

		switch (i) {
		case 0:
			cerrar = true;
			Intent intentadd = new Intent(MisTrayectos.this, Addtrip.class);
			startActivity(intentadd);
			break;
		case 1:
			cerrar = true;
			Intent intent = new Intent(MisTrayectos.this, Busqueda.class);
			startActivity(intent);
			break;
		case 2:
			cerrar = true;
			Intent intentEstado = new Intent(MisTrayectos.this,
					EstadoParking.class);
			startActivity(intentEstado);
			break;
		case 3:
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

		Intent intent = new Intent(MisTrayectos.this, Portada.class);
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

	private class MyPageAdapter extends FragmentPagerAdapter {

		public MyPageAdapter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int position) {
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
