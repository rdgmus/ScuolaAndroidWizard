package it.keyorchestra.registroandroid.admin.options.util;

import it.keyorchestra.registroandroid.admin.options.R;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScuoleArrayAdapter extends ArrayAdapter<String> {
	private final Context _context;
	private final JSONArray _objects;

	public ScuoleArrayAdapter(Context context, JSONArray objects,
			ArrayList<String> values) {
		super(context, R.layout.scuola_row_layout, values);
		// TODO Auto-generated constructor stub
		this._context = context;
		this._objects = objects;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.scuola_row_layout, parent,
				false);

//		ImageView imageView = (ImageView) rowView
//				.findViewById(R.id.ivScuolaIcon);
		TextView tvIdScuola = (TextView) rowView.findViewById(R.id.tvIdScuola);
		TextView tvScuolaAcronimo = (TextView) rowView
				.findViewById(R.id.tvScuolaAcronimo);
		TextView tvNomeScuola = (TextView) rowView
				.findViewById(R.id.tvNomeScuola);
		TextView tvIndirizzoScuola = (TextView) rowView
				.findViewById(R.id.tvIndirizzoScuola);
//		TextView tvEmail = (TextView) rowView
//				.findViewById(R.id.tvEmail);
//		TextView tvWeb = (TextView) rowView
//				.findViewById(R.id.tvWeb);

		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvIdScuola.setText(String.valueOf(s.getLong("id_scuola")));
			tvIdScuola.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIdScuola.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvIdScuola.setTextSize(12);

			tvScuolaAcronimo.setText(s.getString("tipo_scuola_acronimo"));
			tvScuolaAcronimo.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvScuolaAcronimo.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvScuolaAcronimo.setTextSize(12);

			tvNomeScuola.setText(s.getString("nome_scuola"));
			tvNomeScuola.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvNomeScuola.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvNomeScuola.setTextSize(12);

			tvIndirizzoScuola.setText(formatIndirizzoScuola(s));
			tvIndirizzoScuola.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIndirizzoScuola.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvIndirizzoScuola.setTextSize(12);
			
//			tvEmail.setText(s.getString("email"));
//			tvEmail.setTextColor(_context.getResources().getColor(
//					R.color.colorOrange));
//			tvEmail.setBackgroundColor(_context.getResources()
//					.getColor(R.color.colorBlack));
//			tvEmail.setTextSize(12);
//			
//			tvWeb.setText(s.getString("web"));
//			tvWeb.setTextColor(_context.getResources().getColor(
//					R.color.colorOrange));
//			tvWeb.setBackgroundColor(_context.getResources()
//					.getColor(R.color.colorBlack));
//			tvWeb.setTextSize(12);
			
			rowView.setTag(s.getLong("id_scuola"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowView;
	}

	private String formatIndirizzoScuola(JSONObject s) {
		// TODO Auto-generated method stub
		String indirizzo = "";
		try {
			indirizzo = s.getString("indirizzo") +" - "+ s.getString("cap")+" - "
					+ s.getString("citta") +" ("+ s.getString("provincia")+")";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indirizzo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getDropDownView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.scuola_row_layout, parent,
				false);

//		ImageView imageView = (ImageView) rowView
//				.findViewById(R.id.ivScuolaIcon);
		TextView tvIdScuola = (TextView) rowView.findViewById(R.id.tvIdScuola);
		TextView tvScuolaAcronimo = (TextView) rowView
				.findViewById(R.id.tvScuolaAcronimo);
		TextView tvNomeScuola = (TextView) rowView
				.findViewById(R.id.tvNomeScuola);
		TextView tvIndirizzoScuola = (TextView) rowView
				.findViewById(R.id.tvIndirizzoScuola);
//		TextView tvEmail = (TextView) rowView
//				.findViewById(R.id.tvEmail);
//		TextView tvWeb = (TextView) rowView
//				.findViewById(R.id.tvWeb);

		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvIdScuola.setText(String.valueOf(s.getLong("id_scuola")));
			tvIdScuola.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIdScuola.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvIdScuola.setTextSize(12);

			tvScuolaAcronimo.setText(s.getString("tipo_scuola_acronimo"));
			tvScuolaAcronimo.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvScuolaAcronimo.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvScuolaAcronimo.setTextSize(12);

			tvNomeScuola.setText(s.getString("nome_scuola"));
			tvNomeScuola.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvNomeScuola.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvNomeScuola.setTextSize(12);

			tvIndirizzoScuola.setText(formatIndirizzoScuola(s));
			tvIndirizzoScuola.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIndirizzoScuola.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvIndirizzoScuola.setTextSize(12);
			
//			tvEmail.setText(s.getString("email"));
//			tvEmail.setTextColor(_context.getResources().getColor(
//					R.color.colorOrange));
//			tvEmail.setBackgroundColor(_context.getResources()
//					.getColor(R.color.colorBlack));
//			tvEmail.setTextSize(12);
//			
//			tvWeb.setText(s.getString("web"));
//			tvWeb.setTextColor(_context.getResources().getColor(
//					R.color.colorOrange));
//			tvWeb.setBackgroundColor(_context.getResources()
//					.getColor(R.color.colorBlack));
//			tvWeb.setTextSize(12);
			

			rowView.setTag(s.getLong("id_scuola"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowView;
	}

}
