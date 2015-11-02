package canul.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import canul.android.Authentication;
import canul.android.R;

public class EditCommentActivity extends Activity {


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
    private static final String TAG = "ShowExtractsActivity";

    private static final String TAG_ID = "_id";
    private ProgressBar progressBar;

    private EditText authorinput;
    private EditText commentinput;


    private static String comment;
    private static String author;

    static String jsonStringPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);

        Intent in = getIntent();
        IDSTR = in.getStringExtra(TAG_ID);


        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        authorinput = (EditText) findViewById(R.id.author);
        commentinput = (EditText) findViewById(R.id.comment);


    }



    public String ToJson(String author, String comment) {
        String jsonstring = new StringBuilder()
                .append("{\"author\":\"")
                .append(author)
                .append("\",\"content\":\"")
                .append(comment)
                .append("\"}")
                .toString();


        return jsonstring;
    }

    class PostCommentTask extends AsyncTask<String, Void, String> {
        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");


        private String jsonStr;

        JSONArray comments = null;

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
                boolean success = json.getBoolean(SUCCESS);

                if (success) {
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
            if (null == Authentication.getToken())
                authenticate();

            String token = Authentication.getToken();


            OkHttpClient client = new OkHttpClient();

            // Creates Authentication URL
            String url = new StringBuilder()
                    .append(BASE_URL)
                    .append(COMMENTS_URL)
                    .append(IDSTR)
                    .toString();
            Log.v(TAG, url);
            Log.v(TAG, jsonStringPost);

            RequestBody body = RequestBody.create(JSON, jsonStringPost);
            Request request = new Request.Builder()
                    .url(url)
                    .header("x-access-token", token)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();

                String result = response.body().string();
                Log.v(TAG, result);
                return result;


            } catch (IOException e) {
                e.printStackTrace();
            }




            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            Context context = getApplicationContext();
            CharSequence text = "comment sent";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();




            progressBar.setVisibility(View.GONE);



        }
    }
}