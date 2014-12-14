package it.keyorchestra.registroandroid.admin.options.interfaces;

import org.json.JSONObject;

public interface CrudManagerInterface {

	public class CRUD_ACTION {
		public final static int UPDATE = 1;
		public final static int CREATE = 2;
		public final static int DELETE = 3;
		public final static int SELECT = 4;
		public final static int COMMIT = 5;
		public final static int ROLLBACK = 6;
		public final static int CLEAR = 7;
	}

	public boolean UpdateRow(JSONObject data);

	public boolean DeleteRow(JSONObject data);

	public boolean CreateRow(JSONObject data);

	public boolean Select(String table, String filter);

	public void Clear();

	public void Commit();

	public void Rollback();
}
