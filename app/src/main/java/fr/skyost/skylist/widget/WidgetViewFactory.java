package fr.skyost.skylist.widget;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.skyost.skylist.R;
import fr.skyost.skylist.task.TodoTask;
import fr.skyost.skylist.task.database.SkyListDatabase;

/**
 * The widget remote views factory.
 */

public class WidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

	/**
	 * The circle character.
	 */

	private static final Spanned CIRCLE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml("&#11044;", Html.FROM_HTML_MODE_LEGACY) : Html.fromHtml("&#9679;");

	/**
	 * The tasks to display.
	 */

	private List<TodoTask> tasks;

	/**
	 * The Context.
	 */

	private Context context;

	/**
	 * The database.
	 */

	private SkyListDatabase database;

	/**
	 * Creates a new widget view factory instance.
	 *
	 * @param context The Context.
	 * @param database The database.
	 */

	WidgetViewFactory(final Context context, final SkyListDatabase database) {
		this.context = context;
		this.database = database;
	}

	@Override
	public void onCreate() {
		tasks = new ArrayList<>();
	}

	@Override
	public void onDataSetChanged() {
		tasks = database.getTodoTaskDao().queryTasksForDate(LocalDate.now());

		if(tasks.isEmpty()) {
			tasks = Collections.singletonList(null);
		}
	}

	@Override
	public void onDestroy() {
		tasks.clear();
		database.close();
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public RemoteViews getViewAt(final int position) {
		// We create the row view.
		final RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_todo_task);
		final TodoTask task = tasks.get(position);

		// If there is no task we hide the circle and display the corresponding string to the user. Otherwise we use the normal behaviour.
		if(task == null) {
			row.setTextViewText(R.id.widget_todo_description, context.getString(R.string.widget_empty));
			row.setViewVisibility(R.id.widget_todo_colorcircle, View.GONE);
		}
		else {
			row.setTextViewText(R.id.widget_todo_description, task.getDescription());
			row.setTextViewText(R.id.widget_todo_colorcircle, CIRCLE);
			row.setTextColor(R.id.widget_todo_colorcircle, task.getColor());
		}

		return row;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public long getItemId(final int i) {
		return i;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	/**
	 * Returns the tasks to display.
	 *
	 * @return The tasks to display.
	 */

	public List<TodoTask> getTasks() {
		return tasks;
	}

	/**
	 * Sets the tasks to display.
	 *
	 * @param tasks The tasks to display.
	 */

	public void setTasks(final List<TodoTask> tasks) {
		this.tasks = tasks;
	}

	/**
	 * Returns the Context.
	 *
	 * @return The Context.
	 */

	public Context getContext() {
		return context;
	}

	/**
	 * Sets the Context.
	 *
	 * @param context The Context.
	 */

	public void setContext(final Context context) {
		this.context = context;
	}

	/**
	 * Returns the database.
	 *
	 * @return The database.
	 */

	public SkyListDatabase getDatabase() {
		return database;
	}

	/**
	 * Sets the database.
	 *
	 * @param database The database.
	 */

	public void setDatabase(final SkyListDatabase database) {
		this.database = database;
	}

}