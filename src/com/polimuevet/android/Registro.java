package com.polimuevet.android;

import java.io.BufferedReader;
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

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Registro extends Activity implements OnClickListener {

	static final int DATE_DIALOG_ID = 999;
	private ProgressBar progreso;
	EditText user;
	EditText email;
	EditText pass;
	EditText passconf;
	Button registrar;
	Respuesta respuesta;
	private RadioGroup radioSexGroup;
	private RadioGroup radioTipoGroup;
	private RadioButton radioSexButton;
	private RadioButton radioTipoButton;
	// private Button btnDisplay;
	private EditText fecha;
	private int year;
	private int month;
	private int day;

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
		radioTipoGroup = (RadioGroup) findViewById(R.id.radioGroup2);
		Inicializa_fecha();

	}

	/**
	 * Hace que el Edittext de la fecha de nacimiento despliegue un Datepicker
	 * al ser clicado
	 */
	public void Inicializa_fecha() {

		fecha = (EditText) findViewById(R.id.fecha);
		fecha.setClickable(true);
		fecha.setOnClickListener(this);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

	}

	/**
	 * Comprueba la estructura del email y que sea del dominio de la upv
	 * 
	 * @return true si el email pasa las pruebas false en caso contrario
	 */
	private boolean comprobar_email() {
		String mail = email.getText().toString();

		if (mail.matches(".*@.*\\.upv.es")) {
			return true;
		}
		return false;
	}

	public boolean comprobar_pass() {
		String contras = pass.getText().toString();
		String confir = passconf.getText().toString();
		if (contras.compareTo(confir) == 0) {
			return true;
		}
		return false;

	}

	/**
	 * Comprueba si todos los campos tienen informaci�n "correcta" para enviar
	 * al servidor
	 * 
	 * @return true si la info es correcta , false en caso contrario
	 */
	public boolean comprobar_datos() {
		boolean ok = true;

		// campos vacios
		if (user.getText().toString().compareTo("") == 0) {

			ok = false;
		} else if (email.getText().toString().compareTo("") == 0) {

			ok = false;
		} else if (pass.getText().toString().compareTo("") == 0) {

			ok = false;
		} else if (passconf.getText().toString().compareTo("") == 0) {

			ok = false;
		}

		else if (fecha.getText().toString().compareTo("") == 0) {

			ok = false;
		}

		// si no hay ningún campo vacio
		if (ok) {
			if (!comprobar_email()) {
				Toast notification = Toast.makeText(this,
						"El email debe ser dominio @'facultad'.upv.es",
						Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();

				ok = false;
			}

			// si las contraseñas coinciden
			else if (!comprobar_pass()) {
				// toast contraseñas no coinciden
				Toast notification = Toast.makeText(this,
						"La contraseña y la confirmación deben ser iguales",
						Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();
				ok = false;

			}
		}

		else {
			// toast rellena campos
			Toast notification = Toast.makeText(this,
					"Debes rellenar todos los campos", Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			progreso.setVisibility(View.GONE);
		}

		return ok;
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
		post.execute(Config.URL + "/api/newuser");

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
			if (comprobar_datos()) {
				conectar();
			}
		}

		else if (arg0.getId() == R.id.fecha) {
			showDialog(DATE_DIALOG_ID);
		}

	}

	/**
	 * Pone en las variables de los RadioButtons el Radiobutton elegido en cada
	 * RadioGroup
	 */
	public void obtener_radiobuttons() {

		int selectedId = radioSexGroup.getCheckedRadioButtonId();
		radioSexButton = (RadioButton) findViewById(selectedId);
		selectedId = radioTipoGroup.getCheckedRadioButtonId();
		radioTipoButton = (RadioButton) findViewById(selectedId);
	}

	/**
	 * Recoge la informaci�n que ha introducido el usuario en todos los campos
	 * y lo prepara para enviarlo en el POST creando pares de valores
	 * 
	 * @return la lista de pares clave valor
	 */
	public List<NameValuePair> recoger_datos() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("nombre", user.getText()
				.toString()));
		nameValuePairs.add(new BasicNameValuePair("email", email.getText()
				.toString()));
		nameValuePairs.add(new BasicNameValuePair("fechanacimiento", fecha
				.getText().toString()));
		obtener_radiobuttons();
		nameValuePairs.add(new BasicNameValuePair("pass", pass.getText()
				.toString()));

		nameValuePairs.add(new BasicNameValuePair("passconf", passconf
				.getText().toString()));

		obtener_radiobuttons();
		nameValuePairs.add(new BasicNameValuePair("sexo", radioSexButton
				.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("usertype", radioTipoButton
				.getText().toString()));

		return nameValuePairs;
	}

	/**
	 * Petici�n post al servidor a la url urls[0] que recibe como parámetro
	 * al instanciarse,el json que devuelve se guarda en respuesta variable
	 * global de tipo Respuesta
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
				Toast notification = Toast.makeText(Registro.this,
						"Usuario Registrado! Favor entrar a su cuenta.",
						Toast.LENGTH_SHORT);
				notification.setGravity(Gravity.CENTER, 0, 0);
				notification.show();

				finish();
			} else {
				pass.setText("");
				Toast notification = Toast.makeText(Registro.this,
						"Fallo en el registro , intentalo otra vez",
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year - 18,
					month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			fecha.setText(new StringBuilder().append(day).append("-")
					.append(month + 1).append("-").append(year).append(" "));

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro, menu);
		return true;
	}

}
