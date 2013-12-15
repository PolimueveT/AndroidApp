package com.polimuevet.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorParking extends ArrayAdapter<Parking> {
	private ArrayList<Parking> items;
	private Context context;

	public AdaptadorParking(Context context, int textViewResourceId,
			ArrayList<Parking> items) {
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

			v = vi.inflate(R.layout.parking_row, null);

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
				nplazas = p.getPlazas();
			}
			if (ocupadas != null) {
				nocupadas = p.getOcupadas();

				ocupadas.setText("" + (nplazas - nocupadas));
			}
			if (estado != null) {
				if (p.getEstado().compareTo("Cerrado") == 0) {
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
				}
				// estado.setText(p.getEstado());
			}

		}
		return v;
	}
}