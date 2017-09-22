package com.clock.zc.punchtheclock.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clock.zc.punchtheclock.R;
import com.clock.zc.punchtheclock.base.BaseActivity;
import com.clock.zc.punchtheclock.bean.ClockBean;
import com.clock.zc.punchtheclock.util.Content;
import com.clock.zc.punchtheclock.util.TimeUtil;
import com.clock.zc.punchtheclock.view.CircleRefresh.CircleRefreshLayout;
import com.clock.zc.punchtheclock.view.MyDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

@ContentView(R.layout.activity_history)
public class HistoryActivity extends BaseActivity implements OnRefreshListener{
    @ViewInject(R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(R.id.recyclerview)
    private RecyclerView recyclerview;
    @ViewInject(R.id.title)
    private TextView title;

    private HistoryAdapter adapter;
    private List<ClockBean> cList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title.setText("历史打卡记录");
        if (cList == null) cList = new ArrayList<ClockBean>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
//        adapter = new HistoryAdapter(cList);
//        recyclerview.setAdapter(adapter);
        recyclerview.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
//        main_swipe.setColorSchemeColors(Color.WHITE, Color.WHITE);
//        main_swipe.setWaveColor(Color.parseColor("#00aeFF"));
////        main_swipe.setRefreshing(true);
//        main_swipe.setOnRefreshListener(this);

        refreshLayout.setEnableHeaderTranslationContent(true);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setPrimaryColorsId(R.color.background, R.color.white);
        refrsh();
    }
    private void refrsh(){
        cList = liteOrm.query(ClockBean.class);
        if(cList!=null){
//            mRefreshLayout.autoRefresh();
            refreshLayout.finishRefresh();
            adapter = new HistoryAdapter(cList);
            recyclerview.setAdapter(adapter);
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        refrsh();
    }

    @Event({R.id.back})
    private void toggleEvent(View v){
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>
    {
        List<ClockBean> mDatas;
        public HistoryAdapter(List<ClockBean> mDatas){
            this.mDatas = mDatas;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    HistoryActivity.this).inflate(R.layout.item_history, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            long time = mDatas.get(position).getTime();
            holder.id_num.setText(TimeUtil.getDataYear(time));
            holder.clock.setText("打卡时间："+TimeUtil.getDataHour(time));
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView id_num;
            TextView clock;

            public MyViewHolder(View view)
            {
                super(view);
                id_num = (TextView) view.findViewById(R.id.id_num);
                clock = (TextView) view.findViewById(R.id.clock);
            }
        }
    }

}
