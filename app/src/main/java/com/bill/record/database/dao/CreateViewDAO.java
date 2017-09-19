package com.bill.record.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bill.record.database.base.SQLiteHelper;

/**
 * Created by Wookeibun on 2017/8/3.
 */

public class CreateViewDAO implements SQLiteHelper.SQLiteDateTable {
    private Context context;

    public CreateViewDAO(Context context) {
        this.context = context;
    }

    @Override
    public void OnCreate(SQLiteDatabase database) {
        StringBuilder sql = new StringBuilder();
        sql.append(" Create VIEW v_payout as ");
        sql.append(" select p.*, c.parentId, c.categoryName, c.path, a.accountBookName ");
        sql.append(" from payout p left join category c on p.categoryId=c.categoryId ");
        sql.append(" left join accountBook a on p.accountBookId=a.accountBookId ");
        database.execSQL(sql.toString());
    }

    @Override
    public void OnUpgrade(SQLiteDatabase database) {

    }
}