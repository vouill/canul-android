package canul.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import canul.android.Hash;
import canul.android.R;
import canul.android.interfaces.TaskInterface;
import canul.android.tasks.TokenTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment implements TaskInterface{
    private final static String TAG = LoginFragment.class.getName();
    private final static String PERMISSIONS = "public_profile";

    private EditText usernameEditText;
    private EditText passwordEditText;

    private ProgressBar progressBar;

    private Button loginButton;


    private CallbackManager callbackManager;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        private ProfileTracker mProfileTracker;

        @Override
        public void onSuccess(LoginResult loginResult) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    Log.v("facebook - profile", profile2.getFirstName());
                    Profile.setCurrentProfile(profile2);
                    mProfileTracker.stopTracking();
                }
            };
            mProfileTracker.startTracking();
        }

        @Override
        public void onCancel() {
            Log.v("facebook - onCancel", "cancelled");
        }

        @Override
        public void onError(FacebookException e) {
            Log.v("facebook - onError", e.getMessage());
        }
    };


    public LoginFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton facebookButton = (LoginButton) view.findViewById(R.id.login_button);
        //TODO remove this dirty code...
        final LoginFragment that = this;

        facebookButton.setReadPermissions(PERMISSIONS);

        facebookButton.setFragment(this);
        facebookButton.registerCallback(callbackManager, callback);

        usernameEditText = (EditText) view.findViewById(R.id.username);
        passwordEditText = (EditText) view.findViewById(R.id.password);
        loginButton = (Button) view.findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String hash = Hash.digest(password);
                TokenTask task = new TokenTask(that);
                task.execute(username,hash);
            }
        });

        Button cancelButton = (Button) view.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.stay, R.anim.down_slide_out);
            }
        });


        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(JSONObject json) {

    }

    @Override
    public void onFailure(JSONObject json) {

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
