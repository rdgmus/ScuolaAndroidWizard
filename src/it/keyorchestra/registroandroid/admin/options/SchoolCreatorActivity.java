package it.keyorchestra.registroandroid.admin.options;

import it.keyorchestra.registroandroid.admin.options.dbMatthed.DatabaseOps;
import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CrudManagerInterface;
import it.keyorchestra.registroandroid.admin.options.mysqlandroid.MySqlAndroid;
import it.keyorchestra.registroandroid.admin.options.util.FieldsValidator;
import it.keyorchestra.registroandroid.admin.options.util.ScuoleArrayAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SchoolCreatorActivity extends Activity implements
		ActivitiesCommonFunctions, CrudManagerInterface, OnFocusChangeListener {

	private SharedPreferences getPrefs;
	private DatabaseOps databaseOps;
	Spinner spinnerScuole;
	private ArrayList<String> scuoleArray;
	private JSONArray jArrayScuole;
	Button bCrudSelect, bCrudDelete, bCrudUpdate, bCrudCreate, bCrudCommit,
			bCrudRollback, bCrudClear;
	private Bundle beforeChangeBasket;
	private Bundle afterChangeBasket;
	TextView tvCrudMessage, tvScuoleCount;
	protected Long id_scuola;
	EditText etIdScuola, etTipoScuola, etNomeScuola, etIndirizzo, etCap,
			etCitta, etProvincia, etTelefono, etFax, etEmail, etWeb;
	private EditText theEditTextWhichHasFocus;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.school_creator_activity);

		databaseOps = new DatabaseOps(getBaseContext());
		getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		etIdScuola = (EditText) findViewById(R.id.etIdScuola);

		etTipoScuola = (EditText) findViewById(R.id.etTipoScuola);
		etTipoScuola.setOnFocusChangeListener(this);
		// etTipoScuola.addTextChangedListener(this);

		etNomeScuola = (EditText) findViewById(R.id.etNomeScuola);
		etNomeScuola.setOnFocusChangeListener(this);
		// etNomeScuola.addTextChangedListener(this);

		etIndirizzo = (EditText) findViewById(R.id.etIndirizzo);
		etIndirizzo.setOnFocusChangeListener(this);
		// etIndirizzo.addTextChangedListener(this);

		etCap = (EditText) findViewById(R.id.etCap);
		etCap.setOnFocusChangeListener(this);
		// etCap.addTextChangedListener(this);

		etCitta = (EditText) findViewById(R.id.etCitta);
		etCitta.setOnFocusChangeListener(this);
		// etCitta.addTextChangedListener(this);

		etProvincia = (EditText) findViewById(R.id.etProvincia);
		etProvincia.setOnFocusChangeListener(this);
		// etProvincia.addTextChangedListener(this);

		etTelefono = (EditText) findViewById(R.id.etTelefono);
		etTelefono.setOnFocusChangeListener(this);
		// etTelefono.addTextChangedListener(this);

		etFax = (EditText) findViewById(R.id.etFax);
		etFax.setOnFocusChangeListener(this);
		// etFax.addTextChangedListener(this);

		etEmail = (EditText) findViewById(R.id.etEmail);
		etEmail.setOnFocusChangeListener(this);
		// etEmail.addTextChangedListener(this);

		etWeb = (EditText) findViewById(R.id.etWeb);
		etWeb.setOnFocusChangeListener(this);
		// etWeb.addTextChangedListener(this);

		spinnerScuole = (Spinner) findViewById(R.id.spinnerScuole);

		spinnerScuole.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				id_scuola = (Long) view.getTag();
				//Salva id_scuola nelle preferenze
				Editor editor = getPrefs.edit();
				editor.putLong("id_scuola", id_scuola);
				editor.apply();
				// Toast.makeText(getApplicationContext(),
				// "id_scuola:" + id_scuola, Toast.LENGTH_SHORT).show();
				fillFieldsWithData(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		bCrudClear = (Button)findViewById(R.id.bCrudClear);
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
					DeleteRow(jArrayScuole.getJSONObject(spinnerScuole
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
					UpdateRow(jArrayScuole.getJSONObject(spinnerScuole
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
//				setCommitRollback(false);
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
		tvScuoleCount = (TextView) findViewById(R.id.tvScuoleCount);

		
		Thread timer = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					new LoadScuoleTask().execute();
				}
			}

		};
		timer.start();
	}

	@Override
	public void setCommitRollback(boolean visible) {
		// TODO Auto-generated method stub
		if (visible) {
			bCrudCommit.setVisibility(Button.VISIBLE);
			bCrudRollback.setVisibility(Button.VISIBLE);
			tvCrudMessage.setVisibility(TextView.VISIBLE);
			switch(beforeChangeBasket.getInt("action")){
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
			JSONObject jsonObiect = jArrayScuole.getJSONObject(position);
			etIdScuola.setText(jsonObiect.getString("id_scuola"));
			etTipoScuola.setText(jsonObiect.getString("tipo_scuola_acronimo"));
			etNomeScuola.setText(jsonObiect.getString("nome_scuola"));
			etIndirizzo.setText(jsonObiect.getString("indirizzo"));
			etCap.setText(jsonObiect.getString("cap"));
			etCitta.setText(jsonObiect.getString("citta"));
			etProvincia.setText(jsonObiect.getString("provincia"));
			etTelefono.setText(jsonObiect.getString("telefono"));
			etFax.setText(jsonObiect.getString("fax"));
			etEmail.setText(jsonObiect.getString("email"));
			etWeb.setText(jsonObiect.getString("web"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private boolean CaricaArrayScuole() {
		// TODO Auto-generated method stub
		scuoleArray = new ArrayList<String>();
		String retrieveTableData = getPrefs
				.getString("retrieveTableData", null);

		String ip = getDatabaseIpFromPreferences();

		// jsonRuoliAmmessi(retrieveTableData, ip);

		String query = "SELECT * FROM `scuole` "
				+ "ORDER BY `tipo_scuola_acronimo`,`nome_scuola` ASC";

		try {
			jArrayScuole = new MySqlAndroid().retrieveTableData(
					getApplicationContext(),
					"http://" + ip + "/" + retrieveTableData + "?sql="
							+ URLEncoder.encode(query, "UTF-8"), null);
			if (jArrayScuole == null)
				return false;

			for (int i = 0; i < jArrayScuole.length(); i++) {
				JSONObject json_data;
				try {
					json_data = jArrayScuole.getJSONObject(i);

					scuoleArray.add(formatIndirizzoScuola(json_data));
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

	private String formatIndirizzoScuola(JSONObject s) {
		// TODO Auto-generated method stub
		String indirizzo = "";
		try {
			indirizzo = s.getString("indirizzo") + " - " + s.getString("cap")
					+ " - " + s.getString("citta") + " ("
					+ s.getString("provincia") + ")";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return indirizzo;
	}

	private class LoadScuoleTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return CaricaArrayScuole();
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
				ScuoleArrayAdapter scuoleAdapter = new ScuoleArrayAdapter(
						getApplicationContext(), jArrayScuole, scuoleArray);

				scuoleAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// Apply the adapter to the spinner
				spinnerScuole.setAdapter(scuoleAdapter);

				Toast.makeText(getApplicationContext(), "Scuole caricate!",
						Toast.LENGTH_LONG).show();
				tvScuoleCount.setText("("+spinnerScuole.getCount()+")");
			} else {
				Toast.makeText(getApplicationContext(),
						"Impossibile caricare le scuole!", Toast.LENGTH_LONG)
						.show();
			}
		}

	}

	private class UpdateScuolaTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return databaseOps.updateScuola(getApplicationContext(),
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
				new LoadScuoleTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private class DeleteScuolaTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return databaseOps.deleteScuola(getApplicationContext(),
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
				new LoadScuoleTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private class CreateScuolaTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			return databaseOps.createScuola(getApplicationContext(),
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
				new LoadScuoleTask().execute();
			} else {
				Toast.makeText(getApplicationContext(), "Commit fallito!",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	@SuppressLint("NewApi")
	@Override
	public boolean UpdateRow(JSONObject data) {
		// TODO Auto-generated method stub
		// options.putParcelable("data", (Parcelable) data);
		beforeChangeBasket = new Bundle();
		try {
			beforeChangeBasket.putLong("id_scuola", data.getLong("id_scuola"));
			beforeChangeBasket.putString("tipo_scuola_acronimo",
					data.getString("tipo_scuola_acronimo"));
			beforeChangeBasket.putString("nome_scuola",
					data.getString("nome_scuola"));
			beforeChangeBasket.putString("indirizzo",
					data.getString("indirizzo"));
			beforeChangeBasket.putString("cap", data.getString("cap"));
			beforeChangeBasket.putString("citta", data.getString("citta"));
			beforeChangeBasket.putString("provincia",
					data.getString("provincia"));
			beforeChangeBasket
					.putString("telefono", data.getString("telefono"));
			beforeChangeBasket.putString("fax", data.getString("fax"));
			beforeChangeBasket.putString("email", data.getString("email"));
			beforeChangeBasket.putString("web", data.getString("web"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		beforeChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.UPDATE);

		afterChangeBasket = new Bundle();

		afterChangeBasket.putLong("id_scuola",
				beforeChangeBasket.getLong("id_scuola"));
		afterChangeBasket.putString("tipo_scuola_acronimo", etTipoScuola
				.getText().toString());
		afterChangeBasket.putString("nome_scuola", etNomeScuola.getText()
				.toString());
		afterChangeBasket.putString("indirizzo", etIndirizzo.getText()
				.toString());
		afterChangeBasket.putString("cap", etCap.getText().toString());
		afterChangeBasket.putString("citta", etCitta.getText().toString());
		afterChangeBasket.putString("provincia", etProvincia.getText()
				.toString());
		afterChangeBasket
				.putString("telefono", etTelefono.getText().toString());
		afterChangeBasket.putString("fax", etFax.getText().toString());
		afterChangeBasket.putString("email", etEmail.getText().toString());
		afterChangeBasket.putString("web", etWeb.getText().toString());
		afterChangeBasket.putInt("action",
				CrudManagerInterface.CRUD_ACTION.UPDATE);
		return true;
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
		super.onActivityResult(requestCode, resultCode, data);
		afterChangeBasket = data.getExtras();

		if (resultCode == RESULT_OK) {
			String s = afterChangeBasket.getString("answer");
			Toast.makeText(getApplicationContext(), "" + s, Toast.LENGTH_LONG)
					.show();
		} else if (resultCode == RESULT_CANCELED) {
			switch (afterChangeBasket.getInt("action")) {
			case CRUD_ACTION.UPDATE:
				// Toast.makeText(getApplicationContext(),
				// "Devo fare un UPDATE ora!", Toast.LENGTH_LONG).show();
				// FACCIO COMPARIRE I BOTTONI
				setCommitRollback(true);
				break;
			}
		}
	}

	@Override
	public boolean DeleteRow(JSONObject data) {
		// TODO Auto-generated method stub
		beforeChangeBasket = new Bundle();
		try {
			beforeChangeBasket.putLong("id_scuola", data.getLong("id_scuola"));
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

		beforeChangeBasket.putString("tipo_scuola_acronimo", etTipoScuola
				.getText().toString());
		beforeChangeBasket.putString("nome_scuola", etNomeScuola.getText()
				.toString());
		beforeChangeBasket.putString("indirizzo", etIndirizzo.getText()
				.toString());
		beforeChangeBasket.putString("cap", etCap.getText().toString());
		beforeChangeBasket.putString("citta", etCitta.getText().toString());
		beforeChangeBasket.putString("provincia", etProvincia.getText()
				.toString());
		beforeChangeBasket
				.putString("telefono", etTelefono.getText().toString());
		beforeChangeBasket.putString("fax", etFax.getText().toString());
		beforeChangeBasket.putString("email", etEmail.getText().toString());
		beforeChangeBasket.putString("web", etWeb.getText().toString());

		return true;
	}

	@Override
	public boolean Select() {
		// TODO Auto-generated method stub
		new LoadScuoleTask().execute();
		return true;
	}

	@Override
	public void Clear() {
		// TODO Auto-generated method stub
		removeAllTextIntoFields();
		etTipoScuola.setError(null);
		etNomeScuola.setError(null);
	}

	@Override
	public void removeAllTextIntoFields() {
		// TODO Auto-generated method stub
		etIdScuola.setText("");
		etTipoScuola.setText("");
		etNomeScuola.setText("");
		etIndirizzo.setText("");
		etCap.setText("");
		etCitta.setText("");
		etProvincia.setText("");
		etTelefono.setText("");
		etFax.setText("");
		etEmail.setText("");
		etWeb.setText("");
	}

	@Override
	public void Commit() {
		// TODO Auto-generated method stub
		switch (beforeChangeBasket.getInt("action")) {
		case CRUD_ACTION.UPDATE:
			if(!FieldsValidator.Is_Valid_Name(etTipoScuola)){
				etTipoScuola.requestFocus();
				break;
			}
			if(!FieldsValidator.Is_Valid_Name(etNomeScuola)){
				etNomeScuola.requestFocus();
				break;
			}
			new UpdateScuolaTask().execute();
			setCommitRollback(false);
			break;
		case CRUD_ACTION.DELETE:
			new DeleteScuolaTask().execute();
			setCommitRollback(false);
			break;
		case CRUD_ACTION.CREATE:
			if(!FieldsValidator.Is_Valid_Name(etTipoScuola)){
				etTipoScuola.requestFocus();
				break;
			}
			if(!FieldsValidator.Is_Valid_Name(etNomeScuola)){
				etNomeScuola.requestFocus();
				break;
			}
			CreateRow(null);
			new CreateScuolaTask().execute();
			setCommitRollback(false);
			break;
		}
	}

	/**
	 * Ricava la posizione dell'utente indicato nel relativo spinner.
	 * 
	 * @param spinner
	 * @param id
	 * @return
	 */
	protected int getScuolaIdPositionIntoSpinner(Spinner spinner, long id) {
		// TODO Auto-generated method stub
		int count = spinner.getCount();
		for (int i = 0; i < count; i++) {
			View rowView = spinner.getAdapter().getView(i, null, spinner);
			if ((Long) rowView.getTag() == id) {
				return i;
			}

		}
		return 0;
	}

	@Override
	public void Rollback() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(),
				"Rollback! cambiamenti scartati!", Toast.LENGTH_SHORT).show();
		etTipoScuola.setError(null);
		etNomeScuola.setError(null);
		new LoadScuoleTask().execute();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus) {
			Toast.makeText(getApplicationContext(),
					"" + ((EditText) v).getHint(), Toast.LENGTH_SHORT).show();
			setTheEditTextWhichHasFocus((EditText) v);
			// this.theEditTextWhichHasFocus.setBackgroundColor(R.color.colorOrange);
		}
	}

	public EditText getTheEditTextWhichHasFocus() {
		return theEditTextWhichHasFocus;
	}

	public void setTheEditTextWhichHasFocus(EditText theEditTextWhichHasFocus) {
		this.theEditTextWhichHasFocus = theEditTextWhichHasFocus;
	}

}
