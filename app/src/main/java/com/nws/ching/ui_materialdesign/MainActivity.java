package com.nws.ching.ui_materialdesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

public class MainActivity extends Activity {
    CallbackManager callbackManager;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化fb Sdk,要放在第一行
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //宣告callback Manager
        callbackManager = CallbackManager.Factory.create();

        //定義login Button
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

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
    }

    //Let login result return to callback Manager
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
