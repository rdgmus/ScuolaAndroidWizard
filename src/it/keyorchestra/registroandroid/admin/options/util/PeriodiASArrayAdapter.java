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

public class PeriodiASArrayAdapter extends ArrayAdapter<String>{
	private final Context _context;
	private final JSONArray _objects;

	public PeriodiASArrayAdapter(Context context, JSONArray objects,
			ArrayList<String> values) {
		super(context, R.layout.as_row_layout, values);
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
		View rowView = inflater.inflate(R.layout.periodi_as_row_layout, parent,
				false);

		TextView tvIdPeriodo = (TextView) rowView.findViewById(R.id.tvIdPeriodo);
		TextView tvPeriodoString = (TextView) rowView
				.findViewById(R.id.tvPeriodoString);
		TextView tcStartPeriod = (TextView) rowView
				.findViewById(R.id.tcStartPeriod);
		TextView tvEndPeriod = (TextView) rowView
				.findViewById(R.id.tvEndPeriod);
		
		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvIdPeriodo.setText(String.valueOf(s.getLong("id_periodo")));
			tvIdPeriodo.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIdPeriodo.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvIdPeriodo.setTextSize(12);

			tvPeriodoString.setText(s.getString("periodo"));
			tvPeriodoString.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvPeriodoString.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvPeriodoString.setTextSize(12);

			tcStartPeriod.setText(s.getString("start_period"));
			tcStartPeriod.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tcStartPeriod.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tcStartPeriod.setTextSize(12);
			
			tvEndPeriod.setText(s.getString("end_period"));
			tvEndPeriod.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvEndPeriod.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvEndPeriod.setTextSize(12);
			
			
			rowView.setTag(s.getLong("id_periodo"));
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
		View rowView = inflater.inflate(R.layout.periodi_as_row_layout, parent,
				false);

		TextView tvIdPeriodo = (TextView) rowView.findViewById(R.id.tvIdPeriodo);
		TextView tvPeriodoString = (TextView) rowView
				.findViewById(R.id.tvPeriodoString);
		TextView tcStartPeriod = (TextView) rowView
				.findViewById(R.id.tcStartPeriod);
		TextView tvEndPeriod = (TextView) rowView
				.findViewById(R.id.tvEndPeriod);
		
		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvIdPeriodo.setText(String.valueOf(s.getLong("id_periodo")));
			tvIdPeriodo.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIdPeriodo.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvIdPeriodo.setTextSize(12);

			tvPeriodoString.setText(s.getString("periodo"));
			tvPeriodoString.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvPeriodoString.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvPeriodoString.setTextSize(12);

			tcStartPeriod.setText(s.getString("start_period"));
			tcStartPeriod.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tcStartPeriod.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tcStartPeriod.setTextSize(12);
			
			tvEndPeriod.setText(s.getString("end_period"));
			tvEndPeriod.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvEndPeriod.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvEndPeriod.setTextSize(12);
			
			
			rowView.setTag(s.getLong("id_periodo"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowView;
	}
	
	
}
