package it.keyorchestra.registroandroid.admin.options;

import it.keyorchestra.registroandroid.admin.options.dbMatthed.DatabaseOps;
import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CrudManagerInterface;
import it.keyorchestra.registroandroid.admin.options.interfaces.MyDateTimePickersInterface;
import it.keyorchestra.registroandroid.admin.options.mysqlandroid.MySqlAndroid;
import it.keyorchestra.registroandroid.admin.options.util.AnniScolasticiArrayAdapter;
import it.keyorchestra.registroandroid.admin.options.util.FieldsValidator;

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
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AsCreatorActivity extends Activity implements
		ActivitiesCommonFunctions, CrudManagerInterface, OnFocusChangeListener,
		MyDateTimePickersInterface {

	static final int DATE_DIALOG_ID = 0;
	static final int CALENDAR_VIEW_ID = 1;
	static final int TIME_DIALOG_ID = 2;
	static final int START_DATE_DIALOG_ID = 3;
	static final int END_DATE_DIALOG_ID = 4;

	EditText etStartDate, etEndDate, etScuola, etAnnoScolastico, etIdAs;
	Button bChangeStartDate, bChangeEndDate;

	// CRUD
	Button bCrudSelect, bCrudDelete, bCrudUpdate, bCrudCreate, bCrudCommit,
			bCrudRollback, bCrudClear;
	private Bundle beforeChangeBasket;
	private Bundle afterChangeBasket;
	TextView tvCrudMessage, tvAsCount;

	private int mYear;
	private int mMonth;
	private int mDay;

	private int mHour;
	private int mMinute;

	private SharedPreferences getPrefs;
	private DatabaseOps databaseOps;
	private ArrayList<String> anniScolasticiArray;
	private JSONArray jArrayAnniScolastici;
	Spinner spinnerAS;
	long id_anno_scolastico;
	long id_scuola;

	/**
	 * @return the id_scuola
	 */
	public long getId_scuola() {
		id_scuola = getPrefs.getLong("id_scuola", -1);
		return id_scuola;
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
		setContentView(R.layout.as_creator_activity);

		databaseOps = new DatabaseOps(getBaseContext());

		getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		getId_scuola();

		tvAsCount = (TextView) findViewById(R.id.tvRecordsCount);

		bChangeStartDate = (Button) findViewById(R.id.bChangeStartDate);
		bChangeStartDate.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Carica la data

				showDialog(START_DATE_DIALOG_ID);
			}

		});

		bChangeEndDate = (Button) findViewById(R.id.bChangeEndDate);
		bChangeEndDate.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Carica la data

				showDialog(END_DATE_DIALOG_ID);
			}

		});

		etIdAs = (EditText) findViewById(R.id.etIdAs);
		etIdAs.setOnFocusChangeListener(this);

		etAnnoScolastico = (EditText) findViewById(R.id.etAnnoScolastico);
		etAnnoScolastico.setOnFocusChangeListener(this);

		etScuola = (EditText) findViewById(R.id.etScuola);
		etScuola.setOnFocusChangeListener(this);

		etStartDate = (EditText) findViewById(R.id.etStartDate);
		etStartDate.setOnFocusChangeListener(this);

		etEndDate = (EditText) findViewById(R.id.etEndDate);
		etEndDate.setOnFocusChangeListener(this);

		// CRUD
		bCrudClear = (Button) findViewById(R.id.bCrudClear);
		bCrudClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Clear();
			}
		});

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
					DeleteRow(jArrayAnniScolastici.getJSONObject(spinnerAS
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
					UpdateRow(jArrayAnniScolastici.getJSONObject(spinnerAS
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

		tvCrudMessage = (TextView) findViewById(R.id.tvCrudMessage);
		tvAsCount = (TextView) findViewById(R.id.tvRecordsCount);

		// get the current date and time
		now();

		// display the current date
		displayStartDate();
		// displayCalendarViewDate();
		// display the current time
		// displayTime();

		spinnerAS = (Spinner) findViewById(R.id.spinnerRecords);
		spinnerAS.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				id_anno_scolastico = (Long) view.getTag();
				// Salva id_anno_scolastico nelle preferenze
				saveTableIdIntoPreferences("id_anno_scolastico",
						id_anno_scolastico);

				try {
					JSONObject jsonObject = jArrayAnniScolastici
							.getJSONObject(position);

					saveTableStringIntoPreferences("start_date",
							jsonObject.getString("start_date"));
					saveTableStringIntoPreferences("end_date",
							jsonObject.getString("end_date"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Toast.makeText(getApplicationContext(),
						"id_anno_scolastico:" + id_anno_scolastico,
						Toast.LENGTH_SHORT).show();
				fillFieldsWithData(position);
				setNextTabVisiblity(View.VISIBLE, 2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		Thread timer = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					new LoadAnniScolasticiTask().execute();
				}
			}

		};
		timer.start();
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

	private class LoadAnniScolasticiTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return CaricaArrayAnniScolastici();
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
				AnniScolasticiArrayAdapter asAdapter = new AnniScolasticiArrayAdapter(
						getApplicationContext(), jArrayAnniScolastici,
						anniScolasticiArray);

				asAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// Apply the adapter to the spinner
				spinnerAS.setAdapter(asAdapter);

				Toast.makeText(getApplicationContext(),
						"Anni Scolastici caricati!", Toast.LENGTH_LONG).show();
				tvAsCount.setText("(" + spinnerAS.getCount() + ")");
				if (spinnerAS.getCount() == 0) {
					inizializzaNuovoRecord();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Impossibile caricare gli anni scolastici!",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	private class GetScuolaDescriptionTask extends
			AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			etScuola.setTag(id_scuola);
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
			etScuola.setText(result);
		}

	}

	public Boolean CaricaArrayAnniScolastici() {
		// TODO Auto-generated method stub
		anniScolasticiArray = new ArrayList<String>();
		String retrieveTableData = getPrefs
				.getString("retrieveTableData", null);

		String ip = getDatabaseIpFromPreferences();

		// jsonRuoliAmmessi(retrieveTableData, ip);

		String query = "SELECT A.`id_anno_scolastico`, A.`id_scuola`, A.`anno_scolastico`, "
				+ "A.`start_date`, A.`end_date`, B.nome_scuola, B.tipo_scuola_acronimo "
				+ "FROM `anni_scolastici` AS A, scuole as B WHERE A.`id_scuola`="
				+ id_scuola
				+ " AND A.id_scuola = B.id_scuola"
				+ " ORDER BY A.`anno_scolastico` DESC";

		try {
			jArrayAnniScolastici = new MySqlAndroid().retrieveTableData(
					getApplicationContext(),
					"http://" + ip + "/" + retrieveTableData + "?sql="
							+ URLEncoder.encode(query, "UTF-8"), null);
			if (jArrayAnniScolastici == null)
				return false;

			for (int i = 0; i < jArrayAnniScolastici.length(); i++) {
				JSONObject json_data;
				try {
					json_data = jArrayAnniScolastici.getJSONObject(i);

					anniScolasticiArray.add(json_data
							.getString("anno_scolastico"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setAllTabsVisibilityFrom(View.GONE, 2);
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
	protected Dialog onCreateDialog(int id) {
		String[] split;
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		case START_DATE_DIALOG_ID:
			String startDate = etStartDate.getText().toString();
			split = startDate.split("-");
			return new DatePickerDialog(this, mStartDateSetListener,
					Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1,
					Integer.valueOf(split[2]));
		case END_DATE_DIALOG_ID:
			String endDate = etEndDate.getText().toString();
			split = endDate.split("-");
			return new DatePickerDialog(this, mEndDateSetListener,
					Integer.valueOf(split[0]), Integer.valueOf(split[1]) - 1,
					Integer.valueOf(split[2]));
		}
		return null;
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			displayStartDate();
		}
	};

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			displayStartDate();
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

	// the callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			displayTime();
		}
	};

	// updates the date in the EditText
	@Override
	public void displayStartDate() {
		etStartDate.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mYear).append("-").append(pad(mMonth + 1)).append("-")
				.append(pad(mDay)));
	}

	// updates the date in the EditText
	@Override
	public void displayEndDate() {
		etEndDate.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mYear).append("-").append(pad(mMonth + 1)).append("-")
				.append(pad(mDay)));
	}

	// updates the date in the EditText
	@Override
	public void displayCalendarViewDate() {
		etStartDate.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mDay).append("/").append(mMonth + 1).append("/")
				.append(mYear).append(" "));
	}

	// updates the time we display in the EditText
	@Override
	public void displayTime() {
		etStartDate.setText(new StringBuilder().append(pad(mHour)).append(":")
				.append(pad(mMinute)));
	}

	@Override
	public String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus) {
			Toast.makeText(getApplicationContext(),
					"" + ((EditText) v).getHint(), Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case CALENDAR_VIEW_ID:
			if (resultCode == RESULT_OK) {

				Bundle bundle = data.getExtras();

				Toast.makeText(getApplicationContext(),
						"" + bundle.getString("dateSelected"),
						Toast.LENGTH_SHORT).show();
				// mDateDisplay2 = (EditText) findViewById(R.id.dateDisplay2);
				// mDateDisplay2.setText(bundle.getString("dateSelected"));
				break;
			}
		}
	}

	@Override
	public boolean UpdateRow(JSONObject data) {
		// TODO Auto-generated method stub
		beforeChangeBasket = new Bundle();
		try {
			beforeChangeBasket.putLong("id_anno_scolastico",
					data.getLong("id_anno_scolastico"));
			beforeChangeBasket.putLong("id_scuola", data.getLong("id_scuola"));
			beforeChangeBasket.putString("anno_scolastico",
					data.getString("anno_scolastico"));
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

		afterChangeBasket.putLong("id_anno_scolastico",
				beforeChangeBasket.getLong("id_anno_scolastico"));
		afterChangeBasket.putLong("id_scuola",
				beforeChangeBasket.getLong("id_scuola"));

		afterChangeBasket.putString("anno_scolastico", etAnnoScolastico
				.getText().toString());
		afterChangeBasket.putString("start_date", etStartDate.getText()
				.toString());
		afterChangeBasket.putString("end_date", etEndDate.getText().toString());
		afterChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.UPDATE);
		return true;
	}

	@Override
	public boolean DeleteRow(JSONObject data) {
		// TODO Auto-generated method stub
		beforeChangeBasket = new Bundle();
		try {
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
		beforeChangeBasket.putString("anno_scolastico", etAnnoScolastico
				.getText().toString());
		beforeChangeBasket.putLong("id_scuola", (Long) etScuola.getTag());
		beforeChangeBasket.putString("start_date", etStartDate.getText()
				.toString());
		beforeChangeBasket
				.putString("end_date", etEndDate.getText().toString());

		return true;
	}

	@Override
	public boolean Select() {
		// TODO Auto-generated method stub
		getId_scuola();
		new LoadAnniScolasticiTask().execute();
		return true;
	}

	@Override
	public void Clear() {
		// TODO Auto-generated method stub
		removeAllTextIntoFields();
		etAnnoScolastico.setError(null);
		etStartDate.setError(null);
		etEndDate.setError(null);
	}

	@Override
	public void Commit() {
		// TODO Auto-generated method stub
		switch (beforeChangeBasket.getInt("action")) {
		case CRUD_ACTION.UPDATE:
			if (!FieldsValidator.Is_Valid_AnnoScolastico(etAnnoScolastico)) {
				etAnnoScolastico.requestFocus();
				break;
			}
			if (!FieldsValidator.Is_Valid_StartDate(etStartDate)) {
				etStartDate.setFocusable(true);
				etStartDate.requestFocus();
				break;
			}
			if (!FieldsValidator.Is_Valid_EndDate(etEndDate)) {
				etEndDate.setFocusable(true);
				etEndDate.requestFocus();
				break;
			}
			new UpdateAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		case CRUD_ACTION.DELETE:
			new DeleteAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		case CRUD_ACTION.CREATE:
			if (!FieldsValidator.Is_Valid_AnnoScolastico(etAnnoScolastico)) {
				etAnnoScolastico.requestFocus();
				break;
			}
			if (!FieldsValidator.Is_Valid_StartDate(etStartDate)) {
				etStartDate.setFocusable(true);
				etStartDate.requestFocus();
				break;
			}
			if (!FieldsValidator.Is_Valid_EndDate(etEndDate)) {
				etEndDate.setFocusable(true);
				etEndDate.requestFocus();
				break;
			}
			CreateRow(null);
			new CreateAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		}
	}

	@Override
	public void Rollback() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(),
				"Rollback! cambiamenti scartati!", Toast.LENGTH_SHORT).show();
		etAnnoScolastico.setError(null);
		etStartDate.setFocusable(false);
		etStartDate.setError(null);

		etEndDate.setFocusable(false);
		etEndDate.setError(null);
		new LoadAnniScolasticiTask().execute();
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
	public void removeAllTextIntoFields() {
		// TODO Auto-generated method stub
		etEndDate.setText("");
		etStartDate.setText("");
		etIdAs.setText("");
		etAnnoScolastico.setText("");
		// etScuola.setText("");
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
	public void setNextTabVisiblity(int visibility, int tabIndex) {
		// TODO Auto-generated method stub
		TabHost tabHost = ((ScuolaWizard) getParent()).getTabHost();
		View tab = tabHost.getTabWidget().getChildAt(tabIndex);
		if (tab != null)
			tab.setVisibility(visibility);
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
		setAllTabsVisibilityFrom(View.GONE,3);

		getId_scuola();
		if (jArrayAnniScolastici == null)
			return;
		// SE LA SCUOLA SELEZIONATA E' CAMBIATA RICARICO FLI ANNI SCOLASTICI
		if (jArrayAnniScolastici.length() > 0) {
			JSONObject jsonObject;
			try {

				jsonObject = jArrayAnniScolastici.getJSONObject(0);
				long oldId = jsonObject.getLong("id_scuola");
				if (id_scuola != oldId) {
					new LoadAnniScolasticiTask().execute();
				} else {
					setNextTabVisiblity(View.VISIBLE, 2);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				new LoadAnniScolasticiTask().execute();
			}
		}
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
		if (bCrudRollback.getVisibility() == Button.VISIBLE) {
			Rollback();
			setCommitRollback(false);
		}
	}

	@Override
	public void fillFieldsWithData(int position) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObiect = jArrayAnniScolastici
					.getJSONObject(position);
			etIdAs.setText(jsonObiect.getString("id_anno_scolastico"));
			etAnnoScolastico.setText(jsonObiect.getString("anno_scolastico"));

			etScuola.setText("[" + jsonObiect.getLong("id_scuola") + "] "
					+ jsonObiect.getString("tipo_scuola_acronimo") + " - "
					+ jsonObiect.getString("nome_scuola"));
			etScuola.setTag(jsonObiect.getLong("id_scuola"));

			etStartDate.setText(jsonObiect.getString("start_date"));
			etEndDate.setText(jsonObiect.getString("end_date"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class UpdateAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return databaseOps.updateAnnoScolastico(getApplicationContext(),
					beforeChangeBasket, afterChangeBasket);
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
				new LoadAnniScolasticiTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private class DeleteAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return databaseOps.deleteAnnoScolastico(getApplicationContext(),
					beforeChangeBasket);
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
				new LoadAnniScolasticiTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private class CreateAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return databaseOps.createAnnoScolastico(getApplicationContext(),
					beforeChangeBasket);
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
				new LoadAnniScolasticiTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
			}
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
		now();
		// display the current date
		displayStartDate();
		displayEndDate();

		etAnnoScolastico.setText(mYear + "/" + (mYear + 1));

		new GetScuolaDescriptionTask().execute();
	}

	@Override
	public void saveTableStringIntoPreferences(String key, String value) {
		// TODO Auto-generated method stub
		Editor editor = getPrefs.edit();
		editor.putString(key, value);
		editor.apply();
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
