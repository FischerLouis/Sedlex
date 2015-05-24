package com.sedlex.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sedlex.R;
import com.sedlex.adapters.ArticlesAdapter;
import com.sedlex.adapters.ArticlesAdapterOld;
import com.sedlex.objects.Article;
import com.sedlex.objects.Law;
import com.sedlex.tools.Constants;
import com.sedlex.tools.DetailContentManager;
import com.sedlex.tools.EllipsizingTextView;
import com.sedlex.tools.SlidingPanel;
import com.sedlex.tools.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LawDetailActivity extends ActionBarActivity implements View.OnClickListener, View.OnTouchListener {

    public static final String ARG_TITLE = "ARG_TITLE";
    public static final String ARG_PROGRESS = "ARG_PROGRESS";
    public static final String ARG_LAWID = "ARG_LAWID";
    public static final String ARG_INITIATIVE = "ARG_INITIATIVE";
    public static final int ARG_INT_DEFAULT = 0;


    //public static final int IMAGEVIEW_SMALL_DP = 40;
    private static final int IMAGEVIEW_BIG_DP = 60;
    private static final int CONTENTVIEW_MAX_LINES = 8;

    private EllipsizingTextView lawContentView;
    private boolean extendedContent = true;
    private int lawId;
    private String lawTitle;
    private String lawInitiative;
    private Spinner spinnerParties;
    private ListView articlesList;
    private RelativeLayout layoutTransparent;
    private View debateView;
    private DetailContentManager contentManager;

    private FloatingActionButton voteButton;
    private boolean popupVoteIsVisible = false;
    private Animation animShow;
    private Animation animHide;

    private SlidingPanel popup;
    private LinearLayout voteButtonApprove;
    private LinearLayout voteButtonDisapprove;

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
        lawId = getIntent().getIntExtra(ARG_LAWID, ARG_INT_DEFAULT);

        //RETRIEVE VIEWS
        lawContentView = (EllipsizingTextView) findViewById(R.id.detail_content);
        voteButton = (FloatingActionButton) findViewById(R.id.detail_button_vote);
        spinnerParties = (Spinner) findViewById(R.id.detail_dropdown_parties);
        articlesList = (ListView)findViewById(R.id.detail_list_article);
        ImageView debatesButton = (ImageView) findViewById(R.id.detail_debates_button);
        layoutTransparent = (RelativeLayout) findViewById(R.id.detail_layout_transparent);
        debateView = findViewById(R.id.activity_detail_layout_opinions);
        popup = (SlidingPanel) findViewById(R.id.detail_popup_vote);
        voteButtonApprove = (LinearLayout) findViewById(R.id.detail_layout_vote_approve);
        voteButtonDisapprove = (LinearLayout) findViewById(R.id.detail_layout_vote_disapprove);

        //UPDATE PROGRESS VIEWS
        updateProgress(progress);

        //SETUP content manager
        contentManager = new DetailContentManager(this);

        //GET DYNAMIC DATA (CONTENT)
        if (lawId != 0)
            contentManager.fetchLawDetail(lawId);

        //SET Popup action
        initPopup();

        //SET LISTENER
        lawContentView.setOnClickListener(this);
        voteButton.setOnTouchListener(this);
        voteButtonApprove.setOnClickListener(this);
        voteButtonDisapprove.setOnClickListener(this);
        layoutTransparent.setOnClickListener(this);
        debatesButton.setOnTouchListener(this);
    }

    public void updateDetailViews(Law lawDetail){
        //UPDATE CONTENT VIEW
        lawContentView.setText(lawDetail.getContent());
        updateContentView();

        //UPDATE DEBATE SPINNER
        if( lawDetail.getHasDebates() ){
            debateView.setVisibility(View.VISIBLE);

            SpannableString content = new SpannableString("PC");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, lawDetail.getDebatesGroupList());
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerParties.setAdapter(dataAdapter);
        } else {
            debateView.setVisibility(View.GONE);
        }

        //UPDATE ARTICLES
        ArticlesAdapter articlesAdapter = new ArticlesAdapter(this, lawDetail.getArticleList());
        articlesList.setAdapter(articlesAdapter);
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
        TextView stepTwoText = (TextView) findViewById(R.id.detail_step_text_2);
        TextView stepThreeText = (TextView) findViewById(R.id.detail_step_text_3);

        stepOne.setText(lawInitiative);

        switch (progress){
            case 0:
                stepOne.setTextSize(22);
                stepOne.getLayoutParams().height = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepOne.getLayoutParams().width = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepOne.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            case 1:
                stepOne.setBackgroundColor(getResources().getColor(R.color.deep_orange));
                stepTwo.setBackgroundColor(getResources().getColor(R.color.amber));
                stepTwo.getLayoutParams().height = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepTwo.getLayoutParams().width = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepTwoText.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            case 2:
                stepOne.setBackgroundColor(getResources().getColor(R.color.deep_orange));
                stepTwo.setBackgroundColor(getResources().getColor(R.color.amber));
                stepThree.setBackgroundColor(getResources().getColor(R.color.green));
                stepThree.getLayoutParams().height = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepThree.getLayoutParams().width = convertDpToPixel(IMAGEVIEW_BIG_DP, this);
                stepThreeText.setTypeface(Typeface.DEFAULT_BOLD);
                break;
            default:
                break;
        }
    }

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int)(dp * (metrics.densityDpi / 160f));
    }

    private void initPopup() {
        popup.setVisibility(View.GONE);

        animShow = AnimationUtils.loadAnimation(this, R.anim.popup_show);
        animHide = AnimationUtils.loadAnimation( this, R.anim.popup_hide);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.detail_content:
                updateContentView();
                break;
            case R.id.detail_layout_transparent:
                if(popupVoteIsVisible) {
                    popup.setVisibility(View.GONE);
                    popup.startAnimation(animHide);
                    layoutTransparent.setVisibility(View.GONE);
                    popupVoteIsVisible = false;
                }
                break;
            case R.id.detail_layout_vote_approve:
                Toast.makeText(this, "J'approuve ce texte", Toast.LENGTH_SHORT).show();
                break;
            case R.id.detail_layout_vote_disapprove:
                Toast.makeText(this, "Je n'approuve pas ce texte", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            switch(view.getId()) {
                case R.id.detail_debates_button:
                    Intent debatesIntent = new Intent(this, DebatesActivity.class);
                    debatesIntent.putExtra(DebatesActivity.ARG_TITLE, lawTitle);
                    debatesIntent.putExtra(DebatesActivity.ARG_LAWID, lawId);
                    debatesIntent.putExtra(DebatesActivity.ARG_PARTY, spinnerParties.getSelectedItem().toString());
                    startActivity(debatesIntent);
                case R.id.detail_button_vote:
                    if(!popupVoteIsVisible){
                        popup.setVisibility(View.VISIBLE);
                        popup.startAnimation(animShow);
                        layoutTransparent.setVisibility(View.VISIBLE);
                        popupVoteIsVisible = true;
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}
