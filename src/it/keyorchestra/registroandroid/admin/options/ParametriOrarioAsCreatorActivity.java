package it.keyorchestra.registroandroid.admin.options;

import it.keyorchestra.registroandroid.admin.options.dbMatthed.DatabaseOps;
import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CrudManagerInterface;
import it.keyorchestra.registroandroid.admin.options.interfaces.MyDateTimePickersInterface;
import it.keyorchestra.registroandroid.admin.options.mysqlandroid.MySqlAndroid;
import it.keyorchestra.registroandroid.admin.options.util.FieldsValidator;

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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ParametriOrarioAsCreatorActivity extends Activity implements
		ActivitiesCommonFunctions, CrudManagerInterface, OnFocusChangeListener,
		MyDateTimePickersInterface {

	private JSONArray jArrayParametriOrarioAnnoScolastico;
	private int mHour;
	private int mMinute;

	// CRUD
	Button bCrudSelect, bCrudDelete, bCrudUpdate, bCrudCreate, bCrudCommit,
			bCrudRollback, bCrudClear;
	private Bundle beforeChangeBasket;
	private Bundle afterChangeBasket;
	TextView tvCrudMessage, tvPeriodiAsCount;

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
					removeAllTextIntoFields();
				}

				Toast.makeText(getApplicationContext(),
						"Parametri Orario Anno Scolastico caricati!",
						Toast.LENGTH_LONG).show();
				
				new GetScuolaDescriptionTask().execute();
				new GetAnnoScolasticoDescriptionTask().execute();

				setNextTabVisiblity(View.VISIBLE, 4);

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

		if (jArrayParametriOrarioAnnoScolastico == null) {
			return;
		}
		// SE LA SCUOLA SELEZIONATA E' CAMBIATA RICARICO FLI ANNI SCOLASTICI
		if (jArrayParametriOrarioAnnoScolastico.length() > 0) {
			JSONObject jsonObject;
			try {

				jsonObject = jArrayParametriOrarioAnnoScolastico
						.getJSONObject(0);
				long oldId = jsonObject.getLong("id_anno_scolastico");
				if (id_anno_scolastico != oldId) {
					new LoadParametriOrarioPeriodiAnnoScolasticoTask()
							.execute();
				} else {
					setNextTabVisiblity(View.VISIBLE, 4);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				new LoadParametriOrarioPeriodiAnnoScolasticoTask().execute();
			}
		} else {
			setAllTabsVisibilityFrom(View.GONE, 4);
		}
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
		setContentView(R.layout.parametri_orarioas_creator_activity);

		databaseOps = new DatabaseOps(getBaseContext());

		getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		getId_scuola();
		getId_anno_scolastico();

		bChangeStartOrario = (Button) findViewById(R.id.bChangeStartOrario);
		bChangeStartOrario.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
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

		bCrudSelect = (Button) findViewById(R.id.bCrudSelect);
		bCrudSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Select();
			}
		});

		bCrudCreate = (Button) findViewById(R.id.bCrudCreate);
		bCrudCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				removeAllTextIntoFields();
				inizializzaNuovoRecord();
				beforeChangeBasket = new Bundle();
				beforeChangeBasket.putInt("action",
						CrudManagerInterface.CRUD_ACTION.CREATE);

				setCommitRollback(true);
			}
		});

		bCrudDelete = (Button) findViewById(R.id.bCrudDelete);
		bCrudDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					DeleteRow(jArrayParametriOrarioAnnoScolastico
							.getJSONObject(0));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setCommitRollback(true);
			}

		});

		bCrudUpdate = (Button) findViewById(R.id.bCrudUpdate);
		bCrudUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					UpdateRow(jArrayParametriOrarioAnnoScolastico
							.getJSONObject(0));
					setCommitRollback(true);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		bCrudClear = (Button) findViewById(R.id.bCrudClear);
		bCrudClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Clear();
			}
		});

		bCrudCommit = (Button) findViewById(R.id.bCrudCommit);
		bCrudCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commit();
				setCommitRollback(false);
			}
		});

		bCrudRollback = (Button) findViewById(R.id.bCrudRollback);
		bCrudRollback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Rollback();
				setCommitRollback(false);
			}
		});

		tvCrudMessage = (TextView) findViewById(R.id.tvCrudMessage);

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
			String inizioLezioni = etInizioLezioni.getText().toString();
			String[] split = inizioLezioni.split(":");

			mHour = Integer.valueOf(split[0]);
			mMinute = Integer.valueOf(split[1]);

			TimePickerDialog startDialog = new TimePickerDialog(this,
					mStartLezioniTimeSetListener, mHour, mMinute, true);
			return startDialog;
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener mStartLezioniTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			mHour = hourOfDay;
			mMinute = minute;
			etInizioLezioni.setText(pad(mHour) + ":" + pad(mMinute) + ":00");
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
		etInizioLezioni.setText(new StringBuilder().append(pad(mHour))
				.append(":").append(pad(mMinute)));

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
		beforeChangeBasket = new Bundle();
		try {
			beforeChangeBasket.putLong("id_param_orario",
					data.getLong("id_param_orario"));
			beforeChangeBasket.putLong("id_anno_scolastico",
					data.getLong("id_anno_scolastico"));
			beforeChangeBasket.putString("inizio_lezioni",
					data.getString("inizio_lezioni"));
			beforeChangeBasket.putInt("durata_ora_minuti",
					data.getInt("durata_ora_minuti"));
			beforeChangeBasket
					.putInt("durata_intervallo_minuti", data.getInt("durata_intervallo_minuti"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		beforeChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.UPDATE);

		afterChangeBasket = new Bundle();

		afterChangeBasket.putLong("id_param_orario",
				beforeChangeBasket.getLong("id_param_orario"));

		afterChangeBasket.putLong("id_anno_scolastico",
				beforeChangeBasket.getLong("id_anno_scolastico"));

		afterChangeBasket.putString("inizio_lezioni", etInizioLezioni
				.getText().toString());
		afterChangeBasket.putInt("durata_ora_minuti",Integer.valueOf(etDurataOra.getText().toString()));
		afterChangeBasket.putInt("durata_intervallo_minuti", Integer.valueOf(etDurataIntervallo.getText().toString()));
		afterChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.UPDATE);
		return true;
	}

	@Override
	public boolean DeleteRow(JSONObject data) {
		// TODO Auto-generated method stub
		beforeChangeBasket = new Bundle();
		try {
			beforeChangeBasket.putLong("id_param_orario",
					data.getLong("id_param_orario"));
			beforeChangeBasket.putLong("id_anno_scolastico",
					data.getLong("id_anno_scolastico"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		beforeChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.DELETE);

		return true;
	}

	@Override
	public boolean CreateRow(JSONObject data) {
		// TODO Auto-generated method stub
		removeAllTextIntoFields();
		inizializzaNuovoRecord();

		beforeChangeBasket = new Bundle();
		beforeChangeBasket.putLong("id_anno_scolastico",
				(Long) etAsParamOrario.getTag());
		beforeChangeBasket.putString("inizio_lezioni", etInizioLezioni
				.getText().toString());
		beforeChangeBasket.putString("durata_ora_minuti", etDurataOra.getText()
				.toString());
		beforeChangeBasket.putString("durata_intervallo_minuti",
				etDurataIntervallo.getText().toString());

		return true;
	}

	@Override
	public boolean Select() {
		// TODO Auto-generated method stub
		return new LoadParametriOrarioPeriodiAnnoScolasticoTask().execute() != null;
	}

	@Override
	public void Clear() {
		// TODO Auto-generated method stub
		removeAllTextIntoFields();
	}

	private class UpdateParametriOrarioAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return databaseOps.updateParametriOrarioAnnoScolastico(
					getApplicationContext(), beforeChangeBasket,
					afterChangeBasket);
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
				// STATO DEL COMMIT
				Toast.makeText(getApplicationContext(), "Commit effettuato!",
						Toast.LENGTH_SHORT).show();
				// Ricarica i dati in tabella per mostrare lo stato attuale
				// della tabella
				new LoadParametriOrarioPeriodiAnnoScolasticoTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
				new LoadParametriOrarioPeriodiAnnoScolasticoTask().execute();
			}
			// In ogni caso per rimettere a posto i dati nella maschera
			// new LoadPeriodiAnnoScolasticoTask().execute();
		}

	}

	private class DeleteParametriOrarioAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {

			etAsParamOrario.setTag(id_anno_scolastico);
			etScuolaParamOrario.setTag(id_scuola);
			return databaseOps.deleteParametriOrarioAnnoScolastico(
					getApplicationContext(), beforeChangeBasket);
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
				// STATO DEL COMMIT
				Toast.makeText(getApplicationContext(), "Commit effettuato!",
						Toast.LENGTH_SHORT).show();
				// Ricarica i dati in tabella per mostrare lo stato attuale
				// della tabella
				new LoadParametriOrarioPeriodiAnnoScolasticoTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private class CreateParametriOrarioAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {

			etAsParamOrario.setTag(id_anno_scolastico);
			etScuolaParamOrario.setTag(id_scuola);
			return databaseOps.createParametriOrarioAnnoScolastico(
					getApplicationContext(), beforeChangeBasket);
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
				// STATO DEL COMMIT
				Toast.makeText(getApplicationContext(), "Commit effettuato!",
						Toast.LENGTH_SHORT).show();
				// Ricarica i dati in tabella per mostrare lo stato attuale
				// della tabella
				new LoadParametriOrarioPeriodiAnnoScolasticoTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
				new LoadParametriOrarioPeriodiAnnoScolasticoTask().execute();
			}
		}

	}

	@Override
	public void Commit() {
		// TODO Auto-generated method stub
		switch (beforeChangeBasket.getInt("action")) {
		case CRUD_ACTION.UPDATE:
			if (!FieldsValidator.Is_Valid_MinuteExpression(etDurataOra)) {
				etDurataOra.setFocusable(true);
				etDurataOra.requestFocus();
				break;
			}
			if (!FieldsValidator.Is_Valid_MinuteExpression(etDurataIntervallo)) {
				etDurataIntervallo.setFocusable(true);
				etDurataIntervallo.requestFocus();
				break;
			}
			new UpdateParametriOrarioAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		case CRUD_ACTION.DELETE:
			new DeleteParametriOrarioAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		case CRUD_ACTION.CREATE:
			if (!FieldsValidator.Is_Valid_MinuteExpression(etDurataOra)) {
				etDurataOra.setFocusable(true);
				etDurataOra.requestFocus();
				break;
			}
			if (!FieldsValidator.Is_Valid_MinuteExpression(etDurataIntervallo)) {
				etDurataIntervallo.setFocusable(true);
				etDurataIntervallo.requestFocus();
				break;
			}
			CreateRow(null);
			new CreateParametriOrarioAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		}
	}

	@Override
	public void Rollback() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(),
				"Rollback! cambiamenti scartati!", Toast.LENGTH_SHORT).show();
		etDurataOra.setError(null);
		etDurataIntervallo.setError(null);
		beforeChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.NONE);
		new LoadParametriOrarioPeriodiAnnoScolasticoTask().execute();
	}

	@Override
	public void removeAllTextIntoFields() {
		// TODO Auto-generated method stub
		etAsParamOrario.setText("");
		etDurataIntervallo.setText("");
		etDurataOra.setText("");
		etIdParamOrarioAs.setText("");
		etInizioLezioni.setText("");
		etScuolaParamOrario.setText("");
	}

	@Override
	public void setCommitRollback(boolean visible) {
		// TODO Auto-generated method stub
		if (visible) {
			bCrudCommit.setVisibility(Button.VISIBLE);
			bCrudRollback.setVisibility(Button.VISIBLE);
			tvCrudMessage.setVisibility(TextView.VISIBLE);
			switch (beforeChangeBasket.getInt("action")) {
			case CRUD_ACTION.UPDATE:
				tvCrudMessage.setText("UPDATE in attesa di elaborazione...");
				break;
			case CRUD_ACTION.DELETE:
				tvCrudMessage.setText("DELETE in attesa di elaborazione...");
				break;
			case CRUD_ACTION.CREATE:
				tvCrudMessage.setText("CREATE in attesa di elaborazione...");
				break;
			}

			bCrudUpdate.setVisibility(Button.INVISIBLE);
			bCrudCreate.setVisibility(Button.INVISIBLE);
			bCrudDelete.setVisibility(Button.INVISIBLE);
			bCrudSelect.setVisibility(Button.INVISIBLE);
		} else {
			bCrudCommit.setVisibility(Button.INVISIBLE);
			bCrudRollback.setVisibility(Button.INVISIBLE);
			tvCrudMessage.setVisibility(TextView.INVISIBLE);

			bCrudUpdate.setVisibility(Button.VISIBLE);
			bCrudCreate.setVisibility(Button.VISIBLE);
			bCrudDelete.setVisibility(Button.VISIBLE);
			bCrudSelect.setVisibility(Button.VISIBLE);
		}
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
		new GetScuolaDescriptionTask().execute();
		new GetAnnoScolasticoDescriptionTask().execute();

		etInizioLezioni.setText("08:00:00");
		etDurataOra.setText("60");
		etDurataIntervallo.setText("10");
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
