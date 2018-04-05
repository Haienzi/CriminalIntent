package com.example.henu.criminalintent.DbSchema;

/**
 * Created by Haienzi on 2017/7/28.
 *
 */

public class CrimeDbSchema {
    public static final class CrimeTable{
        public static final String NAME = "crimes";//数据库表名
        public static final class Cols{
            public static final String UUID = "uuid";//id
            public static final String TITLE = "title";//标题
            public static final String DATE = "date";//日期
            public static final String SOLVED = "solved";//是否解决
            public static final String SUSPECT = "suspect";//嫌疑人
        }
    }
}
