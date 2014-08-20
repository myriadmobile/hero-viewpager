/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Myriad Mobile
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.myriadmobile.library.heroviewpager;

import android.animation.FloatEvaluator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Implementation of {@link com.myriadmobile.library.heroviewpager.HeroViewPagerActivity}
 * that implements some cool Ken Burns style effects.
 */
public class BurnsHeroActivity extends HeroViewPagerActivity {

    private ImageView movingImage;
    private ImageView backgroundImage;

    private ImageView upImage;
    private TextView actionBarTitle;
    private TextView actionBarSubtitle;

    FloatEvaluator evaluator = new FloatEvaluator();
    Interpolator interpolator = new DecelerateInterpolator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setIcon(new EmptyDrawable(this, 48, 48));

        upImage = (ImageView) getWindow().getDecorView().findViewById(android.R.id.home);

        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        actionBarTitle = (TextView) getWindow().getDecorView().findViewById(titleId);

        int subtitleId = getResources().getIdentifier("action_bar_subtitle", "id", "android");
        actionBarSubtitle = (TextView) getWindow().getDecorView().findViewById(subtitleId);

        //int dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        movingImage = new ImageView(this);
        movingImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        movingImage.setPivotX(0);
        movingImage.setPivotY(0);
        //int dp92 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 92, getResources().getDisplayMetrics());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        getHeroOverlayContainer().addView(movingImage, params);

        FrameLayout heroContent = getHeroContentContainer();

        backgroundImage = new ImageView(this);
        backgroundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        heroContent.addView(backgroundImage, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    public void setHeroBackgroundDrawable(Drawable drawable) {
        backgroundImage.setImageDrawable(drawable);
    }

    public void setMovingIconDrawable(Drawable drawable) {
        movingImage.setImageDrawable(drawable);
    }

    @Override
    public void onHeroScrollUpdated(int scroll, int max) {
        getHeroContentContainer().setTranslationY(scroll * 0.5f);

        float scale = scroll / (float) max;

        if(actionBarTitle != null) {
            actionBarTitle.setAlpha(scale);
        }
        if(actionBarSubtitle != null) {
            actionBarSubtitle.setAlpha(scale);
        }

        scale = interpolator.getInterpolation(scale);

        float ratio = 1;
        if(movingImage.getHeight() != 0) {
            ratio = upImage.getHeight() / (float) movingImage.getHeight();
        }
        float scaleFactor = evaluator.evaluate(scale, 1, ratio);

        movingImage.setScaleX(scaleFactor);
        movingImage.setScaleY(scaleFactor);

        movingImage.setTranslationX(evaluator.evaluate(scale, 0, upImage.getLeft() - movingImage.getLeft() ));
        movingImage.setTranslationY(evaluator.evaluate(scale, 0, upImage.getTop() - movingImage.getTop()));
    }
}
