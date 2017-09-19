package com.bill.record.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bill.record.R;
import com.bill.record.adapter.base.SimpleBaseAdapter;
import com.bill.record.business.AccountBookBusiness;
import com.bill.record.entity.AccountBook;

import java.util.List;


/**
 * Created by Wookeibun on 2017/7/14.
 */

public class AccountBookAdapter extends SimpleBaseAdapter {

    private final AccountBookBusiness accountBookBusiness;

    public AccountBookAdapter(Context context) {
        super(context, null);
        accountBookBusiness = new AccountBookBusiness(context);
        setListFromBusiness();
    }

    private void setListFromBusiness() {
        List<AccountBook> list = accountBookBusiness.getNotHideAccountBook();
        setList(list);
    }

    public void updateList(){
        setListFromBusiness();
        updateDisplay();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.account_book_list_item, null);
            holder = new Holder();
            holder.account_book_item_icon_iv = (ImageView) convertView.
                    findViewById(R.id.account_book_item_icon_iv);
            holder.account_book_item_name_tv = (TextView) convertView.
                    findViewById(R.id.account_book_item_name_tv);
            holder.account_book_item_total_tv = (TextView) convertView.
                    findViewById(R.id.account_book_item_total_tv);
            holder.account_book_item_money_tv = (TextView) convertView.
                    findViewById(R.id.account_book_item_money_tv);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        AccountBook accountBook = (AccountBook) datas.get(position);
        if (accountBook.getIsDefault() == 1) {
            holder.account_book_item_icon_iv.setImageResource(
                    R.drawable.account_book_default);
        }else {
            holder.account_book_item_icon_iv.setImageResource(
                    R.drawable.account_book_icon);
        }
        holder.account_book_item_name_tv.setText(
                accountBook.getAccountBookName());
        holder.account_book_item_total_tv.setText("共10笔");
        holder.account_book_item_money_tv.setText("合计消费888元");

        return convertView;
    }

    private class Holder{
        ImageView account_book_item_icon_iv;
        TextView account_book_item_name_tv;
        TextView account_book_item_total_tv;
        TextView account_book_item_money_tv;
    }
}
