package canul.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import canul.android.R;
import canul.android.activities.ShowArticleAndCommentsActivity;
import canul.android.activities.ShowArticlesActivity;
import canul.android.models.Article;
import canul.android.models.Comment;

/**
 * Created by Chazz on 22/10/15.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Article article;
    private List<Comment> comments;
    private final ShowArticleAndCommentsActivity activity;

    private final static String TAG = ArticlesAdapter.class.getName();

    public ArticleAdapter(ShowArticleAndCommentsActivity activity,
                          Article article, List<Comment> comments) {
        this.activity = activity;
        this.article = article;
        this.comments = comments;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_article, parent, false);

        view.setOnClickListener(activity);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(null != article)
        if(position == 0){
            holder.getExtractTextView().setText(article.getExtract());
            holder.getAuthorTextView().setText(article.getAuthor());
            holder.getPublishedTextView().setText(article.getPublished());
            holder.getTitleTextView().setText(article.getTitle());
        } else {
            holder.getAuthorTextView().setText(comments.get(position).getAuthor());
        }

    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public void setArticle(Article article ){
        this.article= article;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return comments.size() + 1;
    }


}
