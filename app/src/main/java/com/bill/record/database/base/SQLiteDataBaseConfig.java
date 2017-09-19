package com.bill.record.database.base;

import android.content.Context;

import com.bill.record.R;

import java.util.ArrayList;

/**
 * Created by Wookeibun on 2017/7/17.
 */

public class SQLiteDataBaseConfig {
    public static final String DATABASE_NAME= "readily.db"; //数据库名
    private static final int VERSION = 1;//数据库版本
    private static SQLiteDataBaseConfig INSTANCE;
    private static Context CONTEXT;

    private SQLiteDataBaseConfig() {

    }

    public static SQLiteDataBaseConfig getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SQLiteDataBaseConfig();
            CONTEXT = context;
        }
        return INSTANCE;
    }

    //返回数据库名称的
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    //返回数据库版本
    public int getVersion() {
        return VERSION;
    }

    public ArrayList<String> getTables() {
        ArrayList<String> list = new ArrayList<>();
        String[] sqliteDAOClassName = CONTEXT.getResources().getStringArray(R.array.SQLiteDAOClassName);
        String packgePath = CONTEXT.getPackageName()+".database.dao.";
        for (int i =0;i<sqliteDAOClassName.length;i++){
            list.add(packgePath+sqliteDAOClassName[i]);
        }
        return list;
    }
}
