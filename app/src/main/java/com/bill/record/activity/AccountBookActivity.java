package com.bill.record.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bill.record.R;
import com.bill.record.adapter.AccountBookAdapter;
import com.bill.record.business.AccountBookBusiness;
import com.bill.record.entity.AccountBook;
import com.bill.record.uitls.RegexTools;
import com.bill.record.view.SlideMenuItem;
import com.bill.record.view.SlideMenuView;

public class AccountBookActivity extends FrameActivity implements SlideMenuView.OnSlideMenuListener {
    private ListView account_book_list_lv;
    private AccountBookAdapter accountBookAdapter;
    private AccountBookBusiness accountBookBusiness;
    private AccountBook accountBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.account_book_list);
        initVariable();
        initView();
        initListeners();
        initData();
        createSlideMenu(R.array.SlideMenuAccountBook);
    }

    private void initData() {
        if (accountBookAdapter == null) {
            accountBookAdapter = new AccountBookAdapter(this);
            account_book_list_lv.setAdapter(accountBookAdapter);
        } else {
            accountBookAdapter.clear();
            accountBookAdapter.updateList();
        }
        setTitle();

    }

    protected void setTitle() {
        setTopBarTitle(getString(R.string.title_account_book, new Object[]{accountBookAdapter.getCount()}));

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //得到菜单信息
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        ListAdapter listAdapter = account_book_list_lv.getAdapter();
        accountBook = (AccountBook) listAdapter.getItem(acmi.position);
        menu.setHeaderIcon(R.drawable.account_book_small_icon);
        menu.setHeaderTitle(accountBook.getAccountBookName());
        createContextMenu(menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1://修改
                showAccountBookOrEditDialog(accountBook);
                break;
            case 2://删除
                delete();
                break;


        }
        return super.onContextItemSelected(item);
    }

    private void delete() {
        String msg = getString(R.string.dialog_message_account_book_delete, new Object[]{accountBook.getAccountBookName()});

        showAlertDialog(R.string.dialog_title_delete, msg, new OnDeleteClickListener());

    }

    private void initListeners() {
        registerForContextMenu(account_book_list_lv);
    }

    private void initView() {
        account_book_list_lv = findViewById(R.id.account_book_lv);

    }

    private void initVariable() {
        accountBookBusiness = new AccountBookBusiness(this);

    }

    @Override
    public void onSlideMenuItemClick(SlideMenuItem item) {
        showMsg(item.getTitle());
        if (item.getItemId() == 0) {
            showAccountBookOrEditDialog(null);
        }
    }

    private void showAccountBookOrEditDialog(AccountBook accountBook) {
        View view = getInflater().inflate(R.layout.account_book_add_or_edit, null);
        EditText account_book_name_et = view.findViewById(R.id.account_book_name_et);
        CheckBox account_book_check_default_cb = view.findViewById(R.id.account_book_check_default_cb);
        String title;
        if (accountBook == null) {
            title = getString(R.string.dialog_title_account_book, new Object[]{getString(R.string.title_add)});
        } else {
            account_book_name_et.setText(accountBook.getAccountBookName());
            title = getString(R.string.dialog_title_account_book, new Object[]{getString(R.string.title_edit)});
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setView(view)
                .setIcon(R.drawable.grid_account_book)
                .setNeutralButton(getString(R.string.button_text_save),
                        new OnAddOrEditaccountBooklistener(accountBook, account_book_name_et, account_book_check_default_cb, true))
                .setNegativeButton(getString(R.string.button_text_cancel),
                        new OnAddOrEditaccountBooklistener(null, null, null, false))
                .show();

    }

    private class OnAddOrEditaccountBooklistener implements DialogInterface.OnClickListener {
        private AccountBook accountBook;
        private EditText accountBookNameET;
        private CheckBox accountBookDefaultCB;
        private boolean isSaveButton;

        public OnAddOrEditaccountBooklistener(AccountBook accountBook, EditText accountBookNameET, CheckBox accountBookDefaultCB, boolean isSaveButton) {
            this.accountBook = accountBook;
            this.accountBookNameET = accountBookNameET;
            this.accountBookDefaultCB = accountBookDefaultCB;
            this.isSaveButton = isSaveButton;
        }

        @Override
        public void onClick(DialogInterface dialog, int i) {
            if (!isSaveButton) {
                setAlertDialogIsClose(dialog, true);
                return;
            }
            if (accountBook == null) {
                accountBook = new AccountBook();
            }
            String accountBookName = accountBookNameET.getText().toString().trim();
            boolean checkResult = RegexTools.isChineseEnglishNum(accountBookName);

            if (!checkResult) {
                showMsg(getString(R.string.check_text_chinese_english_num, new Object[]{accountBookNameET.getHint()}));
                setAlertDialogIsClose(dialog, false);
                return;
            } else {
                setAlertDialogIsClose(dialog, true);
            }
            checkResult = accountBookBusiness.isExistAccountBookByaccountBookName(accountBookName, accountBook.getAccountBookId());
            if (checkResult) {
                showMsg(getString(R.string.chech_text_account_book_exist));
                setAlertDialogIsClose(dialog, false);
                return;
            } else {
                setAlertDialogIsClose(dialog, true);
            }
            accountBook.setAccountBookName(accountBookName);
            //是否为默认账本的判断
            if (accountBookDefaultCB.isChecked()) {
                accountBook.setIsDefault(1);
            } else {
                accountBook.setIsDefault(0);
            }
            boolean result = false;
            if (accountBook.getAccountBookId() == 0) {
                result = accountBookBusiness.insertAccountBook(accountBook);

            } else {
                result = accountBookBusiness.updateAccountBook(accountBook);
            }
            if (result) {
                initData();
            } else {
                showMsg(getString(R.string.tips_add_fail));
            }
        }
    }

    private class OnDeleteClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            //先删除消费记录,在删除账本,要保证有且只有一个账本
            boolean result = accountBookBusiness.hideAccountBookbyAccountBookId(accountBook.getAccountBookId());

            if (result) {
                initData();
            } else {
                showMsg(getString(R.string.tips_add_fail));
            }
        }
    }
}
