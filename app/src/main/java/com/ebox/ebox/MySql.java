package com.ebox.ebox;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Mark on 11/11/2016.
 */

public class MySql extends Activity {


    private static void showToast(final String toast, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ExecuteQuery(final Context context, final Activity activity, final String url, final String erroresponse, final String[] parameters, final String[] parametervalue) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains(erroresponse)) {

                            showToast(response.toString(), activity);

                        } else {

                            showToast("Success", activity);

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showToast(error.getMessage(), activity);

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        for (int i = 0; i <= parameters.length - 1; i++) {
                            params.put(parameters[i], parametervalue[i]);
                        }

                        return params;
                    }

                };
                MySingleton.getInstance(context).addToRequestQueue(stringRequest);

            }

        };
        new Thread(runnable).start();

    }

    public Boolean Login(String Username, String Password, final Context context, final Activity i, final String errorresponse, final int method, final String url, String... a) {

        final FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
            @Override

            public Boolean call() throws Exception {
                final Boolean[] bool = {false};

                StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains(errorresponse)) {
                            bool[0] = false;
                        } else {
                            bool[0] = true;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Error in Login", i);
                    }
                });
                MySingleton.getInstance(context).addToRequestQueue(stringRequest);
                return bool[0];
            }

            ;

        });
        Thread th = new Thread(futureTask);
        th.start();
        Boolean login = false;
        try {
            th.join();

            login = (Boolean) futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return login;
    }

}
