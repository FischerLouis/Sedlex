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
        public TextView viewCategory;
        public TextView stampView;
        public LinearLayout progressLayout;
        public LinearLayout globalLayout;
        private LayoutInflater layoutInflater;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            lawTitle = (TextView) itemView.findViewById(R.id.law_title);
            lawSummary = (TextView) itemView.findViewById(R.id.law_summary);
            firstStep = itemView.findViewById(R.id.law_step_1);
            secondStep = itemView.findViewById(R.id.law_step_2);
            thirdStep = itemView.findViewById(R.id.law_step_3);
            viewCategory = (TextView)itemView.findViewById(R.id.view_category);
            stampView = (TextView)itemView.findViewById(R.id.law_stamp);
            progressLayout = (LinearLayout)itemView.findViewById(R.id.card_bottom);
            globalLayout = (LinearLayout)itemView.findViewById(R.id.law_global_layout);
            layoutInflater = LayoutInflater.from(context);
        }

        public void updateCategories (ArrayList<Category> categories){

            String categoriesDesc = "";

            for(int i = 0;i<categories.size();i++){
                categoriesDesc += categories.get(i).getTitle();
                if( i != categories.size() - 1){
                    categoriesDesc += ", ";
                }
            }

            viewCategory.setText(categoriesDesc);
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

        public int updateProgression (String progression){

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
            return progress;
        }

        @Override
        public void onClick(View view) {
            Intent detailIntent = new Intent(context, LawDetailActivity.class);
            detailIntent.putExtra(LawDetailActivity.ARG_TITLE, lawTitle.getText().toString());
            detailIntent.putExtra(LawDetailActivity.ARG_PROGRESS, (int)progressLayout.getTag());
            detailIntent.putExtra(LawDetailActivity.ARG_LAWID, (int)globalLayout.getTag());
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
        int progress = viewHolder.updateProgression(law.getProgression());
        viewHolder.progressLayout.setTag(progress);
        viewHolder.updateCategories(law.getCategories());
        viewHolder.updateInitiative(law.getStamp());
        viewHolder.globalLayout.setTag(law.getId());
    }

    @Override
    public int getItemCount() {
        return lawsList == null ? 0 : lawsList.size();
    }
}
