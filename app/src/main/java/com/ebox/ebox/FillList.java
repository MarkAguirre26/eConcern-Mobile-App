package com.ebox.ebox;

import android.content.Context;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dadotnetkid on 10/17/2016.
 */

public class FillList<T> {
    public FillList() {

    }

    public void test(Context context) {

    }



    public void FillSpinner(final Context context, final Object[] viewResId, final Object[] string_value, final int layout, final Spinner spinner, final Class<T> clazz, final String url, final int method, final String[] parameters, final String[] parametervalue) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("wala")) {
                            Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<T> jsonObjects = new JsonConverter<T>().toArrayList(response, clazz);
                            BindDictionary<T> dictionary = new BindDictionary<>();

                            for (int i = 0; i <= viewResId.length - 1; i++) {


                                final int finalI2 = i;
                                dictionary.addStringField((Integer) viewResId[i], new StringExtractor<T>() {
                                    private T item;

                                    @Override
                                    public String getStringValue(T item, int position) {
                                        Field field;
                                        String value = null;
                                        try {
                                            field = item.getClass().getDeclaredField((String) string_value[finalI2]);
                                            value = (String) field.get(item);
                                        } catch (NoSuchFieldException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }

                                        return value;
                                    }
                                });
                            }


                            FunDapter<T> adapter = new FunDapter<>(context, jsonObjects, layout, dictionary);
                            spinner.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void FillSListview(final Context context, final Object[] viewResId, final Object[] string_value, final int layout, final ListView listView, final Class<T> clazz, final String url, final int method, final String[] parameters, final String[] parametervalue) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("wala")) {
                            Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<T> jsonObjects = new JsonConverter<T>().toArrayList(response, clazz);
                            BindDictionary<T> dictionary = new BindDictionary<>();

                            for (int i = 0; i <= viewResId.length - 1; i++) {


                                final int finalI2 = i;
                                dictionary.addStringField((Integer) viewResId[i], new StringExtractor<T>() {
                                    private T item;

                                    @Override
                                    public String getStringValue(T item, int position) {
                                        Field field;
                                        String value = null;
                                        try {
                                            field = item.getClass().getDeclaredField((String) string_value[finalI2]);
                                            value = (String) field.get(item);
                                        } catch (NoSuchFieldException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }

                                        return value;
                                    }
                                });
                            }


                            FunDapter<T> adapter = new FunDapter<>(context, jsonObjects, layout, dictionary);
                            listView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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


}