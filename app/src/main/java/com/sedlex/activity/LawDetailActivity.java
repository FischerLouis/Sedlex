package com.sedlex.activity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sedlex.R;
import com.sedlex.tools.Constants;
import com.sedlex.tools.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LawDetailActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String ARG_TITLE = "ARG_TITLE";
    public static final String ARG_PROGRESS = "ARG_PROGRESS";
    public static final String ARG_LAWID = "ARG_LAWID";
    public static final int ARG_INT_DEFAULT = 0;

    private String lawContent;
    private TextView lawContentView;
    private boolean extendedContent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // RETRIEVE AND SET PASSED DATA
        String title = (String) getIntent().getStringExtra(ARG_TITLE);
        setTitle(title);
        int progress = (int) getIntent().getIntExtra(ARG_PROGRESS, ARG_INT_DEFAULT);
        Log.d("DEBUG", "progess:" + progress);
        int lawId = (int) getIntent().getIntExtra(ARG_LAWID, ARG_INT_DEFAULT);
        Log.d("DEBUG","id:"+lawId);

        //RETRIEVE VIEWS
        lawContentView = (TextView) findViewById(R.id.detail_content);

        //GET DYNAMIC DATA (CONTENT)
        if (lawId != 0)
            updateLawDetailsContent(lawId);

        //SET LISTENER
        lawContentView.setOnClickListener(this);
    }

    private void updateLawDetailsContent(int lawId){

        final String STATIC_LAW = "law";
        final String STATIC_LAW_CONTENT = "content";

        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();

        String urlLawDetails = Constants.URL_LAW_DETAILS+lawId;

        JsonObjectRequest getLawDetailsReq = new JsonObjectRequest(Request.Method.GET, urlLawDetails, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject lawObject = response.getJSONObject(STATIC_LAW);
                            lawContentView.setText(lawObject.getString(STATIC_LAW_CONTENT));
                            updateContentView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );
        queue.add(getLawDetailsReq);

    }

    private void updateContentView(){
        if(extendedContent){
            lawContentView.setMaxLines(8);
            extendedContent = false;
        }
        else{
            lawContentView.setMaxLines(Integer.MAX_VALUE);
            extendedContent = true;
        }
    }

    @Override
    public void onClick(View view) {
        updateContentView();
    }
}
