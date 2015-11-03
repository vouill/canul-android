package canul.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import canul.android.R;
import canul.android.holders.ArticleViewHolder;
import canul.android.holders.CommentViewHolder;
import canul.android.models.Article;
import canul.android.models.Comment;

/**
 * Created by Chazz on 22/10/15.
 * Adapted from http://doublewong.com/2014/create-recyclerview-with-multiple-view-type/
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Object> objects = new ArrayList<>();

    private static final String TAG = ArticleAdapter.class.getName();
    private static final int TYPE_ARTICLE = 0;
    private static final int TYPE_COMMENT = 1;

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (objects.size() == 0)
            return TYPE_COMMENT;
        if (objects.get(position) instanceof Article) {
            viewType = TYPE_ARTICLE;
        } else {
            viewType = TYPE_COMMENT;
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_ARTICLE:
                ViewGroup articleView = (ViewGroup) inflater.inflate(R.layout.view_article, parent, false);
                ArticleViewHolder articleViewHolder = new ArticleViewHolder(articleView);
                return articleViewHolder;
            default:
                ViewGroup commentView = (ViewGroup) inflater.inflate(R.layout.view_comment, parent, false);
                CommentViewHolder commentViewHolder = new CommentViewHolder(commentView);
                return commentViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_ARTICLE:
                Article article = (Article) objects.get(0);
                ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
                articleViewHolder.getAuthorTextView().setText(article.getAuthor());
                articleViewHolder.getTitleTextView().setText(article.getTitle());
                articleViewHolder.getContentTextView().setText(article.getExtract());
                articleViewHolder.getPublishedTextView().setText(article.getPublished());

                break;
            default:
                Comment comment = (Comment) objects.get(position);
                CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
                commentViewHolder.getAuthorTextView().setText(comment.getAuthor());
                commentViewHolder.getContentTextView().setText(comment.getContent());
                commentViewHolder.getPublishedTextView().setText(comment.getPublished());

        }
    }

    public void setArticle(Article article) {
        this.objects.add(article);
        notifyDataSetChanged();
    }

    public void setComments(List<Comment> comments) {
        if (comments.size() > 0) {
            this.objects.addAll(comments);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return this.objects.size();
    }
}
