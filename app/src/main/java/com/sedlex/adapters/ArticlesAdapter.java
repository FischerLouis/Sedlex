package com.sedlex.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sedlex.R;
import com.sedlex.activities.LawDetailActivity;
import com.sedlex.objects.Article;

import java.util.ArrayList;

public class ArticlesAdapter extends ArrayAdapter<Article> {

    private Context context;
    private ArrayList<Article> articlesList;

    public ArticlesAdapter(Context context, ArrayList<Article> articlesList) {
        super(context, R.layout.row_article, articlesList);
        this.context = context;
        this.articlesList = articlesList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_article, parent, false);
        TextView articleTitle = (TextView) rowView.findViewById(R.id.row_article_title);
        TextView articleSource = (TextView) rowView.findViewById(R.id.row_article_source);
        TextView articleDate = (TextView) rowView.findViewById(R.id.row_article_date);

        articleTitle.setText(articlesList.get(position).getTitle());
        articleSource.setText(articlesList.get(position).getSource());
        articleDate.setText(articlesList.get(position).getDate().toString());

        return rowView;
    }

/*




    private static LawDetailActivity lawDetailActivity;
    private final ArrayList<Article> articlesList;

    public ArticlesAdapterTwo(LawDetailActivity lawDetailActivity, ArrayList<Article> articlesList) {
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
    public ArticlesAdapterTwo.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
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

    */
}
