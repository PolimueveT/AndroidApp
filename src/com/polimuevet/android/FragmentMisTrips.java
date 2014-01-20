package com.polimuevet.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentMisTrips extends Fragment implements OnItemClickListener {

	private View view;
	private ProgressBar progreso;
	private String url;
	private Context context;
	private AdaptadorTrips Tadapter;
	ListView TripsView;
	TextView titulo;
	TripList lista;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getArguments();
		this.url = b.getString("url");

		this.context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_mistrayectos, null);

		progreso = (ProgressBar) view.findViewById(R.id.carga);
		TripsView = (ListView) view.findViewById(R.id.trayectos);
		titulo = (TextView) view.findViewById(R.id.titulo);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		conectar();
	}

	public void setTrips(TripList lista) {

		if (lista != null && lista.getTrips() != null) {
			// Collections.sort(lista.getParkings());
			titulo.setText(lista.getTrips().size() + " trayectos encontrados");

			Log.d("ADAPTER", "creamos adapter");
			Tadapter = new AdaptadorTrips(context, R.layout.parking_row,
					lista.getTrips());
			TripsView.setAdapter(Tadapter);
			Log.d("ADAPTER", "ponemos adapter");
			TripsView.setOnItemClickListener(this);
			Log.d("ADAPTER", "ponemos clciklistener");
			// Tadapter.notifyDataSetChanged();

		} else {
			titulo.setText("0 trayectos encontrados");
		}

		getActivity().setProgressBarIndeterminateVisibility(false);
	}

	/**
	 * Intenta conectar con el servidor si el dispositivo tiene conexión a
	 * internet
	 * 
	 */
	public void conectar() {
		if (isOnline()) {
			cargar_trayectos();
			// reintentar.setVisibility(View.GONE);
		} else {
			Toast notification = Toast.makeText(context,
					R.string.conexion_internet, Toast.LENGTH_SHORT);
			notification.setGravity(Gravity.CENTER, 0, 0);
			notification.show();
			progreso.setVisibility(View.GONE);
		}
	}

	/**
	 * Conecta con el servidor para obtener los datos del parking y colocarlos
	 * en el listview lista
	 */
	private void cargar_trayectos() {

		// obtener trayectos
		new HttpMisTrayectos(context, this).execute(this.url);
	}

	/**
	 * Comprueba si el dispositivo tiene conexión a internet
	 * 
	 * @return true si tiene conexi�n ,false en caso contrario
	 */

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		Log.d("LISTVIEW", "Se ha tocado el item de la lista " + arg2);
		Trip trip = lista.getTrips().get(arg2);
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

		Intent intent = new Intent((Activity) context, TripDetail.class);
		intent.putExtras(b);
		getActivity().startActivity(intent);

	}

	public String quitarCP(String direccion) {
		String[] partes = direccion.split(",");
		if (partes.length > 2) {
			return partes[0] + partes[1];
		} else {
			return partes[0];
		}
	}
}
