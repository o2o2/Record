package com.bill.record.business;

import android.content.ContentValues;
import android.content.Context;

import com.bill.record.business.base.BaseBusiness;
import com.bill.record.database.dao.UserDAO;
import com.bill.record.entity.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wookeibun on 2017/7/18.
 */

public class UserBusiness extends BaseBusiness {
    private UserDAO userDAO;

    public UserBusiness(Context context) {
        super(context);
        userDAO = new UserDAO(context);
    }

    public boolean insertUser(Users user){
        boolean result = userDAO.insertUser(user);
        return result;
    }

    public boolean deleteUserByUserId(int userId){
        String condition = " and userId=" + userId;
        boolean result = userDAO.deleteUser(condition);
        return result;
    }

    public boolean updateUser(Users user){
        String condition = "userId=" + user.getUserId();
        boolean result = userDAO.updateUser(condition, user);
        return result;
    }

    public List<Users> getUsers(String condition){
        return userDAO.getUsers(condition);
    }

    public Users getUserByUserId(int userId){
        List<Users> list = userDAO.getUsers(" and userId="+userId);
        if (list != null && list.size() == 1){
            return list.get(0);
        }else {
            return null;
        }
    }

    //获取未删除的用户
    public List<Users> getNotHideUsers(){
        return userDAO.getUsers(" and state=1");
    }

    public boolean isExistUserByUserName(String userName,
                                         Integer userId){
        String condition = " and userName='"+userName+"'";
        if (userId != null){
            condition += " and userId <> " + userId;
        }
        List<Users> list = userDAO.getUsers(condition);
        if (list != null && list.size() > 0){
            return true;
        }else {
            return false;
        }
    }

    //根据用户ID隐藏用户的方法
    public boolean hideUserByUserId(int userid){
        String condition = " userid=" + userid;
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);
        return userDAO.updateUser(condition, contentValues);
    }

    //1,2,3,-->王小强,小李,小张,
    public String getUserNameByUserId(String userId){
        List<Users> list = getUserListByUserIdArray(userId.split(","));
        String name = "";
        for (int i = 0; i < list.size(); i++) {
            name += list.get(i).getUserName()+",";
        }
        return name;
    }

    private List<Users> getUserListByUserIdArray(String[] userIds) {
        List<Users> list = new ArrayList<>();
        for (int i = 0; i < userIds.length; i++) {
            list.add(getUserByUserId(Integer.valueOf(userIds[i])));
        }
        return list;
    }
}
