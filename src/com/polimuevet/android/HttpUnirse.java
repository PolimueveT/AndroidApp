package com.polimuevet.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class HttpUnirse extends AsyncTask<String, Void, Void> {

	//private TripList lista;
	Respuesta respuesta;
	View v;
	Context context;
	ProgressBar progreso;
	String personId;
	String tripId;

	public HttpUnirse(Context context, ProgressBar progreso,String PersonId,String tripId) {
		this.context = context;
		this.progreso = progreso;
		this.personId=PersonId;
		this.tripId=tripId;

	}

	@Override
	protected void onPostExecute(Void result) {

		super.onPostExecute(result);
		View v = new View(context);
		if (respuesta != null &&respuesta.isSuccess()) {
			// Collections.sort(lista.getParkings());
			
			Button unirse = (Button) ((Activity) context)
					.findViewById(R.id.unirse);
			
			unirse.setText("Quitarte");
			

		} else {
			Toast notification = Toast.makeText(context,
					respuesta.getInfo(), Toast.LENGTH_SHORT);
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

		// Ejemplo POST
		HttpPut req = new HttpPut(urls[0]);

		try {
			req.setEntity(new UrlEncodedFormEntity(recoger_datos()));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//HttpGet httpGet = new HttpGet(urls[0]);

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
				// response
				// data

				String responseString = builder.toString();
				String responsefinal = "{\"trips\":" + responseString + "}";
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
	public List<NameValuePair> recoger_datos() {
		//TO DO
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		//el tripId si que lo tengo
		nameValuePairs.add(new BasicNameValuePair("tripId",tripId));
		
		//debe ser el id de la sesion de momento no lo recibo fuerzo a uno local para la demo
		nameValuePairs.add(new BasicNameValuePair("personId","529d062bc32db82e22b92c6f"));
	
		return nameValuePairs;
	}
}
