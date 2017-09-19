package com.bill.record.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bill.record.R;
import com.bill.record.adapter.base.SimpleBaseAdapter;
import com.bill.record.business.UserBusiness;
import com.bill.record.entity.Users;

import java.util.List;


/**
 * Created by Wookeibun on 2017/7/14.
 */

public class UserAdapter extends SimpleBaseAdapter{

    UserBusiness userBusiness;

    public UserAdapter(Context context) {
        super(context, null);
        userBusiness = new UserBusiness(context);
        setListFromBusiness();
    }

    private void setListFromBusiness() {
        List<Users> list = userBusiness.getNotHideUsers();
        setList(list);
    }
    public void updateList(){
        setListFromBusiness();
        updateDisplay();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder ;
        if (view ==null){
            view = LayoutInflater.from(context).inflate(R.layout.user_list_item,null);
            holder = new Holder();
            holder.user_item_icon_iv = view.findViewById(R.id.user_item_icon_iv);
            holder.user_item_name = view.findViewById(R.id.user_item_name_tv);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        Users users = (Users) datas.get(i);
        holder.user_item_icon_iv.setImageResource(R.drawable.grid_user);
        holder.user_item_name.setText(users.getUserName());
        return view;
    }
    private class Holder{
    ImageView user_item_icon_iv;
        TextView user_item_name;
    }
}
