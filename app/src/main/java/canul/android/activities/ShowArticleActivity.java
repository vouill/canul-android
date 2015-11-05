package canul.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import canul.android.DateConverter;
import canul.android.R;
import canul.android.adapters.ArticleAdapter;
import canul.android.interfaces.EndlessScrollInterface;
import canul.android.interfaces.TaskInterface;
import canul.android.listeners.ArticlesEndlessScrollListener;
import canul.android.models.Article;
import canul.android.models.Comment;
import canul.android.tasks.ArticleTask;
import canul.android.tasks.CommentsTask;

/**
 * Created by Chazz on 22/10/15.
 */
public class ShowArticleActivity extends Activity implements EndlessScrollInterface, TaskInterface, View.OnClickListener {

    private static final String TAG = ShowArticleActivity.class.getName();
    private static final String ARTICLE_TAG = "article";

    private static final String ID_TAG = "_id";
    private static final String AUTHOR_TAG = "author";
    private static final String PUBLISHED_TAG = "published";
    private static final String TITLE_TAG = "title";
    private static final String EXTRACT_TAG = "extract";
    public static String COMMENTS_TAG = "comments";
    public static String CONTENT_TAG = "content";
    public static String idstr;

    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private LinearLayoutManager manager;

    private ProgressBar progressBar;

    private Article article;
    private List<Comment> comments ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String id = in.getStringExtra(ID_TAG);
        idstr=id;

        Log.v(TAG, id);

        setContentView(R.layout.activity_show_article_and_comments);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new ArticleAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new ArticlesEndlessScrollListener(manager, this));
        comments = new ArrayList<>();

        if (isConnected()) {
            new ArticleTask(this).execute(id);
            new CommentsTask(this).execute(id);
        }
    }

    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onLoadMore() {
        new CommentsTask(this).execute(comments.get(comments.size()-1).getId());
    }

    @Override
    public void onClick(View v) {

    }
    public void postComment(View v) {
        Intent intent = new Intent(this, EditCommentActivity.class);
        intent.putExtra(ID_TAG,idstr);
        startActivity(intent);
    }


    @Override
    public void onSuccess(JSONObject json) {
        if(json.has("comments")){
            try {
                JSONArray array = json.getJSONArray(COMMENTS_TAG);
                //TODO tidy this
                comments.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject comment = array.getJSONObject(i);
                    String author = comment.getString(AUTHOR_TAG);
                    String published = DateConverter.convert(comment.getString(PUBLISHED_TAG));
                    String content = comment.getString(CONTENT_TAG);
                    String id = comment.getString(ID_TAG);
                    comments.add(new Comment(id, author, published, content));
                }
                adapter.setComments(comments);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.v(TAG, "comments found");
        } else if (json.has("article")){
            try {
                JSONObject article = json.getJSONObject(ARTICLE_TAG);
                String author = article.getString(AUTHOR_TAG);
                String id = article.getString(ID_TAG);
                String published = DateConverter.convert(article.getString(PUBLISHED_TAG));
                String content = article.getString(CONTENT_TAG);
                String title = article.getString(TITLE_TAG);
                adapter.setArticle(new Article(id,  title,  author,  published,  content));
                Log.v(TAG, "article found");
            }
            catch( JSONException e) {
                e.printStackTrace();
            }
        }
        Log.v(TAG, json.toString());
    }

    @Override
    public void onFailure(JSONObject json) {

    }

    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismissProgressBar() {

    }
}
