package it.keyorchestra.registroandroid.admin.options;

import it.keyorchestra.registroandroid.admin.options.dbMatthed.DatabaseOps;
import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CrudManagerInterface;
import it.keyorchestra.registroandroid.admin.options.interfaces.MyDateTimePickersInterface;
import it.keyorchestra.registroandroid.admin.options.mysqlandroid.MySqlAndroid;
import it.keyorchestra.registroandroid.admin.options.util.FieldsValidator;
import it.keyorchestra.registroandroid.admin.options.util.PeriodiASArrayAdapter;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class PeriodiAsCreatorActivity extends Activity implements
		ActivitiesCommonFunctions, CrudManagerInterface, OnFocusChangeListener,
		MyDateTimePickersInterface {

	static final int START_DATE_DIALOG_ID = 3;
	static final int END_DATE_DIALOG_ID = 4;
	static final int BIMESTRE = 0;
	static final int TRIMESTRE = 1;
	static final int QUADRIMESTRE = 2;
	static final int PENTAMESTRE = 3;
	static final int SEMESTRE = 4;

	// CRUD
	Button bCrudSelect, bCrudDelete, bCrudUpdate, bCrudCreate, bCrudCommit,
			bCrudRollback, bCrudClear;
	private Bundle beforeChangeBasket;
	private Bundle afterChangeBasket;
	TextView tvCrudMessage, tvPeriodiAsCount;

	EditText etIdPeriodo, etIdScuolaFK, etIdAnnoScolasticoFK;
	EditText etPeriodoString, etStartPeriod, etEndPeriod;
	Spinner spinnerPeriods, spinnerRecords;

	Button bChangeStartPeriod, bChangeEndPeriod;

	ProgressBar progressBarCoperturaAs;

	private int mYear;
	private int mMonth;
	private int mDay;

	private int mHour;
	private int mMinute;

	private SharedPreferences getPrefs;
	private DatabaseOps databaseOps;
	private ArrayList<String> periodiAnnoScolasticoArray;
	private JSONArray jArrayPeriodiAnnoScolastico;

	long id_periodo;
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
		
		if (jArrayPeriodiAnnoScolastico == null) {
			return;
		}
		// SE LA SCUOLA SELEZIONATA E' CAMBIATA RICARICO FLI ANNI SCOLASTICI
		if (jArrayPeriodiAnnoScolastico.length() > 0) {
			JSONObject jsonObject;
			try {

				jsonObject = jArrayPeriodiAnnoScolastico.getJSONObject(0);
				long oldId = jsonObject.getLong("id_anno_scolastico");
				if (id_anno_scolastico != oldId) {
					new LoadPeriodiAnnoScolasticoTask().execute();
				}else {
					setNextTabVisiblity(View.VISIBLE, 3);
				}
				oldId = jsonObject.getLong("id_scuola");
				if (id_scuola != oldId) {
					new LoadPeriodiAnnoScolasticoTask().execute();
				}else {
					setNextTabVisiblity(View.VISIBLE, 3);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				new LoadPeriodiAnnoScolasticoTask().execute();
			}
		}

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
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
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
		setContentView(R.layout.periodi_as_creator_activity);

		databaseOps = new DatabaseOps(getBaseContext());

		getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		getId_scuola();
		getId_anno_scolastico();
		now();

		bCrudSelect = (Button) findViewById(R.id.bCrudSelect);
		bCrudSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Select();
			}
		});
		bCrudDelete = (Button) findViewById(R.id.bCrudDelete);
		bCrudDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					DeleteRow(jArrayPeriodiAnnoScolastico
							.getJSONObject(spinnerRecords
									.getSelectedItemPosition()));
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
					UpdateRow(jArrayPeriodiAnnoScolastico
							.getJSONObject(spinnerRecords
									.getSelectedItemPosition()));
					setCommitRollback(true);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

		bCrudCommit = (Button) findViewById(R.id.bCrudCommit);
		bCrudCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Commit();
				// setCommitRollback(false);
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

		bCrudClear = (Button) findViewById(R.id.bCrudClear);
		bCrudClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Clear();
			}
		});

		tvCrudMessage = (TextView) findViewById(R.id.tvCrudMessage);

		tvPeriodiAsCount = (TextView) findViewById(R.id.tvRecordsCount);

		etIdPeriodo = (EditText) findViewById(R.id.etIdPeriodo);

		etIdScuolaFK = (EditText) findViewById(R.id.etIdScuolaFK);
		etIdAnnoScolasticoFK = (EditText) findViewById(R.id.etIdAnnoScolasticoFK);
		etPeriodoString = (EditText) findViewById(R.id.etPeriodoString);
		etPeriodoString.setOnFocusChangeListener(this);

		etStartPeriod = (EditText) findViewById(R.id.etStartPeriod);
		etStartPeriod.setOnFocusChangeListener(this);
		etEndPeriod = (EditText) findViewById(R.id.etEndPeriod);
		etEndPeriod.setOnFocusChangeListener(this);

		spinnerPeriods = (Spinner) findViewById(R.id.spinnerPeriods);
		spinnerPeriods.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String periodo = (String) spinnerPeriods
						.getItemAtPosition(position);
				Toast.makeText(getApplicationContext(), "periodo:" + periodo,
						Toast.LENGTH_SHORT).show();
				etPeriodoString.setText(periodo);
				calculateEndDate();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinnerRecords = (Spinner) findViewById(R.id.spinnerRecords);
		spinnerRecords.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				id_periodo = (Long) view.getTag();
				// Salva id_anno_scolastico nelle preferenze
				saveTableIdIntoPreferences("id_periodo", id_periodo);

				Toast.makeText(getApplicationContext(),
						"id_periodo:" + id_periodo, Toast.LENGTH_SHORT).show();
				fillFieldsWithData(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		bChangeStartPeriod = (Button) findViewById(R.id.bChangeStartPeriod);
		bChangeStartPeriod.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(START_DATE_DIALOG_ID);
			}
		});

		bChangeEndPeriod = (Button) findViewById(R.id.bChangeEndPeriod);
		bChangeEndPeriod.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(END_DATE_DIALOG_ID);
			}
		});

		progressBarCoperturaAs = (ProgressBar) findViewById(R.id.progressBarCoperturaAs);

		Thread timer = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					new LoadPeriodiAnnoScolasticoTask().execute();
				}
			}

		};
		timer.start();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		String[] split;
		switch (id) {
		case START_DATE_DIALOG_ID:
			String startDate = etStartPeriod.getText().toString();
			split = startDate.split("-");
			return new DatePickerDialog(this, mStartDateSetListener,
					Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1,
					Integer.valueOf(split[2]));
		case END_DATE_DIALOG_ID:
			String endDate = etEndPeriod.getText().toString();
			split = endDate.split("-");
			return new DatePickerDialog(this, mEndDateSetListener,
					Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1,
					Integer.valueOf(split[2]));
		}
		return null;
	}

	@Override
	public String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	// updates the date in the EditText
	@Override
	public void displayStartDate() {
		etStartPeriod.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mYear).append("-").append(pad(mMonth + 1)).append("-")
				.append(pad(mDay)));
	}

	// updates the date in the EditText
	@Override
	public void displayEndDate() {
		etEndPeriod.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mYear).append("-").append(pad(mMonth + 1)).append("-")
				.append(pad(mDay)));
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			displayStartDate();
			calculateEndDate();
		}
	};

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			displayEndDate();
		}
	};

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
			beforeChangeBasket
					.putLong("id_periodo", data.getLong("id_periodo"));
			beforeChangeBasket.putLong("id_anno_scolastico",
					data.getLong("id_anno_scolastico"));
			beforeChangeBasket.putLong("id_scuola", data.getLong("id_scuola"));

			beforeChangeBasket.putString("periodo", data.getString("periodo"));
			beforeChangeBasket.putString("start_date",
					data.getString("start_date"));
			beforeChangeBasket
					.putString("end_date", data.getString("end_date"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		beforeChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.UPDATE);

		afterChangeBasket = new Bundle();

		afterChangeBasket.putLong("id_periodo",
				beforeChangeBasket.getLong("id_periodo"));
		afterChangeBasket.putLong("id_anno_scolastico",
				beforeChangeBasket.getLong("id_anno_scolastico"));
		afterChangeBasket.putLong("id_scuola",
				beforeChangeBasket.getLong("id_scuola"));

		afterChangeBasket.putString("periodo", etPeriodoString.getText()
				.toString());
		afterChangeBasket.putString("start_date", etStartPeriod.getText()
				.toString());
		afterChangeBasket.putString("end_date", etEndPeriod.getText()
				.toString());
		afterChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.UPDATE);
		return true;
	}

	@Override
	public boolean DeleteRow(JSONObject data) {
		// TODO Auto-generated method stub
		beforeChangeBasket = new Bundle();
		try {
			beforeChangeBasket.putLong("id_scuola", data.getLong("id_scuola"));
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
		beforeChangeBasket.putString("periodo", etPeriodoString.getText()
				.toString());
		beforeChangeBasket.putLong("id_scuola", (Long) etIdScuolaFK.getTag());
		beforeChangeBasket.putLong("id_anno_scolastico",
				(Long) etIdAnnoScolasticoFK.getTag());

		beforeChangeBasket.putString("start_date", etStartPeriod.getText()
				.toString());
		beforeChangeBasket.putString("end_date", etEndPeriod.getText()
				.toString());

		return true;
	}

	@Override
	public boolean Select() {
		// TODO Auto-generated method stub
		getId_scuola();
		getId_anno_scolastico();
		new LoadPeriodiAnnoScolasticoTask().execute();
		return true;
	}

	@Override
	public void Clear() {
		// TODO Auto-generated method stub
		removeAllTextIntoFields();
		// etIdPeriodo.setError(null);
		// etIdScuolaFK.setError(null);
		// etIdAnnoScolasticoFK.setError(null);
		etPeriodoString.setError(null);
		etStartPeriod.setError(null);
		etEndPeriod.setError(null);
	}

	@Override
	public void Commit() {
		// TODO Auto-generated method stub
		switch (beforeChangeBasket.getInt("action")) {
		case CRUD_ACTION.UPDATE:
			if (!FieldsValidator.Is_Valid_StartDate(etStartPeriod)) {
				etStartPeriod.setFocusable(true);
				etStartPeriod.requestFocus();
				break;
			}
			if (!FieldsValidator.Is_Valid_EndDate(etEndPeriod)) {
				etEndPeriod.setFocusable(true);
				etEndPeriod.requestFocus();
				break;
			}
			// new UpdatePeriodoAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		case CRUD_ACTION.DELETE:
			// new DeletePeriodoAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		case CRUD_ACTION.CREATE:
			if (!FieldsValidator.Is_Valid_StartDate(etStartPeriod)) {
				etStartPeriod.setFocusable(true);
				etStartPeriod.requestFocus();
				break;
			}
			if (!FieldsValidator.Is_Valid_EndDate(etStartPeriod)) {
				etStartPeriod.setFocusable(true);
				etStartPeriod.requestFocus();
				break;
			}
			CreateRow(null);
			new CreatePeriodoAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		}
	}

	@Override
	public void Rollback() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(),
				"Rollback! cambiamenti scartati!", Toast.LENGTH_SHORT).show();
		etStartPeriod.setFocusable(false);
		etStartPeriod.setError(null);

		etEndPeriod.setFocusable(false);
		etEndPeriod.setError(null);
		new LoadPeriodiAnnoScolasticoTask().execute();
	}

	@Override
	public void removeAllTextIntoFields() {
		// TODO Auto-generated method stub
		etIdPeriodo.setText("");
		etIdScuolaFK.setText("");
		etIdAnnoScolasticoFK.setText("");
		etPeriodoString.setText("");
		etStartPeriod.setText("");
		etEndPeriod.setText("");
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
			JSONObject jsonObiect = jArrayPeriodiAnnoScolastico
					.getJSONObject(position);
			etIdPeriodo
					.setText(String.valueOf(jsonObiect.getLong("id_periodo")));

			etIdScuolaFK.setTag(jsonObiect.getLong("id_scuola"));
			etIdScuolaFK.setText("[" + jsonObiect.getLong("id_scuola") + "] "
					+ jsonObiect.getString("tipo_scuola_acronimo") + " - "
					+ jsonObiect.getString("nome_scuola"));

			etIdAnnoScolasticoFK.setTag(jsonObiect
					.getLong("id_anno_scolastico"));
			etIdAnnoScolasticoFK.setText("["
					+ jsonObiect.getLong("id_anno_scolastico") + "] "
					+ jsonObiect.getString("anno_scolastico"));

			etPeriodoString.setText(jsonObiect.getString("periodo"));

			etStartPeriod.setText(jsonObiect.getString("start_period"));
			etEndPeriod.setText(jsonObiect.getString("end_period"));
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
		calculateStartDate();
		// spinnerPeriods.setSelection(0);// Seleziona BIMESTRE
		// etPeriodoString.setText((CharSequence) spinnerPeriods
		// .getItemAtPosition(0));
		// calculateEndDate();
		//
		new GetScuolaDescriptionTask().execute();
		new GetAnnoScolasticoDescriptionTask().execute();
	}

	private void calculateEndDate() {
		// TODO Auto-generated method stub
		// QUI COSTRUISCE UN OGGETTO Calendar
		// con la data di inizio periodo mostrata nell'EditText
		String start_date = etStartPeriod.getText().toString();
		if (start_date.length() == 0)
			return;
		String[] split = start_date.split("-");
		mYear = Integer.valueOf(split[0]);
		mMonth = Integer.valueOf(split[1]) - 1;
		mDay = Integer.valueOf(split[2]);

		Calendar c = Calendar.getInstance();
		c.set(mYear, mMonth, mDay);

		// Qui la data di fine anno scolastico dalle preferenze
		String fineAS = getPrefs.getString("end_date", "");

		split = fineAS.split("-");
		int year = Integer.valueOf(split[0]);
		int month = Integer.valueOf(split[1]) - 1;
		int day = Integer.valueOf(split[2]);
		Calendar d = Calendar.getInstance();
		d.set(year, month, day);

		switch (spinnerPeriods.getSelectedItemPosition()) {
		case BIMESTRE:
			c.add(Calendar.MONTH, 2);
			break;
		case TRIMESTRE:
			c.add(Calendar.MONTH, 3);
			break;
		case QUADRIMESTRE:
			c.add(Calendar.MONTH, 4);
			break;
		case PENTAMESTRE:
			c.add(Calendar.MONTH, 5);
			break;
		case SEMESTRE:
			c.add(Calendar.MONTH, 6);
			break;
		}
		// TEST SE DATA FINALE SUPERA FINE ANNO SCOLASTICO
		if (c.after(d)) {
			mYear = d.get(Calendar.YEAR);
			mMonth = d.get(Calendar.MONTH);
			mDay = d.get(Calendar.DAY_OF_MONTH);
		} else {

			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
		}
		displayEndDate();
	}

	private void calculateStartDate() {
		// TODO Auto-generated method stub
		new CalculateStartDateTask().execute();
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

	private class GetScuolaDescriptionTask extends
			AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			etIdScuolaFK.setTag(id_scuola);
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
			etIdScuolaFK.setText(result);
		}

	}

	private class CreatePeriodoAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {

			etIdScuolaFK.setTag(id_scuola);
			return databaseOps.createPeriodoAnnoScolastico(
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
				new LoadPeriodiAnnoScolasticoTask().execute();
			}
		}

	}

	private class GetAnnoScolasticoDescriptionTask extends
			AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			etIdAnnoScolasticoFK.setTag(id_anno_scolastico);
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
			etIdAnnoScolasticoFK.setText(result);
		}

	}

	private class LoadPeriodiAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return CaricaArrayPeriodiAnnoScolastico();
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
				PeriodiASArrayAdapter periodiAsAdapter = new PeriodiASArrayAdapter(
						getApplicationContext(), jArrayPeriodiAnnoScolastico,
						periodiAnnoScolasticoArray);

				periodiAsAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// Apply the adapter to the spinner
				spinnerRecords.setAdapter(periodiAsAdapter);

				Toast.makeText(getApplicationContext(),
						"Periodi Anno Scolastico caricati!", Toast.LENGTH_LONG)
						.show();

				tvPeriodiAsCount.setText("(" + spinnerRecords.getCount() + ")");
				if (spinnerRecords.getCount() == 0) {
					removeAllTextIntoFields();
					inizializzaNuovoRecord();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Impossibile caricare i Periodo Anno Scolastico!",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	private class CalculateStartDateTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			return databaseOps.getNextPeriodStartDateAsString(
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
			String[] split = result.split("-");
			mYear = Integer.valueOf(split[0]);
			mMonth = Integer.valueOf(split[1]) - 1;
			mDay = Integer.valueOf(split[2]);
			displayStartDate();
			spinnerPeriods.setSelection(0);// Seleziona BIMESTRE
			etPeriodoString.setText((CharSequence) spinnerPeriods
					.getItemAtPosition(0));
			calculateEndDate();

		}

	}

	public Boolean CaricaArrayPeriodiAnnoScolastico() {
		// TODO Auto-generated method stub
		periodiAnnoScolasticoArray = new ArrayList<String>();
		String retrieveTableData = getPrefs
				.getString("retrieveTableData", null);

		String ip = getDatabaseIpFromPreferences();

		// jsonRuoliAmmessi(retrieveTableData, ip);

		String query = "SELECT A.`id_periodo`, A.`id_scuola`, A.`id_anno_scolastico`, A.`periodo`, "
				+ "A.`start_date` AS start_period, \n"
				+ "A.`end_date` AS end_period, \n"
				+ "B.`tipo_scuola_acronimo`, B.`nome_scuola`,\n"
				+ "C.`anno_scolastico`,\n"
				+ "C.`start_date` AS start_as,\n"
				+ "C.`end_date` AS end_as\n"
				+ "FROM `periodi_anno_scolastico`AS A, \n"
				+ "`scuole`AS B, \n"
				+ "`anni_scolastici`AS C \n"
				+ "WHERE A.`id_scuola` = B.`id_scuola`\n"
				+ "AND B.`id_scuola` = C.`id_scuola`\n"
				+ "AND A.`id_anno_scolastico` = C.`id_anno_scolastico`\n"
				+ "AND A.`id_scuola` = "
				+ id_scuola
				+ "\n"
				+ "AND A.`id_anno_scolastico` = "
				+ id_anno_scolastico
				+ " ORDER BY C.`anno_scolastico` DESC, A.`start_date`";

		try {
			jArrayPeriodiAnnoScolastico = new MySqlAndroid().retrieveTableData(
					getApplicationContext(),
					"http://" + ip + "/" + retrieveTableData + "?sql="
							+ URLEncoder.encode(query, "UTF-8"), null);
			if (jArrayPeriodiAnnoScolastico == null)
				return false;

			for (int i = 0; i < jArrayPeriodiAnnoScolastico.length(); i++) {
				JSONObject json_data;
				try {
					json_data = jArrayPeriodiAnnoScolastico.getJSONObject(i);

					periodiAnnoScolasticoArray.add(json_data
							.getString("periodo"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
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
	public void saveTableStringIntoPreferences(String field_value,
			String field_name) {
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

}
