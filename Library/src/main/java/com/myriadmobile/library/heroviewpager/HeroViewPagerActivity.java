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

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;

/**
 *
 */
public abstract class HeroViewPagerActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    private FrameLayout mHeroContent;
    private FrameLayout mHeroContainer;
    private ViewPager mPager;
    private HeroPagerAdapter mPagerAdapter;
    private TabHost mTabHost;
    private HorizontalScrollView mTabScrollView;

    private int actionBarHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hvp__activity_base);

        // Calculate ActionBar height
        TypedValue typedValue = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }

        mHeroContent = (FrameLayout) findViewById(R.id.hero_content);
        mHeroContainer = (FrameLayout) findViewById(R.id.hvp__hero_frame);

        mPager = (ViewPager) findViewById(R.id.hvp__pager);
        mPager.setOnPageChangeListener(this);

        mTabHost = (TabHost) findViewById(R.id.hvp__hero_tabs);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);

        mTabScrollView = (HorizontalScrollView) findViewById(R.id.hvp__tab_scroll_view);
    }

    /**
     * Set the PagerAdapter for our ViewPager
     * @param adapter The HeroPagerAdapter
     */
    public final void setAdapter(HeroPagerAdapter adapter) {
        if(mPagerAdapter != null) {
            mPagerAdapter.unregisterDataSetObserver(mObserver);
        }
        adapter.registerDataSetObserver(mObserver);
        mPagerAdapter = adapter;
        mPager.setAdapter(adapter);
        onFragmentSetChanged();
    }

    /**
     * Called by an {@link com.myriadmobile.library.heroviewpager.AbstractHeroFragment} to
     * report changes in scroll position
     *
     * @param caller The fragment that called this method
     * @param scroll The offset that the fragment's content are offset by
     */
    void reportScroll(AbstractHeroFragment caller, int scroll) {

        //We only care about the _currently_ shown fragment. Later in the method, we may
        // cause other fragments to report scrolling
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(FragmentPagerAdapter.makeFragmentName(R.id.hvp__pager, mPager.getCurrentItem()));
        if(caller != currentFragment) {
            return;
        }

        //Change the position of the Hero header
        int height = mHeroContainer.getHeight() - actionBarHeight - mTabHost.getHeight();
        int clampedScroll = Math.max(0, Math.min(height, scroll));
        mHeroContainer.setTranslationY(-clampedScroll);

        //Report the scroll position to the PageAdapter so that it can
        // appropriately instantiate other fragments at this scroll position
        if(mPagerAdapter != null) {
            mPagerAdapter.setCurrentScrollPosition(clampedScroll);
        }

        //Scroll other fragments already added to also scroll to the new position
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment fragment : fragments) {
            if(fragment != null && fragment != caller && fragment.isAdded() && fragment instanceof AbstractHeroFragment) {
                AbstractHeroFragment heroFragment = (AbstractHeroFragment) fragment;
                //Here is where we can cause recursion!
                //The HeroListView implementation will report scrolling
                heroFragment.scrollTo(clampedScroll);
            }
        }

        //Tell implementers that the scroll position changed
        onHeroScrollUpdated(clampedScroll, height);
    }

    /**
     *
     * @return A safe place to add custom views to the Hero header
     */
    public FrameLayout getHeroContent() {
        return mHeroContent;
    }

    /**
     * Called when a fragment scrolls, and MAY change the position of the Hero header view
     * @param scroll The amount that the header has been offset by
     * @param max The max distance the header will be offset by
     */
    public void onHeroScrollUpdated(int scroll, int max) {
        //For people to implement
    }

    /**
     * <p>
     *     Construct the Tab view for the TabWidget. This should be fully populated with content
     *</p>
     * <p>
     *     By default, this method will construct tabs that are themed like ActionBar tabs,
     *     with the text set to the String returned by
     *     {@link com.myriadmobile.library.heroviewpager.HeroPagerAdapter#getPageTitle(int)}.
     * </p>
     * @param inflater View inflater to use
     * @param index The index of the tab
     * @return A View to use as a tab, fully populated with content
     */
    public View makeIndicator(LayoutInflater inflater, int index) {
        View tabIndicator = inflater.inflate(R.layout.hvp__tab_indicator, mTabHost.getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(android.R.id.title);
        title.setText(mPagerAdapter.getPageTitle(index));
        return tabIndicator;
    }

    /**
     * Called when we should re-populate the TabWidget
     */
    private void onFragmentSetChanged() {
        mTabHost.clearAllTabs();
        long now = System.currentTimeMillis();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            TabHost.TabSpec spec = mTabHost.newTabSpec(now + "_tab_" + i);
            spec.setIndicator(makeIndicator(inflater, i));
            spec.setContent(mTabFactory);
            mTabHost.addTab(spec);
        }
    }

    /**
     * Set onto the HeroPagerAdapter to listen for changes in pages
     */
    private final DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            onFragmentSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            onFragmentSetChanged();
        }
    };

    /**
     * A dummy factory so we can use TabWidget
     */
    private final TabHost.TabContentFactory mTabFactory = new TabHost.TabContentFactory() {
        @Override
        public View createTabContent(String s) {
            return new View(HeroViewPagerActivity.this);
        }
    };

    @Override
    public final void onTabChanged(String s) {
        mPager.setCurrentItem(mTabHost.getCurrentTab());

        int left = mTabHost.getCurrentTabView().getLeft();
        mTabScrollView.smoothScrollTo(left - mTabScrollView.getHeight(), 0);
    }

    @Override
    public final void onPageSelected(int i) {
        mTabHost.setCurrentTab(i);
    }

    @Override public void onPageScrolled(int i, float v, int i2) {}
    @Override public void onPageScrollStateChanged(int i) {}

}
