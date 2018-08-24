package fr.skyost.skylist.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import fr.skyost.skylist.application.SkyListApplication;

/**
 * The widget remote views service.
 */

public class WidgetService extends RemoteViewsService {

	@Override
	public RemoteViewsFactory onGetViewFactory(final Intent intent) {
		final SkyListApplication application = (SkyListApplication)getApplication();
		return new WidgetViewFactory(getApplicationContext(), application.getDatabase());
	}

}
