package com.cn.bent.sports.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cn.bent.sports.view.fragment.CardNewsFragment;

/**
 * Created by dawn on 2018/3/20.
 */

public class CardFragmentAdapter extends FragmentPagerAdapter {
    private String[] titles;

    public CardFragmentAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return CardNewsFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
