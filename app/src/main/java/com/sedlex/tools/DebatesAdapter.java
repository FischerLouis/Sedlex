package com.sedlex.tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sedlex.R;
import com.sedlex.object.Debate;

import java.util.ArrayList;

public class DebatesAdapter extends RecyclerView.Adapter<DebatesAdapter.ViewHolder> {

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
        public View levelTwoButton;
        public TextView textLevelOne;
        public LinearLayout textLevelTwoLayout;
        public TextView textLevelThree;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textLevelOne = (TextView) itemView.findViewById(R.id.debates_text_level_one);
            textLevelTwoLayout = (LinearLayout) itemView.findViewById(R.id.debates_layout_level_two);
            textLevelThree = (TextView) itemView.findViewById(R.id.debates_text_level_three);
            levelTwoButton = itemView.findViewById(R.id.debates_text_level_two_button);
            levelTwoButton.setOnClickListener(this);
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
                case R.id.debates_text_level_two_button:
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
        //Debate debate = debatesList.get(i);
        //viewHolder.lawTitle.setText(Html.fromHtml(law.getTitle()));
    }

    @Override
    public int getItemCount() {
        return debatesList == null ? 0 : debatesList.size();
    }
}
