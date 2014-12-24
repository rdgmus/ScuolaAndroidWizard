package it.keyorchestra.registroandroid.admin.options;

import it.keyorchestra.registroandroid.admin.options.dbMatthed.DatabaseOps;
import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CrudManagerInterface;
import it.keyorchestra.registroandroid.admin.options.interfaces.MyDateTimePickersInterface;
import it.keyorchestra.registroandroid.admin.options.mysqlandroid.MySqlAndroid;
import it.keyorchestra.registroandroid.admin.options.util.StudentiClasseArrayAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class StudentiCreatorActivity extends Activity implements
		ActivitiesCommonFunctions, CrudManagerInterface, OnFocusChangeListener,
		MyDateTimePickersInterface {

	static final int DATA_ENTRATA_DIALOG_ID = 3;
	static final int RITIRATO_DATA_DIALOG_ID = 4;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int _which;

	EditText etIdStudente, etIdScuolaStudente, etIdAsStudente,
			etIdClasseStudente, etCognomeStudente, etNomeStudente,
			etDataEntrata, etRitiratoData;
	CheckBox cbAttivo;
	Button bChangeDataEntrata, bChangeRitiratoData;
	Spinner spinnerRecords;
	// CRUD
	Button bCrudSelect, bCrudDelete, bCrudUpdate, bCrudCreate, bCrudCommit,
			bCrudRollback, bCrudClear;
	private Bundle beforeChangeBasket;
	private Bundle afterChangeBasket;
	TextView tvRecordsCount, tvCrudMessage;

	private SharedPreferences getPrefs;
	private DatabaseOps databaseOps;

	private ArrayList<String> studentiClasseArray;
	private JSONArray jArrayStudentiClasse;

	long id_anno_scolastico;
	long id_scuola;
	long id_classe;
	long id_studente;

	/**
	 * @return the id_anno_scolastico
	 */
	public long getId_anno_scolastico() {
		id_anno_scolastico = getPrefs.getLong("id_anno_scolastico", -1);
		return id_anno_scolastico;
	}

	/**
	 * @return the id_scuola
	 */
	public long getId_scuola() {
		id_scuola = getPrefs.getLong("id_scuola", -1);
		return id_scuola;
	}

	/**
	 * @return the id_classe
	 */
	public long getId_classe() {
		id_classe = getPrefs.getLong("id_classe", -1);
		return id_classe;
	}

	private class GetClasseDescriptionTask extends
			AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			etIdScuolaStudente.setTag(id_scuola);
			etIdAsStudente.setTag(id_anno_scolastico);
			etIdClasseStudente.setTag(id_classe);
			return databaseOps.getClasseDescription(getApplicationContext(),
					id_classe);
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
			etIdClasseStudente.setText(result);
		}

	}

	private class GetScuolaDescriptionTask extends
			AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			etIdScuolaStudente.setTag(id_scuola);
			etIdAsStudente.setTag(id_anno_scolastico);
			etIdClasseStudente.setTag(id_classe);
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
			etIdScuolaStudente.setText(result);
		}

	}

	private class GetAnnoScolasticoDescriptionTask extends
			AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			etIdScuolaStudente.setTag(id_scuola);
			etIdAsStudente.setTag(id_anno_scolastico);
			etIdClasseStudente.setTag(id_classe);

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
			etIdAsStudente.setText(result);
		}

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
		setContentView(R.layout.studenti_creator_activity);

		databaseOps = new DatabaseOps(getBaseContext());

		getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		getId_scuola();
		getId_anno_scolastico();
		getId_classe();

		cbAttivo = (CheckBox)findViewById(R.id.cbAttivo);
		cbAttivo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(!isChecked){
					now();
					displayEndDate();
				}else{
					etRitiratoData.setText("null");
				}
			}
		});
		
		etIdStudente = (EditText) findViewById(R.id.etIdStudente);
		etIdScuolaStudente = (EditText) findViewById(R.id.etIdScuolaStudente);
		etIdAsStudente = (EditText) findViewById(R.id.etIdAsStudente);
		etIdClasseStudente = (EditText) findViewById(R.id.etIdClasseStudente);

		etCognomeStudente = (EditText) findViewById(R.id.etCognomeStudente);
		etNomeStudente = (EditText) findViewById(R.id.etNomeStudente);

		etDataEntrata = (EditText) findViewById(R.id.etDataEntrata);
		etDataEntrata.setText("2014-12-24");
		etRitiratoData = (EditText) findViewById(R.id.etRitiratoData);
		etRitiratoData.setText("2014-12-24");

		bChangeDataEntrata = (Button) findViewById(R.id.bChangeDataEntrata);
		bChangeDataEntrata.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Carica la data

				showDialog(DATA_ENTRATA_DIALOG_ID);
			}

		});
		bChangeRitiratoData = (Button) findViewById(R.id.bChangeRitiratoData);
		bChangeRitiratoData.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Carica la data

				showDialog(RITIRATO_DATA_DIALOG_ID);
			}

		});

		tvRecordsCount = (TextView) findViewById(R.id.tvRecordsCount);

		spinnerRecords = (Spinner) findViewById(R.id.spinnerRecords);
		spinnerRecords.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				id_studente = (Long) view.getTag();
				// Salva id_anno_scolastico nelle preferenze
				saveTableIdIntoPreferences("id_studente", id_studente);

				try {
					JSONObject jsonObject = jArrayStudentiClasse
							.getJSONObject(position);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Toast.makeText(getApplicationContext(),
				// "id_anno_scolastico:" + id_anno_scolastico,
				// Toast.LENGTH_SHORT).show();
				fillFieldsWithData(position);
				setNextTabVisiblity(View.VISIBLE, 6);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		new GetScuolaDescriptionTask().execute();
		new GetAnnoScolasticoDescriptionTask().execute();
		new GetClasseDescriptionTask().execute();

		Thread timer = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					new LoadStudentiClasseTask().execute();
				}
			}

		};
		timer.start();
	}

	private class LoadStudentiClasseTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return CaricaStudentiClasse();
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
				StudentiClasseArrayAdapter studentiClasseAdapter = new StudentiClasseArrayAdapter(
						getApplicationContext(), jArrayStudentiClasse,
						studentiClasseArray);

				studentiClasseAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// Apply the adapter to the spinner
				spinnerRecords.setAdapter(studentiClasseAdapter);

				Toast.makeText(getApplicationContext(), "Studenti caricati!",
						Toast.LENGTH_LONG).show();
				tvRecordsCount.setText("(" + spinnerRecords.getCount() + ")");
				if (spinnerRecords.getCount() == 0) {
					inizializzaNuovoRecord();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Impossibile caricare gli studenti!", Toast.LENGTH_LONG)
						.show();
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

		setAllTabsVisibilityFrom(View.GONE,6);

		getId_scuola();
		getId_anno_scolastico();
		getId_classe();
		new GetScuolaDescriptionTask().execute();
		new GetAnnoScolasticoDescriptionTask().execute();
		new GetClasseDescriptionTask().execute();
		
		if (jArrayStudentiClasse == null)
			return;
		// SE LA SCUOLA SELEZIONATA E' CAMBIATA RICARICO FLI ANNI SCOLASTICI
		if (jArrayStudentiClasse.length() > 0) {
			JSONObject jsonObject;
			try {

				jsonObject = jArrayStudentiClasse.getJSONObject(0);
				long oldId = jsonObject.getLong("id_classe");
				if (id_classe != oldId) {
					new LoadStudentiClasseTask().execute();
				} else {
					setNextTabVisiblity(View.VISIBLE, 6);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				new LoadStudentiClasseTask().execute();
			}
		}
	}

	public Boolean CaricaStudentiClasse() {
		// TODO Auto-generated method stub
		studentiClasseArray = new ArrayList<String>();
		String retrieveTableData = getPrefs
				.getString("retrieveTableData", null);

		String ip = getDatabaseIpFromPreferences();

		// jsonRuoliAmmessi(retrieveTableData, ip);

		String query = "SELECT `id_studente`, `id_classe`, `id_anno_scolastico`, "
				+ "`anno_scolastico`, `cognome`, `nome`, `attivo`, `data_entrata`, "
				+ "`ritirato_data` FROM `studenti` WHERE `id_classe`="
				+ id_classe + " ORDER BY `cognome`,`nome`";

		try {
			jArrayStudentiClasse = new MySqlAndroid().retrieveTableData(
					getApplicationContext(),
					"http://" + ip + "/" + retrieveTableData + "?sql="
							+ URLEncoder.encode(query, "UTF-8"), null);
			if (jArrayStudentiClasse == null)
				return false;

			for (int i = 0; i < jArrayStudentiClasse.length(); i++) {
				JSONObject json_data;
				try {
					json_data = jArrayStudentiClasse.getJSONObject(i);

					studentiClasseArray.add(json_data.getString("cognome")
							+ " " + json_data.getString("nome"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setAllTabsVisibilityFrom(View.GONE, 6);
				}

			}

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
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		String[] split;
		switch (id) {
		case DATA_ENTRATA_DIALOG_ID:
			String startDate = etDataEntrata.getText().toString();
			if (startDate.equals("null")) {
				now();
				startDate = mYear + "-" + pad(mMonth+1) + "-" + pad(mDay);
			}
			split = startDate.split("-");
			DatePickerDialog dialogDataEntrata = new DatePickerDialog(this,
					mDataEntrataSetListener, Integer.valueOf(split[0]),
					Integer.valueOf(split[1]) - 1, Integer.valueOf(split[2]));

			dialogDataEntrata.setButton(DialogInterface.BUTTON_POSITIVE, "Set",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							_which = which;
						}
					});

			dialogDataEntrata.setButton(DialogInterface.BUTTON_NEGATIVE,
					"Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEGATIVE) {
								_which = which;
							}
						}
					});

			return dialogDataEntrata;
		case RITIRATO_DATA_DIALOG_ID:
			String endDate = etRitiratoData.getText().toString();
			if (endDate.equals("null")) {
				now();
				endDate = mYear + "-" + pad(mMonth+1) + "-" + pad(mDay);
			}
			split = endDate.split("-");
			DatePickerDialog dialogRitiratoData = new DatePickerDialog(this,
					mRitiratoDataSetListener, Integer.valueOf(split[0]),
					Integer.valueOf(split[1]) - 1, Integer.valueOf(split[2]));
			
			dialogRitiratoData.setButton(DialogInterface.BUTTON_POSITIVE, "Set",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							_which = which;
						}
					});

			dialogRitiratoData.setButton(DialogInterface.BUTTON_NEGATIVE,
					"Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == DialogInterface.BUTTON_NEGATIVE) {
								_which = which;
							}
						}
					});
			
			return dialogRitiratoData;
		}
		return null;
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDataEntrataSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			switch(_which){
			case DialogInterface.BUTTON_POSITIVE:
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;

				displayStartDate();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			}
		}
	};

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mRitiratoDataSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			switch(_which){
			case DialogInterface.BUTTON_POSITIVE:
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;

				displayEndDate();
				cbAttivo.setChecked(false);
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			}
			
		}
	};

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

	// updates the date in the EditText
	@Override
	public void displayStartDate() {
		etDataEntrata.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mYear).append("-").append(pad(mMonth + 1)).append("-")
				.append(pad(mDay)));
	}

	// updates the date in the EditText
	@Override
	public void displayEndDate() {
		etRitiratoData.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mYear).append("-").append(pad(mMonth + 1)).append("-")
				.append(pad(mDay)));
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
		 new LoadStudentiClasseTask().execute();
		 return true;
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
		try {
			JSONObject jsonObiect = jArrayStudentiClasse
					.getJSONObject(position);
			etIdStudente.setText(String.valueOf(jsonObiect
					.getLong("id_studente")));
			etCognomeStudente.setText(jsonObiect.getString("cognome"));
			etNomeStudente.setText(jsonObiect.getString("nome"));

			etDataEntrata
					.setText(jsonObiect.getString("data_entrata") == null ? ""
							: jsonObiect.getString("data_entrata"));
			etRitiratoData
					.setText(jsonObiect.getString("ritirato_data") == null ? ""
							: jsonObiect.getString("ritirato_data"));
			
			cbAttivo.setChecked(jsonObiect.getInt("attivo") == 1);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void saveTableIdIntoPreferences(String field_name, long table_id) {
		// TODO Auto-generated method stub
		Editor editor = getPrefs.edit();
		editor.putLong(field_name, table_id);
		editor.apply();
	}

	@Override
	public void inizializzaNuovoRecord() {
		// TODO Auto-generated method stub
		etIdStudente.setText("");
		etCognomeStudente.setText("");
		etNomeStudente.setText("");
		etDataEntrata.setText("");
		etRitiratoData.setText("null");
		cbAttivo.setChecked(true);
		
		now();
		displayStartDate();
	}

	/**
	 * Imposta le variabili temporali ad ora!
	 */
	private void now() {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public void saveTableStringIntoPreferences(String key, String value) {
		// TODO Auto-generated method stub
		Editor editor = getPrefs.edit();
		editor.putString(key, value);
		editor.apply();
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
	public void setAllTabsVisibilityFrom(int visibility, int tabIndex) {
		// TODO Auto-generated method stub
		ScuolaWizard parent = (ScuolaWizard) getParent();
		TabHost tabHost = parent.getTabHost();
		int count = tabHost.getTabWidget().getChildCount();
		for (int j = tabIndex; j < count; j++) {
			tabHost.getTabWidget().getChildAt(j).setVisibility(visibility);
		}
	}

	@Override
	public void syncronizeSpinner(Spinner spinner, String keyValue) {
		// TODO Auto-generated method stub

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

}
