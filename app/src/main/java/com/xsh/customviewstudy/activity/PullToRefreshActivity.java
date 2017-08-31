package com.xsh.customviewstudy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.xsh.customviewstudy.R;
import com.xsh.customviewstudy.view.PullToRefreshLayout;

public class PullToRefreshActivity extends Activity
        implements PullToRefreshLayout.PullToRefreshListener {

    private ListView mListView;
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] items = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O" };

    private Handler handler = new Handler();
    private PullToRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);
        mListView = (ListView) findViewById(R.id.list_view);
        refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshable_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        mListView.setAdapter(adapter);

        refreshLayout.setPullToRefreshistener(this);
    }

    @Override
    public void onRefresh() {

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.finishPullToRefresh();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
