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

public class ClassiArrayAdapter extends ArrayAdapter<String> {
	private final Context _context;
	private final JSONArray _objects;

	public ClassiArrayAdapter(Context context, JSONArray objects,
			ArrayList<String> values) {
		super(context, R.layout.classi_row_layout, values);
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
		View rowView = inflater.inflate(R.layout.classi_row_layout, parent,
				false);

		TextView tvIdClasse = (TextView) rowView.findViewById(R.id.tvIdClasse);
		TextView tvClasse = (TextView) rowView
				.findViewById(R.id.tvClasse);
		TextView tvSpecializzazione = (TextView) rowView
				.findViewById(R.id.tvSpecializzazione);

		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvIdClasse.setText(String.valueOf(s.getLong("id_classe")));
			tvIdClasse.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIdClasse.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvIdClasse.setTextSize(12);

			tvClasse.setText(s.getString("nome_classe"));
			tvClasse.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvClasse.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvClasse.setTextSize(12);

			tvSpecializzazione.setText(s.getString("specializzazione"));
			tvSpecializzazione.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvSpecializzazione.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvSpecializzazione.setTextSize(12);
			
			rowView.setTag(s.getLong("id_classe"));
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
		View rowView = inflater.inflate(R.layout.classi_row_layout, parent,
				false);

		TextView tvIdClasse = (TextView) rowView.findViewById(R.id.tvIdClasse);
		TextView tvClasse = (TextView) rowView
				.findViewById(R.id.tvClasse);
		TextView tvSpecializzazione = (TextView) rowView
				.findViewById(R.id.tvSpecializzazione);

		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvIdClasse.setText(String.valueOf(s.getLong("id_classe")));
			tvIdClasse.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIdClasse.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvIdClasse.setTextSize(12);

			tvClasse.setText(s.getString("nome_classe"));
			tvClasse.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvClasse.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvClasse.setTextSize(12);

			tvSpecializzazione.setText(s.getString("specializzazione"));
			tvSpecializzazione.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvSpecializzazione.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvSpecializzazione.setTextSize(12);
			
			rowView.setTag(s.getLong("id_classe"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowView;
	}

	
}
