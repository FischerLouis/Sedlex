package com.sedlex.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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

    private Context context = this;
    private EllipsizingTextView lawContentView;
    private boolean extendedContent = true;
    private int lawId;
    private String lawTitle;
    private String lawInitiative;
    private Spinner spinnerParties;
    private ListView articlesList;
    private RelativeLayout layoutTransparent;
    private LinearLayout layoutVote;
    private View debateView;
    private DetailContentManager contentManager;

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
        FloatingActionButton soloButton = (FloatingActionButton) findViewById(R.id.detail_button_solo_vote);
        //FloatingActionsMenu buttonVote = (FloatingActionsMenu) findViewById(R.id.details_button_vote);
        //FloatingActionButton buttonApprove = (FloatingActionButton) findViewById(R.id.detail_button_approve);
        //FloatingActionButton buttonDisapprove = (FloatingActionButton) findViewById(R.id.detail_button_disapprove);
        spinnerParties = (Spinner) findViewById(R.id.detail_dropdown_parties);
        articlesList = (ListView)findViewById(R.id.detail_list_article);
        ImageView debatesButton = (ImageView) findViewById(R.id.detail_debates_button);
        layoutTransparent = (RelativeLayout) findViewById(R.id.detail_layout_transparent);
        layoutVote = (LinearLayout) findViewById(R.id.detail_layout_vote);
        debateView = findViewById(R.id.activity_detail_layout_opinions);

        //UPDATE PROGRESS VIEWS
        updateProgress(progress);

        //SETUP content manager
        contentManager = new DetailContentManager(this);

        //GET DYNAMIC DATA (CONTENT)
        if (lawId != 0)
            contentManager.fetchLawDetail(lawId);

        //SET LISTENER
        lawContentView.setOnClickListener(this);
        soloButton.setOnTouchListener(this);
        //buttonApprove.setOnTouchListener(this);
        //buttonDisapprove.setOnTouchListener(this);
        //buttonVote.setOnClickListener(this);
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
        // setListViewHeightBasedOnChildren(articlesList);

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

    private void slideToAbove() {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -5.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        layoutVote.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                layoutVote.clearAnimation();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        layoutVote.getWidth(), layoutVote.getHeight());
                // lp.setMargins(0, 0, 0, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutVote.setLayoutParams(lp);

            }

        });
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

    private static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.detail_content:
                updateContentView();
                break;
            /*case R.id.details_button_vote:
                layoutTransparent.setVisibility(View.VISIBLE);
                break;*/
            case R.id.detail_layout_transparent:
                layoutTransparent.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            switch(view.getId()) {
                /*case R.id.detail_button_approve:
                    Toast.makeText(this, "Je suis POUR ce texte.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.detail_button_disapprove:
                    Toast.makeText(this, "Je suis CONTRE ce texte.", Toast.LENGTH_SHORT).show();
                    break;*/
                case R.id.detail_debates_button:
                    Intent debatesIntent = new Intent(this, DebatesActivity.class);
                    debatesIntent.putExtra(DebatesActivity.ARG_TITLE, lawTitle);
                    debatesIntent.putExtra(DebatesActivity.ARG_LAWID, lawId);
                    debatesIntent.putExtra(DebatesActivity.ARG_PARTY, spinnerParties.getSelectedItem().toString());
                    startActivity(debatesIntent);
                case R.id.detail_button_solo_vote:
                    Toast.makeText(this, "Button", Toast.LENGTH_SHORT).show();
                    layoutTransparent.setVisibility(View.VISIBLE);
                    slideToAbove();
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}
