package com.ebox.ebox;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText txt_verification, txt_new_password, txt_repeat_password;
    TextView txt_entered_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        this.setTitle("Change Password");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        txt_entered_email = (TextView) findViewById(R.id.txt_entered_email);
        txt_verification = (EditText) findViewById(R.id.txt_verification_code);
        txt_new_password = (EditText) findViewById(R.id.txt_new_password);
        txt_repeat_password = (EditText) findViewById(R.id.txt_repreat_Password);
        txt_entered_email.setText("Note: Please your email " + variable.entered_email + " for verification code.");
    }

    public void cmd_save_password_Clicked(View view) {

        if (txt_verification.getText().toString().length() == 0) {
            txt_verification.setError("Invalid");
        } else if (txt_new_password.getText().length() == 0) {
            txt_new_password.setError("Invalid");
        } else if (txt_repeat_password.getText().toString().length() == 0) {
            txt_repeat_password.setError("Invalid");
        } else {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, variable.url_verifyCode, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("Invalid Code")) {
                                Toast.makeText(getApplicationContext(), "Invalid Code"+response, Toast.LENGTH_SHORT).show();

                            } else {
                               processChange();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("V_code", txt_verification.getText().toString());

                            return params;
                        }
                    };
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                }
            };
            new Thread(runnable).start();

        }


    }
private  void processChange(){
    if (txt_verification.getText().toString().length() == 0) {
        txt_verification.setError("Invalid");
    } else if (txt_new_password.getText().length() == 0) {
        txt_new_password.setError("Invalid");
    } else if (txt_repeat_password.getText().toString().length() == 0) {
        txt_repeat_password.setError("Invalid");
    } else {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, variable.url_change_password, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("Invalid Code")) {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            overridePendingTransition(0, 0);
                            finish();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("pwd", txt_new_password.getText().toString());
                        params.put("V_code", txt_verification.getText().toString());
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }
        };
        new Thread(runnable).start();

    }

}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            gotoAskEmailActivity();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gotoAskEmailActivity();
    }

    void gotoAskEmailActivity() {
        startActivity(new Intent(getApplicationContext(), AskEmailActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }
}
