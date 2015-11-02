package canul.android.holders;


import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import canul.android.R;

/**
 * Created by Chazz on 02/11/15.
 */
public class ArticleViewHolder extends ViewHolder {

    @Bind(R.id.author) TextView authorTextView;
    @Bind(R.id.content) TextView contentTextView;
    @Bind(R.id.title) TextView titleTextView;
    @Bind(R.id.published) TextView publishedTextView;

    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getAuthorTextView() {
        return authorTextView;
    }

    public TextView getContentTextView() {
        return contentTextView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getPublishedTextView() {
        return publishedTextView;
    }
}
