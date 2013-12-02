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

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Portada extends ActionBarActivity implements OnClickListener {
	private ProgressBar progreso;
	Respuesta respuesta;
	Button acceder;
	Button registrar;
	EditText user;
	EditText pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_portada);
		acceder=(Button)findViewById(R.id.acceder);
		acceder.setOnClickListener(this);
		registrar=(Button)findViewById(R.id.registrar);
		registrar.setOnClickListener(this);		
		progreso = (ProgressBar) findViewById(R.id.carga);
		respuesta=new Respuesta();
		user=(EditText)findViewById(R.id.user);
		pass=(EditText)findViewById(R.id.password);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.portada, menu);
		return true;
	}

	/**
	 * Intenta conectar con el parking si el dispositivo tiene conexión a internet 
	 * 
	 */
	public void conectar(){
		if (isOnline()) {
			login_server();
			
		} else {
			Toast notification = Toast.makeText(this,
					"Activa tu conexión a internet", Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			progreso.setVisibility(View.GONE);
		}
	}
	
	   /**
	    * Conecta con el servidor para obtener si el usuario esta registrado o no
	    */
		private void login_server() {
			// TODO url definitiva
			String usuario=user.getText().toString();
			String password=pass.getText().toString();
			HttpLogin get = new HttpLogin();	
			get.execute("http://192.168.1.12:3000/api/isuserregistered/"+usuario+"/"+password);

		}

		/**
		 * Petición get al servidor a la url urls[0] que recibe como parámetro al instanciarse,el json que devuelve se guarda en respuesta
		 * variable global de tipo Respuesta
		 * @author cesar
		 *
		 */
	class HttpLogin extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			// TODO completar intent Acceso al app , error en caso contrario
			super.onPostExecute(result);
			
			progreso.setVisibility(View.GONE);
			Log.d("JSON", respuesta.testrespuesta());
			if(respuesta.data){
				//acceso permitido ;)
				Intent intent = new Intent(Portada.this, EstadoParking.class);
				startActivity(intent);
				
			}
			else{
				pass.setText("");
				Toast notification = Toast.makeText(Portada.this,
						"Usuario o contraseña incorrectos", Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();
				//progreso.setVisibility(View.GONE);
				
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
			HttpGet req = new HttpGet(urls[0]);
			
			//Ejemplo POST
			//HttpPost req = new HttpPost(urls[0]);
			/*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("id", "12345"));
	        nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
	        try {
				req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
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
	public void onClick(View arg0) {
		// TODO intent registro
		if(arg0.getId()==R.id.acceder){
			conectar();
		}
		else if(arg0.getId()==R.id.registrar){
			//intent activity registro
		}
	}


	
	
	
}
