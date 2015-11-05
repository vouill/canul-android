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

import butterknife.Bind;
import butterknife.ButterKnife;
import canul.android.Authentication;
import canul.android.R;
import canul.android.interfaces.TaskInterface;
import canul.android.tasks.PostCommentTask;

public class EditCommentActivity extends Activity implements TaskInterface {


    private static String IDSTR;

    private static final String TAG_ID = "_id";


    @Bind(R.id.progress_bar) ProgressBar progressBar;
    @Bind(R.id.author) EditText authorEditText;
    @Bind(R.id.comment) EditText commentEditText;

    static String jsonStringPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comment);

        Intent in = getIntent();
        IDSTR = in.getStringExtra(TAG_ID);

        ButterKnife.bind(this);

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
    public void sendComm(View v) {
        jsonStringPost=ToJson(authorEditText.getText().toString(), commentEditText.getText().toString());
        new PostCommentTask(this).execute(IDSTR, jsonStringPost);
    }

    @Override
    public void onSuccess(JSONObject json) {
        this.finish();
    }

    @Override
    public void onFailure(JSONObject json) {
        this.finish();
    }

    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismissProgressBar() {

    }
}