package com.example.nobintest.AppPages.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.nobintest.AppPages.WalletFragments.WalletFragment;
import com.example.nobintest.R;

public class WalletFragmentAdapter extends FragmentStatePagerAdapter {

    private Context context;
    public WalletFragmentAdapter(FragmentManager fm, Context context) {
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
                return new WalletFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            //
            //Your tab titles
            //
            case 0:
                return context.getString(R.string.wallet_title);
            default:
                return null;
        }
    }
}
