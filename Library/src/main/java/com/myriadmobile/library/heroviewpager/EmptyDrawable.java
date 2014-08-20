package com.myriadmobile.library.heroviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/**
 * Just a Drawable to take up space!
 */
public class EmptyDrawable extends Drawable {

    private final int width;
    private final int height;

    public EmptyDrawable(Context context, int widthDp, int heightDp) {
        this.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, widthDp, context.getResources().getDisplayMetrics());
        this.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightDp, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
