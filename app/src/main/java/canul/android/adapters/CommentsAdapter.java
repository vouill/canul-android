package canul.android.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import canul.android.R;
import canul.android.models.Comment;

/**
 * Created by Chazz on 14/10/15.
 */
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
                .inflate(R.layout.comment_item, parent, false);
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
