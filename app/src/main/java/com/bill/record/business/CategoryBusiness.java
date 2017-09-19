package com.bill.record.business;

import android.content.ContentValues;
import android.content.Context;
import android.widget.ArrayAdapter;

import com.bill.record.R;
import com.bill.record.business.base.BaseBusiness;
import com.bill.record.database.dao.CategoryDAO;
import com.bill.record.entity.Category;

import java.util.List;

/**
 * Created by Wookeibun on 2017/7/18.
 */

public class CategoryBusiness extends BaseBusiness {
    private CategoryDAO categoryDAO;

    public CategoryBusiness(Context context) {
        super(context);
        categoryDAO = new CategoryDAO(context);
    }

    //获取所有大类的列表
    public List<Category> getNotHideRootCategory(){
        return categoryDAO.getCategory(" and parentId=0 and " +
                "state=1");
    }

    //根据父ID获取未隐藏的子类总数
    public int getNotHideCountByParentId(int parentId){
        return categoryDAO.getCount(" and parentId="+parentId
                +" and state=1");
    }

    //根据父ID获取子类列表
    public List<Category> getNotHideCategoryListByParentId(int parentId){
        return categoryDAO.getCategory(" and parentId="+
                parentId+" and state=1");
    }

    //获取未隐藏的类别总数
    public int getNotHideCount(){
        return categoryDAO.getCount(" and state=1");
    }

    public ArrayAdapter<Category> getRootCategoryArrayAdapter(){
        List<Category> list = getNotHideRootCategory();
        list.add(0, new Category(0, context.getString(
                R.string.spinner_please_choose)));
        ArrayAdapter arrayAdapter = new ArrayAdapter(context,
                R.layout.simmple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    public boolean insertCategory(Category category) {
        categoryDAO.beginTransaction();
        try {
            boolean result = categoryDAO.insertCategory(category);
            boolean result2 = true;
            // 根据父类别的ID查出这个类的父类
            Category parentCategory = getCategoryByCategoryId(
                    category.getParentId());
            String path;
            if (parentCategory != null){//如果有父类别
                //父类别的路径+当前类别的路径
                path = parentCategory.getPath() + category.getCategoryId()+".";
            }else {//当前类就是父类
                path = category.getCategoryId()+".";
            }
            category.setPath(path);
            result2 = editCategory(category);
            if (result && result2){
                categoryDAO.setTransactionSuccessful();
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            categoryDAO.endTransaction();
        }
    }

    public Category getCategoryByCategoryId(int categoryId){
        List<Category> list = categoryDAO.getCategory(" " +
                "and categoryId="+categoryId);
        if(list != null && list.size()==1){
            return list.get(0);
        }else{
            return null;
        }
    }

    private boolean editCategory(Category category) {
        String condition = "categoryId=" + category.getCategoryId();
        boolean result = categoryDAO.updateCategory(condition, category);
        return result;
    }

    public boolean updateCategory(Category category) {
        categoryDAO.beginTransaction();
        try {
            boolean result = editCategory(category);
            boolean result2 = true;
            // 根据父类别的ID查出这个类的父类
            Category parentCategory = getCategoryByCategoryId(
                    category.getParentId());
            String path;
            if (parentCategory != null){//如果有父类别
                //父类别的路径+当前类别的路径
                path = parentCategory.getPath() + category.getCategoryId()+".";
            }else {//当前类就是父类
                path = category.getCategoryId()+".";
            }
            category.setPath(path);
            result2 = editCategory(category);
            if (result && result2){
                categoryDAO.setTransactionSuccessful();
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            categoryDAO.endTransaction();
        }
    }

    //通过路径隐藏类别
    public boolean hideCategoryByPath(String path) {
        // path like '1.%'
        String condition = " path like '" + path + "%'";
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);
        boolean result = categoryDAO.updateCategory(
                condition, contentValues);
        return result;
    }

    public ArrayAdapter getAllCategoryArrayAdapter(){
        List<Category> list = getNotHideCategory();
        ArrayAdapter arrayAdapter = new ArrayAdapter(context,
                R.layout.common_auto_complete, list);
        return arrayAdapter;
    }

    private List<Category> getNotHideCategory() {
        return categoryDAO.getCategory(" and state=1");
    }
}