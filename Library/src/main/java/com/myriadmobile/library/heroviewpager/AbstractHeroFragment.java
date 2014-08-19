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


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 *
 */
abstract class AbstractHeroFragment extends Fragment {

    public static final String ARG_START_SCROLL_POSITION = "com.myriadmobile.library.heroviewpager.arg.START_SCROLL_POSITION";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof HeroViewPagerActivity)) {
            throw new IllegalArgumentException("Parent activity must extend HeroViewPagerActivity");
        }

        if (getArguments() == null) {
            throw new IllegalArgumentException("getArguments() should never be null");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        int scrollTo = args.getInt(ARG_START_SCROLL_POSITION, 0);
        scrollTo(scrollTo);
    }

    public HeroViewPagerActivity getHeroActivity() {
        return (HeroViewPagerActivity) getActivity();
    }

    public abstract int getScroll();

    public final void reportScroll(int scroll) {
        if (isAdded()) {
            getHeroActivity().reportScroll(this, scroll);
        }
    }

    public abstract void scrollTo(int scroll);
}
