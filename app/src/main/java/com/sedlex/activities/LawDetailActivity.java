package com.sedlex.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sedlex.R;
import com.sedlex.tools.Constants;
import com.sedlex.tools.EllipsizingTextView;
import com.sedlex.tools.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

public class LawDetailActivity extends ActionBarActivity implements View.OnClickListener, View.OnTouchListener {

    public static final String ARG_TITLE = "ARG_TITLE";
    public static final String ARG_PROGRESS = "ARG_PROGRESS";
    public static final String ARG_INITIATIVE = "ARG_INITIATIVE";
    public static final String ARG_LAWID = "ARG_LAWID";
    public static final int ARG_INT_DEFAULT = 0;


    //public static final int IMAGEVIEW_SMALL_DP = 40;
    private static final int IMAGEVIEW_BIG_DP = 60;
    private static final int CONTENTVIEW_MAX_LINES = 8;

    private EllipsizingTextView lawContentView;
    private AnimationSet loadingAnimationSet;
    private boolean extendedContent = true;
    private int lawId;
    private String lawTitle;
    private String lawInitiative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // RETRIEVE AND SET PASSED DATA
        lawTitle = getIntent().getStringExtra(ARG_TITLE);
        lawInitiative = getIntent().getStringExtra(ARG_INITIATIVE);
        setTitle(lawTitle);
        int progress = getIntent().getIntExtra(ARG_PROGRESS, ARG_INT_DEFAULT);
        Log.d("DEBUG", "progess:" + progress);
        lawId = getIntent().getIntExtra(ARG_LAWID, ARG_INT_DEFAULT);
        Log.d("DEBUG","id:"+lawId);

        //RETRIEVE VIEWS
        lawContentView = (EllipsizingTextView) findViewById(R.id.detail_content);
        TextView approveButton = (TextView) findViewById(R.id.detail_button_approve);
        TextView disapproveButton = (TextView) findViewById(R.id.detail_button_disapprove);
        TextView debatesOne = (TextView) findViewById(R.id.detail_debates_1);
        TextView debatesTwo = (TextView) findViewById(R.id.detail_debates_2);
        TextView debatesThree = (TextView) findViewById(R.id.detail_debates_3);
        TextView debatesFour = (TextView) findViewById(R.id.detail_debates_4);
        TextView debatesFive = (TextView) findViewById(R.id.detail_debates_5);
        TextView debatesSix = (TextView) findViewById(R.id.detail_debates_6);

        //UPDATE PROGRESS VIEWS
        updateProgress(progress);

        // PREPARE LOADING ANIMATION
        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(2000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(2000);

        loadingAnimationSet = new AnimationSet(true);
        loadingAnimationSet.addAnimation(out);
        loadingAnimationSet.addAnimation(in);

        //GET DYNAMIC DATA (CONTENT)
        if (lawId != 0)
            updateLawDetailsContent(lawId);

        //SET LISTENER
        lawContentView.setOnClickListener(this);
        approveButton.setOnTouchListener(this);
        disapproveButton.setOnTouchListener(this);
        debatesOne.setOnTouchListener(this);
        debatesTwo.setOnTouchListener(this);
        debatesThree.setOnTouchListener(this);
        debatesFour.setOnTouchListener(this);
        debatesFive.setOnTouchListener(this);
        debatesSix.setOnTouchListener(this);
    }

