package com.example.henu.criminalintent.DbSchema;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.henu.criminalintent.Crime;
import com.example.henu.criminalintent.DbSchema.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Haienzi on 2017/7/28.
 * 1、Cursor是表数据处理工具，任务是封装数据表中的原始字段
 * 2、创建可复用的专用Cursor子类
 * 3、CursorWrapper能够封装一个个cursor的对象，并允许在上面添加新的有用的方法
 */

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    //获取记录的相关字段的值
    public Crime getCrime(){
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        Long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));
        //创建Crime
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);
        //返回具有UUID的Crime
        return crime;
    }
}
