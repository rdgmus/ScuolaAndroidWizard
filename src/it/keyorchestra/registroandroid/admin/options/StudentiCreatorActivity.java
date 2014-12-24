package it.keyorchestra.registroandroid.admin.options;

import it.keyorchestra.registroandroid.admin.options.dbMatthed.DatabaseOps;
import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CrudManagerInterface;
import it.keyorchestra.registroandroid.admin.options.interfaces.MyDateTimePickersInterface;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class StudentiCreatorActivity extends Activity implements
		ActivitiesCommonFunctions, CrudManagerInterface, OnFocusChangeListener,
		MyDateTimePickersInterface {
	
	static final int DATA_ENTRATA_DIALOG_ID = 3;
	static final int RITIRATO_DATA_DIALOG_ID = 4;
	private int mYear;
	private int mMonth;
	private int mDay;


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

	private ArrayList<String> classiAnnoScolasticoArray, nomiClasseArray,
			specializzazioniArray;
	private JSONArray jArrayClassiAnnoScolastico, jArrayNomiClasse,
			jArraySpecializzazioni;

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
		
		etIdScuolaStudente = (EditText)findViewById(R.id.etIdScuolaStudente);
		etIdAsStudente = (EditText)findViewById(R.id.etIdAsStudente);
		etIdClasseStudente = (EditText)findViewById(R.id.etIdClasseStudente);
		
		etDataEntrata = (EditText)findViewById(R.id.etDataEntrata);
		etDataEntrata.setText("2014-12-24");
		etRitiratoData= (EditText)findViewById(R.id.etRitiratoData);
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
		
		new GetScuolaDescriptionTask().execute();
		new GetAnnoScolasticoDescriptionTask().execute();
		new GetClasseDescriptionTask().execute();
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
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		String[] split;
		switch (id) {
		case DATA_ENTRATA_DIALOG_ID:
			String startDate = etDataEntrata.getText().toString();
			split = startDate.split("-");
			return new DatePickerDialog(this, mDataEntrataSetListener,
					Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1,
					Integer.valueOf(split[2]));
		case RITIRATO_DATA_DIALOG_ID:
			String endDate = etRitiratoData.getText().toString();
			split = endDate.split("-");
			return new DatePickerDialog(this, mRitiratoDataSetListener,
					Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1,
					Integer.valueOf(split[2]));
		}
		return null;
	}

	// the callback received when the user "sets" the date in the dialog
		private DatePickerDialog.OnDateSetListener mDataEntrataSetListener = new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;

				displayStartDate();
			}
		};
		
		// the callback received when the user "sets" the date in the dialog
		private DatePickerDialog.OnDateSetListener mRitiratoDataSetListener = new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;

				displayEndDate();
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

	}

	@Override
	public void saveTableStringIntoPreferences(String key, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNextTabVisiblity(int visibility, int tabIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAllTabsVisibilityFrom(int visibility, int tabIndex) {
		// TODO Auto-generated method stub

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
