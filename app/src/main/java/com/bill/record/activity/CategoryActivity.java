package com.bill.record.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.bill.record.R;
import com.bill.record.adapter.CategoryAdapter;
import com.bill.record.business.CategoryBusiness;
import com.bill.record.entity.Category;
import com.bill.record.view.SlideMenuItem;
import com.bill.record.view.SlideMenuView;

public class CategoryActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener {
    private ExpandableListView category_list_elv;
    private CategoryAdapter categoryAdapter;
    private CategoryBusiness categoryBusinessp;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.category_list);
        //有顺序的
        initVariable();
        initView();
        initListeners();
        initData();
        createSlideMenu(R.array.SlideMenuCategory);
    }


    //初始化变量
    private void initVariable() {
        categoryBusinessp = new CategoryBusiness(this);
    }

    //控件初始化
    private void initView() {
        category_list_elv = findViewById(R.id.category_list_elv);
    }

    //初始化监听
    private void initListeners() {
        registerForContextMenu(category_list_elv);

    }

    //绑定数据
    private void initData() {

        if(categoryAdapter == null){
            categoryAdapter = new CategoryAdapter(this);
            category_list_elv.setAdapter(categoryAdapter);
        }else{
            categoryAdapter.clear();//数据源清空
            categoryAdapter.updateList();//刷新
        }
        setTitle();

    }

    private void setTitle(){
        int count = categoryBusinessp.getNotHideCount();//包括主类和子类的总数
        setTopBarTitle(getString(R.string.title_category,
                new Object[]{count}));
    }


    //修改删除
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //得到菜单信息
        ExpandableListView.ExpandableListContextMenuInfo elcmi = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
        //获取菜单的位置信息
        long position = elcmi.packedPosition;
        //根据位置信息的到组/子的类型
        int type = ExpandableListView.getPackedPositionType(position);
        //通过位置信息得到组的位置
        int groupPosition = ExpandableListView.getPackedPositionGroup(position);
        switch (type){
            case ExpandableListView.PACKED_POSITION_TYPE_GROUP://是组
                //根据组位置取得实体
                category = (Category) categoryAdapter.getGroup(groupPosition);
                break;
            case ExpandableListView.PACKED_POSITION_TYPE_CHILD://是子
                //先获取子位置
                int childPosition = ExpandableListView.getPackedPositionChild(position);
                //再获取某组下的某子位置的实体
                category = (Category) categoryAdapter.getChild(groupPosition,childPosition);
                break;
        }
        menu.setHeaderIcon(R.drawable.category_small_icon);
        if(category != null){
            menu.setHeaderTitle(category.getCategoryName());
        }
        createContextMenu(menu);
        //添加"统计类别"菜单项
        menu.add(0,3,0,R.string.category_total);
        //如果主类下边有子类,就不允许删除
        if(categoryAdapter.getChildrenCount(groupPosition) != 0 && category.getParentId() == 0){
            menu.findItem(2).setEnabled(false);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case 1://修改
                intent = new Intent(this,CategoryAddOrEditActivity.class);
                intent.putExtra("category",category);
                startActivityForResult(intent,1);
                break;
            case 2://这个删除是逻辑(隐藏了)删除不是物理删除
                delete(category);
                break;
            case 3://统计
                break;
        }
        return super.onContextItemSelected(item);
    }


    //删除
    private void delete(Category category) {
        //替换
        String msg = getString(
                R.string.dialog_message_account_Book_delete,
                new Object[]{category.getCategoryName()});
        showAlertDialog(R.string.dialog_title_delete,msg,
                new OnDeleteClickListener());
    }

    private class OnDeleteClickListener implements
            DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            boolean result = categoryBusinessp.hideCategoryByPath(
                    category.getPath());
            if(result){
                initData();
            }else{
                showMsg(getString(R.string.tips_delete_fail));
            }
        }
    }

    public void onSlideMenuItemClick(SlideMenuItem item) {
        slideMenuToggle();
        if(item.getItemId() == 0){//新建
            Intent intent = new Intent(this,CategoryAddOrEditActivity.class);
            startActivityForResult(intent,1);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initData();
        super.onActivityResult(requestCode, resultCode, data);
    }


}
