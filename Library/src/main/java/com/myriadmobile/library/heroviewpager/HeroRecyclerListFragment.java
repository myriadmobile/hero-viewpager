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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Convenient implementation of {@link com.myriadmobile.library.heroviewpager.AbstractHeroFragment}
 * for a ListView
 */
public class HeroRecyclerListFragment extends AbstractHeroFragment {

    private RecyclerView list;
    private FrameLayout progress;
    private FrameLayout empty;
    private int mHeroHeight;
    private LinearLayoutManager layoutManager;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        View re = inflater.inflate(getLayoutRes(), container, false);
        mHeroHeight = getHeroContainer().getHeroHeight();

        list = (RecyclerView) re.findViewById(android.R.id.list);
        progress = (FrameLayout) re.findViewById(android.R.id.progress);
        empty = (FrameLayout) re.findViewById(android.R.id.empty);

        empty.setPadding(0, mHeroHeight, 0, 0);
        progress.setPadding(0, mHeroHeight, 0, 0);

        layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);

        list.setOnScrollListener(mScrollListener);

        return re;
    }

    protected int getLayoutRes() {
        return R.layout.hvp__fragment_recycler;
    }

    public RecyclerView getRecyclerView() {
        return list;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if(list.getAdapter() != null) {
            list.getAdapter().unregisterAdapterDataObserver(mDataSetObserver);
        }
        HeaderViewRecyclerAdapter adapter1 = new HeaderViewRecyclerAdapter(adapter);

        View header = new View(getActivity());
        header.setLayoutParams(
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeroHeight));
        adapter1.addHeaderView(header);
        list.setAdapter(adapter1);
        listViewItemHeights.clear();
        list.getAdapter().registerAdapterDataObserver(mDataSetObserver);
    }

    public void setShowProgress(boolean showProgress) {
        progress.setVisibility(showProgress ? View.VISIBLE : View.GONE);
        if(!showProgress) {
            scrollTo(getInitialScroll());
        }
        checkAdapterIsEmpty();
    }

    public void setEmptyView(View view) {
        setEmptyView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setEmptyView(View view, FrameLayout.LayoutParams params) {
        if(empty == null) {
            return;
        }

        empty.removeAllViews();
        params.gravity |= Gravity.CENTER;
        empty.addView(view, params);
        checkAdapterIsEmpty();
    }

    @Override
    public final void scrollTo(final int scroll) {
        if(list == null) {
            return;
        }
        list.scrollToPosition(0);
        list.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(list == null) {
                    return;
                }
                list.scrollBy(0, scroll);
                //list.smoothScrollToPositionFromTop(1, mHeroHeight - scroll, 1);
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

        listViewItemHeights.put(layoutManager.findFirstVisibleItemPosition(), c.getHeight());
        for(int i = 0; i < layoutManager.findFirstVisibleItemPosition(); ++i) {
            scrollY += listViewItemHeights.get(i, 0);
        }
        return scrollY;
    }

    private void checkAdapterIsEmpty() {
        //Check count is 1 because of spacer item
        boolean isEmpty = (list.getAdapter() == null || list.getAdapter().getItemCount() <= 1);
        if(progress.getVisibility() == View.VISIBLE) {
            isEmpty = false;
        }
        empty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private final RecyclerView.AdapterDataObserver mDataSetObserver =
            new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listViewItemHeights.clear();
                    checkAdapterIsEmpty();
                }
            };

    private final RecyclerView.OnScrollListener mScrollListener =
            new RecyclerView.OnScrollListener() {

                private volatile boolean isUserScrolling = false;
                private volatile boolean hasUserBeenScrolling = false;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                    super.onScrollStateChanged(recyclerView, i);

                    if(i == RecyclerView.SCROLL_STATE_DRAGGING) {
                        hasUserBeenScrolling = true;
                        isUserScrolling = true;
                    }
                    if(i == RecyclerView.SCROLL_STATE_SETTLING) {
                        if(hasUserBeenScrolling) {
                            isUserScrolling = true;
                        }
                    }
                    if(i == RecyclerView.SCROLL_STATE_IDLE) {
                        hasUserBeenScrolling = false;
                        isUserScrolling = false;
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int scroll = getScroll();
                    if(isUserScrolling) {
                        reportScroll(scroll);
                    }
                }
            };
}
