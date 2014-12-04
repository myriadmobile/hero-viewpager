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
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Interface hero containers need to support
 */
public interface IHeroContainer {

    /**
     * Containers need to implement this to clamp the scroll appropriately,
     * scroll fragments if they need it, and call {@linkplain #onHeroScrollUpdated(int, int)}
     *
     * @param caller The fragment that is reporting a scroll event
     * @param scroll The amount currently scrolled
     */
    public void reportScroll(IHeroFragment caller, int scroll);

    /**
     * A callback for when the scroll of the hero view <i>may</i> have changed.
     *
     * @param scroll The amount currently scrolled
     * @param max    The max amount that scroll can be
     */
    public void onHeroScrollUpdated(int scroll, int max);

    /**
     * @return a reference to context, any context
     */
    public Context getContext();

    /**
     * This is typically {@linkplain android.support.v4.app.FragmentActivity#getSupportFragmentManager()}
     * or {@linkplain android.support.v4.app.Fragment#getChildFragmentManager()}
     *
     * @return a {@linkplain android.support.v4.app.FragmentManager} to add ViewPager fragments with
     */
    public FragmentManager getViewPagersFragmentManager();

    /**
     * @return a {@linkplain android.view.LayoutInflater} to inflate views with
     */
    LayoutInflater getLayoutInflater();

    /**
     * Find relevant views
     *
     * @param id ID of the View to find
     * @return The first View found, if any
     */
    View findViewById(int id);

    /**
     * Callback for when a page changes on the ViewPager
     *
     * @param page new page index
     */
    public void onPageChanged(int page);

    /**
     * A good number here is probably in the 200s
     *
     * @return the number of pixels the hero portion should be
     */
    public int getHeroHeight();

    /**
     * @param page New Page the ViewPager should display
     */
    public void setPage(int page);

    /**
     * @return the height to put between the tabs and the top of the screen
     */
    public int getToolbarHeight();
}
