package com.sedlex.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.sedlex.R;

public class LawDetailActivity extends ActionBarActivity {

    public static final String ARG_ITEM = "ARG_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String title = (String) getIntent().getStringExtra(ARG_ITEM);
        setTitle(title);

        //TextView titleView = (TextView) findViewById(R.id.law_detail_title);

        //titleView.setText(title);
    }


}
