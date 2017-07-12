package com.example.henu.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by hppc on 2017/3/19.
 */
/*
存储crime数组对象 单例模式
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context)
    {
        if(sCrimeLab == null)
        {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    public List<Crime> getCrimes()
    {
        return mCrimes;
    }
    public Crime getCrime(UUID id)
    {
        for(Crime crime :mCrimes)
        {
            if(crime.getId().equals(id))
            {
                return crime;
            }
        }
        return null;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 ==0);
            mCrimes.add(crime);
        }

    }
}
