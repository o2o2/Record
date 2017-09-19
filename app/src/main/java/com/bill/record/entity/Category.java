package com.bill.record.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by Wookeibun on 2017/7/17.
 */

public class Category implements Serializable{
    //主键
    private int categoryId;
    //用户名称
    private String categoryName;
    //添加日期
    @Override
    public String toString() {
        return categoryName;
    }private Date createDate = new Date();
    //状态 0失效,1启用,默认启用
    private int state = 1;
    //父类ID
    private int parentId = 0;

    public Category() {
    }

    public Category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //路径
    private String path;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
