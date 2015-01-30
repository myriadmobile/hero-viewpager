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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * A ScrollView where we can set a scroll listener
 */
public class ObservableScrollView extends ScrollView {

    private static final int newCheck = 100;

    private OnScrollListener onScrollListener;
    private int initialPosition = 0;

    private Runnable scrollerTask = new Runnable() {
        public void run() {

            int newPosition = getScrollY();
            if(initialPosition - newPosition == 0) {//has stopped
                tryAndReportState(OnScrollListener.SCROLL_STATE_IDLE);

            } else {
                initialPosition = getScrollY();
                ObservableScrollView.this.postDelayed(scrollerTask, newCheck);
                tryAndReportState(OnScrollListener.SCROLL_STATE_FLING);
            }
        }
    };

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
        init();
    }

    private void init() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    startScrollerTask();
                } else {
                    tryAndReportState(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
                }
                return false;
            }
        });
    }

    private void startScrollerTask() {
        initialPosition = getScrollY();
        postDelayed(scrollerTask, newCheck);
        tryAndReportState(OnScrollListener.SCROLL_STATE_FLING);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        final OnScrollListener listener = onScrollListener;
        if(listener != null) {
            listener.onScroll(this, oldt, t);
        }
    }

    private volatile int currentState = OnScrollListener.SCROLL_STATE_IDLE;

    private void tryAndReportState(int state) {
        if(currentState == state) {
            return;
        }
        currentState = state;
        final OnScrollListener listener = onScrollListener;
        if(listener != null) {
            listener.onScrollStateChanged(ObservableScrollView.this, state);
        }
    }

    /**
     * Interface definition for a callback to be invoked when the scroll view
     * has been scrolled.
     */
    public interface OnScrollListener {
        /**
         * The view is not scrolling. Note navigating the list using the trackball counts as
         * being in the idle state since these transitions are not animated.
         */
        public static int SCROLL_STATE_IDLE = 0;
        /**
         * The user is scrolling using touch, and their finger is still on the screen
         */
        public static int SCROLL_STATE_TOUCH_SCROLL = 1;
        /**
         * The user had previously been scrolling using touch and had performed a fling. The
         * animation is now coasting to a stop
         */
        public static int SCROLL_STATE_FLING = 2;

        public void onScrollStateChanged(ObservableScrollView view, int scrollState);

        public void onScroll(ObservableScrollView view, int oldScroll, int newScroll);
    }
}
