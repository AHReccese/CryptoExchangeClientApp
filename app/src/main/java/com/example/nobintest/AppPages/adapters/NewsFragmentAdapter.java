package com.example.nobintest.AppPages.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.nobintest.AppPages.NewsFragments.NewsTextFragment;
import com.example.nobintest.AppPages.NewsFragments.NewsVideosFragment;
import com.example.nobintest.R;

public class NewsFragmentAdapter extends FragmentStatePagerAdapter {

    private Context context;
    public NewsFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewsTextFragment();
            case 1:
                return new NewsVideosFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            //
            //Your tab titles
            //
            case 0:
                return context.getString(R.string.news_title);
            case 1:
                return context.getString(R.string.videos_title);
            default:
                return null;
        }
    }
}
