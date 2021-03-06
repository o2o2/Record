package com.bill.record.business;

import android.content.ContentValues;
import android.content.Context;

import com.bill.record.database.dao.AccountBookDAO;
import com.bill.record.business.base.BaseBusiness;
import com.bill.record.entity.AccountBook;

import java.util.List;

/**
 * Created by Wookeibun on 2017/7/18.
 */

public class AccountBookBusiness extends BaseBusiness {
    private AccountBookDAO accountBookDAO;


    public AccountBookBusiness(Context context) {
        super(context);
        accountBookDAO = new AccountBookDAO(context);
    }

    //添加
    public boolean insertAccountBook(AccountBook accountBook) {
        return insertOrUpdateAccountBook(accountBook, true);
    }

    //更新
    public boolean updateAccountBook(AccountBook accountBook) {
        return insertOrUpdateAccountBook(accountBook, false);
    }

    //添加和更新
    public boolean insertOrUpdateAccountBook(AccountBook accountBook, boolean isInsert) {
        accountBookDAO.beginTransaction();
        try {
            boolean result;
            if (isInsert) {
                result = accountBookDAO.insertAccountBook(accountBook);
            } else {
                String condition = "accountBookId=" + accountBook.getAccountBookId();
                result = accountBookDAO.updateAccountBook(condition, accountBook);
            }
            boolean result2 = true;
            if (accountBook.getIsDefault() == 1 && result) {
                result2 = setIsDefault(accountBook.getAccountBookId());
            }
            if (result && result2) {
                accountBookDAO.setTransactionSuccessful();
                return true;
            } else {
                return false;
            }
//            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            accountBookDAO.endTransaction();
        }
    }

    public Boolean setIsDefault(int accountBookId) throws Exception {
        //将原来的默认账本设为非默认


        String condition = "isDefault=1";
        ContentValues contentValues = new ContentValues();
        contentValues.put("isDefault", 0);
        boolean result = accountBookDAO.updateAccountBook(condition, contentValues);

        //再将传入的id的账本设为默认账本
        condition = "accountBookId=" + accountBookId;
        contentValues.clear();
        contentValues.put("isDefault", 1);
        boolean result2 = accountBookDAO.updateAccountBook(condition, contentValues);
        if (result && result2) {
            return true;
        } else {
            return false;
        }

    }

    public boolean deleteAccountBookByAccountBookId(int accountBookId) {
        accountBookDAO.beginTransaction();

        try {
            String condition = "and accountBookId" + accountBookId;
            boolean result = accountBookDAO.deleteAccountBook(condition);
            boolean result2 = true;
            if (result) {
                PayoutBusiness payoutBusiness = new PayoutBusiness(context);
                result2 = payoutBusiness.deletePayoutByAccountBookId(accountBookId);
            }
            if (result && result2) {
                accountBookDAO.setTransactionSuccessful();
                return true;
            } else {
                return false;
            }
        } finally {
            accountBookDAO.endTransaction();
        }

    }


    public List<AccountBook> getAccountBook(String condition) {
        return accountBookDAO.getAccountBook(condition);
    }

    public AccountBook getAccountBookByAccountBookId(int accountBookId) {
        List<AccountBook> list = accountBookDAO.getAccountBook("and AccountBookId=" + accountBookId);
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<AccountBook> getNotHideAccountBook() {
        return accountBookDAO.getAccountBook("and state = 1");
    }

    public boolean isExistAccountBookByaccountBookName(String accountBookName, Integer accountBookId) {
        String condition = " and accountBookname= '" + accountBookName + "'";
        if (accountBookId != null) {
            condition += " and accountBookId <> " + accountBookId;
        }
        List<AccountBook> list = accountBookDAO.getAccountBook(condition);
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //根据用户的id隐藏用户的方法
    public boolean hideAccountBookbyAccountBookId(int accountBookId) {
        String condition = " accountBookId=" + accountBookId;
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 0);
        return accountBookDAO.updateAccountBook(condition, contentValues);
    }

    //获取默认账本
    public AccountBook getDefaultAccountBook() {
        List<AccountBook> list = accountBookDAO.getAccountBook(" and isDefault=1");
        if (list != null && list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }

    }

    //根据ID去获取账本的名称
    public String getAccountBookNameByAccountId(int accountBookId) {
        String condition = " and accountBookId=" + accountBookId;
        List<AccountBook> list = accountBookDAO.getAccountBook(condition);
        return list.get(0).getAccountBookName();
    }
}
