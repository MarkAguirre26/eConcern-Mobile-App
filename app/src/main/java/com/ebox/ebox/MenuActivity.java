package com.ebox.ebox;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Tools.setFullScreen(this);
    }


    public void cmd_menu_Clicked(View view) {
        Button b = (Button) view;
        int i = Integer.valueOf(b.getTag().toString());
        if (i == 1) {
            if(variable.isLogin == true){
                startActivity(new Intent(getApplicationContext(), EmptyActivity.class));
            }else{
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
            overridePendingTransition(0, 0);
            finish();
        } else if (i == 2) {
            startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        variable.isLogin = false;
        finish();
    }
}
