package com.bill.record.database.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bill.record.uitls.Reflection;

import java.util.List;

/**
 * Created by Wookeibun on 2017/7/17.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private static SQLiteDataBaseConfig CONFIG;
    private static SQLiteHelper INSTANCE;
    private Reflection reflection;
private Context context;
    private SQLiteHelper(Context context) {
        super(context, CONFIG.getDatabaseName(),null,CONFIG.getVersion());
        this.context = context;
    }
 public static  SQLiteHelper getInstance(Context context){
     if (INSTANCE == null){
         CONFIG = SQLiteDataBaseConfig.getInstance(context);
         INSTANCE = new SQLiteHelper(context);
     }
     return INSTANCE;
 }
    @Override
    public void onCreate(SQLiteDatabase db) {
        List<String> list = CONFIG.getTables();
        reflection = new Reflection();
        for (int i = 0;i<list.size();i++){
            //参数:1全类名 2.构造方法 3.构造方法中参数的类型
            try {
                SQLiteDateTable sqLiteDateTable = (SQLiteDateTable) reflection.newInstance(
                        list.get(i),new Object[]{context},new Class[] {Context.class});
                //具体调用UserDAode onCreate的方法
                sqLiteDateTable.OnCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public interface SQLiteDateTable{
        void OnCreate(SQLiteDatabase database);
        void OnUpgrade(SQLiteDatabase database);
    }
}
