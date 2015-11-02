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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import canul.android.DateConverter;
import canul.android.R;
import canul.android.adapters.ExtractAdapter;
import canul.android.interfaces.EndlessScrollInterface;
import canul.android.interfaces.TaskInterface;
import canul.android.listeners.ArticlesEndlessScrollListener;
import canul.android.models.Article;
import canul.android.tasks.ArticlesTask;

public class ShowExtractsActivity extends Activity implements EndlessScrollInterface, TaskInterface, View.OnClickListener {


    private static final String TAG = ShowExtractsActivity.class.getName();
    private static final String ARTICLES_TAG = "articles";

    private static final String ID_TAG = "_id";
    private static final String AUTHOR_TAG = "author";
    private static final String PUBLISHED_TAG = "published";
    private static final String TITLE_TAG = "title";
    private static final String EXTRACT_TAG = "extract";

    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.progress_bar) ProgressBar progressBar;


    private ExtractAdapter adapter;
    private LinearLayoutManager manager;

    private List<Article> articles;

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_show_articles);

        ButterKnife.bind(this);

        this.manager = new LinearLayoutManager(this);
        this.articles = new ArrayList();
        this.adapter = new ExtractAdapter(this);

        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(manager);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setOnScrollListener(new ArticlesEndlessScrollListener(manager, this));

        if (isConnected())
            new ArticlesTask(this).execute();

    }

    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.up_slide_in,R.anim.stay);
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onSuccess(JSONObject json) {
        Log.v(TAG, json.toString());
        List<Article> extracts = new ArrayList<>();
        try {
            JSONArray array = json.getJSONArray(ARTICLES_TAG);
            for (int i = 0; i < array.length(); i++) {
                JSONObject article = array.getJSONObject(i);
                String id = article.getString(ID_TAG);
                String author = article.getString(AUTHOR_TAG);
                String published = DateConverter.convert(article.getString(PUBLISHED_TAG));
                String extract = article.getString(EXTRACT_TAG);
                String title = article.getString(TITLE_TAG);
                extracts.add(new Article(id, title, author, published, extract));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.articles.addAll(extracts);
        adapter.setArticles(this.articles);
    }

    @Override
    public void onFailure(JSONObject json) {
        //TODO
    }

    @Override
    public void setProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildPosition(view);
        String id = this.articles.get(position).getId();

        Intent intent = new Intent(getApplicationContext(), ShowArticleActivity.class);
        intent.putExtra(ID_TAG, id);
        startActivity(intent);
        overridePendingTransition(R.anim.right_slide_in,R.anim.left_slide_out);

    }

    @Override
    public void onLoadMore() {
        String id = this.articles.get(this.articles.size()-1).getId();
        new ArticlesTask(this).execute(id);
    }
}
