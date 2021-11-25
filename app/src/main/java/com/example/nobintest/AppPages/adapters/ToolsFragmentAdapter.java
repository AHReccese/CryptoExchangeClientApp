package com.example.nobintest.AppPages.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.nobintest.AppPages.ToolsFragments.ChartFragment;
import com.example.nobintest.AppPages.ToolsFragments.PriceAlertFragment;
import com.example.nobintest.AppPages.ToolsFragments.TradeBotFragment;
import com.example.nobintest.R;

public class ToolsFragmentAdapter extends FragmentStatePagerAdapter {

    private Context context;
    public ToolsFragmentAdapter(FragmentManager fm, Context context) {
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
                return new ChartFragment();
            case 1:
                return new PriceAlertFragment();
            case 2:
                return new TradeBotFragment();
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
                return context.getString(R.string.chart_title);
            case 1:
                return context.getString(R.string.price_alert_title);
            case 2:
                return context.getString(R.string.trade_bot_title);
            default:
                return null;
        }
    }
}
