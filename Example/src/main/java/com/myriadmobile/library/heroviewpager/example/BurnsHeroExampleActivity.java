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

package com.myriadmobile.library.heroviewpager.example;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.myriadmobile.library.heroviewpager.BurnsHeroActivity;
import com.myriadmobile.library.heroviewpager.HeroPagerAdapter;

/**
 *
 */
public class BurnsHeroExampleActivity extends BurnsHeroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        HeroPagerAdapter adapter = new HeroPagerAdapter(this);
        adapter.add("Test 1", DummyListFragment.class, null);
        adapter.add("Test 2", DummyListFragment.class, null);
        adapter.add("Test 3", DummyListFragment.class, null);
        adapter.add("Test 4", DummyListFragment.class, null);
        adapter.add("Test 5", DummyListFragment.class, null);
        adapter.add("Test 6", DummyListFragment.class, null);
        adapter.add("Test 7", DummyListFragment.class, null);
        adapter.add("Test 8", DummyListFragment.class, null);
        setAdapter(adapter);

        setHeroBackgroundDrawable(getResources().getDrawable(R.drawable.carnarvon_castle));

        setMovingIconDrawable(getResources().getDrawable(R.drawable.myriad_shield));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.action_view_github) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MainActivity.GITHUB_URL));
            if(intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
