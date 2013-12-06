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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Registro extends Activity implements OnClickListener {
	private ProgressBar progreso;
	EditText user;
	EditText email;
	EditText pass;
	EditText passconf;
	Button registrar;
	Respuesta respuesta;
	private RadioGroup radioSexGroup;
	private RadioButton radioSexButton;
	//private Button btnDisplay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);
		registrar = (Button) findViewById(R.id.registrar);
		registrar.setOnClickListener(this);
		progreso = (ProgressBar) findViewById(R.id.carga);
		respuesta = new Respuesta();
		user = (EditText) findViewById(R.id.nombreusuario);
		email = (EditText) findViewById(R.id.emailusuario);
		pass = (EditText) findViewById(R.id.passusuario);
		passconf = (EditText) findViewById(R.id.confirmacion);
		radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro, menu);
		return true;
	}

	/**
	 * Intenta regristar un nuevo usuario si no se puede muestra toast con error
	 * 
	 */
	public void conectar() {
		if (isOnline()) {
			register_user();

		} else {
			Toast notification = Toast.makeText(this,
					"Activa tu conexión a internet", Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			progreso.setVisibility(View.GONE);
		}
	}

	/**
	 * Conecta con el servidor para intentar registrar un usuario nuevo
	 */
	private void register_user() {

		HttpRegister post = new HttpRegister();
		//post.execute("http://polimuevet.eu01.aws.af.cm/api/newuser");
		post.execute("http://192.168.1.14/api/newuser");

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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.registrar) {
			conectar();
		}

	}

	
	public void obtener_genero(){
		 // get selected radio button from radioGroup
		int selectedId = radioSexGroup.getCheckedRadioButtonId();

		// find the radiobutton by returned id
	        radioSexButton = (RadioButton) findViewById(selectedId);
	}
	/**
	 * Petición get al servidor a la url urls[0] que recibe como parámetro al
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
				pass.setText("");
				Toast notification = Toast.makeText(Registro.this,
						"Fallo en el registro , intentalo otra vez", Toast.LENGTH_SHORT);
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
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("nombre", user.getText()
					.toString()));
			nameValuePairs.add(new BasicNameValuePair("email", email.getText()
					.toString()));
			nameValuePairs.add(new BasicNameValuePair("pass", pass.getText()
					.toString()));
			obtener_genero();
			nameValuePairs.add(new BasicNameValuePair("sexo", radioSexButton.getText().toString()));
			
			// nameValuePairs.add(new BasicNameValuePair("passconf",
			// passconf.getText().toString()));
			try {
				req.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

}
