package canul.android.holders;

import android.view.View;
import android.widget.TextView;


import butterknife.ButterKnife;
import butterknife.Bind;
import canul.android.R;

/**
 * Created by Chazz on 02/11/15.
 */
public class CommentViewHolder extends ViewHolder {

    @Bind(R.id.author) TextView authorTextView;
    @Bind(R.id.published) TextView publishedTextView;
    @Bind(R.id.content) TextView contentTextView;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getAuthorTextView() {
        return authorTextView;
    }

    public TextView getPublishedTextView(){
        return publishedTextView;
    }

    public TextView getContentTextView() {
        return contentTextView;
    }
}
