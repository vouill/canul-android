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
 * Created by Chazz on 22/10/15.
 */
public class ArticleTask extends CanulTask {

    private final static String TAG = ArticleTask.class.getName();

    private final TaskInterface taskInterface;

    public ArticleTask(TaskInterface taskInterface) {
        this.taskInterface = taskInterface;
    }

    @Override
    protected void onPreExecute() {
        taskInterface.setProgressBar();
    }

    @Override
    protected Void doInBackground(String... params) {
        super.doInBackground();

        String id = params[0];
        if (null != id) {

            // Creates Http Client
            OkHttpClient client = new OkHttpClient();

            // Creates Authentication URL
            String url = new StringBuilder()
                    .append(BASE_URL)
                    .append(ARTICLES_URL)
                    .append("/")
                    .append(id)
                    .toString();

            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .header("x-access-token", Authentication.getToken());

            // Sends the request
            try {
                Response response = client.newCall(builder.build()).execute();
                String string = response.body().string();
                this.json = new JSONObject(string);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
