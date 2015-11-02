package canul.android.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import canul.android.Authentication;
import canul.android.DateConverter;
import canul.android.R;

public class ShowArticleActivity extends ListActivity {
    //URL
    private static final String BASE_URL = "http://dev.canul.fr/api/";
    private static final String AUTHENTICATE_URL = "authenticate";
    private static final String ARTICLES_URL = "articles/";
    private static final String COMMENTS_URL = "comments/byArticle/";
    private static String IDSTR;


    // JSON node key
    private static final String TOKEN = "token";
    private static final String SUCCESS = "success";
    private static final String MESSAGE = "message";
    private static final String TAG = "ShowArticlesActivity";

    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_ID = "_id";
    private static final String TAG_PUBLISHED = "published";
    private static final String TAG_AUTHOR = "author";
    private ProgressBar progressBar;


    private static TextView title;
    private static TextView author;
    private static TextView content;
    private static TextView published;
    private static TextView comment_status;

    ListView list;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        IDSTR = in.getStringExtra(TAG_ID);
        title = (TextView)findViewById(R.id.title);
        content= (TextView)findViewById(R.id.content);
        author = (TextView)findViewById(R.id.author);
        published = (TextView)findViewById(R.id.published);
        comment_status = (TextView)findViewById(R.id.comments);

        ListView lv = getListView();

        oslist = new ArrayList<HashMap<String, String>>();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        if(isConnected()) {
            new DownloadArticleTask().execute();
        }
        else{
            //no network connection
        }

        ActionBar actionBar = getActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition( R.anim.left_slide_in, R.anim.right_slide_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onClick(View v) {
        ListView lv = getListView();
        if(lv.getVisibility()==View.VISIBLE) {
            lv.setVisibility(View.GONE);
        }
        else{
            lv.setVisibility(View.VISIBLE);
        }

    }

    public boolean isConnected(){
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    class DownloadArticleTask extends AsyncTask<String, Void, String> {


        private String jsonStr;

        JSONArray article_content = null;

        @Override
        protected void onPreExecute() {
            // Here, show progress bar
            progressBar.setVisibility(View.VISIBLE);

        }
        private void authenticate() {
            /* Creates Http Client */
            OkHttpClient client = new OkHttpClient();

            /* Creates the request body */
            RequestBody body = new FormEncodingBuilder()
                    .add("name", "user")
                    .add("password", "password")
                    .build();

            /* Creates Authentication URL */
            String url = new StringBuilder()
                    .append(BASE_URL)
                    .append(AUTHENTICATE_URL)
                    .toString();



            /* Creates & sends the POST request */
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try {

                /* Sends the request and waits for the response */
                Response response = client.newCall(request).execute();
                String string = response.body().string();

                Log.v(TAG,string);
                /* Parses the response body */
                JSONObject json = new JSONObject(string);
                boolean  success = json.getBoolean(SUCCESS);

                if (success){
                    /* Adds token to the authentication object */
                    String token = json.getString(TOKEN);
                    Authentication.init(token);
                } else {
                    /* Prints the error message */

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... urls) {
            if(null == Authentication.getToken())
                authenticate();
            String token = Authentication.getToken();

            /* Creates Http Client */
            OkHttpClient client = new OkHttpClient();


            /* Creates Authentication URL */
            String url = new StringBuilder()
                    .append(BASE_URL)
                    .append(ARTICLES_URL)
                    .append(IDSTR)
                    .toString();


            /* Creates & sends the POST request */
            Request request = new Request.Builder()
                    .url(url)
                    .header("x-access-token", token)
                    .build();

            /* Sends the request */
            try {
                Response response = client.newCall(request).execute();
                String strresponse = response.body().string();
                Log.v(TAG, strresponse);
                JSONObject json = new JSONObject(strresponse);
                if(json.getBoolean("success")){

                    jsonStr = strresponse;
                }
                else {
                    jsonStr=null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e ) {
                e.printStackTrace();
            }

            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    JSONObject article = jsonObj.getJSONObject("article");

                    String jsontitle = article.getString("title");
                    String jsonauthor = article.getString("author");
                    String jsonpublished = article.getString("published");



                    //loading comments
                    article_content = article.getJSONArray("content");



                    StringBuilder ss = new StringBuilder("");


                    for(int i = 0; i < article_content.length(); i++) {
                        JSONObject c = article_content.getJSONObject(i);
                        String header = c.getString("header");
                        String body =c.getString("body");

                        ss.append("<h1>");
                        ss.append(header);
                        ss.append("</h1> <p>");
                        ss.append(body);
                        ss.append("</p> ");
                    }


                    Log.v(TAG,ss.toString());
                    setTitle(jsontitle);
                    author.setText(jsonauthor);
                    published.setText("date : "+ DateConverter.convert(jsonpublished));
                    content.setText(Html.fromHtml(ss.toString()));

                }catch(JSONException e){
                        e.printStackTrace();
                    }

                }
            progressBar.setVisibility(View.GONE);


        }
    }

}
