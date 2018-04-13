package com.example.henu.criminalintent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.henu.criminalintent.bean.Crime;
import com.example.henu.criminalintent.bean.CrimeLab;
import com.example.henu.criminalintent.R;
import com.example.henu.criminalintent.fragment.CrimeFragment;

import java.util.List;
import java.util.UUID;

/**
 * Maqiuhong
 */
public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    public static final String EXTRA_CRIME_ID ="package com.example.henu.criminalintent.activity.crime_id";

    /**
     * 启动CrimeActivity时，传递要启动的Crime的ID,显示对应的Crime
     * @param packageContext
     * @param crimeId
     * @return
     */
    public static Intent newIntent(Context packageContext, UUID crimeId)
    {
        Intent intent = new Intent(packageContext,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);//传递附加信息
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager);
        //从CrimeLab中获取数据集
        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        //为viewPager设置适配器 提供视图  设置adapter为FragmentStatePageAdapter的一个匿名实例 需要fragmentManager作为参数
        // 获取并显示指定位置的Crime时，它会返回配置过的CrimeFragment
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);//获取指定位置的crime
                return CrimeFragment.newInstance(crime.getId());//将配置过的CrimeFragment添加给activity
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        //ViewPage默认的是只显示PageAdapter的第一个列表项，因此需要循环检查找到对应的Crime
        for(int i=0;i<mCrimes.size();i++)
        {
            if(mCrimes.get(i).getId().equals(crimeId)){
                //Set the currently selected page.
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
