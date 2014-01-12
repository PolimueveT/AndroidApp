package com.polimuevet.android;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
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
	private ProgressBar progreso;
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
		for (int i = 0; i < 15; i++) {
			c.add(Calendar.DAY_OF_MONTH, +1);
			day = c.get(Calendar.DAY_OF_MONTH);
			month = c.get(Calendar.MONTH);
			year = c.get(Calendar.YEAR);
			int mes = month + 1;
			SpinnerArray.add(day + "/" + mes + "/" + year);
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

		} else {
			showDialog(TIME_DIALOG_ID);
		}

	}
	
	
	/**
	 * Recoge la información que ha introducido el usuario en todos los campos y
	 * lo prepara para enviarlo en el POST creando pares de valores
	 * 
	 * @return la lista de pares clave valor
	 */
	public List<NameValuePair> recoger_datos() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//		nameValuePairs.add(new BasicNameValuePair("nombre", user.getText()
//				.toString()));
//		nameValuePairs.add(new BasicNameValuePair("email", email.getText()
//				.toString()));
//		nameValuePairs.add(new BasicNameValuePair("fechanacimiento", fecha
//				.getText().toString()));
//		obtener_radiobuttons();
//		nameValuePairs.add(new BasicNameValuePair("pass", pass.getText()
//				.toString()));
//
//		nameValuePairs.add(new BasicNameValuePair("passconf", passconf
//				.getText().toString()));
//
//		obtener_radiobuttons();
//		nameValuePairs.add(new BasicNameValuePair("sexo", radioSexButton
//				.getText().toString()));
//		nameValuePairs.add(new BasicNameValuePair("usertype", radioTipoButton
//				.getText().toString()));

		return nameValuePairs;
	}
	
	/**
	 * Petición post al servidor a la url urls[0] que recibe como parÃ¡metro al
	 * instanciarse,el json que devuelve se guarda en respuesta variable global
	 * de tipo Respuesta
	 * 
	 * @author cesar
	 * 
	 */
	class HttpRegister extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO completar intent Acceso al app , error en caso contrario
			super.onPostExecute(result);

			progreso.setVisibility(View.GONE);
			Log.d("JSON", respuesta.testrespuesta());
			if (respuesta.success) {
				finish();
			} else {
				
				Toast notification = Toast.makeText(Addtrip.this,
						"Fallo al crear el trayecto , intentalo otra vez",
						Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();
				// progreso.setVisibility(View.GONE);

			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progreso.setVisibility(View.VISIBLE);

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(String... urls) {
			// TODO Auto-generated method stub
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();

			// Ejemplo POST
			HttpPost req = new HttpPost(urls[0]);

			try {
				req.setEntity(new UrlEncodedFormEntity(recoger_datos()));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				HttpResponse response = client.execute(req);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				Log.d("RESPUESTA", "statusCode: " + statusCode);
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
					String line;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					Log.v("Getter", "Your data: " + builder.toString());

					String responseString = builder.toString();
					String responsefinal = "{\"respuesta\":" + responseString
							+ "}";
					Log.d("JSON", responsefinal);
					GsonBuilder gbuilder = new GsonBuilder();
					Gson gson = gbuilder.create();
					JSONObject json = new JSONObject(responseString);
					respuesta = gson.fromJson(json.toString(), Respuesta.class);

				} else {
					Log.e("Getter", "Failed to download file");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
	}

}
