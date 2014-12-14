package it.keyorchestra.registroandroid.admin.options.tables;

import it.keyorchestra.registroandroid.admin.options.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Scuole extends PreferenceActivity {
	
	private int crudAction;
	private Bundle data;
	private SharedPreferences getPrefs;
	private int activityResult = RESULT_OK;

	/**
	 * @return the activityResult
	 */
	public int getActivityResult() {
		return activityResult;
	}

	/**
	 * @param activityResult
	 *            the activityResult to set
	 */
	public void setActivityResult(int activityResult) {
		this.activityResult = activityResult;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.scuole_table);
		
		
		getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		// RETRIEVE ROW DATA
		Intent intent = getIntent();
		data = intent.getExtras();
		crudAction = data.getInt("action");

		unPackDataAndSetPreferences();

	}

	@SuppressLint("NewApi")
	private void unPackDataAndSetPreferences() {
		// TODO Auto-generated method stub

		// Editor prefEditor = getPrefs.edit(); // Get preference in editor mode
		// prefEditor.putString("id_scuola",
		// String.valueOf(data.getLong("id_scuola"))); // set your default
		// // value here (could
		// // be empty as well)
		// prefEditor.commit(); // finally save changes

		this.resetElementValue("id_scuola", data.getLong("id_scuola"));
		this.resetElementValue("tipo_scuola_acronimo",
				data.getString("tipo_scuola_acronimo"));
		this.resetElementValue("nome_scuola", data.getString("nome_scuola"));
		this.resetElementValue("indirizzo", data.getString("indirizzo"));
		this.resetElementValue("cap", data.getString("cap"));
		this.resetElementValue("citta", data.getString("citta"));
		this.resetElementValue("provincia", data.getString("provincia"));
		this.resetElementValue("telefono", data.getString("telefono"));
		this.resetElementValue("fax", data.getString("fax"));
		this.resetElementValue("email", data.getString("email"));
		this.resetElementValue("web", data.getString("web"));

	}

	
	private String getElementValueAsString(String elementKey) {
		@SuppressWarnings("deprecation")
		EditTextPreference myPrefText = (EditTextPreference) super
				.findPreference(elementKey);
		// Now, manually update it's value to default/empty
		return myPrefText.getText(); // Now, if you click on the item, you'll
										// see
										// the value you've just set here
	}

	protected void resetElementValue(final String elementKey, Object value) {
		// TODO Auto-generated method stub
		@SuppressWarnings("deprecation")
		EditTextPreference myPrefText = (EditTextPreference) super
				.findPreference(elementKey);

		myPrefText.setTitle(myPrefText.getTitle() + ":" + value);
		// Now, manually update it's value to default/empty
		myPrefText.setText(value.toString()); // Now, if you click on the item, you'll see
									// the value you've just set here
		myPrefText
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						// TODO Auto-generated method stub
						Toast.makeText(
								getApplicationContext(),
								preference.getKey() + "=" + newValue.toString(),
								Toast.LENGTH_SHORT).show();
						if (!newValue.toString().equals(
								getElementValueAsString(preference.getKey()))) {
							setActivityResult(RESULT_CANCELED);
							resetElementTitle(elementKey, newValue);
						}
						return true;
					}
				});
	}

	protected void resetElementTitle(String elementKey, Object newValue) {
		// TODO Auto-generated method stub
		@SuppressWarnings("deprecation")
		EditTextPreference myPrefText = (EditTextPreference) super
				.findPreference(elementKey);
		String title = (String)myPrefText.getTitle();
		int index = title.lastIndexOf(':');
		if(index > 0){
			title = title.substring(0, index+1);
			title += newValue;
			myPrefText.setTitle(title);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub

		Intent person = new Intent();
		Bundle backPack = new Bundle();

		packPreferencesIntoBundle(backPack);

		backPack.putString("answer", "Nessun cambiamento nei dati!");

		person.putExtras(backPack);

		// SETTA IL RISULTATO IN BASE AI CAMBIAMENTI EFFETTUATI O MENO
		setResult(getActivityResult(), person);

		Editor editor = getPrefs.edit();
		editor.clear();
		editor.commit();

		super.finish();
	}

	@SuppressLint("NewApi")
	private void packPreferencesIntoBundle(Bundle backPack) {
		// TODO Auto-generated method stub

		backPack.putLong("id_scuola", data.getLong("id_scuola", -1l));

		backPack.putString("tipo_scuola_acronimo",
				data.getString("tipo_scuola_acronimo", ""));
		backPack.putString("nome_scuola", data.getString("nome_scuola", ""));
		backPack.putString("indirizzo", data.getString("indirizzo", ""));
		backPack.putString("cap", data.getString("cap", ""));
		backPack.putString("citta", data.getString("citta", ""));
		backPack.putString("provincia", data.getString("provincia", ""));
		backPack.putString("telefono", data.getString("telefono", ""));
		backPack.putString("fax", data.getString("fax", ""));
		backPack.putString("email", data.getString("email", ""));
		backPack.putString("web", data.getString("web", ""));
		
		backPack.putInt("action", crudAction);
		
	}

}
