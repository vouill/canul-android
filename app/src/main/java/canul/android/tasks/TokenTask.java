package canul.android.tasks;

import android.util.Log;

import canul.android.interfaces.TaskInterface;

/**
 * Created by Chazz on 20/10/15.
 */
public class TokenTask extends CanulTask {

    TaskInterface taskInterface;
    private final static String TAG = TokenTask.class.getName();


    @Override
    protected void onPreExecute() {
        taskInterface.setProgressBar();
    }

    public TokenTask(TaskInterface taskInterface) {
        this.taskInterface = taskInterface;
    }


    @Override
    protected Void doInBackground(String... params) {
        String username = params[0];
        String password = params[1];


        Log.v(TAG, username);
        Log.v(TAG, password);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        taskInterface.dismissProgressBar();
    }
}
