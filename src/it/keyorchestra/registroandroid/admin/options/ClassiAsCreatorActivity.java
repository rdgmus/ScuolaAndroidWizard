package it.keyorchestra.registroandroid.admin.options;

import it.keyorchestra.registroandroid.admin.options.dbMatthed.DatabaseOps;
import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CrudManagerInterface;
import it.keyorchestra.registroandroid.admin.options.mysqlandroid.MySqlAndroid;
import it.keyorchestra.registroandroid.admin.options.util.ClassiArrayAdapter;
import it.keyorchestra.registroandroid.admin.options.util.FieldsValidator;
import it.keyorchestra.registroandroid.admin.options.util.NomiClasseArrayAdapter;
import it.keyorchestra.registroandroid.admin.options.util.SpecializzazioniArrayAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class ClassiAsCreatorActivity extends Activity implements
		ActivitiesCommonFunctions, CrudManagerInterface, OnFocusChangeListener {
	// CRUD
	Button bCrudSelect, bCrudDelete, bCrudUpdate, bCrudCreate, bCrudCommit,
			bCrudRollback, bCrudClear;
	private Bundle beforeChangeBasket;
	private Bundle afterChangeBasket;
	TextView tvCrudMessage, tvRecordsCount;

	private SharedPreferences getPrefs;
	private DatabaseOps databaseOps;

	private ArrayList<String> classiAnnoScolasticoArray, nomiClasseArray,
			specializzazioniArray;
	private JSONArray jArrayClassiAnnoScolastico, jArrayNomiClasse,
			jArraySpecializzazioni;

	EditText etIdClasse, etIdScuolaClasse, etIdAsClasse, etNomeClasse,
			etSpecializzazione;

	Spinner spinnerNomiClasse, spinnerSpecializzazioni, spinnerRecords;

	long id_anno_scolastico;
	long id_scuola;
	long id_classe;

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

			etIdScuolaClasse.setTag(id_scuola);
			etIdAsClasse.setTag(id_anno_scolastico);
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
			etIdScuolaClasse.setText(result);
		}

	}

	private class GetAnnoScolasticoDescriptionTask extends
			AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			etIdScuolaClasse.setTag(id_scuola);
			etIdAsClasse.setTag(id_anno_scolastico);
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
			etIdAsClasse.setText(result);
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
		setContentView(R.layout.classi_creator_activity);

		databaseOps = new DatabaseOps(getBaseContext());

		getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		getId_scuola();
		getId_anno_scolastico();

		spinnerNomiClasse = (Spinner) findViewById(R.id.spinnerNomiClasse);
		spinnerNomiClasse
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						// if (bCrudRollback.getVisibility() == Button.VISIBLE)
						// {
						etNomeClasse.setText((CharSequence) spinnerNomiClasse
								.getItemAtPosition(position));
						// }
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});
		spinnerSpecializzazioni = (Spinner) findViewById(R.id.spinnerSpecializzazioni);
		spinnerSpecializzazioni
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						// if (bCrudRollback.getVisibility() == Button.VISIBLE)
						// {
						etSpecializzazione
								.setText((CharSequence) spinnerSpecializzazioni
										.getItemAtPosition(position));
						// }
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
				id_classe = (Long) view.getTag();
				// Salva id_anno_scolastico nelle preferenze
				saveTableIdIntoPreferences("id_classe", id_classe);
				//
				// try {
				// JSONObject jsonObject = jArrayClassiAnnoScolastico
				// .getJSONObject(position);
				//
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				// Toast.makeText(getApplicationContext(),
				// "id_classe:" + id_classe, Toast.LENGTH_SHORT).show();
				fillFieldsWithData(position);
				setNextTabVisiblity(View.VISIBLE, 5);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		etIdScuolaClasse = (EditText) findViewById(R.id.etIdScuolaClasse);
		etIdAsClasse = (EditText) findViewById(R.id.etIdAsClasse);

		etIdClasse = (EditText) findViewById(R.id.etIdClasse);

		etNomeClasse = (EditText) findViewById(R.id.etNomeClasse);
		etNomeClasse.setOnFocusChangeListener(this);

		etSpecializzazione = (EditText) findViewById(R.id.etSpecializzazione);
		etSpecializzazione.setOnFocusChangeListener(this);

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
				beforeChangeBasket = new Bundle();
				beforeChangeBasket.putInt("action",
						CrudManagerInterface.CRUD_ACTION.CREATE);

				new TestNomeClasseExistsForAsTask().execute();
			}
		});

		bCrudDelete = (Button) findViewById(R.id.bCrudDelete);
		bCrudDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					int position = spinnerRecords.getSelectedItemPosition();
					DeleteRow(jArrayClassiAnnoScolastico
							.getJSONObject(position));
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
				// new TestNomeClasseExistsForAsTask().execute();
				etNomeClasse.setError(null);
				int position = spinnerRecords.getSelectedItemPosition();
				try {
					UpdateRow(jArrayClassiAnnoScolastico
							.getJSONObject(position));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setCommitRollback(true);
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
		tvRecordsCount = (TextView) findViewById(R.id.tvRecordsCount);

		new LoadAllDistinctNomeClasseTask().execute();
		new LoadAllDistinctSpecializzazioniTask().execute();

		Thread timer = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					new LoadClassiAnnoScolasticoTask().execute();
				}
			}

		};
		timer.start();

	}

	private class LoadClassiAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return CaricaClassiAnnoScolastico();
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
				ClassiArrayAdapter classiAsAdapter = new ClassiArrayAdapter(
						getApplicationContext(), jArrayClassiAnnoScolastico,
						classiAnnoScolasticoArray);

				classiAsAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// Apply the adapter to the spinner
				spinnerRecords.setAdapter(classiAsAdapter);

				Toast.makeText(getApplicationContext(),
						"Classi dell'Anno Scolastico caricate!",
						Toast.LENGTH_LONG).show();

				tvRecordsCount.setText("(" + spinnerRecords.getCount() + ")");
				if (spinnerRecords.getCount() == 0) {
					removeAllTextIntoFields();
					inizializzaNuovoRecord();
					new GetScuolaDescriptionTask().execute();
					new GetAnnoScolasticoDescriptionTask().execute();

				} else {
					new GetScuolaDescriptionTask().execute();
					new GetAnnoScolasticoDescriptionTask().execute();

					setNextTabVisiblity(View.VISIBLE, 5);

				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Impossibile caricare le CLassi dell'Anno Scolastico!",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	private class TestNomeClasseExistsForAsTask extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Bisogna controllare che il nome classe non sia già stato
			// utilizzato
			return databaseOps.existsNomeClasseIntoAS(getApplicationContext(),
					etNomeClasse.getText().toString(), id_anno_scolastico);
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
			// Bisogna controllare che il nome classe non sia già stato
			// utilizzato
			if (result) {
				etNomeClasse
						.setError(Html
								.fromHtml("<font color='red'>Una classe con lo stesso nome già esiste!</font>"));

				etNomeClasse.setFocusable(true);
				etNomeClasse.requestFocus();
			} else {
				etNomeClasse.setError(null);
				if (beforeChangeBasket.getInt("action") != CrudManagerInterface.CRUD_ACTION.CREATE) {
					int position = spinnerRecords.getSelectedItemPosition();
					try {
						UpdateRow(jArrayClassiAnnoScolastico
								.getJSONObject(position));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				setCommitRollback(true);
			}
		}
	}

	private class LoadAllDistinctNomeClasseTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return CaricaAllDistinctNomeClasse();
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
				NomiClasseArrayAdapter classiAsAdapter = new NomiClasseArrayAdapter(
						getApplicationContext(), jArrayNomiClasse,
						nomiClasseArray);

				classiAsAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// Apply the adapter to the spinner
				spinnerNomiClasse.setAdapter(classiAsAdapter);
				syncronizeSpinner(spinnerNomiClasse, etNomeClasse.getText()
						.toString());
			}
		}

	}

	private class LoadAllDistinctSpecializzazioniTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return CaricaAllDistinctSpecializzazioni();
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
				SpecializzazioniArrayAdapter classiAsAdapter = new SpecializzazioniArrayAdapter(
						getApplicationContext(), jArraySpecializzazioni,
						specializzazioniArray);

				classiAsAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// Apply the adapter to the spinner
				spinnerSpecializzazioni.setAdapter(classiAsAdapter);
				syncronizeSpinner(spinnerSpecializzazioni, etSpecializzazione
						.getText().toString());

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
		setAllTabsVisibilityFrom(View.GONE, 5);

		getId_scuola();
		getId_anno_scolastico();
		if (jArrayClassiAnnoScolastico == null)
			return;
		// SE LA SCUOLA SELEZIONATA E' CAMBIATA RICARICO FLI ANNI SCOLASTICI
		if (jArrayClassiAnnoScolastico.length() > 0) {
			JSONObject jsonObject;
			try {

				jsonObject = jArrayClassiAnnoScolastico.getJSONObject(0);
				long oldId = jsonObject.getLong("id_anno_scolastico");
				if (id_anno_scolastico != oldId) {
					new LoadClassiAnnoScolasticoTask().execute();
				} else {
					setNextTabVisiblity(View.VISIBLE, 5);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				new LoadClassiAnnoScolasticoTask().execute();
			}
		}
	}

	public Boolean CaricaAllDistinctNomeClasse() {
		// TODO Auto-generated method stub
		nomiClasseArray = new ArrayList<String>();
		String retrieveTableData = getPrefs
				.getString("retrieveTableData", null);

		String ip = getDatabaseIpFromPreferences();

		String query = "SELECT DISTINCT `nome_classe` FROM `classi` WHERE 1\n"
				+ "ORDER BY `nome_classe` ASC";

		try {
			jArrayNomiClasse = new MySqlAndroid().retrieveTableData(
					getApplicationContext(),
					"http://" + ip + "/" + retrieveTableData + "?sql="
							+ URLEncoder.encode(query, "UTF-8"), null);
			if (jArrayNomiClasse == null)
				return false;

			for (int i = 0; i < jArrayNomiClasse.length(); i++) {
				JSONObject json_data;
				try {
					json_data = jArrayNomiClasse.getJSONObject(i);

					nomiClasseArray.add(json_data.getString("nome_classe"));
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

	public Boolean CaricaAllDistinctSpecializzazioni() {
		// TODO Auto-generated method stub
		specializzazioniArray = new ArrayList<String>();
		String retrieveTableData = getPrefs
				.getString("retrieveTableData", null);

		String ip = getDatabaseIpFromPreferences();

		String query = "SELECT DISTINCT `specializzazione` FROM `classi` "
				+ "WHERE 1 ORDER BY `specializzazione` ASC";

		try {
			jArraySpecializzazioni = new MySqlAndroid().retrieveTableData(
					getApplicationContext(),
					"http://" + ip + "/" + retrieveTableData + "?sql="
							+ URLEncoder.encode(query, "UTF-8"), null);
			if (jArraySpecializzazioni == null)
				return false;

			for (int i = 0; i < jArraySpecializzazioni.length(); i++) {
				JSONObject json_data;
				try {
					json_data = jArraySpecializzazioni.getJSONObject(i);

					specializzazioniArray.add(json_data
							.getString("specializzazione"));
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

	public Boolean CaricaClassiAnnoScolastico() {
		// TODO Auto-generated method stub
		classiAnnoScolasticoArray = new ArrayList<String>();
		String retrieveTableData = getPrefs
				.getString("retrieveTableData", null);

		String ip = getDatabaseIpFromPreferences();

		String query = "SELECT `id_classe`, `id_scuola`, `id_anno_scolastico`, "
				+ "`anno_scolastico`, `nome_classe`, `specializzazione` "
				+ " FROM `classi` WHERE `id_scuola`="
				+ id_scuola
				+ " AND `id_anno_scolastico` = "
				+ id_anno_scolastico
				+ " ORDER BY`anno_scolastico` DESC , `nome_classe` ASC";

		try {
			jArrayClassiAnnoScolastico = new MySqlAndroid().retrieveTableData(
					getApplicationContext(),
					"http://" + ip + "/" + retrieveTableData + "?sql="
							+ URLEncoder.encode(query, "UTF-8"), null);
			if (jArrayClassiAnnoScolastico == null)
				return false;

			for (int i = 0; i < jArrayClassiAnnoScolastico.length(); i++) {
				JSONObject json_data;
				try {
					json_data = jArrayClassiAnnoScolastico.getJSONObject(i);

					classiAnnoScolasticoArray.add(json_data
							.getString("nome_classe"));
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
			beforeChangeBasket.putLong("id_classe", data.getLong("id_classe"));
			beforeChangeBasket.putLong("id_anno_scolastico",
					data.getLong("id_anno_scolastico"));
			beforeChangeBasket.putLong("id_scuola", data.getLong("id_scuola"));
			beforeChangeBasket.putString("nome_classe",
					data.getString("nome_classe"));
			beforeChangeBasket.putString("specializzazione",
					data.getString("specializzazione"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		beforeChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.UPDATE);

		afterChangeBasket = new Bundle();

		afterChangeBasket.putLong("id_classe",
				beforeChangeBasket.getLong("id_classe"));
		afterChangeBasket.putLong("id_anno_scolastico",
				beforeChangeBasket.getLong("id_anno_scolastico"));
		afterChangeBasket.putLong("id_scuola",
				beforeChangeBasket.getLong("id_scuola"));

		afterChangeBasket.putString("nome_classe", etNomeClasse.getText()
				.toString());
		afterChangeBasket.putString("specializzazione", etSpecializzazione
				.getText().toString());
		afterChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.UPDATE);
		return true;
	}

	@Override
	public boolean DeleteRow(JSONObject data) {
		// TODO Auto-generated method stub
		beforeChangeBasket = new Bundle();
		try {
			beforeChangeBasket.putLong("id_classe", data.getLong("id_classe"));
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
		beforeChangeBasket.putLong("id_scuola",
				(Long) etIdScuolaClasse.getTag());
		beforeChangeBasket.putLong("id_anno_scolastico",
				(Long) etIdAsClasse.getTag());
		beforeChangeBasket.putString("nome_classe", etNomeClasse.getText()
				.toString());
		beforeChangeBasket.putString("specializzazione", etSpecializzazione
				.getText().toString());

		return true;
	}

	@Override
	public boolean Select() {
		// TODO Auto-generated method stub
		getId_scuola();
		getId_anno_scolastico();
		new LoadClassiAnnoScolasticoTask().execute();
		return true;
	}

	@Override
	public void Clear() {
		// TODO Auto-generated method stub
		removeAllTextIntoFields();
		etNomeClasse.setError(null);
		etSpecializzazione.setError(null);
	}

	@Override
	public void Commit() {
		// TODO Auto-generated method stub
		switch (beforeChangeBasket.getInt("action")) {
		case CRUD_ACTION.UPDATE:
			if (!FieldsValidator.Is_Valid_Name(etNomeClasse)) {
				etNomeClasse.setFocusable(true);
				etNomeClasse.requestFocus();
				break;
			}
			if (!FieldsValidator.Is_Valid_Name(etSpecializzazione)) {
				etSpecializzazione.setFocusable(true);
				etSpecializzazione.requestFocus();
				break;
			}
			int position = spinnerRecords.getSelectedItemPosition();
			try {
				UpdateRow(jArrayClassiAnnoScolastico.getJSONObject(position));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new UpdateClasseAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		case CRUD_ACTION.DELETE:
			new DeleteClasseAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		case CRUD_ACTION.CREATE:
			if (!FieldsValidator.Is_Valid_Name(etNomeClasse)) {
				etNomeClasse.setFocusable(true);
				etNomeClasse.requestFocus();
				break;
			}
			if (!FieldsValidator.Is_Valid_Name(etSpecializzazione)) {
				etSpecializzazione.setFocusable(true);
				etSpecializzazione.requestFocus();
				break;
			}
			CreateRow(null);
			new CreateClasseAnnoScolasticoTask().execute();
			setCommitRollback(false);
			break;
		}
	}

	private class DeleteClasseAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return databaseOps.deleteClasseAnnoScolastico(
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
				new LoadClassiAnnoScolasticoTask().execute();
				new LoadAllDistinctNomeClasseTask().execute();
				new LoadAllDistinctSpecializzazioniTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private class UpdateClasseAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return databaseOps.updateClasseAnnoScolastico(
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
				new LoadClassiAnnoScolasticoTask().execute();
				new LoadAllDistinctNomeClasseTask().execute();
				new LoadAllDistinctSpecializzazioniTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
				new LoadClassiAnnoScolasticoTask().execute();
			}
		}

	}

	private class CreateClasseAnnoScolasticoTask extends
			AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return databaseOps.createClasseAnnoScolastico(
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
				new LoadAllDistinctNomeClasseTask().execute();
				new LoadAllDistinctSpecializzazioniTask().execute();
				new LoadClassiAnnoScolasticoTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
				new LoadClassiAnnoScolasticoTask().execute();
			}
		}

	}

	@Override
	public void Rollback() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAllTextIntoFields() {
		// TODO Auto-generated method stub
		etNomeClasse.setText("");
		etSpecializzazione.setText("");
		etIdClasse.setText("");
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
			JSONObject jsonObiect = jArrayClassiAnnoScolastico
					.getJSONObject(position);

			etIdClasse.setText("" + jsonObiect.getLong("id_classe") + "");

			etNomeClasse.setText(jsonObiect.getString("nome_classe"));
			syncronizeSpinner(spinnerNomiClasse,
					jsonObiect.getString("nome_classe"));
			etSpecializzazione
					.setText(jsonObiect.getString("specializzazione"));
			syncronizeSpinner(spinnerSpecializzazioni,
					jsonObiect.getString("specializzazione"));
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
		etNomeClasse
				.setText((CharSequence) spinnerNomiClasse
						.getItemAtPosition(spinnerNomiClasse
								.getSelectedItemPosition()));
		etSpecializzazione.setText((CharSequence) spinnerSpecializzazioni
				.getItemAtPosition(spinnerSpecializzazioni
						.getSelectedItemPosition()));
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
	public void syncronizeSpinner(Spinner spinner, String keyValue) {
		// TODO Auto-generated method stub
		for (int j = 0; j < spinner.getCount(); j++) {
			if (keyValue
					.equalsIgnoreCase((String) spinner.getItemAtPosition(j))) {
				spinner.setSelection(j);
				break;
			}
		}
	}

}
