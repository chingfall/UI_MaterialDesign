package com.nws.ching.ui_materialdesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;
import org.json.JSONObject;

public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = " bKANkr4cgPZSd45i5N1quFGQO";
    private static final String TWITTER_SECRET = "ZLstdc2FvbwRBKlljeuV9bHjU6opvB8EZ2EIaubMe0XL22XOM5";

    CallbackManager callbackManager;
    private AccessToken accessToken;

    private TwitterLoginButton TloginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化fb Sdk,要放在第一行
        FacebookSdk.sdkInitialize(getApplicationContext());
        //twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //宣告callback Manager
        callbackManager = CallbackManager.Factory.create();

        //定義Button(FB & Twitter)
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        TloginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        //loginButton增加callback function
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();

                Log.d("FB", "access token got.");

                //send request and call graph api
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //read user's name, ID, fb page link
                        Log.d("FB", "complete");
                        Log.d("FB", object.optString("name"));
                        Log.d("FB", object.optString("link"));
                        Log.d("FB", object.optString("id"));
                    }
                });
                //package user's information,and send request
                Bundle parameters = new Bundle();
                parameters.putString("Fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("FB", "CANCEL");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB", error.toString());
            }
        });

        //Twitter login button callback
        TloginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                String msg = "@" + session.getUserName() + "logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    //Let login result return to callback Manager
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        //FB callback result
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //Twitter callback result
        TloginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
