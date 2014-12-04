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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>
 * Base Activity that has everything you need to have a parallax Hero header with tabs and a ViewPager
 * </p>
 */
public abstract class HeroViewPagerActivity extends ActionBarActivity implements IHeroContainer {

    private HvpHelper hvpHelper;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.hvp__activity_base);
        hvpHelper = new HvpHelper(this);
        hvpHelper.onCreate(savedInstanceState);

        toolbar = (Toolbar) findViewById(R.id.hvp__toolbar);
        setSupportActionBar(toolbar);
    }

    public void setUnderlayView(int layoutResID) {
        hvpHelper.setUnderlayView(layoutResID);
    }

    public void setUnderlayView(View view) {
        hvpHelper.setUnderlayView(view);
    }

    public void setUnderlayView(View view, ViewGroup.LayoutParams params) {
        hvpHelper.setUnderlayView(view, params);
    }

    public void setOverlayView(int layoutResID) {
        hvpHelper.setOverlayView(layoutResID);
    }

    public void setOverlayView(View view) {
        hvpHelper.setOverlayView(view);
    }

    public void setOverlayView(View view, ViewGroup.LayoutParams params) {
        hvpHelper.setOverlayView(view, params);
    }

    /**
     * Set the PagerAdapter for our ViewPager
     *
     * @param adapter The HeroPagerAdapter
     */
    public final void setAdapter(HeroPagerAdapter adapter) {
        hvpHelper.setAdapter(adapter);
    }

    @Override
    public void reportScroll(IHeroFragment caller, int scroll) {
        hvpHelper.reportScroll(caller, scroll);
    }

    @Override
    public void onHeroScrollUpdated(int scroll, int max) {
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public FragmentManager getViewPagersFragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    public void onPageChanged(int page) {
    }

    @Override
    public int getHeroHeight() {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, getResources().getDisplayMetrics());
    }

    @Override
    public int getToolbarHeight() {
        return toolbar.getHeight();
    }

    @Override
    public void setPage(int page) {
        hvpHelper.setPage(page);
    }
}
