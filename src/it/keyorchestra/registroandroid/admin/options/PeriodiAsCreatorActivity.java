package it.keyorchestra.registroandroid.admin.options;

import org.json.JSONObject;

import it.keyorchestra.registroandroid.admin.options.interfaces.ActivitiesCommonFunctions;
import it.keyorchestra.registroandroid.admin.options.interfaces.CrudManagerInterface;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageButton;

public class PeriodiAsCreatorActivity extends Activity implements
ActivitiesCommonFunctions, CrudManagerInterface, OnFocusChangeListener{

	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.periodi_as_creator_activity);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean UpdateRow(JSONObject data) {
		// TODO Auto-generated method stub
		return false;
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
	public boolean Select() {
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

	@Override
	public void removeAllTextIntoFields() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCommitRollback(boolean visible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fillFieldsWithData(int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveTableIdIntoPreferences(String field_name, long table_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inizializzaNuovoRecord() {
		// TODO Auto-generated method stub
		
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
		return null;
	}

	@Override
	public String getDatabaseIpFromPreferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startAnimation(View ib, long durationInMilliseconds) {
		// TODO Auto-generated method stub
		
	}

}
