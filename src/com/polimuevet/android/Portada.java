package com.polimuevet.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Portada extends ActionBarActivity implements OnClickListener {

	Respuesta respuesta;
	Button acceder;
	Button registrar;
	EditText mail;
	EditText pass;

	boolean cerrar = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_portada);
		acceder = (Button) findViewById(R.id.acceder);
		acceder.setOnClickListener(this);
		registrar = (Button) findViewById(R.id.registrar);
		registrar.setOnClickListener(this);
		
		respuesta = new Respuesta();
		mail = (EditText) findViewById(R.id.email);
		pass = (EditText) findViewById(R.id.password);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.portada, menu);
		return true;
	}

	/**
	 * Intenta conectar con el parking si el dispositivo tiene conexión a
	 * internet
	 * 
	 */
	public void conectar() {
		if (isOnline()) {
			login_server();

		} else {
			Toast notification = Toast.makeText(this,
					R.string.conexion_internet, Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			setProgressBarIndeterminateVisibility(false);
		}
	}

	/**
	 * Conecta con el servidor para obtener si el usuario esta registrado o no
	 */
	private void login_server() {
		// TODO url definitiva
		
		HttpLogin post = new HttpLogin(Portada.this,recoger_datos());
		//get.execute("http://polimuevet.eu01.aws.af.cm/api/isuserregistered/"+ usuario + "/" + password);
		//post.execute("http://192.168.1.10:3000/api/ismailregistered");
		post.execute("http://polimuevet.eu01.aws.af.cm/api/ismailregistered");

	}
	public List<NameValuePair> recoger_datos() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("mail", mail.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("pass", pass.getText().toString()));
		return nameValuePairs;
	}
	

	/**
	 * Comprueba si el dispositivo tiene conexi�n a internet
	 * 
	 * @return true si tiene conexi�n ,false en caso contrario
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
		if (arg0.getId() == R.id.acceder) {
			conectar();
		} else if (arg0.getId() == R.id.registrar) {
			// intent activity registro
			Intent intent = new Intent(Portada.this, Registro.class);
			startActivity(intent);
		}
	}

	private void restoreData() {
		SharedPreferences preferences = getSharedPreferences("sesion",
				Context.MODE_PRIVATE);
		if (preferences.getBoolean("login", false)) {
			cerrar = true;
			Intent intent = new Intent(Portada.this, Busqueda.class);
			startActivity(intent);
		}

	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		restoreData();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (cerrar) {
			finish();
		}

	}

}
