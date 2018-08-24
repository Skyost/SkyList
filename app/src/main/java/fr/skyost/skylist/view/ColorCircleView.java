package fr.skyost.skylist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * A simple view that displays a circle with a colored background and a little border.
 */

public class ColorCircleView extends View {

	/**
	 * The Paint object.
	 */

	private Paint paint;

	/**
	 * The color to display.
	 */

	private int color;

	/**
	 * Creates a new color circle view instance.
	 *
	 * @param context The Context.
	 */

	public ColorCircleView(final Context context) {
		super(context);

		initView();
	}

	/**
	 * Creates a new color circle view instance.
	 *
	 * @param context The Context.
	 * @param attributeSet The attribute set.
	 */

	public ColorCircleView(final Context context, final AttributeSet attributeSet) {
		super(context, attributeSet);

		initView();
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		// The center of our circle must be at the center of the canvas.
		final float cx = canvas.getWidth() / 2;
		final float cy = canvas.getHeight() / 2;

		// The radius is the minimum between our X and Y coordinates.
		final float radius = Math.min(cx, cy);

		// The stroke width is one-tenth of our stroke width.
		final float strokeWidth = radius / 10f;

		// We change the color and the style of our paint object and we draw the circle.
		paint.setColor(color);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(cx, cy, radius, paint);

		// Same here but we must be careful to not draw outside of our canvas.
		paint.setColor(darkenColor(color, 0.75f));
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		canvas.drawCircle(cx, cy, radius - (strokeWidth / 2f), paint);
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		// Very basic implementation of onMeasure(...), first we get all required parameters from the specified specs.
		final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

		// We put the default dimensions.
		int width = 20;
		int height = 20;

		// We change the dimensions according to the specs.
		if(widthSpecMode == MeasureSpec.EXACTLY || (widthSpecMode == MeasureSpec.AT_MOST && width < widthSpecSize)) {
			width = widthSpecSize;
		}

		if(heightSpecMode == MeasureSpec.EXACTLY || (heightSpecMode == MeasureSpec.AT_MOST && height < heightSpecSize)) {
			height = heightSpecSize;
		}

		// And we can call setMeasuredDimension(...).
		setMeasuredDimension(width, height);
	}

	/**
	 * Returns the color to display.
	 *
	 * @return The color to display.
	 */

	public int getColor() {
		return color;
	}

	/**
	 * Sets the color to display.
	 *
	 * @param color The color to display.
	 */

	public void setColor(final int color) {
		this.color = color;
		invalidate();
	}

	/**
	 * Initializes the View.
	 */

	private void initView() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(color = Color.WHITE);
	}

	/**
	 * Darkens a color.
	 *
	 * @param color The color.
	 * @param factor The factory.
	 *
	 * @return The darkened color.
	 */

	private int darkenColor(final int color, final float factor) {
		return Color.argb(Color.alpha(color),
				Math.min(Math.round(Color.red(color) * factor), 255),
				Math.min(Math.round(Color.green(color) * factor), 255),
				Math.min(Math.round(Color.blue(color) * factor), 255));
	}

}
