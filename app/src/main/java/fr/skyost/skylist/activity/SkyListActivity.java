package fr.skyost.skylist.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.skyost.skylist.application.SkyListApplication;
import fr.skyost.skylist.application.theme.SkyListTheme;

/**
 * Represents a themeable activity that belongs to SkyList.
 */

public abstract class SkyListActivity extends AppCompatActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		// We apply the theme.
		final SkyListTheme theme = getSkyListTheme();
		theme.apply(this);

		// We then can call onCreate(...).
		super.onCreate(savedInstanceState);
	}

	/**
	 * Returns the SkyList theme that is applied to this activity.
	 *
	 * @return The SkyList theme that is applied to this activity.
	 */

	public SkyListTheme getSkyListTheme() {
		return ((SkyListApplication)this.getApplication()).getSkyListTheme();
	}

}
