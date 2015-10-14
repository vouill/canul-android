package canul.android.tasks;

import android.os.AsyncTask;
import android.util.Log;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import canul.android.Authentication;
import canul.android.interfaces.CommentsTaskInterface;

/**
 * Created by Chazz on 14/10/15.
 */
public class CommentsTask extends CanulTask{

    private final static String TAG = CommentsTask.class.getName();

    CommentsTaskInterface taskInterface;




    @Override
    protected void onPreExecute() {
        taskInterface.setProgressBar();
    }

    public CommentsTask(CommentsTaskInterface taskInterface) {
        this.taskInterface = taskInterface;
    }


    @Override
    protected Void doInBackground(String... params) {
        /* authentication is done on the super class */
        super.doInBackground();

        /* Creates Http Client */
        OkHttpClient client = new OkHttpClient();

        /* Creates Authentication URL */
        String url = new StringBuilder()
                .append(BASE_URL)
                .append(COMMENTS_BY_ARTICLE_URL)
                .append("/561e23026f8e19f41074ee21")
                .toString();


            /* Creates & sends the POST request */
        Request request = new Request.Builder()
                .url(url)
                .header("x-access-token", Authentication.getToken())
                .build();

            /* Sends the request */
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            this.json = new JSONObject(string);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e ) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        taskInterface.dismissProgressBar();
        if(isSucceed())
            taskInterface.onSuccess(this.json);
        else
            taskInterface.onFailure(this.json);
    }
}
