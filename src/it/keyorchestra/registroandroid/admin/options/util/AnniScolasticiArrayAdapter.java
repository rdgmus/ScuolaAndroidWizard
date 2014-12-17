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

public class AnniScolasticiArrayAdapter extends ArrayAdapter<String> {
	private final Context _context;
	private final JSONArray _objects;

	public AnniScolasticiArrayAdapter(Context context, JSONArray objects,
			ArrayList<String> values) {
		super(context, R.layout.as_row_layout, values);
		// TODO Auto-generated constructor stub
		this._context = context;
		this._objects = objects;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getDropDownView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.as_row_layout, parent,
				false);

		TextView tvIdAS = (TextView) rowView.findViewById(R.id.tvIdAS);
		TextView tvAnnoScolastico = (TextView) rowView
				.findViewById(R.id.tvAnnoScolastico);
		TextView tvScuolaId = (TextView) rowView
				.findViewById(R.id.tvScuolaId);
		TextView tvStartDate = (TextView) rowView
				.findViewById(R.id.tvStartDate);
		TextView tvEndDate = (TextView) rowView
				.findViewById(R.id.tvEndDate);
		
		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvIdAS.setText(String.valueOf(s.getLong("id_anno_scolastico")));
			tvIdAS.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIdAS.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvIdAS.setTextSize(12);

			tvAnnoScolastico.setText(s.getString("anno_scolastico"));
			tvAnnoScolastico.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvAnnoScolastico.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvAnnoScolastico.setTextSize(12);

			tvScuolaId.setText(String.valueOf(s.getLong("id_scuola")));
			tvScuolaId.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvScuolaId.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvScuolaId.setTextSize(12);
			
			tvStartDate.setText(s.getString("start_date"));
			tvStartDate.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvStartDate.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvStartDate.setTextSize(12);
			
			tvEndDate.setText(s.getString("end_date"));
			tvEndDate.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvEndDate.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvEndDate.setTextSize(12);
			
			rowView.setTag(s.getLong("id_anno_scolastico"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return rowView;

	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.as_row_layout, parent,
				false);

		TextView tvIdAS = (TextView) rowView.findViewById(R.id.tvIdAS);
		TextView tvAnnoScolastico = (TextView) rowView
				.findViewById(R.id.tvAnnoScolastico);
		TextView tvScuolaId = (TextView) rowView
				.findViewById(R.id.tvScuolaId);
		TextView tvStartDate = (TextView) rowView
				.findViewById(R.id.tvStartDate);
		TextView tvEndDate = (TextView) rowView
				.findViewById(R.id.tvEndDate);

		try {
			JSONObject s = (JSONObject) _objects.get(position);
			tvIdAS.setText(String.valueOf(s.getLong("id_anno_scolastico")));
			tvIdAS.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvIdAS.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvIdAS.setTextSize(12);

			tvAnnoScolastico.setText(s.getString("anno_scolastico"));
			tvAnnoScolastico.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvAnnoScolastico.setBackgroundColor(_context.getResources()
					.getColor(R.color.colorBlack));
			tvAnnoScolastico.setTextSize(12);

			tvScuolaId.setText(String.valueOf(s.getLong("id_scuola")));
			tvScuolaId.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvScuolaId.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvScuolaId.setTextSize(12);
			
			tvStartDate.setText(s.getString("start_date"));
			tvStartDate.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvStartDate.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvStartDate.setTextSize(12);
			
			tvEndDate.setText(s.getString("end_date"));
			tvEndDate.setTextColor(_context.getResources().getColor(
					R.color.colorOrange));
			tvEndDate.setBackgroundColor(_context.getResources().getColor(
					R.color.colorBlack));
			tvEndDate.setTextSize(12);
			
			rowView.setTag(s.getLong("id_anno_scolastico"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rowView;
	}
	
	
}
