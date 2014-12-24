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

public class StudentiClasseArrayAdapter extends ArrayAdapter<String> {
	private final Context _context;
	private final JSONArray _objects;

	public StudentiClasseArrayAdapter(Context context, JSONArray objects,
			ArrayList<String> values) {
		super(context, R.layout.studenti_row_layout, values);
		// TODO Auto-generated constructor stub
		this._context = context;
		this._objects = objects;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.studenti_row_layout, parent,
				false);

		TextView tvIdStudente = (TextView) rowView.findViewById(R.id.tvIdStudente);
		TextView tvCognomeStudente = (TextView) rowView
				.findViewById(R.id.tvCognomeStudente);
		TextView tvNomeStudente = (TextView) rowView
				.findViewById(R.id.tvNomeStudente);

		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvIdStudente.setText(String.valueOf(s.getLong("id_studente")));
			tvIdStudente.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIdStudente.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvIdStudente.setTextSize(12);

			tvCognomeStudente.setText(s.getString("cognome"));
			tvCognomeStudente.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvCognomeStudente.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvCognomeStudente.setTextSize(12);

			tvNomeStudente.setText(s.getString("nome"));
			tvNomeStudente.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvNomeStudente.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvNomeStudente.setTextSize(12);
			
			
			rowView.setTag(s.getLong("id_studente"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowView;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getDropDownView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.studenti_row_layout, parent,
				false);

		TextView tvIdStudente = (TextView) rowView.findViewById(R.id.tvIdStudente);
		TextView tvCognomeStudente = (TextView) rowView
				.findViewById(R.id.tvCognomeStudente);
		TextView tvNomeStudente = (TextView) rowView
				.findViewById(R.id.tvNomeStudente);

		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvIdStudente.setText(String.valueOf(s.getLong("id_studente")));
			tvIdStudente.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIdStudente.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvIdStudente.setTextSize(12);

			tvCognomeStudente.setText(s.getString("cognome"));
			tvCognomeStudente.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvCognomeStudente.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvCognomeStudente.setTextSize(12);

			tvNomeStudente.setText(s.getString("nome"));
			tvNomeStudente.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvNomeStudente.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvNomeStudente.setTextSize(12);
			
			
			rowView.setTag(s.getLong("id_studente"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowView;
	}
	
	
}
