package com.myriadmobile.library.heroviewpager;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Interface hero containers need to support
 */
public interface IHeroContainer {
    public void reportScroll(IHeroFragment caller, int scroll);

    public void onHeroScrollUpdated(int scroll, int max);

    public Context getContext();

    public FragmentManager getViewPagersFragmentManager();

    LayoutInflater getLayoutInflater();

    View findViewById(int id);

    public void onPageChanged(int page);

    public int getHeroHeight();

    public void setPage(int page);
}
