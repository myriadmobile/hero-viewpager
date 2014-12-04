package com.myriadmobile.library.heroviewpager;

/**
 * interface fragments need to support
 */
public interface IHeroFragment {

    String ARG_START_SCROLL_POSITION =
            "com.myriadmobile.library.heroviewpager.arg.START_SCROLL_POSITION";

    int getInitialScroll();

    public IHeroContainer getHeroContainer();

    public int getScroll();

    public void scrollTo(int scroll);
}
