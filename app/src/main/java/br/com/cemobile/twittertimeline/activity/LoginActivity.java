package br.com.cemobile.twittertimeline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.cemobile.twittertimeline.Constant;
import br.com.cemobile.twittertimeline.R;
import br.com.cemobile.twittertimeline.facade.TwitterFacade;

/**
 * Created by celso on 22/04/16.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewById(R.id.twitter_button)
    TwitterLoginButton loginButton;

    TwitterAuthClient client;

    @AfterViews
    void setUpLoginButton() {
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(Constant.TAG, "App authenticated!");
            }

            @Override
            public void failure(TwitterException e) {
                Log.e(Constant.TAG, "App authentication failed! " + e.getMessage());
                Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterFacade.initFabric(getApplicationContext());

        client = new TwitterAuthClient();
        client.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(Constant.TAG, "Login worked!");
            }

            @Override
            public void failure(TwitterException e) {
                Log.e(Constant.TAG, "Login failed! " + e.getMessage());

                Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        client.onActivityResult(requestCode, resultCode, data);

        Intent irParaTimeline = new Intent(this, TimelineActivity_.class);
        startActivity(irParaTimeline);
    }

}
