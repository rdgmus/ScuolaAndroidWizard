package it.keyorchestra.registroandroid.admin.options.dbMatthed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DatabaseOps {
	SharedPreferences getPrefs;

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
	 * in modo da non permettere l'accesso all'applicazione, senza autorizzazione.
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

}
