package canul.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import canul.android.DateConverter;
import canul.android.R;
import canul.android.adapters.CommentsAdapter;
import canul.android.interfaces.CommentsTaskInterface;
import canul.android.models.Comment;
import canul.android.tasks.CommentsTask;

public class CommentsActivity extends Activity implements CommentsTaskInterface {

    public static String TAG = CommentsActivity.class.getName();
    public static String COMMENTS_TAG = "comments";
    public static String CONTENT_TAG = "content";
    public static String AUTHOR_TAG = "author";
    public static String PUBLISHED_TAG = "published";
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private CommentsAdapter adapter;
    private RecyclerView.LayoutManager manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        List<Comment> comments = new ArrayList();

        adapter = new CommentsAdapter(comments);

        recyclerView.setAdapter(adapter);


        new CommentsTask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(JSONObject json) {
        List<Comment> comments = new ArrayList<>();
        try {
            JSONArray array = json.getJSONArray(COMMENTS_TAG);
            for(int i = 0 ; i < array.length() ; i++){
                JSONObject comment = array.getJSONObject(i);
                String author = comment.getString(AUTHOR_TAG);
                String published = DateConverter.convert(comment.getString(PUBLISHED_TAG));
                String content = comment.getString(CONTENT_TAG);
                comments.add(new Comment(author,published,content));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.append(comments);
    }

    @Override
    public void onFailure(JSONObject json) {
        Log.v(TAG, "failure");
        try {
            Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show();
        } catch(JSONException e) {
            Toast.makeText(this, "An internal error has occurred...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
