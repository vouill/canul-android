package canul.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleArticleActivity extends Activity {
    // JSON node keys

    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_ID = "_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_article);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String title = in.getStringExtra(TAG_TITLE);
        String content = in.getStringExtra(TAG_CONTENT);
        String id_articles = in.getStringExtra(TAG_ID);


        // Displaying all values on the screen
        TextView lbltitle = (TextView) findViewById(R.id.arttitle);
        TextView lblcontent = (TextView) findViewById(R.id.artcontent);
        TextView lblid = (TextView) findViewById(R.id.id_articles);



        lbltitle.setText(title);
        lblcontent.setText(content);
        lblid.setText(id_articles);

    }

}
