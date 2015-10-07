package canul.android;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class IndexActivity extends Activity {

    private static final String DEBUG_TAG = "HttpExample";
    private EditText urlText;
    private TextView textView;
    private Button submitBtn;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        urlText = (EditText) findViewById(R.id.myUrl);
        textView = (TextView) findViewById(R.id.myText);
        submitBtn = (Button) findViewById(R.id.submit_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stringUrl = urlText.getText().toString();
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadWebpageTask().execute(stringUrl);

                } else {
                    textView.setText("No network connection available.");
                }
            }
        });

    }


    public class GetExample {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }



    }

    class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        private String datasite;
        private static final String DEBUG_TAG = "HttpExample";
        private static final String TAG = "MyActivity";


        @Override
        protected void onPreExecute() {
            // Here, show progress bar
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... urls) {
            GetExample example = new GetExample();
            String response = null;
            try {

                response = example.run(urls[0]);
                Thread.sleep(2000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            datasite=response;


            return response;

            }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            textView.setText(datasite);
        }




    }


}
