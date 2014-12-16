package it.keyorchestra.registroandroid.admin.options;

import it.keyorchestra.registroandroid.admin.options.dbMatthed.DatabaseOps;
import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CallableOptionsInterface;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class ScuolaWizard extends TabActivity implements
		ActivitiesCommonFunctions, CallableOptionsInterface {

	Intent intent;
	String action;
	String type;
	private DatabaseOps databaseOps;
	private SharedPreferences getPrefs;

	TextView tvUserName, tvHash;
	ImageButton ibBack;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scuola_wizard);

		intent = getIntent();
		action = intent.getAction();
		type = intent.getType();

		databaseOps = new DatabaseOps(getBaseContext());
		getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());

		tvHash = (TextView) findViewById(R.id.tvHash);

		tvUserName = (TextView) findViewById(R.id.tvUserName);

		ibBack = (ImageButton) findViewById(R.id.ibBack);
		ibBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startAnimation((ImageButton) v, 2000);
				ScuolaWizard.this.finish();
			}
		});
		// TAB
		// create the TabHost that will contain the Tabs
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		TabSpec tab1 = tabHost.newTabSpec("First Tab");
		TabSpec tab2 = tabHost.newTabSpec("Second Tab");
		TabSpec tab3 = tabHost.newTabSpec("Third tab");
		// Set the Tab name and Activity
		// that will be opened when particular Tab will be selected
		tab1.setIndicator("Scuole",
				getResources().getDrawable(R.drawable.school128));
		tab1.setContent(new Intent(this, SchoolCreatorActivity.class));

		tab2.setIndicator("Anni Scolastici",
				getResources().getDrawable(R.drawable.calendar_schedule128));
		tab2.setContent(new Intent(this, AsCreatorActivity.class));

		tab3.setIndicator("Periodi A.S.",
				getResources().getDrawable(R.drawable.time_session128));
		tab3.setContent(new Intent(this, Tab3Activity.class));

		/** Add the tabs to the TabHost to display. */
		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		tabHost.addTab(tab3);

		if ("it.keyorchestra.registroandroid.admin.options.REG_ANDROID_ADMIN_OPTIONS"
				.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent

			} else if (type.startsWith("image/")) {
				handleSendImage(intent); // Handle single image being sent
			}
		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			if (type.startsWith("image/")) {
				handleSendMultipleImages(intent); // Handle multiple images
													// being sent
			}
		} else {
			// Handle other intents, such as being started from the home
			// screen
		}
		AuthorizeUserWithHash();
	}

	@Override
	public void registerToolTipFor(ImageButton ib) {
		// TODO Auto-generated method stub
		ib.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {

				customToast(view.getContentDescription(), R.drawable.help32,
						R.layout.info_layout);

				return true;
			}
		});
	}

	@Override
	public boolean customToast(CharSequence charSequence, int iconId,
			int layoutId) {
		// TODO Auto-generated method stub
		Resources res = getResources();
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(layoutId,
				(ViewGroup) findViewById(R.id.toast_layout_root));
		TextView tvToastConnect = (TextView) layout
				.findViewById(R.id.tvToastConnect);
		tvToastConnect.setText(charSequence);

		ImageView ivToastConnect = (ImageView) layout
				.findViewById(R.id.ivToastConnect);
		ivToastConnect.setImageDrawable(res.getDrawable(iconId));

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
		return true;
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
	public void startAnimation(final View ib, final long durationInMilliseconds) {
		// TODO Auto-generated method stub
		// BUTTONS ANIMATION
		final String TAG = "ImageButton Animation";
		Animation animation = new AlphaAnimation(1.0f, 0.25f); // Change alpha
																// from
		// fully visible to
		// invisible
		animation.setDuration(500); // duration - half a second
		animation.setInterpolator(new LinearInterpolator()); // do not alter
																// animation
																// rate
		animation.setRepeatCount(Animation.INFINITE); // Repeat animation
														// infinitely
		animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
													// end so the button will
													// fade back in

		ib.startAnimation(animation);

		Thread t = new Thread() {
			long timeElapsed = 0l;

			public void run() {
				try {
					while (timeElapsed <= durationInMilliseconds) {
						long start = System.currentTimeMillis();
						sleep(1000);
						timeElapsed += System.currentTimeMillis() - start;
					}
				} catch (InterruptedException e) {
					Log.e(TAG, e.toString());
				} finally {
					ib.clearAnimation();
				}
			}
		};
		t.start();
	}

	private class RemoveHashTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			boolean retval = databaseOps.removeUserHash(getBaseContext(),
					getPrefs.getLong("id_utente", -1l),
					getPrefs.getString("hash", ""));

			return retval;
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
				Toast.makeText(getApplicationContext(),
						"Autorizzazione rimossa!", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Autorizzazione non presente!", Toast.LENGTH_LONG)
						.show();
			}
		}

	}

	private class AuthorizeUserWithHash extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			boolean retval = databaseOps.testUserHasHash(
					getApplicationContext(), getPrefs.getLong("id_utente", -1),
					getPrefs.getString("hash", ""));
			return retval;
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
				String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				if (sharedText != null) {
					// Update UI to reflect text being shared
					tvUserName.setVisibility(TextView.VISIBLE);
					tvUserName.setText(sharedText);
//					Toast.makeText(getApplicationContext(), sharedText,
//							Toast.LENGTH_LONG).show();
				}
				tvHash.setVisibility(TextView.VISIBLE);
				tvHash.setText(getPrefs.getString("hash", ""));
