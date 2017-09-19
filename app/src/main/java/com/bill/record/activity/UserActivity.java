package com.bill.record.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bill.record.R;
import com.bill.record.adapter.UserAdapter;
import com.bill.record.business.UserBusiness;
import com.bill.record.entity.Users;
import com.bill.record.uitls.RegexTools;
import com.bill.record.view.SlideMenuItem;
import com.bill.record.view.SlideMenuView;

public class UserActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener {
    private ListView user_list_lv;
    private UserAdapter userAdapter;
    private UserBusiness userBusiness;
    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.user_list);
        initVariable();
        initView();
        initListeners();
        initData();
        createSlideMenu(R.array.SlideMenuUser);
    }

    private void initData() {
        if (userAdapter == null){
            userAdapter = new UserAdapter(this);
            user_list_lv.setAdapter(userAdapter);
        }else{
            userAdapter.clear();
            userAdapter.updateList();
        }
setTitle();

    }
protected void setTitle(){
    setTopBarTitle(getString(R.string.title_user,new Object[]{userAdapter.getCount()}));

}
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //得到菜单信息
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListAdapter listAdapter = user_list_lv.getAdapter();
        users = (Users) listAdapter.getItem(acmi.position);
        menu.setHeaderIcon(R.drawable.user_small_icon);
        menu.setHeaderTitle(users.getUserName());
       createContextMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1 ://修改
                showUserOrEditDialog(users);
                break;
            case 2://删除
                delete();
                break;


        }
        return super.onContextItemSelected(item);
    }

    private void delete() {
        String msg = getString(R.string.dialog_message_user_delete,new Object[]{users.getUserName()});
        showAlertDialog(R.string.dialog_title_delete,msg,new OnDeleteClickListener());

    }

    private void initListeners() {
    registerForContextMenu(user_list_lv);
    }

    private void initView() {
        user_list_lv = findViewById(R.id.user_list_lv);

    }

    private void initVariable() {
        userBusiness = new UserBusiness(this);

    }

    @Override
    public void onSlideMenuItemClick(SlideMenuItem item) {
        showMsg(item.getTitle());
        if (item.getItemId()  == 0){
            showUserOrEditDialog(null);
        }
    }

    private void showUserOrEditDialog(Users users) {
        View view = getInflater().inflate(R.layout.user_add_or_edit, null);
        EditText user_name_et = view.findViewById(R.id.user_name_et);
        String title;
        if (users == null) {
            title = getString(R.string.dialog_title_user, new Object[]{getString(R.string.title_add)});
        } else {
            user_name_et.setText(users.getUserName());
            title = getString(R.string.dialog_title_user, new Object[]{getString(R.string.title_edit)});
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(view)
                .setIcon(R.drawable.grid_user)
                .setNeutralButton(getString(R.string.button_text_save),
                        new OnAddOrEdituserlistener(users, user_name_et, true))
                .setNegativeButton(getString(R.string.button_text_cancel),
                        new OnAddOrEdituserlistener(null, null, false))
                .show();

    }

    private class OnAddOrEdituserlistener implements DialogInterface.OnClickListener {
        private Users users;
        private EditText userNameET;
        private boolean isSaveButton;

        public OnAddOrEdituserlistener(Users users, EditText userNameET, boolean isSaveButton) {
            this.users = users;
            this.userNameET = userNameET;
            this.isSaveButton = isSaveButton;
        }

        @Override
        public void onClick(DialogInterface dialog, int i) {
            if (!isSaveButton) {
                setAlertDialogIsClose(dialog, true);
                return;
            }
            if (users == null) {
                users = new Users();
            }
            String userName = userNameET.getText().toString().trim();
            boolean checkResult = RegexTools.isChineseEnglishNum(userName);
            if (!checkResult) {
                showMsg(getString(R.string.check_text_chinese_english_num, new Object[]{userNameET.getHint()}));
                setAlertDialogIsClose(dialog,false);
                return;
            }else{
                setAlertDialogIsClose(dialog,true);
            }
            checkResult = userBusiness.isExistUserByUserName(userName,users.getUserId());
            if (checkResult){
                showMsg(getString(R.string.chech_text_user_exist));
                setAlertDialogIsClose(dialog,false);
                return;
            }else{
                setAlertDialogIsClose(dialog,true);
            }
            users.setUserName(userName);
            boolean result = false;
            if (users.getUserId() == 0){
                result = userBusiness.insertUser(users);

            }else{
                result = userBusiness.updateUser(users);
            }
            if (result) {
                initData();
            }else{
                showMsg(getString(R.string.tips_add_fail));
            }
        }
    }

    private class OnDeleteClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            boolean result = userBusiness.hideUserByUserId(users.getUserId());
            if (result){
                initData();
            }else{
                showMsg(getString(R.string.tips_add_fail));
            }
        }
    }
}
