package canul.android.holders;

import android.view.View;
import android.widget.TextView;

import canul.android.R;

/**
 * Created by Chazz on 02/11/15.
 */
public class ExtractViewHolder extends ViewHolder {

    private TextView authorTextView;
    private TextView publishedTextView;
    private TextView extractTextView;
    private TextView titleTextView;

    public ExtractViewHolder(View view) {
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