//				Toast.makeText(getApplicationContext(),
//						"hash:" + getPrefs.getString("hash", ""),
//						Toast.LENGTH_LONG).show();

				Toast.makeText(getApplicationContext(), "Utente Autorizzato!",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Utente non autorizzato!", Toast.LENGTH_LONG).show();

				ScuolaWizard.this.finish();
			}
		}

	}

	private void handleSendMultipleImages(Intent intent) {
		// TODO Auto-generated method stub
		ArrayList<Uri> imageUris = intent
				.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
		if (imageUris != null) {
			// Update UI to reflect multiple images being shared
		}
	}

	private void handleSendImage(Intent intent) {
		// TODO Auto-generated method stub
		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			// Update UI to reflect image being shared
		}
	}

	private void handleSendText(Intent intent) {
		// TODO Auto-generated method stub
		Bundle basket = intent.getExtras();

		if (basket != null) {
			unPackPreferences(basket);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ActivityGroup#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		RemoveHashTask();
		Editor editor = getPrefs.edit();
		editor.clear().commit();

		Toast.makeText(getApplicationContext(), "Scuola Wizard terminata!",
				Toast.LENGTH_LONG).show();
	}

	@SuppressLint("NewApi")
	@Override
	public void unPackPreferences(Bundle basket) {
		Editor editor = getPrefs.edit();
		editor.putLong("id_utente", basket.getLong("id_utente", -1l));
		editor.putString("hash", basket.getString("hash", ""));

		// DATABASE
		editor.putString("databaseList", basket.getString("databaseList", ""));

		// PostgreSQL
		editor.putString("ipPostgreSQL", basket.getString("ipPostgreSQL", ""));
		editor.putString("userNamePostgreSQL",
				basket.getString("userNamePostgreSQL", ""));
		editor.putString("userPasswdPostgreSQL",
				basket.getString("userPasswdPostgreSQL", ""));
		editor.putString("portPostgreSQL",
				basket.getString("portPostgreSQL", ""));
		editor.putString("schemaPostgreSQL",
				basket.getString("schemaPostgreSQL", ""));

		// MySQL
		editor.putString("ipMySQL", basket.getString("ipMySQL", ""));
		editor.putString("userNameMySQL", basket.getString("userNameMySQL", ""));
		editor.putString("userPasswdMySQL",
				basket.getString("userPasswdMySQL", ""));
		editor.putString("portMySQL", basket.getString("portMySQL", ""));
		editor.putString("schemaMySQL", basket.getString("schemaMySQL", ""));

		// PhpMySqlAndroid - SERVER SIDE SCRIPTINGs
		editor.putString("phpencoder", basket.getString("phpencoder", ""));
		editor.putString("retrieveTableData",
				basket.getString("retrieveTableData", ""));
		editor.putString("GraphViewInterface",
				basket.getString("GraphViewInterface", ""));
		editor.putString("LogEventsRegisterInterface",
				basket.getString("LogEventsRegisterInterface", ""));

		editor.apply();
	}

	@Override
	public void AuthorizeUserWithHash() {
		// TODO Auto-generated method stub
		new AuthorizeUserWithHash().execute();
	}

	@Override
	public void RemoveHashTask() {
		// TODO Auto-generated method stub
		new RemoveHashTask().execute();
	}

}
