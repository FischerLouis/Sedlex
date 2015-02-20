package com.sedlex.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sedlex.R;
import com.sedlex.object.Debate;
import com.sedlex.tools.Constants;
import com.sedlex.tools.DebatesAdapter;
import com.sedlex.tools.LawsAdapter;
import com.sedlex.tools.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class DebatesActivity extends ActionBarActivity {

    public static final String ARG_TITLE = "ARG_TITLE";
    public static final String ARG_LAWID = "ARG_LAWID";
    public static final int ARG_INT_DEFAULT = 0;

    private RecyclerView listView;
    private DebatesAdapter adapter;
    private TextView emptyView;
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

        //GET VIEWS
        emptyView = (TextView) findViewById(R.id.debates_empty);

        //DEBATES LIST FIRST SETUP
        listView = (RecyclerView) findViewById(R.id.debates_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(linearLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());

        //setDebatesList();
        updateDebatesList(lawId);

        adapter = new DebatesAdapter(this, R.layout.row_debates, debatesList);
        listView.setAdapter(adapter);
    }

    private void updateDebatesList(int lawId){
        //VOLLEY QUEUE
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        //URL TO LOAD
        String urlDebates = Constants.URL_LAW_DETAILS+lawId+Constants.URL_GET_DEBATES;
        //CACHE CHECK
        if(queue.getCache().get(urlDebates)!=null){
            Log.d("VOLLEY_VIEW_3","CACHE");
            //GET JSON FROM CACHE
            try {
                String cachedResponse = new String(queue.getCache().get(urlDebates).data);
                JSONObject jsonCached = new JSONObject(cachedResponse);
                updateFromJSON(jsonCached);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("VOLLEY_VIEW_3","NO CACHE");
            //GET JSON FROM SERVER
            JsonObjectRequest getDebatesReq = new JsonObjectRequest(Request.Method.GET, urlDebates, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                updateFromJSON(response);
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
            queue.add(getDebatesReq);
        }
    }

    private void updateFromJSON(JSONObject jsonLoaded) throws JSONException {
        final String STATIC_DEBATES = "debateInterventions";
        final String STATIC_DEBATES_DEPUTY_NAME = "name";
        final String STATIC_DEBATES_TEXT = "text";

        JSONArray debatesArray = jsonLoaded.getJSONArray(STATIC_DEBATES);
        if(debatesArray.length() != 0) {
            debatesList = new ArrayList<>();
            for (int i = 0; i < debatesArray.length(); i++) {
                Debate curDebate = new Debate();
                curDebate.setDeputyName(debatesArray.getJSONObject(i).getString(STATIC_DEBATES_DEPUTY_NAME));
                curDebate.setText(debatesArray.getJSONObject(i).getString(STATIC_DEBATES_TEXT));
                debatesList.add(curDebate);
            }
            emptyView.setVisibility(View.GONE);
        }
        else{
            emptyView.setVisibility(View.VISIBLE);
            Log.d("JSON","EMPTY JSON");
        }
    }

}
