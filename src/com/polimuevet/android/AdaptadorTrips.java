package com.polimuevet.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorTrips extends ArrayAdapter<Trip> {
	private ArrayList<Trip> items;
	private Context context;

	public AdaptadorTrips(Context context, int textViewResourceId,
			ArrayList<Trip> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		int nplazas = 0, nocupadas = 0;
		// posicion=position;
		View v = convertView;
		if (v == null) {
			// Log.d("indice", "indice " + position);

			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			v = vi.inflate(R.layout.elemento_fila, null);

		}
		Trip t = items.get(position);
		if (t != null) {

			TextView codigo = (TextView) v.findViewById(R.id.codigo);
			TextView lugar = (TextView) v.findViewById(R.id.lugar);
			TextView plazas = (TextView) v.findViewById(R.id.plazas);
			TextView ocupadas = (TextView) v.findViewById(R.id.ocupadas);
			TextView estado = (TextView) v.findViewById(R.id.estado);

			if (codigo != null) {
				codigo.setText("a");
			}
			if (lugar != null) {
				lugar.setText("a");
			}
			if (plazas != null) {
				plazas.setText("a");
				
			}
			if (ocupadas != null) {
				

				ocupadas.setText("a");
			}
			if (estado != null) {
				/*if (p.getEstado().compareTo("Cerrado") == 0) {
					estado.setBackgroundColor(context.getResources().getColor(
							R.color.Gray));
					estado.setText(p.getEstado());
				} else if (p.getEstado().compareTo("Libre") == 0) {
					estado.setBackgroundColor(context.getResources().getColor(
							R.color.Icverde));
					estado.setText(p.getEstado());
				} else if (p.getEstado().compareTo("Completo") == 0) {
					estado.setBackgroundColor(context.getResources().getColor(
							R.color.Icrojo));
					estado.setText(p.getEstado());
				} else if (p.getEstado().compareTo("Personal Autorizado") == 0) {
					estado.setBackgroundColor(context.getResources().getColor(
							R.color.Icamarillo));
					estado.setText("Autorizado");
				}*/
				// estado.setText(p.getEstado());
			}

		}
		return v;
	}
}