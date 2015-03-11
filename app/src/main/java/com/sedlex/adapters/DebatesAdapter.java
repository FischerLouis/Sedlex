package com.sedlex.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sedlex.R;
import com.sedlex.objects.Debate;
import com.sedlex.objects.Stamp;
import com.sedlex.tools.EllipsizingTextView;

import java.util.ArrayList;

public class DebatesAdapter extends RecyclerView.Adapter<DebatesAdapter.ViewHolder> {

    private static int TEXT_LEVEL_ONE_NUMBER_LINES = 1;
    private static int TEXT_LEVEL_TWO_NUMBER_LINES = 10;

    private static Context context;
    private final ArrayList<Debate> debatesList;
    private int rowLayoutId;
    private static boolean expended = false;
    private static boolean semiExpended = false;
    private static boolean wasExpended = false;

    public DebatesAdapter(Context context, int rowLayoutId, ArrayList<Debate> debatesList) {
        this.rowLayoutId = rowLayoutId;
        this.debatesList = debatesList;
        this.context = context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public EllipsizingTextView textLevelOne;

        public TextView deputyName;
        public TextView stampView;

        public ViewHolder(View itemView) {
            super(itemView);
            textLevelOne = (EllipsizingTextView) itemView.findViewById(R.id.debates_text_level_one);
            deputyName = (TextView) itemView.findViewById(R.id.debates_deputy_name);
            stampView = (TextView)itemView.findViewById(R.id.debates_deputy_stamp);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            EllipsizingTextView test = (EllipsizingTextView)view.findViewById(R.id.debates_text_level_one);
            Log.d("ELLIPS", "BOOL: " + test.isEllipsized());
            //LEVEL 1 => LEVEL 2
            if(!semiExpended && !expended){
                test.setMaxLines(TEXT_LEVEL_TWO_NUMBER_LINES);
                semiExpended = true;
            }
            //LEVEL 3 => LEVEL 2
            else if(expended){
                test.setMaxLines(TEXT_LEVEL_TWO_NUMBER_LINES);
                semiExpended = true;
                expended = false;
            }
            //LEVEL 2 => LEVEL 1
            else if( (semiExpended && wasExpended) || (semiExpended && !test.isEllipsized())){
                test.setMaxLines(TEXT_LEVEL_ONE_NUMBER_LINES);
                semiExpended = false;
                wasExpended = false;
            }
            //LEVEL 2 => LEVEL 3
            else if(semiExpended && !wasExpended){
                test.setMaxLines(Integer.MAX_VALUE);
                semiExpended = false;
                expended = true;
                wasExpended = true;
            }
        }

        public void updateInitiative(Stamp stamp){
            if(!stamp.getTitle().equals(null)) {
                //TITLE
                stampView.setText(stamp.getTitle());
                //COLOR
                GradientDrawable circleBackground = (GradientDrawable) context.getResources().getDrawable(R.drawable.circle);
                String color = "#" + stamp.getColor();
                circleBackground.setColor(Color.parseColor(color));
                stampView.setBackground(circleBackground);
            }
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
        viewHolder.textLevelOne.setText(Html.fromHtml(debate.getText()));
        Log.d("STAMP",""+debate.getStamp().toString());
        viewHolder.updateInitiative(debate.getStamp());



        /*viewHolder.textLevelOne.setOnLayoutListener(new EllipsizingTextView.OnLayoutListener() {
            @Override
            public void onLayouted(TextView view) {
                int line = view.getLineCount();
                if(line <= 1){
                    view.setId(line);
                }
                else if(line <=10){
                    view.setId(line);
                }
                else{
                    view.setId(line);
                }
                Log.d("ID","tag3:"+view.getId());
            }
        });*/
        viewHolder.textLevelOne.setMaxLines(TEXT_LEVEL_ONE_NUMBER_LINES);
    }

    @Override
    public int getItemCount() {
        return debatesList == null ? 0 : debatesList.size();
    }


}
