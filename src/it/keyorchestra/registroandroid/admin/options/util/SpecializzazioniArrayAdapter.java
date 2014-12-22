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

public class SpecializzazioniArrayAdapter extends ArrayAdapter<String> {
	private final Context _context;
	private final JSONArray _objects;

	public SpecializzazioniArrayAdapter(Context context, JSONArray objects,
			ArrayList<String> values) {
		super(context, R.layout.icon_row_layout, values);
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
		View rowView = inflater.inflate(R.layout.icon_row_layout, parent,
				false);

		TextView tvMyText = (TextView) rowView.findViewById(R.id.tvMyText);

		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvMyText.setText(s.getString("specializzazione"));
			tvMyText.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvMyText.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvMyText.setTextSize(12);

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
		View rowView = inflater.inflate(R.layout.icon_row_layout, parent,
				false);

		TextView tvMyText = (TextView) rowView.findViewById(R.id.tvMyText);

		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvMyText.setText(s.getString("specializzazione"));
			tvMyText.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvMyText.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvMyText.setTextSize(12);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowView;
	}
	
}
