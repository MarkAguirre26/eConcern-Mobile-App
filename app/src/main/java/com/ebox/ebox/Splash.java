package com.ebox.ebox;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Tools.setFullScreen(this);

        Thread thread = new Thread() {
            @Override
            public void run() {
                //super.run();
                try {
                    sleep(3000);
                } catch (Exception ex) {
                    Log.d("Error", ex.getMessage());
                } finally {

                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        };
        thread.start();

    }
}
