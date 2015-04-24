package com.sedlex.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sedlex.R;
import com.sedlex.activities.LawDetailActivity;
import com.sedlex.objects.Article;

import java.util.ArrayList;

public class ArticlesAdapterOld extends RecyclerView.Adapter<ArticlesAdapterOld.ViewHolder> {

    private static LawDetailActivity lawDetailActivity;
    private final ArrayList<Article> articlesList;

    public ArticlesAdapterOld(LawDetailActivity lawDetailActivity, ArrayList<Article> articlesList) {
        this.articlesList = articlesList;
        this.lawDetailActivity = lawDetailActivity;
    }
    //VIEW HOLDER FOR DEBATE ROWS
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView articleTitle;
        public TextView articleSource;
        public TextView articleDate;
        //public RelativeLayout articleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("DEBUG", "ViewHolderArticle");
            articleTitle = (TextView) itemView.findViewById(R.id.row_article_title);
            articleSource = (TextView) itemView.findViewById(R.id.row_article_source);
            articleDate = (TextView) itemView.findViewById(R.id.row_article_date);
            //articleLayout = (RelativeLayout) itemView.findViewById(R.id.row_article_layout);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(lawDetailActivity, "Url: "+view.getTag().toString() ,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public ArticlesAdapterOld.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d("DEBUG", "onCreateViewHolder");

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_article, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Log.d("DEBUG", "onBindViewHolder");
        Article article = articlesList.get(i);
        viewHolder.articleTitle.setText(article.getTitle());
        viewHolder.articleSource.setText(article.getSource());
        viewHolder.articleDate.setText(article.getDate().toString());
        viewHolder.itemView.setTag(article.getLink());
    }

    @Override
    public int getItemCount() {
        Log.d("DEBUG","getItemCount "+articlesList.size());
        return articlesList == null ? 0 : articlesList.size();
    }
}
