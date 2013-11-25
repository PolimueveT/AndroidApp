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

	ListView ParkingsView;
	private AdaptadorParking Padapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		progreso = (ProgressBar) findViewById(R.id.carga);
	
		ParkingsView = (ListView) findViewById(R.id.parkings);
	
		
		conectar();

	}
	/**
	 * Crea el adapter para el Listview lista , este adapter se encarga de poner  un layout a cada elemento de la  lista, de colocar el color del estado del parking , de calcular plazas disponibles etc.
	 */
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
	    	conectar();
	            return true;
	     
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	/**
	 * Intenta conectar con el parking si el dispositivo tiene conexión a internet 
	 * 
	 */
	public void conectar(){
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
    * Conecta con el servidor para obtener los datos del parking y colocarlos en el listview lista
    */
	private void cargar_parkings() {
		// TODO Auto-generated method stub
		// obtener estado parkings

		HttpParkings get = new HttpParkings();	
		get.execute("http://polimuevet.eu01.aws.af.cm/api/parking");

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
	/**
	 * Comprueba si el dispositivo tiene conexión a internet
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
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
			int nplazas = 0, nocupadas=0;
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
					nplazas=p.getPlazas();
				}
				if (ocupadas != null) {
					nocupadas=p.getOcupadas();
					
					ocupadas.setText("" + (nplazas-nocupadas));
				}
				if (estado != null) {
					if (p.getEstado().compareTo("Cerrado") == 0) {
						estado.setBackgroundColor(getResources().getColor(
								R.color.Gray));
						estado.setText(p.getEstado());
					}
					else if (p.getEstado().compareTo("Libre") == 0) {
						estado.setBackgroundColor(getResources().getColor(
								R.color.Icverde));
						estado.setText(p.getEstado());
					}
					else if (p.getEstado().compareTo("Completo") == 0) {
						estado.setBackgroundColor(getResources().getColor(
								R.color.Icrojo));
						estado.setText(p.getEstado());
					}
					else if (p.getEstado().compareTo("Personal Autorizado") == 0) {
						estado.setBackgroundColor(getResources().getColor(
								R.color.Icamarillo));
						estado.setText("Autorizado");
					}
					//estado.setText(p.getEstado());
				}

			}
			return v;
		}
	}
	

}
