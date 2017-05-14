package com.ebox.ebox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.MainThread;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    EditText txt_username, txt_password, txt_companyid;
    ProgressDialog progressDialog;
    Spinner spinner_login;
    String userType = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        // Tools.setFullScreen(this);
        spinner_login = (Spinner) findViewById(R.id.spinner_login);
        txt_companyid = (EditText) findViewById(R.id.txt_companyid);
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);
        txt_username.setText("");
        txt_password.setText("");
        spinner_login.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    userType = "2";
                } else if (position == 1) {
                    userType = "3";
                } else if (position == 2) {
                    userType = "4";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void cmd_LoginButton_clicked(final View view) {
        Button b = (Button) view;
        int i = Integer.valueOf(b.getTag().toString());

        if (isNetworkAvailable()) {
            //Login Button click event
            if (i == 1) {
                if (txt_companyid.getText().toString().length() == 0) {
                    txt_companyid.setError("Required");
                } else if (txt_username.getText().toString().length() == 0) {
                    txt_username.setError("Username is  required!");
                } else if (txt_password.getText().toString().length() == 0) {
                    txt_password.setError("Password is required!");
                } else {
                    progressDialog.show();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, variable.url_login,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("result", response + "");
                                            progressDialog.dismiss();
                                            if (response.contains("wala")) {
                                                errorMessage(view, 1);
                                                variable.isLogin = false;
                                            } else {
                                                JSONObject Object = null;
                                                try {
                                                    Object = new JSONObject(response.replace("[", "").replace("]", ""));
                                                    variable.Current_user_cn = Object.get("cn").toString();
                                                    variable.Current_user_level = Object.get("UserType").toString();
                                                    variable.Current_user_Fullname = Object.get("Fullname").toString();
                                                    variable.Current_user_name = txt_username.getText().toString();

                                                } catch (JSONException e) {
                                                    Log.d("result", response);
                                                    e.printStackTrace();
                                                }


                                                variable.isLogin = true;
                                                startActivity(new Intent(getApplicationContext(), EmptyActivity.class));
                                                overridePendingTransition(0, 0);
                                                LoginActivity.this.finish();

                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("value0",txt_companyid.getText().toString());
                                    params.put("value1", txt_username.getText().toString());
                                    params.put("value2", txt_password.getText().toString());
                                    params.put("value3", userType);

                                    return params;
                                }
                            };
                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

                        }
                    };
                    new Thread(runnable).start();

                }
                //Forgot password Button click event
            } else if (i == 2) {
                startActivity(new Intent(getApplicationContext(), AskEmailActivity.class));
                overridePendingTransition(0, 0);
                finish();
            }
            //end network if
        } else {
            errorMessage(view, 2);
        ;
        }

    }


    private void errorMessage(View view, Integer tag) {
        String message = null;
        if (tag == 1) {
            message = "Invalid Login";
        } else if (tag == 2) {
            message = "No Internet Connection!";
        }
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }
}
