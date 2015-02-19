package com.sedlex.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sedlex.R;
import com.sedlex.object.Debate;
import com.sedlex.tools.DebatesAdapter;
import com.sedlex.tools.LawsAdapter;

import java.util.ArrayList;

public class DebatesActivity extends ActionBarActivity {

    public static final String ARG_TITLE = "ARG_TITLE";
    public static final String ARG_LAWID = "ARG_LAWID";
    public static final int ARG_INT_DEFAULT = 0;

    private RecyclerView listView;
    private DebatesAdapter adapter;
    private ArrayList<Debate> debatesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debates);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // RETRIEVE AND SET PASSED DATA
        String lawTitle = getIntent().getStringExtra(ARG_TITLE);
        setTitle(lawTitle);
        int lawId = getIntent().getIntExtra(ARG_LAWID, ARG_INT_DEFAULT);

        //DEBATES LIST FIRST SETUP
        listView = (RecyclerView) findViewById(R.id.debates_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        setDebatesList();

        adapter = new DebatesAdapter(this, R.layout.row_debates, debatesList);
        listView.setAdapter(adapter);
    }

    private void setDebatesList(){
        debatesList = new ArrayList<>();
        for(int i=0;i<10;i++){
            Debate curDebate = new Debate();
            debatesList.add(curDebate);
        }
    }
}
