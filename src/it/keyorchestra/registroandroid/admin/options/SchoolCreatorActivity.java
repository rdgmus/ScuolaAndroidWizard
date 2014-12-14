package it.keyorchestra.registroandroid.admin.options;

import it.keyorchestra.registroandroid.admin.options.dbMatthed.DatabaseOps;
import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CrudManagerInterface;
import it.keyorchestra.registroandroid.admin.options.mysqlandroid.MySqlAndroid;
import it.keyorchestra.registroandroid.admin.options.tables.Scuole;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SchoolCreatorActivity extends Activity implements
		ActivitiesCommonFunctions, CrudManagerInterface {

	private SharedPreferences getPrefs;
	private DatabaseOps databaseOps;
	Spinner spinnerScuole;
	private ArrayList<String> scuoleArray;
	private JSONArray jArrayScuole;
	Button bSelect, bDelete, bUpdate, bCommit, bRollback, bClear;

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

		spinnerScuole = (Spinner) findViewById(R.id.spinnerScuole);

		spinnerScuole.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				long id_scuola = (Long) view.getTag();
				Toast.makeText(getApplicationContext(),
						"id_scuola:" + id_scuola, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		bSelect = (Button) findViewById(R.id.bCrudSelect);
		bSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new LoadScuoleTask().execute();
			}
		});

		bDelete = (Button) findViewById(R.id.bCrudDelete);
		bDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// DeleteRow("scuole", -1);
			}
		});

		bUpdate = (Button) findViewById(R.id.bCrudUpdate);
		bUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					UpdateRow(jArrayScuole.getJSONObject(spinnerScuole
							.getSelectedItemPosition()));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		bCommit = (Button)findViewById(R.id.bCrudCommit);
		bCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bCommit.setVisibility(Button.INVISIBLE);
				bRollback.setVisibility(Button.INVISIBLE);
				Commit();
			}
		});
		bRollback = (Button)findViewById(R.id.bCrudRollback);
		bRollback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bCommit.setVisibility(Button.INVISIBLE);
				bRollback.setVisibility(Button.INVISIBLE);	
				Rollback();
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
					new LoadScuoleTask().execute();
				}
			}

		};
		timer.start();
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
			} else {
				Toast.makeText(getApplicationContext(),
						"Impossibile caricare le scuole!", Toast.LENGTH_LONG)
						.show();
			}
		}

	}

	@SuppressLint("NewApi")
	@Override
	public boolean UpdateRow(JSONObject data) {
		// TODO Auto-generated method stub
		// options.putParcelable("data", (Parcelable) data);
		Bundle basket = new Bundle();
		try {
			basket.putLong("id_scuola", data.getLong("id_scuola"));
			basket.putString("tipo_scuola_acronimo",
					data.getString("tipo_scuola_acronimo"));
			basket.putString("nome_scuola", data.getString("nome_scuola"));
			basket.putString("indirizzo", data.getString("indirizzo"));
			basket.putString("cap", data.getString("cap"));
			basket.putString("citta", data.getString("citta"));
			basket.putString("provincia", data.getString("provincia"));
			basket.putString("telefono", data.getString("telefono"));
			basket.putString("fax", data.getString("fax"));
			basket.putString("email", data.getString("email"));
			basket.putString("web", data.getString("web"));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		basket.putInt("action", CrudManagerInterface.CRUD_ACTION.UPDATE);

		// INTENT
		Intent d = new Intent(this, Scuole.class);
		d.putExtras(basket);
		startActivityForResult(d, 0);
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
		Bundle basket = data.getExtras();

		if (resultCode == RESULT_OK) {
			String s = basket.getString("answer");
			Toast.makeText(getApplicationContext(), "" + s,
					Toast.LENGTH_LONG).show();
		} else if (resultCode == RESULT_CANCELED) {
			switch (basket.getInt("action")) {
			case CRUD_ACTION.UPDATE:
//				Toast.makeText(getApplicationContext(),
//						"Devo fare un UPDATE ora!", Toast.LENGTH_LONG).show();
				//FACCIO COMPARIRE I BOTTONI
				bCommit.setVisibility(Button.VISIBLE);
				bRollback.setVisibility(Button.VISIBLE);
				break;
			}
		}
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
	public boolean Select(String table, String filter) {
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

}