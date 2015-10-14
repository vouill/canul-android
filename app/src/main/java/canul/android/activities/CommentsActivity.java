package canul.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import canul.android.R;
import canul.android.interfaces.CommentsTaskInterface;
import canul.android.tasks.CommentsTask;

public class CommentsActivity extends Activity implements CommentsTaskInterface {

    public static String TAG = CommentsActivity.class.getName();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        new CommentsTask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(JSONObject json) {
        
    }

    @Override
    public void onFailure(JSONObject json) {
        Log.v(TAG, "failure");
        try {
            Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show();
        } catch(JSONException e) {
            Toast.makeText(this, "An internal error has occurred...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
