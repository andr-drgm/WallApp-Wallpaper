package com.adrw.wallappwallpaper.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.adrw.wallappwallpaper.R;

import java.util.HashMap;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private Fragment mCurrentFragment;
    HashMap< Integer, PlaceholderFragment> cachedFragmentMap;
    HashMap<String, Uri> uriMap;

    public SectionsPagerAdapter(Context context, FragmentManager fm,HashMap<String, Uri> uriMap) {
        super(fm);
        mContext = context;
        cachedFragmentMap = new HashMap<>();
        this.uriMap = uriMap;
    }

    public Fragment getmCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if(getmCurrentFragment() != object){
            mCurrentFragment = ((Fragment)object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        PlaceholderFragment newFragment = new PlaceholderFragment(position,cachedFragmentMap,this.uriMap);
        Bundle args = new Bundle();
        args.putInt("INDEX", position);
        newFragment.setArguments(args);
        cachedFragmentMap.put(position, newFragment);

        return newFragment;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        cachedFragmentMap.remove(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }



    @Override
    public int getCount() {
        return 3;
    }
}