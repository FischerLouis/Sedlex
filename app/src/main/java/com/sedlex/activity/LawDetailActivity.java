package com.sedlex.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sedlex.R;
import com.sedlex.tools.Constants;
import com.sedlex.tools.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class LawDetailActivity extends ActionBarActivity implements View.OnClickListener, View.OnTouchListener {

    public static final String ARG_TITLE = "ARG_TITLE";
    public static final String ARG_PROGRESS = "ARG_PROGRESS";
    public static final String ARG_LAWID = "ARG_LAWID";
    public static final int ARG_INT_DEFAULT = 0;

    public static final int IMAGEVIEW_SMALL_DP = 40;
    public static final int IMAGEVIEW_BIG_DP = 60;

    private TextView lawContentView;
    private TextView approveButton;
    private TextView disapproveButton;
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
        approveButton = (TextView) findViewById(R.id.detail_button_approve);
        disapproveButton = (TextView) findViewById(R.id.detail_button_disapprove);

        //UPDATE PROGRESS VIEWS
        updateProgress(progress);

        //GET DYNAMIC DATA (CONTENT)
        if (lawId != 0)
            updateLawDetailsContent(lawId);

        //SET LISTENER
        lawContentView.setOnClickListener(this);
        approveButton.setOnTouchListener(this);
        disapproveButton.setOnTouchListener(this);
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

    private void updateProgress(int progress){

        ImageView stepOne = (ImageView) findViewById(R.id.detail_step_1);
        ImageView stepTwo = (ImageView) findViewById(R.id.detail_step_2);
        ImageView stepThree = (ImageView) findViewById(R.id.detail_step_3);

        switch (progress){
            case 0:
                stepOne.getLayoutParams().height = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                break;
            case 1:
                stepTwo.setImageResource(R.drawable.process_plt_color);
                stepTwo.getLayoutParams().height = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                break;
            case 2:
                stepThree.setImageResource(R.drawable.process_adoption_color);
                stepThree.getLayoutParams().height = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                break;
            default:
                break;
        }
    }

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int)(dp * (metrics.densityDpi / 160f));
        return px;
    }

    @Override
    public void onClick(View view) {
        updateContentView();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(view.getId() == R.id.detail_button_approve) {
                Toast.makeText(this,"Je suis POUR ce texte." , Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Je suis CONTRE ce texte." , Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
}
