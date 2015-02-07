package com.sedlex.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sedlex.R;
import com.sedlex.activity.LawDetailActivity;
import com.sedlex.object.Category;
import com.sedlex.object.Law;
import com.sedlex.object.Stamp;

import java.util.ArrayList;

public class LawsAdapter extends RecyclerView.Adapter<LawsAdapter.ViewHolder> {

    private static Context context;
    private final ArrayList<Law> lawsList;
    private int rowLayoutId;

    public LawsAdapter(Context context, int rowLayoutId, ArrayList<Law> lawsList) {
        this.rowLayoutId = rowLayoutId;
        this.lawsList = lawsList;
        this.context = context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView lawTitle;
        public TextView lawSummary;
        public View firstStep;
        public View secondStep;
        public View thirdStep;
        public LinearLayout categoriesLayout;
        public TextView stampView;
        private LayoutInflater layoutInflater;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            lawTitle = (TextView) itemView.findViewById(R.id.law_title);
            lawSummary = (TextView) itemView.findViewById(R.id.law_summary);
            firstStep = itemView.findViewById(R.id.law_step_1);
            secondStep = itemView.findViewById(R.id.law_step_2);
            thirdStep = itemView.findViewById(R.id.law_step_3);
            categoriesLayout = (LinearLayout)itemView.findViewById(R.id.layout_categories);
            stampView = (TextView)itemView.findViewById(R.id.law_stamp);
            layoutInflater = LayoutInflater.from(context);
        }

        public void updateCategories (ArrayList<Category> categories){
            //CLEANING OLD VIEWS
            categoriesLayout.removeAllViews();

            //PARAMS VIEW
            LinearLayout.LayoutParams paramsView = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsView.rightMargin = 5;

            for(int i = 0;i<categories.size();i++){
                //UPDATE TITLE
                TextView categoryView = (TextView) layoutInflater.inflate(R.layout.view_category, null);
                categoryView.setText(categories.get(i).getTitle());
                //UPDATE COLOR
                GradientDrawable rectBackground = (GradientDrawable) categoryView.getBackground();
                String color = "#"+categories.get(i).getColor();
                rectBackground.setColor(Color.parseColor(color));

                categoriesLayout.addView(categoryView, paramsView);
            }
        }

        public void updateInitiative(Stamp stamp){
            //TITLE
            stampView.setText(stamp.getTitle());
            //COLOR
            GradientDrawable circleBackground = (GradientDrawable) context.getResources().getDrawable(R.drawable.circle);
            String color = "#"+stamp.getColor();
            circleBackground.setColor(Color.parseColor(color));
            stampView.setBackground(circleBackground);
        }

        public void updateProgression (String progression){

            int progress = Constants.GetProgressFromMapping(progression);

            switch (progress){
                case 0:
                    firstStep.setVisibility(View.VISIBLE);
                    secondStep.setVisibility(View.GONE);
                    thirdStep.setVisibility(View.GONE);
                    break;
                case 1:
                    firstStep.setVisibility(View.VISIBLE);
                    secondStep.setVisibility(View.VISIBLE);
                    thirdStep.setVisibility(View.GONE);
                    break;
                default:
                    firstStep.setVisibility(View.VISIBLE);
                    secondStep.setVisibility(View.VISIBLE);
                    thirdStep.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            Intent detailIntent = new Intent(context, LawDetailActivity.class);
            TextView titleView = (TextView)view.findViewById(R.id.law_title);
            String lawTitleClicked = titleView.getText().toString();
            //String lawTitleClicked = adapter.getItem(position).getTitle();
            detailIntent.putExtra(LawDetailActivity.ARG_ITEM, lawTitleClicked);
            context.startActivity(detailIntent);
        }
    }

    @Override
    public LawsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayoutId, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LawsAdapter.ViewHolder viewHolder, int i) {
        Law law = lawsList.get(i);
        viewHolder.lawTitle.setText(Html.fromHtml(law.getTitle()));
        viewHolder.lawSummary.setText(Html.fromHtml(law.getSummary()));
        viewHolder.updateProgression(law.getProgression());
        viewHolder.updateCategories(law.getCategories());
        viewHolder.updateInitiative(law.getStamp());
    }

    @Override
    public int getItemCount() {
        return lawsList == null ? 0 : lawsList.size();
    }
}
