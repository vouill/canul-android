package canul.android;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class IndexActivity extends ListActivity {

    private static final String DEBUG_TAG = "HttpExample";
    private TextView textView;
    private ProgressBar progressBar;
    String stringUrlArticles = "http://dev.canul.fr/api/articles";


    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_AUTHOR = "author";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        textView = (TextView) findViewById(R.id.myText);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrlArticles);

        } else {
            textView.setText("No network connection available.");
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
            String success=null;
            try {

                response = example.run(urls[0]);
                Thread.sleep(2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            jsonStr=response;
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






                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                intermediate = String.valueOf(articles.length());
                //intermediate=response;
                //intermediate=success;

            }
            return response;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            textView.setText(intermediate);

        }




    }


}
