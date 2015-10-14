package canul.android.interfaces;

import android.widget.ProgressBar;

import org.json.JSONObject;

/**
 * Created by Chazz on 14/10/15.
 */
public interface CommentsTaskInterface {


    void onSuccess(JSONObject json);

    void onFailure(JSONObject json);

    void setProgressBar();

    void dismissProgressBar();


}
