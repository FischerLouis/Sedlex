package com.sedlex.tools;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sedlex.R;
import com.sedlex.activities.MainActivity;
import com.sedlex.objects.Category;
import com.sedlex.objects.Law;
import com.sedlex.objects.Stamp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyContentManager {

    private static final String STATIC_LAWS = "laws";
    private static final String STATIC_ID = "id";
    private static final String STATIC_LAST_UPDATE = "published";
    private static final String STATIC_TITLE = "title";
    private static final String STATIC_SUMMARY = "content";
    private static final String STATIC_PROGRESSION = "vp_status";
    private static final String STATIC_CATEGORIES = "Categories";
    private static final String STATIC_CATEGORY_ID = "id";
    private static final String STATIC_CATEGORY_TITLE = "title";
    private static final String STATIC_CATEGORY_COLOR = "color";
    private static final String STATIC_INITIATIVE = "initiative";
    private static final String STATIC_INITIATIVE_DEPUTY = "InitiativeDeputy";
    private static final String STATIC_INITIATIVE_DEPUTY_PARTY = "Party";
    private static final String STATIC_PARTY_ID = "id";
    private static final String STATIC_PARTY_ACRONYM = "acronym";
    private static final String STATIC_PARTY_COLOR = "color";

    private ArrayList<Law> lawsList;
    private RequestQueue queue;
    private MainActivity activity;
    private int curPage = 0;
    private boolean refresh = false;


    public MyContentManager(MainActivity activity) {
        this.activity = activity;
        lawsList = new ArrayList<>();
        queue = VolleySingleton.getInstance().getRequestQueue();
    }

    final public void updateList(int page, boolean refreshList) {
        refresh = refreshList;
        //CURPAGE SETUP
        curPage = page;
        //URL SETUP
        String url = Constants.URL_LAWS_PAGE+page;
        //CACHE CHECK
        if(queue.getCache().get(url)!=null && !refresh){
            Log.d("VOLLEY_VIEW_1","CACHE");
            //GET JSON FROM CACHE
            try {
                String cachedResponse = new String(queue.getCache().get(url).data);
                JSONObject jsonCached = new JSONObject(cachedResponse);
                buildLawsList(jsonCached);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            queue.getCache().invalidate(url,true);
            Log.d("VOLLEY_VIEW_1","NO CACHE");
            //GET JSON FROM SERVER
            JsonObjectRequest getLawsReq = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                buildLawsList(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("DEBUG","ERROR NETWORK");
                            activity.stopRefreshingAnim();
                            Toast.makeText(activity, activity.getResources().getText(R.string.network_error), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            queue.add(getLawsReq);
        }
    }

    private void buildLawsList(JSONObject jsonLoaded) throws JSONException {
        JSONArray lawsArray = jsonLoaded.getJSONArray(STATIC_LAWS);
        //EMPTY LIST IF REFRESH
        if(refresh)
            lawsList.clear();
        //BUILD LIST
        for(int i=0;i<lawsArray.length();i++){
            Law curLaw = new Law();
            //SET ID
            curLaw.setId(lawsArray.getJSONObject(i).getInt(STATIC_ID));
            //SET DATE
            curLaw.setLastUpdate(lawsArray.getJSONObject(i).getString(STATIC_LAST_UPDATE));
            //SET TITLE
            curLaw.setTitle(lawsArray.getJSONObject(i).getString(STATIC_TITLE));
            //SET SUMMARY
            curLaw.setSummary(lawsArray.getJSONObject(i).getString(STATIC_SUMMARY));
            //SET PROGRESSION
            curLaw.setProgression(lawsArray.getJSONObject(i).getString(STATIC_PROGRESSION));
            //SET CATEGORIES
            JSONArray categoriesArray = lawsArray.getJSONObject(i).getJSONArray(STATIC_CATEGORIES);
            ArrayList<Category> categories = new ArrayList<>();
            for(int j=0;j<categoriesArray.length();j++){
                if(categoriesArray.getJSONObject(j).getString(STATIC_CATEGORY_ID) != "null") {
                    Category curCategory = new Category();
                    curCategory.setId(categoriesArray.getJSONObject(j).getInt(STATIC_CATEGORY_ID));
                    curCategory.setTitle(categoriesArray.getJSONObject(j).getString(STATIC_CATEGORY_TITLE));
                    curCategory.setColor(categoriesArray.getJSONObject(j).getString(STATIC_CATEGORY_COLOR));
                    categories.add(curCategory);
                }
            }
            curLaw.setCategories(categories);
            //SET STAMP
            Stamp curStamp = new Stamp();

            if (lawsArray.getJSONObject(i).getString(STATIC_INITIATIVE).equals("government")) {
                curStamp.setId(1);
                curStamp.setTitle("G");
                curStamp.setColor("795548");
            } else if (lawsArray.getJSONObject(i).getString(STATIC_INITIATIVE).equals("deputy") && !lawsArray.getJSONObject(i).getString(STATIC_INITIATIVE_DEPUTY).equals("null")) {
                if(!lawsArray.getJSONObject(i).getJSONObject(STATIC_INITIATIVE_DEPUTY).getString(STATIC_INITIATIVE_DEPUTY_PARTY).equals("null")) {
                    JSONObject party = lawsArray.getJSONObject(i).getJSONObject(STATIC_INITIATIVE_DEPUTY).getJSONObject(STATIC_INITIATIVE_DEPUTY_PARTY);
                    curStamp.setId(party.getInt(STATIC_PARTY_ID));
                    curStamp.setTitle(party.getString(STATIC_PARTY_ACRONYM));
                    curStamp.setColor(party.getString(STATIC_PARTY_COLOR));
                }
                else{
                    curStamp.setId(0);
                    curStamp.setTitle("?");
                    curStamp.setColor("9e9e9e");
                }
            }
            else{
                curStamp.setId(0);
                curStamp.setTitle("?");
                curStamp.setColor("9e9e9e");
            }
            curLaw.setStamp(curStamp);
            lawsList.add(curLaw);
        }
        activity.setList(lawsList, curPage);
    }
}
