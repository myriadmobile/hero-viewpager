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

package com.myriadmobile.library.heroviewpager.example;

import android.animation.FloatEvaluator;
import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.myriadmobile.library.heroviewpager.HeroPagerAdapter;
import com.myriadmobile.library.heroviewpager.HeroViewPagerActivity;


public class MainActivity extends HeroViewPagerActivity {

    private ImageView movingImage;
    private ImageView upImage;
    private int dp8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new EmptyDrawable(this, 48, 48));

        upImage = (ImageView) getWindow().getDecorView().findViewById(android.R.id.home);
        assert upImage != null;

        HeroPagerAdapter adapter = new HeroPagerAdapter(this);
        adapter.add("Test 1", DummyFragment.class, null);
        adapter.add("Test 2", DummyFragment.class, null);
        adapter.add("Test 3", DummyFragment.class, null);
        adapter.add("Test 4", DummyFragment.class, null);
        adapter.add("Test 5", DummyFragment.class, null);
        adapter.add("Test 6", DummyFragment.class, null);
        adapter.add("Test 7", DummyFragment.class, null);
        adapter.add("Test 8", DummyFragment.class, null);
        setAdapter(adapter);

        dp8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        movingImage = new ImageView(this);
        movingImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        movingImage.setAdjustViewBounds(true);
        movingImage.setPivotX(0);
        movingImage.setPivotY(0);
        movingImage.setImageResource(R.drawable.ic_launcher);
        int dp92 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 92, getResources().getDisplayMetrics());
        LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp92);
        params.gravity = Gravity.CENTER;
        getHeroOverlayContainer().addView(movingImage, params);

        FrameLayout heroContent = getHeroContentContainer();

        ImageView image = new ImageView(this);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setImageResource(R.drawable.carnarvon_castle);
        heroContent.addView(image, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    FloatEvaluator evaluator = new FloatEvaluator();

    @Override
    public void onHeroScrollUpdated(int scroll, int max) {
        getHeroContentContainer().setTranslationY(scroll * 0.5f);

        float scale = scroll / (float) max;

        float scaleFactor = evaluator.evaluate(scale, 1, 32f / 92f);

        movingImage.setScaleX(scaleFactor);
        movingImage.setScaleY(scaleFactor);

        movingImage.setTranslationX(evaluator.evaluate(scale, 0, upImage.getLeft() - movingImage.getLeft() ));
        movingImage.setTranslationY(evaluator.evaluate(scale, 0, upImage.getTop() - movingImage.getTop()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
