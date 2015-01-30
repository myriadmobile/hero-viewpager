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

/**
 * interface fragments need to support
 */
public interface IHeroFragment {

    String ARG_START_SCROLL_POSITION =
            "com.myriadmobile.library.heroviewpager.arg.START_SCROLL_POSITION";


    /**
     * @return the scroll position to initially scroll to
     */
    public int getInitialScroll();

    /**
     * Typically it's either {@linkplain android.support.v4.app.Fragment#getActivity()} or
     * {@linkplain android.support.v4.app.Fragment#getParentFragment()}
     *
     * @return the parent container
     */
    public IHeroContainer getHeroContainer();

    /**
     * @return the current scroll position of the view
     */
    public int getScroll();

    /**
     * Will be called when the view should be scrolled to a certain position
     *
     * @param scroll y to scroll to
     */
    public void scrollTo(int scroll);
}
