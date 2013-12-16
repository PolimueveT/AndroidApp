package com.polimuevet.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
		
		// posicion=position;
		View v = convertView;
		if (v == null) {
			// Log.d("indice", "indice " + position);

			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

			v = vi.inflate(R.layout.trip_row, null);

		}
		Trip t = items.get(position);
		if (t != null) {

			TextView origen = (TextView) v.findViewById(R.id.origen);
			TextView destino = (TextView) v.findViewById(R.id.destino);
			TextView fecha = (TextView) v.findViewById(R.id.fecha);
			TextView precio = (TextView) v.findViewById(R.id.precio);
			

			if (origen != null) {
				
				origen.setText(quitarCP(t.getOrigen()));
			}
			if (destino != null) {
				destino.setText(quitarCP(t.getDestino()));
			}
			if (precio != null) {
				precio.setText(""+t.getPrecio_plaza()+"€");
				color_precio(precio,t.getPrecio_plaza());
			}
			if (fecha != null) {
				Date parsedDateInstance;
				try {
					parsedDateInstance = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(t.getFecha_time());
					String formattedDate = new SimpleDateFormat("dd/MM/yyyy - HH:mm" ).format(parsedDateInstance);
					fecha.setText(formattedDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				
			}
		

		}
		return v;
	}
	
	private void color_precio(TextView precio,Float Precio_plaza) {
		// TODO Auto-generated method stub
		if(Precio_plaza<=0.75){
			precio.setTextColor(context.getResources().getColor(R.color.Icverde));
			
		}
		else if(Precio_plaza >0.75 && Precio_plaza<2.0){
			precio.setTextColor(context.getResources().getColor(R.color.Orange));
		}
		else if(Precio_plaza>=2.0){
			precio.setTextColor(context.getResources().getColor(R.color.Icrojo));
		}
		
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