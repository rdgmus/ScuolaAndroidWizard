package it.keyorchestra.registroandroid.admin.options.dbMatthed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class DatabaseOps {
	SharedPreferences getPrefs;
	private boolean addStartComma = false;

	public DatabaseOps(Context context) {
		super();
		getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * Viene effettuato un test dall'Activity richiamata dall'utente per
	 * controllare le credenziali dell'utente: id_utente e hash.
	 * 
	 * @param context
	 * @param id_utente
	 * @param hash
	 * @return
	 */
	public boolean testUserHasHash(Context context, long id_utente, String hash) {
		// TODO Auto-generated method stub
		String url = getUrl(context);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "SELECT * FROM  utenti_scuola " + " WHERE  id_utente="
					+ id_utente + " AND hash = '" + hash + "'";
			ResultSet result = st.executeQuery(sql);
			while (result.next()) {
				if (hash.equals(result.getString("hash"))) {
					st.close();
					conn.close();
					return true;
				}
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Cotruisce la URL per la conessione al database fornando IP, credenziali
	 * dell'amministratore e altro, prelevando i valori dalle preferenze. Questi
	 * valori devono essere forniti dall'ammnistratore dell'applicazione e
	 * dipendono dalla rete in cui è situato il server di database e dalle
	 * credenziali fornite al momento dell'installazione di XAMPP o di
	 * PostgreSQL o altro, in futuro.
	 * 
	 * @param context
	 * @return
	 */
	private String getUrl(Context context) {
		String ip = null;
		String userName = null;
		String userPasswd = null;
		String port = null;
		String schema = null;
		String url = "";

		// SharedPreferences getPrefs = PreferenceManager
		// .getDefaultSharedPreferences(context);

		String defaultDatabase = getPrefs.getString("databaseList", "1");

		try {
			if (defaultDatabase.contentEquals("PostgreSQL")) {
				ip = getPrefs.getString("ipPostgreSQL", "");
				userName = getPrefs.getString("userNamePostgreSQL", "");
				userPasswd = getPrefs.getString("userPasswdPostgreSQL", "");
				port = getPrefs.getString("portPostgreSQL", "");
				schema = getPrefs.getString("schemaPostgreSQL", "");

				Class.forName("org.postgresql.Driver");

				// "jdbc:postgresql://192.168.0.150:5432/postgres?user=postgres&password=iw3072yl";
				url = "jdbc:postgresql://" + ip + ":" + port + "/" + schema
						+ "?user=" + userName + "&password=" + userPasswd;

			} else if (defaultDatabase.contentEquals("MySQL")) {
				ip = getPrefs.getString("ipMySQL", "");
				userName = getPrefs.getString("userNameMySQL", "");
				userPasswd = getPrefs.getString("userPasswdMySQL", "");
				port = getPrefs.getString("portMySQL", "");
				schema = getPrefs.getString("schemaMySQL", "");

				Class.forName("com.mysql.jdbc.Driver");

				// "jdbc:mysql://192.168.0.110:3306/scuola?user=root&password=iw3072ylA";
				url = "jdbc:mysql://" + ip + ":" + port + "/" + schema
						+ "?user=" + userName + "&password=" + userPasswd;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return url;
	}

	/**
	 * Rimuove l'hash creato al momento di entrare nell'attività Scuola Wizard
	 * in modo da non permettere l'accesso all'applicazione, senza
	 * autorizzazione.
	 * 
	 * @param context
	 * @param id_utente
	 * @param hash
	 * @return
	 */
	public boolean removeUserHash(Context context, long id_utente, String hash) {
		// TODO Auto-generated method stub
		String url = getUrl(context);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "UPDATE utenti_scuola " + " SET hash=NULL"
					+ " WHERE  id_utente=" + id_utente + " AND hash = '" + hash
					+ "'";
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Update della tabella scuole con i Bundle inviato e ricevuto facendo un
	 * confronto tra i dati inviati e ricevuti effettua l'update della riga in
	 * tabella.
	 * 
	 * @param context
	 * @param beforeChangeBasket
	 * @param afterChangeBasket
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public boolean updateScuola(Context applicationContext,
			Bundle beforeChangeBasket, Bundle afterChangeBasket) {
		// TODO Auto-generated method stub
		long id_scuola = beforeChangeBasket.getLong("id_scuola");
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			addStartComma = false;

			sql = "UPDATE `scuole` " + "SET ";
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"nome_scuola", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"tipo_scuola_acronimo", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"indirizzo", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"cap", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"citta", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"provincia", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"telefono", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"fax", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"email", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"web", sql);

			sql += " WHERE id_scuola=" + id_scuola;
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Aggiunge campi alla query se sono stati cambiati dall'utente per
	 * effettuare l'operazione di Commit con una query dove figurano solo i
	 * campi effettivamente cambiati. Onde evitare effetti indesiderati sui dati
	 * in tabella.
	 * 
	 * @param beforeChangeBasket
	 *            - Bundle inviato
	 * @param afterChangeBasket
	 *            - Bundle ricevuto
	 * @param key
	 *            - campo della tabella e chiave dei Bundles
	 * @param sql
	 *            - query
	 * @param adStartComma
	 *            - se TRUE => aggiunge comma (virgola) prima di aggiungere il
	 *            campo alla query
	 * @return - query integrat con il campo aggiunto
	 */
	private String integrateSqlWithField(Bundle beforeChangeBasket,
			Bundle afterChangeBasket, String key, String sql) {
		// TODO Auto-generated method stub
		if (!beforeChangeBasket.getString(key).equals(
				afterChangeBasket.getString(key))) {
			if (addStartComma) {
				sql += ",";
			}
			sql += " " + key + "='" + afterChangeBasket.getString(key) + "' ";
			addStartComma = true;
		}
		return sql;
	}

	private String integrateSqlWithNumberField(Bundle beforeChangeBasket,
			Bundle afterChangeBasket, String key, String sql) {
		// TODO Auto-generated method stub
		if (beforeChangeBasket.getLong(key) != afterChangeBasket.getLong(key)) {
			if (addStartComma) {
				sql += ",";
			}
			sql += " " + key + "=" + afterChangeBasket.getLong(key) + " ";
			addStartComma = true;
		}
		return sql;
	}

	private String integrateSqlWithNumberFieldInt(Bundle beforeChangeBasket,
			Bundle afterChangeBasket, String key, String sql) {
		// TODO Auto-generated method stub
		if (beforeChangeBasket.getInt(key) != afterChangeBasket.getInt(key)) {
			if (addStartComma) {
				sql += ",";
			}
			sql += " " + key + "=" + afterChangeBasket.getInt(key) + " ";
			addStartComma = true;
		}
		return sql;
	}

	public Boolean deleteScuola(Context applicationContext,
			Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "DELETE FROM `scuole`" + " WHERE  id_scuola="
					+ beforeChangeBasket.getLong("id_scuola");
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean createScuola(Context applicationContext,
			Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "INSERT INTO `scuole`"
					+ "(`nome_scuola`, `tipo_scuola_acronimo`, "
					+ "`indirizzo`, `cap`, `citta`, "
					+ "`provincia`, `telefono`, `fax`, `email`, `web`) "
					+ "VALUES (" + "'"
					+ beforeChangeBasket.getString("nome_scuola")
					+ "',"
					+ "'"
					+ beforeChangeBasket.getString("tipo_scuola_acronimo")
					+ "',"
					+ "'"
					+ beforeChangeBasket.getString("indirizzo")
					+ "',"
					+ "'"
					+ beforeChangeBasket.getString("cap")
					+ "',"
					+ "'"
					+ beforeChangeBasket.getString("citta")
					+ "',"
					+ "'"
					+ beforeChangeBasket.getString("provincia")
					+ "',"
					+ "'"
					+ beforeChangeBasket.getString("telefono")
					+ "',"
					+ "'"
					+ beforeChangeBasket.getString("fax")
					+ "',"
					+ "'"
					+ beforeChangeBasket.getString("email")
					+ "',"
					+ "'"
					+ beforeChangeBasket.getString("web") + "'" + ")";
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean updateAnnoScolastico(Context applicationContext,
			Bundle beforeChangeBasket, Bundle afterChangeBasket) {
		// TODO Auto-generated method stub
		long id_anno_scolastico = beforeChangeBasket
				.getLong("id_anno_scolastico");
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			addStartComma = false;

			sql = "UPDATE `anni_scolastici` " + "SET ";
			sql = integrateSqlWithNumberField(beforeChangeBasket,
					afterChangeBasket, "id_scuola", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"anno_scolastico", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"start_date", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"end_date", sql);

			sql += " WHERE id_anno_scolastico=" + id_anno_scolastico;
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean deleteAnnoScolastico(Context applicationContext,
			Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "DELETE FROM `anni_scolastici`"
					+ " WHERE  id_anno_scolastico="
					+ beforeChangeBasket.getLong("id_anno_scolastico");
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean createAnnoScolastico(Context applicationContext,
			Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "INSERT INTO `anni_scolastici`"
					+ "( `id_scuola`, `anno_scolastico`, `start_date`, `end_date`) "
					+ "VALUES (" + "" + beforeChangeBasket.getLong("id_scuola")
					+ "," + "'"
					+ beforeChangeBasket.getString("anno_scolastico") + "',"
					+ "'" + beforeChangeBasket.getString("start_date") + "',"
					+ "'" + beforeChangeBasket.getString("end_date") + "'"
					+ ")";
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Descrizione della scuola per anni scolastici nel campo foreign key
	 * id_scuola
	 * 
	 * @param applicationContext
	 * @param id_scuola
	 * @return
	 */
	public String getScuolaDescription(Context applicationContext,
			long id_scuola) {
		// TODO Auto-generated method stub
		String description = "";

		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "SELECT `id_scuola`,`tipo_scuola_acronimo`,`nome_scuola` FROM `scuole` WHERE `id_scuola`="
					+ id_scuola;
			ResultSet result = st.executeQuery(sql);
			while (result.next()) {
				description += "[" + result.getLong("id_scuola") + "] ";
				description += result.getString("tipo_scuola_acronimo") + " - ";
				description += result.getString("nome_scuola");
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return description;
	}

	public String getAnnoScolasticoDescription(Context applicationContext,
			long id_anno_scolastico) {
		// TODO Auto-generated method stub
		String description = "";

		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "SELECT `id_anno_scolastico`, `anno_scolastico` FROM `anni_scolastici` WHERE `id_anno_scolastico`="
					+ id_anno_scolastico;
			ResultSet result = st.executeQuery(sql);
			while (result.next()) {
				description += "[" + result.getLong("id_anno_scolastico")
						+ "] ";
				description += result.getString("anno_scolastico");
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return description;
	}

	/**
	 * Ottiene la data da impostare come inizio del periodo dell'anno scolastico
	 * indicato. Se non vi sono periodi ritorna la data di inizio A.S.
	 * Altrimenti la data MAX('end_date') dei periodi presenti in tabella.
	 * 
	 * @param applicationContext
	 * @param id_anno_scolastico
	 * @return
	 */
	public String getNextPeriodStartDateAsString(Context applicationContext,
			long id_anno_scolastico) {
		// TODO Auto-generated method stub
		String startDateAsString = "";

		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "SELECT COUNT(*) AS count FROM `periodi_anno_scolastico` WHERE `id_anno_scolastico`="
					+ id_anno_scolastico;

			ResultSet result = st.executeQuery(sql);
			int count = 0;
			while (result.next()) {
				count = result.getInt("count");
			}

			if (count > 0) {
				sql = "SELECT MAX(`end_date`) AS max_date FROM `periodi_anno_scolastico` WHERE `id_anno_scolastico`="
						+ id_anno_scolastico;

				result = st.executeQuery(sql);
				while (result.next()) {
					startDateAsString = result.getString("max_date");
					String[] split = startDateAsString.split("-");
					int year = Integer.valueOf(split[0]);
					int month = Integer.valueOf(split[1]);
					int day = Integer.valueOf(split[2]);
					Calendar c = Calendar.getInstance();
					c.set(year, month, day);
					c.add(Calendar.DAY_OF_MONTH, 1);
					startDateAsString = c.get(Calendar.YEAR) + "-"
							+ pad(c.get(Calendar.MONTH)) + "-"
							+ pad(c.get(Calendar.DAY_OF_MONTH));
				}
			} else {
				startDateAsString = getPrefs.getString("start_date", "");
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return startDateAsString;
	}

	public String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	/**
	 * Crea un record PERIODO ANNO SCOLASTICO
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @return
	 */
	public Boolean createPeriodoAnnoScolastico(Context applicationContext,
			Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "INSERT INTO `periodi_anno_scolastico`"
					+ "( `id_scuola`, `id_anno_scolastico`, `periodo`, `start_date`, `end_date`) "
					+ "VALUES (" + "" + beforeChangeBasket.getLong("id_scuola")
					+ "," + beforeChangeBasket.getLong("id_anno_scolastico")
					+ "," + "'" + beforeChangeBasket.getString("periodo")
					+ "'," + "'" + beforeChangeBasket.getString("start_date")
					+ "'," + "'" + beforeChangeBasket.getString("end_date")
					+ "'" + ")";
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Cancella dal database la riga di periodo anno scolastico id nel basket
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @return
	 */
	public Boolean deletePeriodoAnnoScolastico(Context applicationContext,
			Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "DELETE FROM `periodi_anno_scolastico`"
					+ " WHERE  id_anno_scolastico="
					+ beforeChangeBasket.getLong("id_anno_scolastico")
					+ " AND id_scuola="
					+ beforeChangeBasket.getLong("id_scuola")
					+ " AND id_periodo="
					+ beforeChangeBasket.getLong("id_periodo");
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Calcola la copertura dell'Anno Scolastico in base ai periodi creati in
	 * percentuale da 0% a 100%
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @return
	 */
	public Double[] calculateProgressOfAsCoperture(Context applicationContext,
			Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		Double[] arrayResults = { 0.0, 0.0, 0.0 };

		Double dResult = (double) 0;
		Double diffDateOfAs = (double) 0, sumDaysOfPeriodsAs = (double) 0;

		long id_anno_scolastico = beforeChangeBasket
				.getLong("id_anno_scolastico");
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			// NUMERO GIORNI DURATA ANNO SCOLASTICO
			sql = "SELECT DATEDIFF(`end_date`,`start_date`) AS DiffDate "
					+ "FROM `anni_scolastici` WHERE id_anno_scolastico="
					+ id_anno_scolastico;
			ResultSet result = st.executeQuery(sql);
			while (result.next()) {
				diffDateOfAs = result.getDouble("DiffDate");
			}
			// SOMMA GIORNI DEI PERIODI ESISTENTI
			sql = "SELECT SUM(DATEDIFF(`end_date`,`start_date`)+1)-1 AS sum FROM `periodi_anno_scolastico` WHERE `id_anno_scolastico`="
					+ id_anno_scolastico;
			result = st.executeQuery(sql);
			while (result.next()) {
				sumDaysOfPeriodsAs = result.getDouble("sum");
			}

			// CALCOLA PERCENTUALE
			dResult = sumDaysOfPeriodsAs / diffDateOfAs;

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		arrayResults[0] = dResult;
		arrayResults[1] = sumDaysOfPeriodsAs;
		arrayResults[2] = diffDateOfAs;
		return arrayResults;
	}

	/**
	 * UPDATE su tabella periodi anno scolastico
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @param afterChangeBasket
	 * @return
	 */
	public Boolean updatePeriodoAnnoScolastico(Context applicationContext,
			Bundle beforeChangeBasket, Bundle afterChangeBasket) {
		// TODO Auto-generated method stub
		long id_periodo = beforeChangeBasket.getLong("id_periodo");

		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;
			addStartComma = false;

			sql = "UPDATE `periodi_anno_scolastico` " + "SET ";
			sql = integrateSqlWithNumberField(beforeChangeBasket,
					afterChangeBasket, "id_scuola", sql);
			sql = integrateSqlWithNumberField(beforeChangeBasket,
					afterChangeBasket, "id_anno_scolastico", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"periodo", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"start_date", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"end_date", sql);

			sql += " WHERE id_periodo=" + id_periodo;

			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * UPDATE tabella parametri_orario_as
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @param afterChangeBasket
	 * @return
	 */
	public Boolean updateParametriOrarioAnnoScolastico(
			Context applicationContext, Bundle beforeChangeBasket,
			Bundle afterChangeBasket) {
		// TODO Auto-generated method stub
		long id_param_orario = beforeChangeBasket.getLong("id_param_orario");
		long id_anno_scolastico = beforeChangeBasket
				.getLong("id_anno_scolastico");

		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;
			addStartComma = false;

			sql = "UPDATE `parametri_orario_as` " + "SET ";
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"inizio_lezioni", sql);
			sql = integrateSqlWithNumberFieldInt(beforeChangeBasket,
					afterChangeBasket, "durata_ora_minuti", sql);
			sql = integrateSqlWithNumberFieldInt(beforeChangeBasket,
					afterChangeBasket, "durata_intervallo_minuti", sql);

			sql += " WHERE id_anno_scolastico=" + id_anno_scolastico
					+ " AND id_param_orario = " + id_param_orario;

			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * DELETE tabella parametri_orario_as
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @return
	 */
	public Boolean deleteParametriOrarioAnnoScolastico(
			Context applicationContext, Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "DELETE FROM `parametri_orario_as`"
					+ " WHERE  id_anno_scolastico="
					+ beforeChangeBasket.getLong("id_anno_scolastico")
					+ " AND id_param_orario="
					+ beforeChangeBasket.getLong("id_param_orario");
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Crea una riga di parametri orario per l'anno scolastico nel basket
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @return
	 */
	public Boolean createParametriOrarioAnnoScolastico(
			Context applicationContext, Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "INSERT INTO `parametri_orario_as`("
					+ "`id_anno_scolastico`, `inizio_lezioni`, `durata_ora_minuti`, "
					+ "`durata_intervallo_minuti`) VALUES ("
					+ beforeChangeBasket.getLong("id_anno_scolastico") + ", '"
					+ beforeChangeBasket.getString("inizio_lezioni") + "', "
					+ beforeChangeBasket.getString("durata_ora_minuti") + ","
					+ beforeChangeBasket.getString("durata_intervallo_minuti")
					+ ")";
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Crea una classe nell'anno scolastico nel Bundle il campo anno_scolastico
	 * non viene passato nella query perchè viene settato da un trigger che
	 * esegue anche la upperCase() su i campi: nome_classe e specializzazione
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @return
	 */
	public Boolean createClasseAnnoScolastico(Context applicationContext,
			Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;
			sql = "INSERT INTO `classi`(`id_scuola`, `id_anno_scolastico`, "
					+ "`nome_classe`, `specializzazione`) VALUES ("
					+ beforeChangeBasket.getLong("id_scuola") + ","
					+ beforeChangeBasket.getLong("id_anno_scolastico") + ","
					+ "'" + beforeChangeBasket.getString("nome_classe") + "',"
					+ "'" + beforeChangeBasket.getString("specializzazione")
					+ "'" + ")";
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Controlla se esiste nome_classe nell'anno scolastico indicato
	 * 
	 * @param context
	 * @param nome_classe
	 * @param id_anno_scolastico
	 * @return
	 */
	public boolean existsNomeClasseIntoAS(Context context, String nome_classe,
			long id_anno_scolastico) {
		// TODO Auto-generated method stub

		String url = getUrl(context);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "SELECT `nome_classe` FROM `classi` WHERE `id_anno_scolastico` = "
					+ id_anno_scolastico
					+ " AND `nome_classe` = '"
					+ nome_classe + "'";
			ResultSet result = st.executeQuery(sql);
			while (result.next()) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Update tabella classi
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @param afterChangeBasket
	 * @return
	 */
	public Boolean updateClasseAnnoScolastico(Context applicationContext,
			Bundle beforeChangeBasket, Bundle afterChangeBasket) {
		// TODO Auto-generated method stub
		long id_classe = beforeChangeBasket.getLong("id_classe");
		long id_anno_scolastico = beforeChangeBasket
				.getLong("id_anno_scolastico");
		long id_scuola = beforeChangeBasket.getLong("id_scuola");
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			addStartComma = false;

			sql = "UPDATE `classi` " + "SET ";
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"nome_classe", sql);
			sql = integrateSqlWithField(beforeChangeBasket, afterChangeBasket,
					"specializzazione", sql);

			sql += " WHERE id_classe=" + id_classe + " AND id_anno_scolastico="
					+ id_anno_scolastico + " AND id_scuola=" + id_scuola;
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}
			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Cancella classe dal database
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @return
	 */
	public Boolean deleteClasseAnnoScolastico(Context applicationContext,
			Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "DELETE FROM `classi`" + " WHERE  id_classe="
					+ beforeChangeBasket.getLong("id_classe");
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getClasseDescription(Context applicationContext,
			long id_classe) {
		// TODO Auto-generated method stub
		String description = "";

		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			sql = "SELECT `id_classe`,`nome_classe`,`specializzazione` FROM `classi` \n"
					+ "WHERE `id_classe`=" + id_classe;
			ResultSet result = st.executeQuery(sql);
			while (result.next()) {
				description += "[" + result.getLong("id_classe") + "] ";
				description += result.getString("nome_classe");
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return description;
	}

	/**
	 * Crea un nuovo studente nella classe prescelta
	 * 
	 * @param applicationContext
	 * @param beforeChangeBasket
	 * @return
	 */
	public Boolean createStudente(Context applicationContext,
			Bundle beforeChangeBasket) {
		// TODO Auto-generated method stub
		String url = getUrl(applicationContext);

		Connection conn;
		try {
			DriverManager.setLoginTimeout(15);
			conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			String sql = null;

			String data_entrata = beforeChangeBasket.getString("data_entrata");
			if(data_entrata.length()==0){
				data_entrata = "null";
			}
			String ritirato_data = beforeChangeBasket.getString("ritirato_data");
			if(ritirato_data.length()==0){
				ritirato_data = "null";
			}
			
			sql = "INSERT INTO `studenti`(`id_classe`, `id_anno_scolastico`, "
					+ "`cognome`, `nome`, `attivo`, `data_entrata`, `ritirato_data`) "
					+ "VALUES (" + beforeChangeBasket.getLong("id_classe")
					+ "," + beforeChangeBasket.getLong("id_anno_scolastico")
					+ ", '" + beforeChangeBasket.getString("cognome") + "',"
					+ "'" + beforeChangeBasket.getString("nome") + "',"
					+ beforeChangeBasket.getInt("attivo") + "," + (data_entrata.equalsIgnoreCase("null")?"":"'")
					+ beforeChangeBasket.getString("data_entrata") +(data_entrata.equalsIgnoreCase("null")?",":"',")
					+(ritirato_data.equalsIgnoreCase("null")?"":"'")
					+ beforeChangeBasket.getString("ritirato_data") + (ritirato_data.equalsIgnoreCase("null")?"":"'") + ")";
			int result = st.executeUpdate(sql);
			if (result == 1) {
				st.close();
				conn.close();
				return true;
			}

			st.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
