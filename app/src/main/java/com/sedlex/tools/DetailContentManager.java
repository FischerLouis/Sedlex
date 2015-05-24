package com.sedlex.tools;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sedlex.R;
import com.sedlex.activities.LawDetailActivity;
import com.sedlex.objects.Article;
import com.sedlex.objects.Law;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailContentManager {

    private static final String LOG_TAG = DetailContentManager.class.getSimpleName();

    private LawDetailActivity activity;
    private Law lawDetail;
    private RequestQueue queue;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    public DetailContentManager(LawDetailActivity activity) {
        this.activity = activity;
        queue = VolleySingleton.getInstance().getRequestQueue();
        lawDetail = new Law();
    }

    public void fetchLawDetail(int lawId){
        //VOLLEY QUEUE
        RequestQueue queue = VolleySingleton.getInstance().getRequestQueue();
        //URL TO LOAD
        String urlLawDetails = Constants.URL_LAW_DETAILS+lawId;
        //CACHE CHECK
        if(queue.getCache().get(urlLawDetails)!=null){
            Log.d("VOLLEY_VIEW_2", "CACHE");
            //GET JSON FROM CACHE
            try {
                String cachedResponse = new String(queue.getCache().get(urlLawDetails).data);
                JSONObject jsonCached = new JSONObject(cachedResponse);
                updateLawDetail(jsonCached);
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
                                updateLawDetail(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(activity.getApplicationContext(), activity.getResources().getText(R.string.network_error), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            queue.add(getLawDetailsReq);
        }
    }

    private void updateLawDetail(JSONObject lawDetailJSON) throws JSONException {
        final String STATIC_LAW = "law";
        final String STATIC_LAW_CONTENT = "content";
        final String STATIC_LAW_HASDEBATES = "has_debates";
        final String STATIC_LAW_ARTICLES = "news";
        final String STATIC_ARTICLE_TITLE = "title";
        final String STATIC_ARTICLE_SOURCE = "source";
        final String STATIC_ARTICLE_URL = "url";
        final String STATIC_ARTICLE_DATE = "pub_date";
        final String STATIC_LAW_DEBATES_GROUP = "debates_group";


        JSONObject lawObject = lawDetailJSON.getJSONObject(STATIC_LAW);
        lawDetail.setContent(lawObject.getString(STATIC_LAW_CONTENT));
        lawDetail.setHasDebates(lawObject.getBoolean(STATIC_LAW_HASDEBATES));
        JSONArray articleArray = lawObject.getJSONArray(STATIC_LAW_ARTICLES);

        JSONArray groupArray = lawObject.getJSONArray(STATIC_LAW_DEBATES_GROUP);
        List<String> groupList = new ArrayList<>();
        for( int i = 0; i < groupArray.length() ; i++ ){
            groupList.add((String) groupArray.get(i));
        }
        lawDetail.setDebatesGroupList(groupList);

        ArrayList<Article> articleList = new ArrayList<>();
        for( int i = 0; i < articleArray.length() ; i++ ){
            Log.d("SOURCE",((JSONObject) articleArray.get(i)).getString(STATIC_ARTICLE_SOURCE).toString());
            Article article = new Article();
            article.setId(i);
            article.setLink(((JSONObject) articleArray.get(i)).getString(STATIC_ARTICLE_URL));
            article.setSource(((JSONObject) articleArray.get(i)).getString(STATIC_ARTICLE_SOURCE));
            article.setTitle(((JSONObject) articleArray.get(i)).getString(STATIC_ARTICLE_TITLE));

            String dateString = ((JSONObject) articleArray.get(i)).getString(STATIC_ARTICLE_DATE);
            try {
                article.setDate(dateFormatter.parse(dateString));
            } catch ( ParseException e ){
                Log.e(LOG_TAG, e.getMessage());
                article.setDate(null);
            }

            articleList.add(article);
        }
        lawDetail.setArticleList(articleList);
        Log.d("CONTENTMANAGER", lawDetail.toString());

        activity.updateDetailViews(lawDetail);
    }
}
