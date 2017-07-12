package com.example.henu.criminalintent.activity;

import android.support.v4.app.Fragment;

import com.example.henu.criminalintent.fragment.CrimeListFragment;

/**
 * Created by hppc on 2017/3/20.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
