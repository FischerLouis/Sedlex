package com.sedlex.tools;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sedlex.R;
import com.sedlex.object.Category;
import com.sedlex.object.Law;
import com.sedlex.object.Stamp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyContentManager {

    private static final String STATIC_LAWS = "laws";
    private static final String STATIC_ID = "id";
    private static final String STATIC_LAST_UPDATE = "published";
    private static final String STATIC_TITLE = "title";
    private static final String STATIC_SUMMARY = "vp_summary";
    private static final String STATIC_PROGRESSION = "vp_status";
    private static final String STATIC_CATEGORIES = "Categories";
    private static final String STATIC_CATEGORY_ID = "id";
    private static final String STATIC_CATEGORY_TITLE = "title";
    private static final String STATIC_CATEGORY_COLOR = "color";
    private static final String STATIC_INITIATIVE = "initiative";
    private static final String STATIC_INITIATIVE_DEPUTY = "InitiativeDeputy";
    private static final String STATIC_INITIATIVE_DEPUTY_PARTY = "Party";
    private static final String STATIC_PARTY_ID = "id";
    private static final String STATIC_PARTY_TITLE = "PS";
    private static final String STATIC_PARTY_COLOR = "color";

    private ArrayList<Law> lawsList;
    private RequestQueue queue;
    private Context context;
    private LawsAdapter adapter;
    private Activity activity;


    public MyContentManager(Context context, Activity activity) {
        this.context=context;
        this.activity = activity;
        lawsList = new ArrayList<>();
        queue = VolleySingleton.getInstance().getRequestQueue();
    }

    final public ArrayList<Law> updateList(RecyclerView listView, int page, final boolean refresh){

        final RecyclerView finalListView = listView;
        final int finalPage = page;
        final boolean finalRefresh = refresh;

        //FINAL ROW LOADING
        String url = Constants.URL_LAWS+finalPage;
        JsonObjectRequest getLawsReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray lawsArray = response.getJSONArray(STATIC_LAWS);
                            //if(refresh)
                               // lawsList.clear();
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
                                    JSONObject party = lawsArray.getJSONObject(i).getJSONObject(STATIC_INITIATIVE_DEPUTY).getJSONObject(STATIC_INITIATIVE_DEPUTY_PARTY);
                                    curStamp.setId(party.getInt(STATIC_PARTY_ID));
                                    curStamp.setTitle(STATIC_PARTY_TITLE);
                                    curStamp.setColor(party.getString(STATIC_PARTY_COLOR));
                                }
                                else{
                                    curStamp.setId(0);
                                    curStamp.setTitle("?");
                                    curStamp.setColor("9e9e9e");
                                }
                                curLaw.setStamp(curStamp);
                                lawsList.add(curLaw);
                            }
                            //UPDATING DATA ACCORDING TO PAGES / UPDATE
                            if(!finalRefresh) {
                                if (finalPage == 0) {
                                    adapter = new LawsAdapter(context, R.layout.rowlayout, lawsList);
                                    activity.findViewById(R.id.loading_view).setVisibility(View.GONE);
                                    finalListView.setAdapter(adapter);
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            else{
                                adapter.notifyDataSetChanged();
                                //adapter = new LawsAdapter(context, R.layout.rowlayout, lawsList);
                                //finalListView.setAdapter(adapter);
                                SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.activity_main_swipe_refresh_layout);
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
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
        queue.add(getLawsReq);

        return lawsList;
    }
}
