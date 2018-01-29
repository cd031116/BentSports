package com.cn.bent.sports.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by lyj on 2018/1/29 0029.
 * description
 */

public class MainTabFragment extends Fragment {

    public static MainTabFragment newInstance(int type) {
        MainTabFragment fragment = new MainTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

}
