package canul.android.interfaces;

import org.json.JSONObject;

/**
 * Created by Chazz on 15/10/15.
 */
public interface TaskInterface {

    void onSuccess(JSONObject json);

    void onFailure(JSONObject json);

    void setProgressBar();

    void dismissProgressBar();


}
