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
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Convenient implementation of {@link AbstractHeroFragment} for a ListView
 */
public class HeroListFragment extends AbstractHeroFragment {

    private ListView list;
    private FrameLayout empty;
    private FrameLayout progress;
    private int mHeroHeight;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View re = inflater.inflate(R.layout.hvp__fragment_list, container, false);
        mHeroHeight = getHeroContainer().getHeroHeight();

        list = (ListView) re.findViewById(android.R.id.list);
        empty = (FrameLayout) re.findViewById(android.R.id.empty);
        progress = (FrameLayout) re.findViewById(android.R.id.progress);

        empty.setPadding(0, mHeroHeight, 0, 0);
        progress.setPadding(0, mHeroHeight, 0, 0);

        View header = new View(re.getContext());
        header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeroHeight));
        list.addHeaderView(header, null, false);

        list.setSelectionAfterHeaderView();
        list.setOnScrollListener(mScrollListener);

        return re;
    }

    public ListView getListView() {
        return list;
    }

    public void setListAdapter(ListAdapter adapter) {
        if(list.getAdapter() != null) {
            list.getAdapter().unregisterDataSetObserver(mDataSetObserver);
        }
        list.setAdapter(adapter);
        listViewItemHeights.clear();
        list.getAdapter().registerDataSetObserver(mDataSetObserver);
        checkAdapterIsEmpty();
    }

    public void setShowProgress(boolean showProgress) {
        progress.setVisibility(showProgress ? View.VISIBLE : View.GONE);
        if(!showProgress) {
            scrollTo(getInitialScroll());
        }
    }

    public void setEmptyView(View view) {
        setEmptyView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setEmptyView(View view, FrameLayout.LayoutParams params) {
        if(empty == null) {
            return;
        }

        empty.removeAllViews();
        params.gravity |= Gravity.CENTER;
        empty.addView(view, params);
    }

    @Override
    public final void scrollTo(final int scroll) {
        if(list == null) {
            return;
        }
        list.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(list == null) {
                    return;
                }
                list.smoothScrollToPositionFromTop(1, mHeroHeight - scroll, 1);
            }
        }, 10);
    }

    private final SparseIntArray listViewItemHeights = new SparseIntArray();

    public final int getScroll() {
        View c = list.getChildAt(0); //this is the first visible row
        if(c == null) {
            return 0;
        }
        int scrollY = -c.getTop();
        listViewItemHeights.put(list.getFirstVisiblePosition(), c.getHeight());
        for(int i = 0; i < list.getFirstVisiblePosition(); ++i) {
            scrollY += listViewItemHeights.get(i, 0);
        }
        return scrollY;
    }

    private void checkAdapterIsEmpty() {
        //Check count is 1 because of spacer item
        boolean isEmpty = (list.getAdapter() == null || list.getAdapter().getCount() <= 1);
        if(progress.getVisibility() == View.VISIBLE) {
            isEmpty = false;
        }
        empty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            listViewItemHeights.clear();
            checkAdapterIsEmpty();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            listViewItemHeights.clear();
            checkAdapterIsEmpty();
        }
    };

    private final AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {

        private volatile boolean isUserScrolling = false;
        private volatile boolean hasUserBeenScrolling = false;

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            if(i == SCROLL_STATE_TOUCH_SCROLL) {
                hasUserBeenScrolling = true;
                isUserScrolling = true;
            }
            if(i == SCROLL_STATE_FLING) {
                if(hasUserBeenScrolling) {
                    isUserScrolling = true;
                }
            }
            if(i == SCROLL_STATE_IDLE) {
                hasUserBeenScrolling = false;
                isUserScrolling = false;
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            int scroll = getScroll();
            if(isUserScrolling) {
                reportScroll(scroll);
            }
        }
    };
}
