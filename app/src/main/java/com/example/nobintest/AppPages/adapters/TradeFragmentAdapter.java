package com.example.nobintest.AppPages.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.nobintest.AppPages.TradeFragments.TradeBuyFragment;
import com.example.nobintest.AppPages.TradeFragments.TradeOrderListFragment;
import com.example.nobintest.AppPages.TradeFragments.TradeSellFragment;
import com.example.nobintest.R;

public class TradeFragmentAdapter extends FragmentStatePagerAdapter {

    private Context context;
    
    public TradeFragmentAdapter(FragmentManager fm, Context context) {
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
                return new TradeBuyFragment();
            case 1:
                return new TradeSellFragment();
            case 2:
                return new TradeOrderListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            //
            //Your tab titles
            //
            case 0:
                return context.getString(R.string.buy_title);
            case 1:
                return context.getString(R.string.sell_title);
            case 2:
                return context.getString(R.string.orders_title);
            default:
                return null;
        }
    }
}
