package fr.skyost.skylist.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import fr.skyost.skylist.application.SkyListApplication;
import fr.skyost.skylist.application.theme.SkyListTheme;

/**
 * Represents a themeable activity that belongs to SkyList.
 */

public abstract class SkyListActivity extends AppCompatActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// We apply the theme (and we have to ensure the correct background color has been applied because it does not work all the time).
		final SkyListTheme theme = ((SkyListApplication)this.getApplication()).getSkyListTheme();
		theme.apply(this);
		getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, theme.getPrimaryColorDark()));
	}

}
