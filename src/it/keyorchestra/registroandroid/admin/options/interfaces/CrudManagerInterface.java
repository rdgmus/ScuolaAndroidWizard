package it.keyorchestra.registroandroid.admin.options.interfaces;

import org.json.JSONObject;

import android.widget.Spinner;

public interface CrudManagerInterface {

	public class CRUD_ACTION {
		public final static int UPDATE = 1;
		public final static int CREATE = 2;
		public final static int DELETE = 3;
		public final static int SELECT = 4;
		public final static int COMMIT = 5;
		public final static int ROLLBACK = 6;
		public final static int CLEAR = 7;
		public final static int NONE = 8;
	}

	public boolean UpdateRow(JSONObject data);

	public boolean DeleteRow(JSONObject data);

	public boolean CreateRow(JSONObject data);

	public boolean Select();

	public void Clear();

	public void Commit();

	public void Rollback();
	
	/**
	 * Pulisce opportunamente i campi in tabella, lasciando le relazioni con
	 * le altre tabelle intonse; necessario per i CREATE avere le foreign keys attive
	 */
	public void removeAllTextIntoFields();
	
	/**
	 * Funzione ausiliaria per settare la visibilità dei bottoni nel CRUD panel
	 * @param visible
	 */
	public void setCommitRollback(boolean visible);
	
	/**
	 * Riempie i campi in tabella opportunamente con i dati dell'item
	 * selezionato nello spinner relativo. Prendendo i dati da un array
	 * contenente le righe della tabella , selezionate in formato JSON
	 * @param position
	 */
	public void fillFieldsWithData(int position);
	
	/**
	 * Dopo un evento spinner.OnItemSelectedListener()
	 * bisogna salvare l'id della riga selezionata nelle preference
	 * in modo che i tab successivi possano aggiornarsi correttamente
	 * durante Scuola Wizard ....
	 * @param field_name
	 * @param table_id
	 */
	public void saveTableIdIntoPreferences(String field_name, long table_id);
	
	/**
	 * Inizializza opportunamente i campi in tabella lasciando le foreign keys
	 * l'implementazione è fortemente dipendente dalla tabella della scuola wizard
	 * e dipende in particolare dalle foreign keys 
	 */
	public void inizializzaNuovoRecord();

	/**
	 * Salva un valore nelle preferenze come String
	 * @param key
	 * @param value
	 */
	public void saveTableStringIntoPreferences(String key, String value);
	
	/**
	 * Setta la visibilità del tab indicato
	 * In genere il prossimo deve essere attivato o disattivato
	 * secondo i risultati caricati dalla tabella del database. 
	 * @param visibility
	 * @param tabIndex
	 */
	public void setNextTabVisiblity(int visibility, int tabIndex) ;
	
	/**
	 * Setta la visibilità di tutti tabs a partire da tabIndex
	 * @param visibility
	 * @param tabIndex
	 */
	public void setAllTabsVisibilityFrom(int visibility, int tabIndex);

	/**
	 * Sincronizza lo spinner con il valore indicato in keyValue
	 * ovvero setta la selezione dello spinner al suddetto valore
	 * se esiste.
	 * @param spinner
	 * @param keyValue
	 */
	public void syncronizeSpinner(Spinner spinner, String keyValue);
}
