package canul.android.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import canul.android.Authentication;

/**
 * Created by Chazz on 14/10/15.
 */
public abstract class CanulTask extends AsyncTask<String,Void,Void> {

    public static final String BASE_URL = "http://dev.canul.fr/api/";
    public static final String AUTHENTICATE_URL = "authenticate";
    public static final String ARTICLES_URL = "articles";

    public static final String TAG = CanulTask.class.getName();

    public static final String SUCCESS_TAG = "success";
    public static final String MESSAGE_TAG = "message";
    public static final String TOKEN_TAG = "token";

    protected JSONObject json;


    private void authenticate() {
        Log.v(TAG, "authentication");
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
            json = new JSONObject(string);

            if (isSucceed()){
                /* Adds pro to the authentication object */
                String token = json.getString(TOKEN_TAG);
                Authentication.init(token);
            } else {
                /* Prints the error message */
                Log.v(TAG, json.getString(MESSAGE_TAG));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Void doInBackground(String... params) {
        /* authenticate before doing the actual request */
        authenticate();
        /* authentication is good */
        return null;
    }

    boolean isSucceed(){
        boolean success = false;
        try {
            success = json.getBoolean(SUCCESS_TAG);
        } catch  (JSONException e) {
            e.printStackTrace();
        }
        return success;
    }




}
