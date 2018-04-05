package com.example.henu.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.henu.criminalintent.DbSchema.CrimeBaseHelper;
import com.example.henu.criminalintent.DbSchema.CrimeCursorWrapper;
import com.example.henu.criminalintent.DbSchema.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hppc on 2017/3/19.
 */
/*
存储crime数组对象 单例模式]
7/28 改用mDateBase存储数据
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    //private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());

        return values;
    }
    //查询Crime记录
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(CrimeTable.NAME,
                null,//Columns - null selects all columns
                whereClause,
                whereArgs,
                null,//groupBy
                null,//having
                null//orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    //添加犯罪记录
    public void addCrime(Crime c) {
        //mCrimes.add(c);
        ContentValues contentValues = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, contentValues);
    }

    //删除犯罪记录
    public void deleteCrime(Crime c) {
        String uuidString = c.getId().toString();
        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?",new String[]{uuidString});
    }
    //更新数据库记录
    public void updateCrime(Crime crime)
    {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME,values,CrimeTable.Cols.UUID + " = ?",new String[]{uuidString});

    }
    //返回所有的犯罪记录列表
    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?",
                new String[]{id.toString()});
        try
        {
            if(cursor.getCount() == 0)
            {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        //如果不存在就首次创建crimebase.db数据库文件
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
        //mCrimes = new ArrayList<>();
       /* for (int i = 0; i < 100; i++)
        {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 ==0);
            mCrimes.add(crime);
        }*/

    }
}
