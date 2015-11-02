package canul.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import canul.android.R;
import canul.android.activities.ShowArticlesActivity;
import canul.android.models.Article;

/**
 * Created by Chazz on 15/10/15.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder>  {

    private List<Article> articles;
    private final ShowArticlesActivity activity;

    private final static String TAG = ArticlesAdapter.class.getName();

    public ArticlesAdapter(ShowArticlesActivity activity,
                           List<Article> articles) {
        this.activity = activity;
        this.articles = articles;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView authorTextView;
        private TextView publishedTextView;
        private TextView extractTextView;
        private TextView titleTextView;

        public ViewHolder(View view) {
            super(view);
            authorTextView = (TextView) view.findViewById(R.id.author);
            //TODO rename to extract
            extractTextView = (TextView) view.findViewById(R.id.content);
            publishedTextView = (TextView) view.findViewById(R.id.published);
            titleTextView = (TextView) view.findViewById(R.id.title);
        }

        public TextView getAuthorTextView() {
            return authorTextView;
        }

        public TextView getExtractTextView() {
            return extractTextView;
        }

        public TextView getPublishedTextView() {
            return publishedTextView;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_article, parent, false);

        view.setOnClickListener(activity);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getExtractTextView().setText(articles.get(position).getExtract());
        holder.getAuthorTextView().setText(articles.get(position).getAuthor());
        holder.getPublishedTextView().setText(articles.get(position).getPublished());
        holder.getTitleTextView().setText(articles.get(position).getTitle());
    }

    public void setArticles(List<Article> articles){
        this.articles = articles;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return articles.size();
    }




}

/*
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    private List<Comment> comments;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView contentTextView;
        private TextView authorTextView;
        private TextView publishedTextView;

        public ViewHolder(View view) {
            super(view);
            contentTextView = (TextView) view.findViewById(R.id.content);
            authorTextView = (TextView) view.findViewById(R.id.author);
            publishedTextView = (TextView) view.findViewById(R.id.published);
        }

        public TextView getContentTextView() {
            return contentTextView;
        }

        public TextView getAuthorTextView() {
            return authorTextView;
        }

        public TextView getPublishedTextView() {
            return publishedTextView;
        }


    }

    public void append(List<Comment> comments){
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getContentTextView().setText(comments.get(position).getContent());
        holder.getAuthorTextView().setText(comments.get(position).getAuthor());
        holder.getPublishedTextView().setText(comments.get(position).getPublished());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
*/