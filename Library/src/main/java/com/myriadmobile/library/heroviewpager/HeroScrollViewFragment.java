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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

/**
 * Convenient implementation of {@link AbstractHeroFragment} for a ScrollView
 */
public abstract class HeroScrollViewFragment extends AbstractHeroFragment {

    private ObservableScrollView mScrollView;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View re = inflater.inflate(R.layout.hvp__fragment_scrollview, container, false);
        int heroHeight = getResources().getDimensionPixelSize(R.dimen.hvp__hero_height);
        mScrollView = (ObservableScrollView) re;
        mScrollView.removeAllViews();
        View contents = onCreateScrollViewContent(inflater, mScrollView, savedInstanceState);
        contents.setPadding(
                contents.getPaddingLeft(),
                contents.getPaddingTop() + heroHeight,
                contents.getPaddingRight(),
                contents.getPaddingBottom()
        );
        mScrollView.addView(contents);
        mScrollView.setOnScrollListener(mScrollListener);
        mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                scrollTo(getInitialScroll());
            }
        });

        return re;
    }

    public abstract View onCreateScrollViewContent(LayoutInflater inflater, ScrollView container, Bundle savedInstanceState);

    public ScrollView getScrollView() {
        return mScrollView;
    }

    @Override
    public int getScroll() {
        return mScrollView.getScrollY();
    }

    @Override
    public void scrollTo(int scroll) {
        mScrollView.scrollTo(0, scroll);
    }

    private final ObservableScrollView.OnScrollListener mScrollListener = new ObservableScrollView.OnScrollListener() {

        private volatile boolean isUserScrolling = true;
        private volatile boolean hasUserBeenScrolling = false;

        @Override
        public void onScrollStateChanged(ObservableScrollView view, int scrollState) {
            if(scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                hasUserBeenScrolling = true;
                isUserScrolling = true;
            }
            if(scrollState == SCROLL_STATE_FLING) {
                if(hasUserBeenScrolling) {
                    isUserScrolling = true;
                }
            }
            if(scrollState == SCROLL_STATE_IDLE) {
                hasUserBeenScrolling = false;
                isUserScrolling = false;
            }
        }

        @Override
        public void onScroll(ObservableScrollView view, int oldScroll, int newScroll) {
            if(isUserScrolling) {
                reportScroll(newScroll);
            }
        }
    };
}
