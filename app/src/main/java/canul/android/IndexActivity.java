package canul.android;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
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

public class IndexActivity extends ListActivity {

    //URLS
    private static final String BASE_URL = "http://dev.canul.fr/api/";
    private static final String AUTHENTICATE_URL = "authenticate";
    private static final String ARTICLES_URL = "articles";


    private static final String TOKEN = "token";
    private static final String SUCCESS = "success";
    private static final String MESSAGE = "message";
    private static final String TAG = "IndexActivity";


    /* Recycler View */

   // ListView list;
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "extract";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_PUBLISHED = "published";
    private static final String ARTICLES = "articles";
    private static final String TAG_ID = "_id";

    //LAYOUT
    private TextView textView;
        private ProgressBar progressBar;

    //list
    ListView list;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        oslist = new ArrayList<HashMap<String, String>>();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        Log.v(TAG, "asks for tokens");

        if(isConnected())
            new DownloadArticlesListTask().execute();
        else
            textView.setText("No network connection available.");


        ListView lv = getListView();

        // Listview on item click listener

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),SingleArticleActivity.class);

                in.putExtra(TAG_ID, oslist.get(position).get(TAG_ID));

                startActivity(in);

            }
        });

    }

    public boolean isConnected(){
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean isSucceed(JSONObject json) throws JSONException{
        return json.getBoolean(SUCCESS);
    }


    class DownloadArticlesListTask extends AsyncTask<String, Void, String> {


        private String jsonStr;
        // contacts JSONArray
        JSONArray articles = null;

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


                /* Parses the response body */
                JSONObject json = new JSONObject(string);
                boolean  success = json.getBoolean(SUCCESS);

                if (success){
                    /* Adds pro to the authentication object */
                    String token = json.getString(TOKEN);
                    Authentication.init(token);
                } else {
                    /* Prints the error message */
                    Log.v(TAG, json.getString(MESSAGE));
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
                    .toString();


            /* Creates & sends the POST request */
            Request request = new Request.Builder()
                    .url(url)
                    .header("x-access-token", token)
                    .build();

            /* Sends the request */
            try {
                Response response = client.newCall(request).execute();
                String string = response.body().string();
                JSONObject json = new JSONObject(string);
                if(isSucceed(json)){
                    Log.v(TAG, json.getJSONArray(ARTICLES).toString());
                    jsonStr = string;
                }
                Log.v(TAG, json.toString());
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
            String success=null;


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    articles = jsonObj.getJSONArray(ARTICLES);

                    for(int i = 0; i < articles.length(); i++) {
                        JSONObject c = articles.getJSONObject(i);
                        String title = c.getString(TAG_TITLE);
                        String content = c.getString(TAG_CONTENT);
                        String author = c.getString(TAG_AUTHOR);
                        String published = c.getString(TAG_PUBLISHED);
                        String id_articles = c.getString(TAG_ID);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_TITLE,title);
                        map.put(TAG_CONTENT,content);
                        map.put(TAG_AUTHOR,author);
                        map.put(TAG_PUBLISHED,published);
                        map.put(TAG_ID,id_articles);

                        oslist.add(map);

                        ListAdapter adapter = new SimpleAdapter(IndexActivity.this, oslist,
                                R.layout.list_item,
                                new String[] { TAG_TITLE,TAG_CONTENT, TAG_AUTHOR  , TAG_PUBLISHED}, new int[] {
                                R.id.title,R.id.content, R.id.author,R.id.published});
                        setListAdapter(adapter);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                progressBar.setVisibility(View.GONE);


            //textView.setText(intermediate);

        }
    }
}
