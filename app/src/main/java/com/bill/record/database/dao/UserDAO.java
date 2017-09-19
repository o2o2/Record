package com.bill.record.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bill.record.R;
import com.bill.record.database.base.SQLiteDAOBase;
import com.bill.record.entity.Users;
import com.bill.record.uitls.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Wookeibun on 2017/7/17.
 */

public class UserDAO extends SQLiteDAOBase {
    public UserDAO(Context context) {
        super(context);
    }

    @Override
    protected String[] getTableNameAndPK() {
        return new String[]{"users", "userId"};
    }

    @Override
    protected Object findModel(Cursor cursor) {
        Users users = new Users();
        users.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
        users.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
        Date createDate = DateUtil.StringToDate(cursor.getString(cursor.getColumnIndex("createDate")), "yyyy-MM-dd HH:mm:ss");
        users.setCreateDate(createDate);
        users.setState(cursor.getInt(cursor.getColumnIndex("state")));
        return users;
    }

    @Override
    public void OnCreate(SQLiteDatabase database) {
        StringBuilder sql = new StringBuilder();
        sql.append("Create  TABLE [users](");
        sql.append(" [userId] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
        sql.append(",[userName] varchar(20) NOT NULL");
        sql.append(",[createDate] datetime NOT NULL");
        sql.append(",[state] int NOT NULL");
        sql.append(")");
        database.execSQL(sql.toString());
        initDefaultData(database);
    }

    @Override
    public void OnUpgrade(SQLiteDatabase database) {

    }

    public boolean insertUser(Users users) {
        ContentValues contentValues = createParms(users);
        long newid = getDatabase().insert(getTableNameAndPK()[0], null, contentValues);
        return newid > 0;
    }

    public boolean deleteUser(String condition) {
        return delete(getTableNameAndPK()[0], condition);
    }

    public boolean updateUser(String condition, Users users) {
        ContentValues contentValues = createParms(users);
        return updateUser(condition,contentValues);
    }

    public boolean updateUser(String condition, ContentValues contentValues) {

        return getDatabase().update(getTableNameAndPK()[0], contentValues, condition, null) > 0;
    }

    public List<Users> getUsers(String condition) {
        String sql = "select * from users where 1=1 " + condition;
        return getList(sql);
    }

    public ContentValues createParms(Users info) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", info.getUserName());
        contentValues.put("createDate", DateUtil.DateToString(info.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
        contentValues.put("state", info.getState());
        return contentValues;
    }

    private void initDefaultData(SQLiteDatabase database) {
        Users users = new Users();
        String[] userNames = getContext().getResources().getStringArray(R.array.InitDefaultUserName);
        for (int i = 0; i < userNames.length; i++) {
            users.setUserName(userNames[i]);
            ContentValues contentValues = createParms(users);
            database.insert(getTableNameAndPK()[0], null, contentValues);
        }
    }

}
