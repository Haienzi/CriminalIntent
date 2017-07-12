package com.example.henu.criminalintent.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.henu.criminalintent.R;

/**
 * Created by hppc on 2017/3/19.
 * 为避免重复，将重复代码封装为抽象类  通用超类
 */

public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null)
        {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();

        }
    }
}