    private void updateLawDetailsContent(int lawId){
        //ANIMATION
        lawContentView.setAnimation(loadingAnimationSet);
        //VOLLEY QUEUE
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        //URL TO LOAD
        String urlLawDetails = Constants.URL_LAW_DETAILS+lawId;
        //CACHE CHECK
        if(queue.getCache().get(urlLawDetails)!=null){
            Log.d("VOLLEY_VIEW_2","CACHE");
            //GET JSON FROM CACHE
            try {
                //GET DATA
                String cachedResponse = new String(queue.getCache().get(urlLawDetails).data);
                JSONObject jsonCached = new JSONObject(cachedResponse);
                updateFromJSON(jsonCached);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("VOLLEY_VIEW_2","NO CACHE");
            //GET JSON FROM SERVER
            JsonObjectRequest getLawDetailsReq = new JsonObjectRequest(Request.Method.GET, urlLawDetails, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //GET DATA
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
                            Log.d("Error.Response", "ERROR");
                        }
                    }
            );
            queue.add(getLawDetailsReq);
        }
    }

    private void updateFromJSON(JSONObject jsonLoaded) throws JSONException {
        final String STATIC_LAW_CONTENT = "content";
        final String STATIC_LAW = "law";

        JSONObject lawObject = jsonLoaded.getJSONObject(STATIC_LAW);
        //STOP LOADING ANIM
        lawContentView.clearAnimation();
        //SET TEXT
        lawContentView.setText(lawObject.getString(STATIC_LAW_CONTENT));
        updateContentView();
    }

    private void updateContentView(){
        if(extendedContent){
            lawContentView.setMaxLines(CONTENTVIEW_MAX_LINES);
            extendedContent = false;
        }
        else{
            lawContentView.setMaxLines(Integer.MAX_VALUE);
            extendedContent = true;
        }
    }

    private void updateProgress(int progress){

        TextView stepOne = (TextView) findViewById(R.id.detail_step_1);
        ImageView stepTwo = (ImageView) findViewById(R.id.detail_step_2);
        ImageView stepThree = (ImageView) findViewById(R.id.detail_step_3);
        TextView stepOneText = (TextView) findViewById(R.id.detail_step_text_1);
        TextView stepTwoText = (TextView) findViewById(R.id.detail_step_text_2);
        TextView stepThreeText = (TextView) findViewById(R.id.detail_step_text_3);

        //UPDATE INITIATIVE
        stepOne.setText(lawInitiative);

        switch (progress){
            case 0:
                stepOne.getLayoutParams().height = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepOne.getLayoutParams().width = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepOneText.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            case 1:
                stepTwo.setImageResource(R.drawable.process_plt_color);
                stepTwo.getLayoutParams().height = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepTwo.getLayoutParams().width = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepTwoText.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            case 2:
                stepThree.setImageResource(R.drawable.process_adoption_color);
                stepThree.getLayoutParams().height = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepThree.getLayoutParams().width = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepThreeText.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            default:
                break;
        }
    }

    private void loadingAnim(boolean start){
        if (start){
            final Animation in = new AlphaAnimation(0.0f, 1.0f);
            in.setDuration(1000);

            final Animation out = new AlphaAnimation(1.0f, 0.0f);
            out.setDuration(3000);

            AnimationSet as = new AnimationSet(true);
            as.addAnimation(out);
            in.setStartOffset(3000);
            as.addAnimation(in);
        }
        else{

        }
    }

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int)(dp * (metrics.densityDpi / 160f));
    }

    @Override
    public void onClick(View view) {
        updateContentView();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            switch(view.getId()){
                case R.id.detail_button_approve:
                    Toast.makeText(this,"Je suis POUR ce texte." , Toast.LENGTH_SHORT).show();
                    break;
                case R.id.detail_button_disapprove:
                    Toast.makeText(this,"Je suis CONTRE ce texte." , Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Intent debatesIntent = new Intent(this, DebatesActivity.class);
                    debatesIntent.putExtra(DebatesActivity.ARG_TITLE, lawTitle);
                    debatesIntent.putExtra(DebatesActivity.ARG_LAWID, lawId);
                    switch (view.getId()){
                        case R.id.detail_debates_1:
                            debatesIntent.putExtra(DebatesActivity.ARG_PARTY, "Co");
                            startActivity(debatesIntent);
                            break;
                        case R.id.detail_debates_2:
                            debatesIntent.putExtra(DebatesActivity.ARG_PARTY, "PS");
                            startActivity(debatesIntent);
                            break;
                        case R.id.detail_debates_3:
                            debatesIntent.putExtra(DebatesActivity.ARG_PARTY, "Ra");
                            startActivity(debatesIntent);
                            break;
                        case R.id.detail_debates_4:
                            debatesIntent.putExtra(DebatesActivity.ARG_PARTY, "Ve");
                            startActivity(debatesIntent);
                            break;
                        case R.id.detail_debates_5:
                            debatesIntent.putExtra(DebatesActivity.ARG_PARTY, "UDI");
                            startActivity(debatesIntent);
                            break;
                        case R.id.detail_debates_6:
                            debatesIntent.putExtra(DebatesActivity.ARG_PARTY, "UMP");
                            startActivity(debatesIntent);
                            break;
                    }
                    break;
            }
        }
        return false;
    }
}
