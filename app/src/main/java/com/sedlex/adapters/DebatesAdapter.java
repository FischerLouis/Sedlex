package com.sedlex.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sedlex.R;
import com.sedlex.activities.DebatesActivity;
import com.sedlex.objects.Debate;
import com.sedlex.objects.Stamp;
import com.sedlex.tools.EllipsizingTextView;

import java.util.ArrayList;

public class DebatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int TEXT_LINES_COUNT_TO_SHOW = 5;

    private static DebatesActivity debatesActivity;
    private final ArrayList<Debate> debatesList;

    public DebatesAdapter(DebatesActivity debatesActivity, ArrayList<Debate> debatesList ) {
        this.debatesList = debatesList;
        this.debatesActivity = debatesActivity;
    }
    //VIEW HOLDER FOR DEBATE ROWS
    public static class ViewHolderDebate extends RecyclerView.ViewHolder implements View.OnClickListener {
        public EllipsizingTextView debateText;

        public TextView deputyName;
        public TextView stampView;

        public ViewHolderDebate(View itemView) {
            super(itemView);
            debateText = (EllipsizingTextView) itemView.findViewById(R.id.debates_text);
            deputyName = (TextView) itemView.findViewById(R.id.debates_deputy_name);
            stampView = (TextView)itemView.findViewById(R.id.debates_deputy_stamp);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            EllipsizingTextView text = (EllipsizingTextView)view.findViewById(R.id.debates_text);

            if(text.isEllipsized()){
                text.setMaxLines(Integer.MAX_VALUE);
            }
            else{
                text.setMaxLines(TEXT_LINES_COUNT_TO_SHOW);
            }

            /*
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
            }*/
        }

        public void updateInitiative(Stamp stamp){
            if(!stamp.getTitle().equals(null)) {
                //TITLE
                stampView.setText(stamp.getTitle());
                //COLOR
                GradientDrawable circleBackground = (GradientDrawable) debatesActivity.getResources().getDrawable(R.drawable.circle);
                String color = "#" + stamp.getColor();
                circleBackground.setColor(Color.parseColor(color));
                stampView.setBackground(circleBackground);
            }
        }
    }

    //VIEW HOLDER FOR SEPARATOR ROWS
    public static class ViewHolderSeparator extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView countView;
        public RelativeLayout separatorLayout;

        public ViewHolderSeparator(View itemView) {
            super(itemView);
            countView = (TextView)itemView.findViewById(R.id.debates_separator_count);
            separatorLayout = (RelativeLayout)itemView.findViewById(R.id.debates_row_separator);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = (Integer)view.getTag();
            debatesActivity.showHiddenDebate(id);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            return new ViewHolderDebate(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_debates, viewGroup, false));
        }
        else{
            return new ViewHolderSeparator(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_debates_separator, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Debate debate = debatesList.get(i);
        if(viewHolder.getItemViewType() == 0) {
            ViewHolderDebate rowDebate = (ViewHolderDebate) viewHolder;
            rowDebate.deputyName.setText(debate.getDeputyName());
            rowDebate.debateText.setText(Html.fromHtml(debate.getText()));
            rowDebate.updateInitiative(debate.getStamp());
            rowDebate.debateText.setMaxLines(TEXT_LINES_COUNT_TO_SHOW);
        }
        else{
            ViewHolderSeparator rowSeparator = (ViewHolderSeparator) viewHolder;
            rowSeparator.countView.setText(""+debate.getDebatesCount());
            viewHolder.itemView.setTag(debate.getId());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(debatesList.get(position).isSeparator())
            return 1;
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return debatesList == null ? 0 : debatesList.size();
    }


}
