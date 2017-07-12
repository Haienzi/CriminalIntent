package com.example.henu.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hppc on 2017/3/18.
 * 关于所做的市的数据属性
 */

public class Crime {
    private UUID mId;
    private Date mDate;//表示crime发生的时间
    private boolean mSolved;//表示crime是否已经得到处理
    private String mTitle;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }



    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    //UUID Universal unique identifier 通用唯一标识符
    public Crime()
    {
        mId = UUID.randomUUID();
        //随机产生一个UUID类型的数值
        mDate = new Date();//初始化mDate变量
    }
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

}
