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
 * <p>Base Fragment used in {@link HvpHelper}</p>
 * <p>If you're implementing a ListView fragment, use {@link com.myriadmobile.library.heroviewpager.HeroListFragment}</p>
 */
public abstract class AbstractHeroFragment extends Fragment implements IHeroFragment {

    private IHeroContainer helper;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof IHeroContainer) {
            helper = (IHeroContainer) activity;
        } else if(getParentFragment() instanceof IHeroContainer) {
            helper = (IHeroContainer) getParentFragment();
        } else {
            throw new IllegalArgumentException("Parent activity must extend HeroViewPagerActivity");
        }

        if(getArguments() == null) {
            throw new IllegalArgumentException("getArguments() should never be null");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scrollTo(getInitialScroll());
    }

    @Override
    public void onDetach() {
        super.onDetach();

        helper = null;
    }

    @Override
    public int getInitialScroll() {
        return getArguments().getInt(ARG_START_SCROLL_POSITION, 0);
    }

    @Override
    public IHeroContainer getHeroContainer() {
        return helper;
    }

    public final void reportScroll(int scroll) {
        if(isAdded()) {
            getHeroContainer().reportScroll(this, scroll);
        }
    }
}
