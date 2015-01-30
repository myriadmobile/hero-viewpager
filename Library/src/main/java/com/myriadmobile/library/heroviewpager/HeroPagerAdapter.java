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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

/**
 * Specific implementation of {@link FragmentPagerAdapter} that will pass the current scroll
 * position to each {@link AbstractHeroFragment} when it is added.
 */
public class HeroPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<FragmentInfo> items = new ArrayList<FragmentInfo>();
    private final FragmentActivity context;
    private int currentScrollPosition = 0;

    public HeroPagerAdapter(FragmentActivity context) {
        super(context.getSupportFragmentManager());
        this.context = context;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void add(String title, Class<? extends IHeroFragment> clazz, Bundle args) {
        items.add(new FragmentInfo(title, clazz, args));
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position).title;
    }

    @Override
    public Fragment getItem(int i) {
        FragmentInfo info = items.get(i);
        Bundle args = new Bundle(info.args);
        args.putInt(IHeroFragment.ARG_START_SCROLL_POSITION, currentScrollPosition);
        return Fragment.instantiate(context, info.clazz.getName(), args);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void setCurrentScrollPosition(int currentScrollPosition) {
        this.currentScrollPosition = currentScrollPosition;
    }

    private static class FragmentInfo {
        final String title;
        final Class<? extends IHeroFragment> clazz;
        final Bundle args;

        private FragmentInfo(String title, Class<? extends IHeroFragment> clazz,
                             Bundle args) {
            this.title = title;
            this.clazz = clazz;
            //args shouldn't be null!
            this.args = args == null ? new Bundle() : args;
        }
    }
}
