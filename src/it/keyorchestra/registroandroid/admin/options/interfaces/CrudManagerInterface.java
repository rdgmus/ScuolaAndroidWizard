package it.keyorchestra.registroandroid.admin.options.interfaces;

import org.json.JSONObject;

public interface CrudManagerInterface {
	public boolean UpdateRow(String table, JSONObject data);
	public boolean DeleteRow(String table, long id);
	public boolean CreateRow(String table, JSONObject data);
	public boolean Select(String table, String filter);
	public void Clear();
	public void Commit();
	public void Rollback();
}
