package com.bill.record.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bill.record.R;
import com.bill.record.business.CategoryBusiness;
import com.bill.record.entity.Category;

import java.util.List;

/**
 * Created by Wookeibun on 2017/7/24.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List list;
    private CategoryBusiness categoryBusiness;

    public CategoryAdapter(Context context) {
        this.context = context;
        categoryBusiness = new CategoryBusiness(context);
        list = categoryBusiness.getNotHideRootCategory();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        Category parentCategory = (Category) getGroup(i);
        //根据父类的ID获取子类的总数
        int count = categoryBusiness.getNotHideCountByParentId(parentCategory.getCategoryId());
        return count;
    }

    @Override
    public Object getGroup(int i) {
        return (Category) list.get(i);
    }

    //获取子条目
    @Override
    public Object getChild(int i, int i1) {
        Category parentCategory = (Category) getGroup(i);
        List<Category> childList = categoryBusiness.getNotHideCategoryListByParentId(
                parentCategory.getCategoryId()
        );
        return childList.get(i1);
    }


    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.category_group_list_item, null);
            groupHolder = new GroupHolder();
            groupHolder.category_group_name_tv = convertView.findViewById(R.id.category_group_name_tv);
            groupHolder.category_group_count_tv = convertView.findViewById(R.id.category_group_count_tv);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        Category category = (Category) getGroup(groupPosition);
        groupHolder.category_group_name_tv.setText(category.getCategoryName());
        int count = getChildrenCount(groupPosition);
        groupHolder.category_group_count_tv.setText(context.getString(R.string.textview_text_childer_category
                , new Object[]{count}));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.category_children_list_item, null);
            childHolder = new ChildHolder();
            childHolder.category_children_name_tv = convertView.findViewById(R.id.category_children_name_tv);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        Category category = (Category) getChild(groupPosition, childPosition);
        childHolder.category_children_name_tv.setText(category.getCategoryName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void clear() {
        list.clear();
    }

    public void updateList() {
        setListFromBusiness();
        notifyDataSetChanged();
    }

    private void setListFromBusiness() {
        List<Category> list = categoryBusiness.getNotHideRootCategory();
        setList(list);
    }

    public void setList(List<Category> list) {
        this.list = list;
    }


    private class GroupHolder {
        TextView category_group_name_tv;
        TextView category_group_count_tv;
    }

    private class ChildHolder {
        TextView category_children_name_tv;
    }
}
