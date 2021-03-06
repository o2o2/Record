package com.bill.record.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.bill.record.R;
import com.bill.record.business.AccountBookBusiness;
import com.bill.record.business.StatisticsBusiness;
import com.bill.record.entity.AccountBook;
import com.bill.record.view.SlideMenuItem;
import com.bill.record.view.SlideMenuView;

/**
 * Created by Wookeibun on 2017/8/4.
 */

public class StatisticsActivity extends FrameActivity implements
        SlideMenuView.OnSlideMenuListener {
    private TextView statistics_result_tv;
    private StatisticsBusiness statisticsBusiness;
    private AccountBookBusiness accountBookBusiness;
    private AccountBook accountBook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appendMainBody(R.layout.activity_statistics);
        initVariable();
        initView();
        initListeners();
        initData();
        setTitle();
        createSlideMenu(R.array.SlideMenuStatistics);
    }

    private void setTitle(){
        String title = getString(R.string.title_statistics,
                new Object[]{accountBook.getAccountBookName()});
        setTopBarTitle(title);
    }

    public void initVariable(){
        statisticsBusiness = new StatisticsBusiness(this);
        accountBookBusiness = new AccountBookBusiness(this);
        accountBook = accountBookBusiness.getDefaultAccountBook();
    }

    public void initView(){
        statistics_result_tv = (TextView) findViewById(R.id.statistics_result_tv);
    }

    public void initListeners(){
    }

    public void initData(){
        showProgressDialog(R.string.dialog_title_statistics_progress,
                R.string.dialog_waiting_statistics_progress);
        //开一个线程
        new BindDataThread().start();
    }

    private class BindDataThread extends Thread{
        @Override
        public void run() {
            String result = statisticsBusiness.
                    getPayoutUserIdByAccountBookId(
                            accountBook.getAccountBookId());
            Message msg = handler.obtainMessage();
            msg.obj = result;
            msg.what = 1;
            handler.sendMessage(msg);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String result = (String) msg.obj;
                    statistics_result_tv.setText(result);
                    dismissProgressDialog();
                    break;
            }
        }
    };

    @Override
    public void onSlideMenuItemClick(SlideMenuItem item) {
        slideMenuToggle();
        if (item.getItemId() == 1){//导出数据
            exportData();
        }
    }

    private void exportData() {
        String result = "";
        try {
            result = statisticsBusiness.exportStatistics(
                    accountBook.getAccountBookId());
        } catch (Exception e) {
            e.printStackTrace();
            result = getString(R.string.export_data_fail);
        }
        showMsg(result);
    }
}
