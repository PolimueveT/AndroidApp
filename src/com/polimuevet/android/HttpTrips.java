package com.polimuevet.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class HttpTrips extends AsyncTask<String, Void, Void> implements
		OnItemClickListener {

	private TripList lista;
	View v;
	Context context;
	ProgressBar progreso;
	private AdaptadorTrips Tadapter;

	public HttpTrips(Context context, ProgressBar progreso) {
		this.context = context;
		this.progreso = progreso;

	}

	@Override
	protected void onPostExecute(Void result) {

		super.onPostExecute(result);
		View v = new View(context);
		if (lista.getTrips() != null) {
			// Collections.sort(lista.getParkings());
			ListView TripsView = (ListView) ((Activity) context)
					.findViewById(R.id.trayectos);
			TextView titulo = (TextView) ((Activity) context)
					.findViewById(R.id.titulo);
			titulo.setText(lista.getTrips().size() + " trayectos encontrados");
			Tadapter = new AdaptadorTrips(context, R.layout.parking_row,
					lista.getTrips());
			TripsView.setAdapter(Tadapter);
			TripsView.setOnItemClickListener(this);
			Tadapter.notifyDataSetChanged();

		} else {
			Toast notification = Toast.makeText(context,
					"Fallo de conexión con el servidor", Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
		}
		progreso.setVisibility(View.GONE);

	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();
		progreso.setVisibility(View.VISIBLE);

	}

	@Override
	protected void onProgressUpdate(Void... values) {

		super.onProgressUpdate(values);
	}

	@Override
	protected Void doInBackground(String... urls) {

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
				String responsefinal = "{\"trips\":" + responseString + "}";
				Log.d("JSON", responsefinal);
				GsonBuilder gbuilder = new GsonBuilder();
				Gson gson = gbuilder.create();
				JSONObject json = new JSONObject(responseString);
				lista = gson.fromJson(json.toString(), TripList.class);

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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		Log.d("LISTVIEW", "Se ha tocado el item de la lista " + arg2);
		Trip trip=lista.getTrips().get(arg2);
		Bundle b = new Bundle();
		b.putInt("Num_plazas", trip.getNum_plazas());
		b.putString("_id", trip.get_id());
		b.putString("Creador_id", trip.getCreador_id());
		b.putString("Destino", quitarCP(trip.getDestino()));
		b.putString("Fecha_time", trip.getFecha_time());
		b.putString("Max_tamanyo_equipaje", trip.getMax_tamanyo_equipaje());
		b.putString("Observaciones", trip.getObservaciones());
		b.putString("Origen", quitarCP(trip.getOrigen()));
		b.putFloat("Precio_plaza", trip.getPrecio_plaza());
		b.putInt("Tiempo_max_espera", trip.getTiempo_max_espera());
	

		
		Intent intent = new Intent((Activity)context, TripDetail.class);
		intent.putExtras(b);
		((Activity)context).startActivity(intent);
		

	}
	public String quitarCP(String direccion){
		String[]partes=direccion.split(",");
		if (partes.length>2){
		return partes[0]+partes[1];
		}
		else {
			return partes[0];
		}
	}

}
