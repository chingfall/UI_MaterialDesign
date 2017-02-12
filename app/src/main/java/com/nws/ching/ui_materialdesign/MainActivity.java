package com.nws.ching.ui_materialdesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button SocailSDK_Button = (Button) findViewById(R.id.SocialMediaSDK);

        SocailSDK_Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SocialMediaSDK.class);
                startActivity(intent);
                finish();
            }
        });
    }

   /* //Button Click Listener
    private Button.OnClickListener SocialMediaListener = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SocialMediaSDK.class);
            startActivity(intent);
            finish();
        }
    };*/
}
