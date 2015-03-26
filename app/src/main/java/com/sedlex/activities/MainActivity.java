package com.sedlex.activities;

import android.graphics.drawable.AnimationDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sedlex.objects.Law;
import com.sedlex.adapters.LawsAdapter;
import com.sedlex.tools.MyContentManager;
import com.sedlex.R;
import com.sedlex.tools.InfiniteScrollListener;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private MyContentManager myContentManager = new MyContentManager(this);
    private RecyclerView listView;
    private LawsAdapter adapter;
    private boolean refresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Sedlex icon in ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.sedlex_icon_margin);

        //LOADING ANIMATION
        ImageView loadingView = (ImageView) findViewById(R.id.loading_view);
        loadingView.setBackgroundResource(R.drawable.loading_animation);
        AnimationDrawable loadingAnimation = (AnimationDrawable) loadingView.getBackground();
        loadingAnimation.start();

        //LISTVIEW FIRST SETUP
        listView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        myContentManager.updateList(0, false);

        //SWIPETOREFRESH LISTENER SETUP
        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        MyRefreshListener refreshListener = new MyRefreshListener(listView);
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);

        //ONSCROLL LISTENER SETUP
        InfiniteScrollListener infiniteScrollListener = new InfiniteScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.v("DEBUG", "page= "+current_page);
                myContentManager.updateList(current_page, false);
            }
        };
        listView.setOnScrollListener(infiniteScrollListener);
    }

    public void setList(ArrayList<Law> lawsList, int page){
        //UPDATING DATA ACCORDING TO PAGES / UPDATE
        if(!refresh) {
            if (page == 0) {
                adapter = new LawsAdapter(this, R.layout.row_lawslist, lawsList);
                findViewById(R.id.loading_view).setVisibility(View.GONE);
                listView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
        else{
            adapter.notifyDataSetChanged();
            SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
            mSwipeRefreshLayout.setRefreshing(false);
            refresh = false;
        }
    }

    // CUSTOM REFRESH LISTENER
    private class MyRefreshListener implements SwipeRefreshLayout.OnRefreshListener{

        RecyclerView listView;

        public MyRefreshListener(RecyclerView listView){
            this.listView = listView;
        }

        @Override
        public void onRefresh() {
            refresh = true;
            myContentManager.updateList(0, true);
        }
    }
}
