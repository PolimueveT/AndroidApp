package com.polimuevet.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polimuevet.android.R.id;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	TextView iptv;
	EditText ip;
	String server;
	ParkingList lista = new ParkingList();
	private ProgressBar progreso;
	private TableLayout table;
	private Button actualizar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// iptv =(TextView)findViewById(R.id.ipTextView);
		table = (TableLayout) findViewById(R.id.tablaparkings);
		ip = (EditText) findViewById(R.id.ipEditText);
		progreso = (ProgressBar) findViewById(R.id.carga);
		actualizar=(Button)findViewById(R.id.actualizar);
		actualizar.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void cargar_parkings() {
		// TODO Auto-generated method stub
		// obtener estado parkings
		/*
		 * SharedPreferences preferences = getSharedPreferences("prefs",
		 * Context.MODE_PRIVATE);
		 */

		HttpParkings get = new HttpParkings();
		Log.d("URL", "http://" + ip.getText().toString() + "/api/parking");

		get.execute("http://" + ip.getText().toString() + "/api/parking");

	}

	class HttpParkings extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (lista != null) {
				Collections.sort(lista.getParkings());

				for (int s = lista.getParkings().size() - 1; s >= 0; s--) {
					// poner una fila///////////////////////
					TableRow row = new TableRow(MainActivity.this);
					TextView tvnombre = new TextView(MainActivity.this);
					tvnombre.setText(lista.getParkings().get(s).getLugar());
					tvnombre.setTextSize(24);
					tvnombre.setTextColor(getResources()
							.getColor(R.color.Black));

					/*
					 * Log.d("tamaño", " " +
					 * getResources().getDimension(R.dimen.textofila));
					 */
					row.addView(tvnombre);
					TextView tvp = new TextView(MainActivity.this);
					tvp.setText("" + lista.getParkings().get(s).getPlazas());
					tvp.setTextSize(24);
					tvp.setTextColor(getResources().getColor(R.color.Black));
					row.addView(tvp);
					table.addView(row);
					// //////////////////////////

				}

			}
			progreso.setVisibility(View.GONE);
			// host.getTabWidget().getChildTabViewAt(1).setEnabled(true);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// host.getTabWidget().getChildTabViewAt(1).setEnabled(false);
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
			HttpGet httpGet = new HttpGet(urls[0]);

			try {
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				Log.d("RESPUESTA", "statusCode: "+statusCode);
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
					// response
					// data

					String responseString = builder.toString();
					String responsefinal= "{\"parkings\":"+responseString+"}";
					Log.d("JSON", responsefinal);
					GsonBuilder gbuilder = new GsonBuilder();
					Gson gson = gbuilder.create();
					JSONObject json = new JSONObject(responsefinal);
					lista = gson.fromJson(json.toString(), ParkingList.class);

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

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		progreso.setVisibility(View.VISIBLE);
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

}
