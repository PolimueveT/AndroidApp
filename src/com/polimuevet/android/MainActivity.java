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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	TextView iptv;
	EditText ip;
	String server;
	ParkingList lista = new ParkingList();
	private ProgressBar progreso;
	private TableLayout table;
	private Button actualizar;
	ListView ParkingsView;
	private AdaptadorParking Padapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// iptv =(TextView)findViewById(R.id.ipTextView);

		ip = (EditText) findViewById(R.id.ipEditText);
		ip.setText("192.168.1.10:3000");
		progreso = (ProgressBar) findViewById(R.id.carga);
		actualizar = (Button) findViewById(R.id.actualizar);
		actualizar.setOnClickListener(this);
		ParkingsView = (ListView) findViewById(R.id.parkings);
		// lista.getParkings().add(new Parking(1,"test","ETSINF",4,2,"libre"));

	}

	public void asociarAdapter() {
		Padapter = new AdaptadorParking(this, R.layout.elemento_fila,
				lista.getParkings());
		ParkingsView.setAdapter(Padapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	        	cargar_parkings();
	            return true;
	     
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
			if (lista.getParkings() != null) {
				Collections.sort(lista.getParkings());
				asociarAdapter();

				
				Padapter.notifyDataSetChanged();

			} else {
				Toast notification = Toast
						.makeText(MainActivity.this,
								"Fallo de conexión con el servidor",
								Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();
			}
			progreso.setVisibility(View.GONE);
		
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
			HttpGet httpGet = new HttpGet(urls[0]);

			try {
				HttpResponse response = client.execute(httpGet);
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
					// response
					// data

					String responseString = builder.toString();
					String responsefinal = "{\"parkings\":" + responseString
							+ "}";
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

	public class AdaptadorParking extends ArrayAdapter<Parking> {
		private ArrayList<Parking> items;

		public AdaptadorParking(Context context, int textViewResourceId,
				ArrayList<Parking> items) {
			super(context, textViewResourceId, items);
			this.items = items;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			// posicion=position;
			View v = convertView;
			if (v == null) {
				Log.d("indice", "indice " + position);
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.elemento_fila, null);

			}
			Parking p = items.get(position);
			if (p != null) {

				TextView codigo = (TextView) v.findViewById(R.id.codigo);
				TextView lugar = (TextView) v.findViewById(R.id.lugar);
				TextView plazas = (TextView) v.findViewById(R.id.plazas);
				TextView ocupadas = (TextView) v.findViewById(R.id.ocupadas);
				TextView estado = (TextView) v.findViewById(R.id.estado);

				if (codigo != null) {
					codigo.setText(p.getCodigo());
				}
				if (lugar != null) {
					lugar.setText(p.getLugar());
				}
				if (plazas != null) {
					plazas.setText("" + p.getPlazas());
				}
				if (ocupadas != null) {
					ocupadas.setText("" + p.getOcupadas());
				}
				if (estado != null) {
					if (p.getEstado().compareTo("Cerrado") == 0) {
						estado.setBackgroundColor(getResources().getColor(
								R.color.Icrojo));
					}
					estado.setText(p.getEstado());
				}

			}
			return v;
		}
	}

}
