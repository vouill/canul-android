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

import android.widget.EditText;

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

    private static final String DEBUG_TAG = "HttpExample";
    private static final String AUTHENTICATE_URL = "http://dev.canul.fr/api/authenticate";
    private static final String TOKEN = "token";
    private static final String SUCCESS = "success";
    private static final String TAG = "IndexActivity";
    private EditText urlText;

    private TextView textView;
    private ProgressBar progressBar;
    String stringUrlArticles = "http://dev.canul.fr/api/articles";

    ListView list;
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_AUTHOR = "author";


    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();


    private AuthenticateTask authenticateTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        oslist = new ArrayList<HashMap<String, String>>();


        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
          /* Creates & executes an authenticateTask */
          new AuthenticateTask().execute();
            if()
            new DownloadWebpageTask().execute();
        } else
            textView.setText("No network connection available.");


        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String title = ((TextView) view.findViewById(R.id.title))
                        .getText().toString();
                String content = ((TextView) view.findViewById(R.id.content))
                        .getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),SingleArticleActivity.class);
                in.putExtra(TAG_TITLE, title);
                in.putExtra(TAG_CONTENT, content);

                startActivity(in);

            }
        });

    }

    public class AuthenticateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            /* Creates Http Client */
            OkHttpClient client = new OkHttpClient();

            /* Creates the request body */
            RequestBody body = new FormEncodingBuilder()
                    .add("name", "user")
                    .add("password", "password")
                    .build();

            /* Creates & sends the POST request */
            Request request = new Request.Builder()
                    .url(AUTHENTICATE_URL)
                    .post(body)
                    .build();

            try {

                /* Sends the request and waits for the response */
                Response response = client.newCall(request).execute();
                String string = response.body().string();


                /* Parses the response body */
                JSONObject json = new JSONObject(string);
                Boolean success = json.getBoolean(SUCCESS);

                if (success){
                    String token = json.getString(TOKEN);
                    Authentication.init(token);
                    Log.v(TAG, Authentication.getToken());
                } else {

                }

                Log.v(TAG, string);
            } catch (IOException e) {
                  e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public class GetJson {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .header("x-access-token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoidXNlciIsInBhc3N3b3JkIjoicGFzc3dvcmQiLCJhZG1pbiI6ZmFsc2UsIl9pZCI6IjU2MTY3ZjMyZjI5YTBkNTc0ZTAyZTk0YyIsIl9fdiI6MH0.n_Wve4X-nN4yZ5z9ratiT4cVI7aV0A8shXT9WMHEVl8")
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }

    class DownloadWebpageTask extends AsyncTask<String, Void, String> {


        private static final String DEBUG_TAG = "HttpExample";
        private static final String TAG = "MyActivity";
        private String intermediate;

        // JSON Node names
        //JSON DATA
        private String jsonStr;
        // contacts JSONArray
        JSONArray articles = null;





        @Override
        protected void onPreExecute() {
            // Here, show progress bar
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... urls) {
            GetJson example = new GetJson();
            String response = null;

            try {
                String url = "http://dev.canul.fr/api/articles";
                response = example.run(url);
                Thread.sleep(2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            //intermediate = String.valueOf(articles.length());
            intermediate=response;
            //intermediate=success;

            return response;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            String success=null;
            jsonStr=intermediate;


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    success =jsonObj.getString("success");
                    articles = jsonObj.getJSONArray("articles");

                    for(int i = 0; i < articles.length(); i++) {
                        JSONObject c = articles.getJSONObject(i);
                        String title = c.getString(TAG_TITLE);
                        String content = c.getString(TAG_CONTENT);
                        String author = c.getString(TAG_AUTHOR);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_TITLE, title);
                        map.put(TAG_CONTENT, content);
                        map.put(TAG_AUTHOR, author);



                        oslist.add(map);

                        ListAdapter adapter = new SimpleAdapter(IndexActivity.this, oslist,
                                R.layout.list_item,
                                new String[] { TAG_TITLE,TAG_CONTENT, TAG_AUTHOR }, new int[] {
                                R.id.title,R.id.content, R.id.author});
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
