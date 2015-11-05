package canul.android.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
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
import canul.android.interfaces.TaskInterface;

/**
 * Created by Chazz on 05/11/15.
 */
public class PostCommentTask extends CanulTask {

    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private TaskInterface taskInterface;


    public PostCommentTask(TaskInterface taskInterface) {
        this.taskInterface = taskInterface;
    }

    @Override
    protected void onPreExecute() {
        // Here, show progress bar
        taskInterface.setProgressBar();

    }


    @Override
    protected Void doInBackground(String... params) {
        /* authentication is done on the super class */
        super.doInBackground();

        String id = params[0];
        String json = params[1];

        OkHttpClient client = new OkHttpClient();

        // Creates Authentication URL
        String url = new StringBuilder()
                .append(BASE_URL)
                .append(COMMENTS_BY_ARTICLE_URL)
                .append("/")
                .append(id)
                .toString();


        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header("x-access-token", Authentication.getToken())
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = null;

        try {
            response = client.newCall(request).execute();

            String string = response.body().string();
            this.json = new JSONObject(string);
            Log.v(TAG, string);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // onPostExecute displays the results of the AsyncTask.
/*
    @Override
    protected void onPostExecute() {
        Context context = getApplicationContext();
        CharSequence text = "comment sent";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        taskInterface.dismissProgressBar();
    }

*/
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
