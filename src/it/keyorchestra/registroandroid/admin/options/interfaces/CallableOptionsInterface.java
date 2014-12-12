package it.keyorchestra.registroandroid.admin.options.interfaces;

import android.os.Bundle;

public interface CallableOptionsInterface {
	public void unPackPreferences(Bundle basket);

	public void AuthorizeUserWithHash();

	public void RemoveHashTask();
}
