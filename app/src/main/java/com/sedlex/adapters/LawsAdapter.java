package com.sedlex.adapters;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sedlex.R;
import com.sedlex.activities.LawDetailActivity;
import com.sedlex.objects.Category;
import com.sedlex.objects.Law;
import com.sedlex.objects.Stamp;
import com.sedlex.tools.Constants;
import com.sedlex.tools.EllipsizingTextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LawsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int SUMMARY_MAX_LINES = 4;

    private static Context context;
    private final ArrayList<Law> lawsList;

    public LawsAdapter(Context context, ArrayList<Law> lawsList) {
        this.lawsList = lawsList;
        this.context = context;
    }


    public static class ViewHolderLaw extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView lawTitle;
        public EllipsizingTextView lawSummary;
        public View firstStep;
        public View secondStep;
        public View thirdStep;
        public TextView viewCategory;
        public TextView stampView;
        public TextView lawDayOrder;
        public LinearLayout progressLayout;
        public LinearLayout globalLayout;
        private LayoutInflater layoutInflater;
        private DateFormat dateFormatter;

        public ViewHolderLaw(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            lawTitle = (TextView) itemView.findViewById(R.id.law_title);
            lawSummary = (EllipsizingTextView) itemView.findViewById(R.id.law_summary);
            firstStep = itemView.findViewById(R.id.law_step_1);
            secondStep = itemView.findViewById(R.id.law_step_2);
            thirdStep = itemView.findViewById(R.id.law_step_3);
            viewCategory = (TextView)itemView.findViewById(R.id.view_category);
            stampView = (TextView)itemView.findViewById(R.id.law_stamp);
            lawDayOrder = (TextView)itemView.findViewById(R.id.law_day_order);
            progressLayout = (LinearLayout)itemView.findViewById(R.id.card_bottom);
            globalLayout = (LinearLayout)itemView.findViewById(R.id.law_global_layout);
            layoutInflater = LayoutInflater.from(context);
            dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE);
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

        public void updateDayOrder(Date day_order){
            if( day_order == null ){
                lawDayOrder.setVisibility(View.GONE);
            } else {
                lawDayOrder.setVisibility(View.VISIBLE);
                lawDayOrder.setText(context.getResources().getText(R.string.day_order) + " " + dateFormatter.format(day_order));
            }
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
            detailIntent.putExtra(LawDetailActivity.ARG_PROGRESS, (int)progressLayout.getTag(R.string.activity_tag_id_progress));
            detailIntent.putExtra(LawDetailActivity.ARG_INITIATIVE, (String)progressLayout.getTag(R.string.activity_tag_id_initiative));
            detailIntent.putExtra(LawDetailActivity.ARG_LAWID, (int)globalLayout.getTag());
            context.startActivity(detailIntent);
        }
    }

    //VIEW HOLDER FOR SEPARATOR ROWS
    public static class ViewHolderLoadingView extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ViewHolderLoadingView(View itemView) {
            super(itemView);
            progressBar = (ProgressBar)itemView.findViewById(R.id.laws_loading_progressbar);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            return new ViewHolderLaw(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_laws, viewGroup, false));
        }
        else{
            return new ViewHolderLoadingView(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_laws_loading_view, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Law law = lawsList.get(i);
        if(viewHolder.getItemViewType() == 0) {
            ViewHolderLaw rowLaw = (ViewHolderLaw) viewHolder;
            rowLaw.lawTitle.setText(Html.fromHtml(law.getTitle()));
            rowLaw.lawSummary.setText(Html.fromHtml(law.getSummary()));
            rowLaw.lawSummary.setMaxLines(SUMMARY_MAX_LINES);
            int progress = rowLaw.updateProgression(law.getProgression());
            rowLaw.progressLayout.setTag(R.string.activity_tag_id_progress, progress);
            rowLaw.progressLayout.setTag(R.string.activity_tag_id_initiative,law.getStamp().getTitle());
            rowLaw.updateCategories(law.getCategories());
            rowLaw.updateInitiative(law.getStamp());
            rowLaw.updateDayOrder(law.getDayOrder());
            rowLaw.globalLayout.setTag(law.getId());
        }
        else{
            ViewHolderLoadingView rowLoadingView = (ViewHolderLoadingView) viewHolder;


        }






    }

    @Override
    public int getItemCount() {
        return lawsList == null ? 0 : lawsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(lawsList.get(position).isDummyLoadingView())
            return 1;
        else
            return 0;
    }
}
