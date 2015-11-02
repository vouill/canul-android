package canul.android.tasks;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import canul.android.Authentication;
import canul.android.interfaces.TaskInterface;

/**
 * Created by Chazz on 15/10/15.
 */
public class ArticlesTask extends CanulTask {

    private final static String TAG = ArticlesTask.class.getName();

    TaskInterface taskInterface;


    @Override
    protected void onPreExecute() {
        taskInterface.setProgressBar();
    }

    public ArticlesTask(TaskInterface taskInterface) {
        this.taskInterface = taskInterface;
    }

    @Override
    protected Void doInBackground(String... params) {
        // authentication is done on the super class
        super.doInBackground();
        String last = null;
        if(params.length > 0){
          last = params[0];
        }

        // Creates Http Client
        OkHttpClient client = new OkHttpClient();


        // Creates Authentication URL
        String url = new StringBuilder()
                .append(BASE_URL)
                .append(ARTICLES_URL)
                .toString();
        Log.v(TAG, url);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .header("x-access-token", Authentication.getToken());

        if(last != null)
            builder.addHeader("last-article", last);


        // Sends the request
        try {
            Response response = client.newCall(builder.build()).execute();
            String string = response.body().string();
            this.json = new JSONObject(string);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        taskInterface.dismissProgressBar();
        if (isSucceed())
            taskInterface.onSuccess(this.json);
        else
            taskInterface.onFailure(this.json);
    }
}

