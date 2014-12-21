package it.keyorchestra.registroandroid.admin.options;

import it.keyorchestra.registroandroid.admin.options.dbMatthed.DatabaseOps;
import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CrudManagerInterface;
import it.keyorchestra.registroandroid.admin.options.interfaces.MyDateTimePickersInterface;
import it.keyorchestra.registroandroid.admin.options.mysqlandroid.MySqlAndroid;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.Toast;

public class ParametriOrarioAsActivity extends Activity implements
		ActivitiesCommonFunctions, CrudManagerInterface, OnFocusChangeListener,
		MyDateTimePickersInterface {

	private JSONArray jArrayParametriOrarioAnnoScolastico;

	Button bChangeStartOrario;
	EditText etIdParamOrarioAs, etScuolaParamOrario, etAsParamOrario,
			etInizioLezioni, etDurataOra, etDurataIntervallo;

	private SharedPreferences getPrefs;
	private DatabaseOps databaseOps;
	static final int START_TIME_DIALOG_ID = 1;

	long id_scuola;
	long id_anno_scolastico;

	/**
	 * @return the id_scuola
	 */
	public long getId_scuola() {
		id_scuola = getPrefs.getLong("id_scuola", -1);
		return id_scuola;
	}

	/**
	 * @return the id_anno_scolastico
	 */
	public long getId_anno_scolastico() {
		id_anno_scolastico = getPrefs.getLong("id_anno_scolastico", -1);

		return id_anno_scolastico;
	}

	private class GetScuolaDescriptionTask extends
			AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			etScuolaParamOrario.setTag(id_scuola);
			return databaseOps.getScuolaDescription(getApplicationContext(),
					id_scuola);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			etScuolaParamOrario.setText(result);
		}

	}

	private class GetAnnoScolasticoDescriptionTask extends
			AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			etAsParamOrario.setTag(id_anno_scolastico);
			return databaseOps.getAnnoScolasticoDescription(
					getApplicationContext(), id_anno_scolastico);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			etAsParamOrario.setText(result);
		}

	}

	private class LoadParametriOrarioPeriodiAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return CaricaParametriOrarioAnnoScolastico();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result) {

				// SELECT `id_param_orario`, `id_anno_scolastico`,
				// `inizio_lezioni`, "
				// + "`durata_ora_minuti`, `durata_intervallo_minuti`
				try {
					JSONObject jsonObject = jArrayParametriOrarioAnnoScolastico
							.getJSONObject(0);
					etIdParamOrarioAs.setText(String.valueOf(jsonObject
							.getLong("id_param_orario")));
					etInizioLezioni.setText(jsonObject
							.getString("inizio_lezioni"));
					etDurataOra.setText(jsonObject
							.getString("durata_ora_minuti"));
					etDurataIntervallo.setText(jsonObject
							.getString("durata_intervallo_minuti"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Toast.makeText(getApplicationContext(),
						"Parametri Orario Anno Scolastico caricati!",
						Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(
						getApplicationContext(),
						"Impossibile caricare i Parametri Orario Anno Scolastico!",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getId_scuola();
		getId_anno_scolastico();

		new LoadParametriOrarioPeriodiAnnoScolasticoTask()
		.execute();
		
//		if (jArrayParametriOrarioAnnoScolastico == null) {
//			return;
//		}
//		// SE LA SCUOLA SELEZIONATA E' CAMBIATA RICARICO FLI ANNI SCOLASTICI
//		if (jArrayParametriOrarioAnnoScolastico.length() > 0) {
//			JSONObject jsonObject;
//			try {
//
//				jsonObject = jArrayParametriOrarioAnnoScolastico
//						.getJSONObject(0);
//				long oldId = jsonObject.getLong("id_anno_scolastico");
//				if (id_anno_scolastico != oldId) {
//					new LoadParametriOrarioPeriodiAnnoScolasticoTask()
//							.execute();
//				} else {
//					setAllTabsVisibilityFrom(View.VISIBLE, 4);
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				new LoadParametriOrarioPeriodiAnnoScolasticoTask().execute();
//			}
//		}else{
//			setAllTabsVisibilityFrom(View.GONE, 4);			
//		}
	}

	public Boolean CaricaParametriOrarioAnnoScolastico() {
		// TODO Auto-generated method stub
		String retrieveTableData = getPrefs
				.getString("retrieveTableData", null);

		String ip = getDatabaseIpFromPreferences();

		String query = "SELECT `id_param_orario`, `id_anno_scolastico`, `inizio_lezioni`, "
				+ "`durata_ora_minuti`, `durata_intervallo_minuti` "
				+ "FROM `parametri_orario_as` WHERE id_anno_scolastico="
				+ id_anno_scolastico;

		try {
			jArrayParametriOrarioAnnoScolastico = new MySqlAndroid()
					.retrieveTableData(getApplicationContext(),
							"http://" + ip + "/" + retrieveTableData + "?sql="
									+ URLEncoder.encode(query, "UTF-8"), null);
			if (jArrayParametriOrarioAnnoScolastico == null)
				return false;

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parametri_orario_as_activity);

		databaseOps = new DatabaseOps(getBaseContext());

		getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		getId_scuola();
		getId_anno_scolastico();

		bChangeStartOrario = (Button) findViewById(R.id.bChangeStartOrario);
		bChangeStartOrario.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(START_TIME_DIALOG_ID);
			}
		});

		etIdParamOrarioAs = (EditText) findViewById(R.id.etIdParamOrarioAs);
		etScuolaParamOrario = (EditText) findViewById(R.id.etScuolaParamOrario);
		etAsParamOrario = (EditText) findViewById(R.id.etAsParamOrario);

		etInizioLezioni = (EditText) findViewById(R.id.etInizioLezioni);
		etInizioLezioni.setOnFocusChangeListener(this);

		etDurataOra = (EditText) findViewById(R.id.etDurataOra);
		etDurataOra.setOnFocusChangeListener(this);

		etDurataIntervallo = (EditText) findViewById(R.id.etDurataIntervallo);
		etDurataIntervallo.setOnFocusChangeListener(this);

		new GetScuolaDescriptionTask().execute();
		new GetAnnoScolasticoDescriptionTask().execute();

		Thread timer = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					new LoadParametriOrarioPeriodiAnnoScolasticoTask()
							.execute();
				}
			}

		};
		timer.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case START_TIME_DIALOG_ID:
			TimePickerDialog startDialog = new TimePickerDialog(this,
					mStartLezioniTimeSetListener, 8, 10, true);
			// startDialog.getDatePicker().setMinDate(
			// getInizioAsCal().getTimeInMillis());
			// startDialog.getDatePicker().setMaxDate(
			// getFineAsCal().getTimeInMillis());
			return startDialog;
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener mStartLezioniTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			etInizioLezioni.setText(pad(hourOfDay)+":"+pad(minute));
		}
	};

	@Override
	public void displayEndDate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayStartDate() {
		// TODO Auto-generated method stub

	}

	@Override
	public String pad(int c) {
		// TODO Auto-generated method stub
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	public void displayCalendarViewDate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayTime() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus) {
			Toast.makeText(getApplicationContext(),
					"" + ((EditText) v).getHint(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean UpdateRow(JSONObject data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean DeleteRow(JSONObject data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean CreateRow(JSONObject data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Select() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void Clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Commit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void Rollback() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAllTextIntoFields() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCommitRollback(boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillFieldsWithData(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveTableIdIntoPreferences(String field_name, long table_id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void inizializzaNuovoRecord() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveTableStringIntoPreferences(String key, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNextTabVisiblity(int visibility, int tabIndex) {
		// TODO Auto-generated method stub
		TabHost tabHost = ((ScuolaWizard) getParent()).getTabHost();
		View tab = tabHost.getTabWidget().getChildAt(tabIndex);
		if (tab != null)
			tab.setVisibility(visibility);
	}

	@Override
	public void registerToolTipFor(ImageButton ib) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean customToast(CharSequence charSequence, int iconId,
			int layoutId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDefaultDatabaseFromPreferences() {
		// TODO Auto-generated method stub
		String defaultDatabase = getPrefs.getString("databaseList", "1");
		return defaultDatabase;
	}

	@Override
	public String getDatabaseIpFromPreferences() {
		// TODO Auto-generated method stub
		String defaultDatabase = getDefaultDatabaseFromPreferences();

		String ip = null;

		if (defaultDatabase.contentEquals("MySQL")) {
			ip = getPrefs.getString("ipMySQL", "");
		} else if (defaultDatabase.contentEquals("PostgreSQL")) {
			ip = getPrefs.getString("ipPostgreSQL", "");
		}
		return ip;
	}

	@Override
	public void startAnimation(View ib, long durationInMilliseconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAllTabsVisibilityFrom(int visibility, int tabIndex) {
		// TODO Auto-generated method stub
		ScuolaWizard parent = (ScuolaWizard) getParent();
		TabHost tabHost = parent.getTabHost();
		int count = tabHost.getTabWidget().getChildCount();
		for (int j = tabIndex; j < count; j++) {
			tabHost.getTabWidget().getChildAt(j).setVisibility(visibility);
		}
	}

}
