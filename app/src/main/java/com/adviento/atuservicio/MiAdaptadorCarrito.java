package com.adviento.atuservicio;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by oficina on 05/06/2016.
 */
public class MiAdaptadorCarrito extends ArrayAdapter {
	private final Context context;
	//TODO: Cambiar el tipo de elementos contenidos en la lista

	private final ArrayList<String[]> valuesList; //Lista de objetos que va a contener la lista


	public MiAdaptadorCarrito(Context context, ArrayList valuesList) {
		//TODO: Comprobar el nombre del layout que contiene estructura de elemento de la lista
		super(context, R.layout.cubos, valuesList);
		this.context = context;
		this.valuesList = valuesList;
	}

///
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View rowView = inflater.inflate(R.layout.cubos, parent, false);

			TextView Textito10 = (TextView) rowView.findViewById(R.id.Textito10);
		Textito10.setText(valuesList.get(position)[0]);
		TextView Textito11 = (TextView) rowView.findViewById(R.id.Textito11);
		Textito11.setText(valuesList.get(position)[1]);
		ImageView Imaginita11 = (ImageView) rowView.findViewById(R.id.Imaginita11);
      //  Uri uri = Uri.parse(valuesList.get(position)[2]);
       // "android.resource://com.adviento.atuservicio/drawable/icono_a.png"
		String icono = valuesList.get(position)[2];

		Imaginita11.setImageResource(context.getResources().getIdentifier(icono, "drawable",context.getPackageName()));
		//Imaginita11.setImageResource(R.drawable.icono_g);

		return rowView;
	}
}
