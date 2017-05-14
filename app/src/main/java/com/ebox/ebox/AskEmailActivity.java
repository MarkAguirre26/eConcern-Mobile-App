package com.ebox.ebox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AskEmailActivity extends AppCompatActivity {

    EditText txt_email, txt_ID;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_email);
        this.setTitle("Registered Email");
        txt_email = (EditText) findViewById(R.id.txt_askEmail);
        txt_ID = (EditText) findViewById(R.id.txt_id_askMail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(AskEmailActivity.this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void cmd_enter_email(View view) {

        Button b = (Button) view;
        int i = Integer.valueOf(b.getTag().toString());
        if (i == 1) {
            //Toast.makeText(getApplicationContext(), "Confirmation code sent to your email, please check!", Toast.LENGTH_SHORT).show();
            variable.entered_email = txt_email.getText().toString();
            variable.ID_Number = txt_ID.getText().toString();
            if (Tools.validate(txt_email.getText().toString()) == true) {


                if (txt_ID.getText().toString().length() == 0) {
                    txt_ID.setError("Invalid");
                } else if (txt_email.getText().length() == 0) {
                    txt_email.setError("Invalid");
                } else {
                    //Progress dialog
                    dialog.show();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, variable.url_send_vCode, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                    if (response.contains("Failed!")) {
                                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                                    } else {

                                        gotoChangePasswordActivity();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {


                                    if (error.getMessage() == null) {
                                        gotoChangePasswordActivity();
                                    } else {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("ID", txt_ID.getText().toString());
                                    params.put("to", txt_email.getText().toString());

                                    return params;
                                }
                            };

                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                        }
                    };
                    new Thread(runnable).start();

                }

            } else {
                Snackbar.make(view, "Invalid email", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        } else if (i == 2) {
            gotoChangePasswordActivity();
        }
    }

    private void gotoChangePasswordActivity() {
        dialog.dismiss();
        startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            gotoLoginActivity();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gotoLoginActivity();
    }

    private void gotoLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }
}
