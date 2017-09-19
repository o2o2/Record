package com.bill.record.entity;


import java.util.Date;

/**
 * Created by Wookeibun on 2017/7/17.
 */

public class AccountBook {
    //主键
    private int accountbookId;
    //账本名称
    private String accountbookName;
    //添加日期
    private Date createDate = new Date();
    //状态 0失效,1启用,默认启用
    private int state = 1;
    private int isDefault;

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getAccountBookId() {
        return accountbookId;
    }

    public void setAccountBookId(int accountbookId) {
        this.accountbookId = accountbookId;
    }

    public String getAccountBookName() {
        return accountbookName;
    }

    public void setAccountBookName(String accountbookName) {
        this.accountbookName = accountbookName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
