package com.sedlex.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sedlex.R;
import com.sedlex.objects.Article;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ArticlesAdapter extends ArrayAdapter<Article> implements View.OnTouchListener {

    private Activity context;
    private ArrayList<Article> articlesList;
    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.FRANCE);

    public ArticlesAdapter(Activity context, ArrayList<Article> articlesList) {
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
        //RelativeLayout articleLayout = (RelativeLayout) rowView.findViewById(R.id.row_article_layout);
        rowView.setOnTouchListener(this);

        articleTitle.setText(articlesList.get(position).getTitle());
        articleSource.setText(articlesList.get(position).getSource());
        articleDate.setText(dateFormat.format(articlesList.get(position).getDate()));
        rowView.setTag(articlesList.get(position).getLink());

        return rowView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            Intent articleIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(view.getTag().toString()));
            context.startActivity(articleIntent);
        }
        return true;
    }
}
