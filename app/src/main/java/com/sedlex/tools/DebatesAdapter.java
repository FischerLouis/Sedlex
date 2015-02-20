package com.sedlex.tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sedlex.R;
import com.sedlex.object.Debate;

import java.util.ArrayList;

public class DebatesAdapter extends RecyclerView.Adapter<DebatesAdapter.ViewHolder> {

    private int TEXT_LEVEL_ONE_SIZE = 30;
    private int TEXT_LEVEL_TWO_SIZE = 30;

    private static Context context;
    private final ArrayList<Debate> debatesList;
    private int rowLayoutId;
    private static boolean expended = false;
    private static boolean semiExpended = false;

    public DebatesAdapter(Context context, int rowLayoutId, ArrayList<Debate> debatesList) {
        this.rowLayoutId = rowLayoutId;
        this.debatesList = debatesList;
        this.context = context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textLevelOne;
        public TextView textLevelTwoTop;
        public TextView textLevelTwoBottom;
        public LinearLayout textLevelTwoLayout;
        public RelativeLayout separatorLevelTwoLayout;
        public TextView textLevelThree;
        public TextView deputyName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textLevelOne = (TextView) itemView.findViewById(R.id.debates_text_level_one);
            textLevelTwoTop = (TextView) itemView.findViewById(R.id.debates_text_level_two_top);
            textLevelTwoBottom = (TextView) itemView.findViewById(R.id.debates_text_level_two_bottom);
            textLevelTwoLayout = (LinearLayout) itemView.findViewById(R.id.debates_layout_level_two);
            separatorLevelTwoLayout = (RelativeLayout) itemView.findViewById(R.id.debates_layout_level_two_separator);
            textLevelThree = (TextView) itemView.findViewById(R.id.debates_text_level_three);
            deputyName = (TextView) itemView.findViewById(R.id.debates_deputy_name);
            separatorLevelTwoLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.debates_row:
                    if(!expended && !semiExpended){
                        textLevelOne.setVisibility(View.GONE);
                        textLevelTwoLayout.setVisibility(View.VISIBLE);
                        semiExpended = true;
                    }
                    else if(expended){
                        textLevelThree.setVisibility(View.GONE);
                        textLevelTwoLayout.setVisibility(View.VISIBLE);
                        semiExpended = true;
                        expended = false;
                    }
                    else if(semiExpended){
                        textLevelTwoLayout.setVisibility(View.GONE);
                        textLevelOne.setVisibility(View.VISIBLE);
                        semiExpended = false;
                    }
                    break;
                case R.id.debates_layout_level_two_separator:
                    if(semiExpended){
                        textLevelThree.setVisibility(View.VISIBLE);
                        textLevelTwoLayout.setVisibility(View.GONE);
                        semiExpended = false;
                        expended = true;
                    }
                    break;
                default:
                    break;
            }
            Log.d("CLICK", "semi: "+semiExpended);
            Log.d("CLICK", "expended: "+expended);
        }
    }

    @Override
    public DebatesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayoutId, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DebatesAdapter.ViewHolder viewHolder, int i) {
        Debate debate = debatesList.get(i);
        viewHolder.deputyName.setText(debate.getDeputyName());
        String fullText = debate.getText();
        viewHolder.textLevelOne.setText(Html.fromHtml(getTextLevelOne(fullText)));
        viewHolder.textLevelTwoTop.setText(Html.fromHtml(getTextLevelTwoTop(fullText)));
        viewHolder.textLevelTwoBottom.setText(Html.fromHtml(getTextLevelTwoBottom(fullText)));
        viewHolder.textLevelThree.setText(Html.fromHtml(getTextLevelThree(fullText)));
    }

    @Override
    public int getItemCount() {
        return debatesList == null ? 0 : debatesList.size();
    }

    private String getTextLevelOne(String fullText){
        return fullText.substring(0,TEXT_LEVEL_ONE_SIZE);
    }

    private String getTextLevelTwoTop(String fullText){
        return fullText.substring(0,TEXT_LEVEL_TWO_SIZE);
    }

    private String getTextLevelTwoBottom(String fullText){
        return fullText.substring(fullText.length()-TEXT_LEVEL_TWO_SIZE,fullText.length());
    }

    private String getTextLevelThree(String fullText){
        return fullText;
    }
}
